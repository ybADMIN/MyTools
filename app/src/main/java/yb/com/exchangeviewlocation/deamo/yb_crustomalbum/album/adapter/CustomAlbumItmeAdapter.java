package yb.com.exchangeviewlocation.deamo.yb_crustomalbum.album.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import yb.com.exchangeviewlocation.R;
import yb.com.exchangeviewlocation.deamo.yb_crustomalbum.album.bitmapProcess.ImageLoader;
import yb.com.exchangeviewlocation.deamo.yb_crustomalbum.album.extar.CustomAlbumFileTraversal;
import yb.com.exchangeviewlocation.deamo.yb_crustomalbum.album.listener.ImageLoadingListener;


public class CustomAlbumItmeAdapter extends BaseAdapter implements ImageLoadingListener {

	/**
	 * 初始化数据用于判断是是否inflate视图
	 */
	private Context mContext;
	private List<CustomAlbumFileTraversal> fileTraversals;
	private int oldposition=-1;
	//状态
	private boolean [] change;
	
	public boolean[] getChange() {
		return change;
	}
	public void setChange(boolean[] change) {
		this.change = change;
	}
	/**
	 *点击其他相册之前已选择的相册默认值为-1表示没有已选定相册
	 * @return
	 */
	public int getOldposition() {
		return oldposition;
	}

	/**
	 * 回调接口
	 */
	private OnAlbumItmeClick albumItmeClick;
	
	public void setAlbumItmeClick(OnAlbumItmeClick albumItmeClick) {
		this.albumItmeClick = albumItmeClick;
	}

	/**
	 * 缓存View
	 */
//	private List<View> holderlist;

	public CustomAlbumItmeAdapter(Context context) {
		this.mContext = context;
//		holderlist = new ArrayList<View>();
	}
	public CustomAlbumItmeAdapter(Context context,List<CustomAlbumFileTraversal> fileTraversals) {
		this.mContext = context;
		this.fileTraversals = fileTraversals;
//		holderlist = new ArrayList<View>();
		change=new boolean[fileTraversals.size()];
	}
	
	public List<CustomAlbumFileTraversal> getFileTraversals() {
		return fileTraversals;
	}
	public void setFileTraversals(List<CustomAlbumFileTraversal> fileTraversals) {
		this.fileTraversals = fileTraversals;
		change=new boolean[fileTraversals.size()];
	}
	@Override
	public int getCount() {
		return fileTraversals.size();
	}

	@Override
	public Object getItem(int position) {
		return fileTraversals.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView==null) {
			holder=new ViewHolder();
			convertView=LayoutInflater.from(mContext).inflate(R.layout.activity_common_album_itme_menuitme, null);
			holder.iv_aibum_menuitme=	(ImageView) convertView.findViewById(R.id.iv_aibum_menuitme);
			holder.tv_aibum_menuitme_name=(TextView) convertView.findViewById(R.id.tv_aibum_menuitme_name);
			holder.tv_aibum_menuitme_num=(TextView) convertView.findViewById(R.id.tv_aibum_menuitme_num);
			holder.cb=(CheckBox) convertView.findViewById(R.id.cb_aibum_menuitme);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		viewChange(holder,position);
		CustomAlbumFileTraversal filet = fileTraversals.get(position);
//		ImageLoaderUtil.getOtherConfig().displayImage("file://"+filet.filecontent.get(0).imgPath, holder.iv_aibum_menuitme, ImageLoaderUtil.getAlbumOptions());
		ImageLoader.getInstance(3, ImageLoader.Type.LIFO).loadImage(filet.filecontent.get(0).imgPath, holder.iv_aibum_menuitme,true,this);
		holder.tv_aibum_menuitme_name.setText(filet.filename);
		holder.tv_aibum_menuitme_num.setText(filet.filecontent.size()+"张");
			convertView.setOnClickListener(new AlbumOnItmeClick(holder.cb,position ));
			if (oldposition==-1 && position==0) {
				holder.cb.setChecked(true);
				oldposition=0;
			}
		return convertView;
	}
private class ViewHolder{
	ImageView iv_aibum_menuitme;
	TextView tv_aibum_menuitme_name;
	TextView tv_aibum_menuitme_num;
	CheckBox cb;
}
private void viewChange(ViewHolder holder,int position){
	holder.cb.setChecked(change[position]);
}
/**
 * 点击时间回调接口
 * @author Administrator
 *
 */
public interface OnAlbumItmeClick{
	public void ItmeClick(View v, CheckBox cb, CustomAlbumFileTraversal fileTraversal);
}

/**
 * 自定义Itme监听
 * @author Administrator
 *
 */
private class AlbumOnItmeClick implements OnClickListener{
	private CheckBox cb;
	private int position;
		public AlbumOnItmeClick(CheckBox cb,int position) {
			this.cb=cb;
			this.position=position;
		}
	@Override
	public void onClick(View v) {
		if (albumItmeClick!=null) {
			//将选中的撤销
				if (oldposition!=-1 && oldposition!=position) {
					getChange()[oldposition]=false;
					getChange()[position]=true;
				}
				oldposition=position;
				
				albumItmeClick.ItmeClick(v, cb,fileTraversals.get(position));
		}
	}
	
}
@Override
public void onLoadingStarted(String imageUri, View view) {
	((ImageView)view).setImageResource(R.mipmap.common_album_imgbg);
}
@Override
public void onLoadingFailed(String imageUri, View view) {
	((ImageView)view).setImageResource(R.mipmap.common_album_imgbg);
}
@Override
public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
	((ImageView)view).setImageBitmap(loadedImage);
}

}
