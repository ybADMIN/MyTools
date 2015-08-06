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
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import yb.com.exchangeviewlocation.R;
import yb.com.exchangeviewlocation.deamo.yb_crustomalbum.album.bitmapProcess.ImageLoader;
import yb.com.exchangeviewlocation.deamo.yb_crustomalbum.album.extar.ImgData;
import yb.com.exchangeviewlocation.deamo.yb_crustomalbum.album.listener.ImageLoadingListener;


public class CustomAlbumAdapter extends BaseAdapter implements ImageLoadingListener {
	private Context mContext ;
	/**
	 * 用于存放初始化完成的Itme
	 */
	private List<View> holderlist;
	private OnItemClickCallBack onItemClickClass;
	private List<ImgData> mData=new ArrayList<ImgData>();
	private String albumName="";//相册名字
	/**
	 * 初始化数据用于判断是是否inflate视图
	 */
	private int INITDATE=-1;
	
	public CustomAlbumAdapter(Context context) {
		this.mContext=context;
		holderlist=new ArrayList<View>();
	}
	public CustomAlbumAdapter(Context context,List<ImgData>data,String albumName) {
		this.mContext=context;
		holderlist=new ArrayList<View>();
		cacheList.clear();
//		change=new boolean[data.size()];
		this.mData=data;
	}
	
	
public String getAlbumName() {
		return albumName;
	}
	public void setAlbumName(String albumName) {
		this.albumName = albumName;
	}
public List<View> getHolderlist() {
		return holderlist;
	}
/**
 * 得到数据
 * @return
 */
public List<ImgData> getData() {
		return mData;
	}
/**
 * 设置数据
 * @param mData
 */
	public void setData(List<ImgData> mData) {
//		change=new boolean[mData.size()];
		holderlist.clear();
		cacheList.clear();
		this.mData = mData;
	}
/**
 * 设置图片点击监听
 * @param onItemClickClass
 */
	public void setOnItemClickCallBack(OnItemClickCallBack onItemClickClass) {
		this.onItemClickClass = onItemClickClass;
	}


	@Override
	public int getCount() {
		
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	

	public int getINITDATE() {
		return INITDATE;
	}
	
	ArrayList<Integer> cacheList = new ArrayList<Integer>();//缓存视图
	//缓存大小
//	private int CACHESIZE=20;
	//状态
//	private boolean [] change;
	
//	public boolean[] getChange() {
//		return change;
//	}
//	public void setChange(boolean[] change) {
//		this.change = change;
//	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		
		/**
		 * 优化缓存
		 */
		if (convertView==null) {//初始化视图
//		if (!cacheList.contains(position)) {//初始化视图
//			if(cacheList.size()>CACHESIZE)//这里设置缓存的Item数量  
//            {  
//				cacheList.remove(0);//删除第一项  
//				holderlist.remove(0);//删除第一项  
//            }  
			holder=new ViewHolder();
			convertView =LayoutInflater.from(mContext).inflate(R.layout.activity_common_album_imgsitem, null);
			holder.iv_album_imgsitem=(ImageView) convertView.findViewById(R.id.iv_album_imgsitem);
			holder.cb_album_imgsitem=(CheckBox) convertView.findViewById(R.id.cb_album_imgsitem);
			holder.ll_album_cb=(LinearLayout) convertView.findViewById(R.id.ll_album_cb);
			convertView.setTag(holder);
//			holderlist.add(convertView);
//			cacheList.add(position);//添加最新项  
		}else {
//			convertView=holderlist.get(cacheList.indexOf(position));
			holder= (ViewHolder)convertView.getTag();
//			viewChange(holder, position);
//			return convertView;
		}
		viewChange(holder, position);
	 //载入数据
//		ImageLoaderUtil.getOtherConfig().displayImage("file://"+mData.get(position).imgPath,holder.iv_album_imgsitem,ImageLoaderUtil.getAlbumOptions());
//		new ImgAsyncTask(mData.get(position).imgPath, holder).execute();
//		holder.iv_album_imgsitem.setImageResource(R.drawable.common_album_imgbg);
	ImageLoader.getInstance(3, ImageLoader.Type.LIFO).loadImage(mData.get(position).imgPath, holder.iv_album_imgsitem,true,this,new int[]{mContext.getResources().getDisplayMetrics().widthPixels/3,mContext.getResources().getDisplayMetrics().widthPixels/3});
			viewChange(holder,position);
			holder.ll_album_cb.setOnClickListener(new OnPhotoClick(position, holder.cb_album_imgsitem));
			holder.iv_album_imgsitem.setOnClickListener(new OnPhotoClick(position, holder.iv_album_imgsitem));
		return convertView;
	}
	private void viewChange(ViewHolder holder,int position){
		holder.cb_album_imgsitem.setChecked(false);
		if (mData.get(position).isClick) {
			holder.cb_album_imgsitem.setChecked(true);
		}
}
	
	/**
	 * View 助手
	 * @author Administrator
	 *
	 */
	private class ViewHolder{
		ImageView iv_album_imgsitem;
		CheckBox cb_album_imgsitem;
		LinearLayout ll_album_cb;
	}
	/**
	 * 回调监听
	 * @author Administrator
	 *
	 */
	public interface OnItemClickCallBack{
		public void OnItemClick(View v, int Position, View client);
	}
	
	/**
	 * 照片监听
	 * @author Administrator
	 *
	 */
	class OnPhotoClick implements OnClickListener{
			int position;
			View client;
		
		public OnPhotoClick(int position,View client) {
				this.position=position;
				this.client=client;
		}
		@Override
		public void onClick(View v) {
					if (mData!=null && onItemClickClass!=null ) {
						onItemClickClass.OnItemClick(v, position, client);
					}
		}
	}
//	class ImgAsyncTask extends AsyncTask<Void, Void, Void> {
//		private Bitmap mBitmap;
//		private ViewHolder mHolder;
//		private String mPath;
//
//		public ImgAsyncTask(String path,ViewHolder holder) {
//			this.mPath=path;
//			this.mHolder=holder;
//		}
//		@Override
//		protected Void doInBackground(Void... params) {
//			Md5FileNameGenerator md5 = new Md5FileNameGenerator();
//			String name = md5.generate(mPath);
//			String filepathe = AlbumSettingBase.ALBUM_PATH+"/"+name;
//			File f = new File(AlbumSettingBase.ALBUM_PATH);
//			if (!f.exists() &&! f.mkdirs()) {
//				AlbumSettingBase.ALBUM_PATH=AlbumSettingBase.ALBUM_TEMPPATH;
//				f=new File(AlbumSettingBase.ALBUM_PATH);
//				f.mkdirs();
//			}
////			UnlimitedDiscCache diskcache =new UnlimitedDiscCache(new File(AlbumSettingBase.ALBUM_PATH));
//			if (new File(filepathe).exists()) {
////				new File(filepathe).delete();
//				mBitmap=BitmapUtil.setOption(new File(filepathe).getAbsolutePath());
////				mBitmap=ImageLoader.getInstance().loadImageSync("file://"+filepathe);
//			}else{
//			mBitmap=CustomAlbumUtil.getImageThumbnail(mPath, mContext.getResources().getDisplayMetrics().widthPixels/3, mContext.getResources().getDisplayMetrics().widthPixels/3);
//			try {
//				BitmapUtil.saveThumbnailImage(mBitmap, filepathe);
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			}
//			return null;
//		}
//
//		@Override
//		protected void onPostExecute(Void result) {
//			mHolder.iv_album_imgsitem.setImageBitmap(mBitmap);
//		}
//	}
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
	};

}
