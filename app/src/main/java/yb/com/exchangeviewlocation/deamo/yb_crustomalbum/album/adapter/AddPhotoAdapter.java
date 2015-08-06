package yb.com.exchangeviewlocation.deamo.yb_crustomalbum.album.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;

import java.util.ArrayList;

import yb.com.exchangeviewlocation.R;
import yb.com.exchangeviewlocation.deamo.yb_crustomalbum.album.bitmapProcess.ImageLoader;
import yb.com.exchangeviewlocation.deamo.yb_crustomalbum.album.extar.ImgData;
import yb.com.exchangeviewlocation.deamo.yb_crustomalbum.album.listener.ImageLoadingListener;


public class AddPhotoAdapter extends BaseAdapter implements ListAdapter {
	private Context mContext;
	private PhotoDeleteOnclickListener deleteOnclickListener;


	public AddPhotoAdapter(Context context) {
		this.mContext=context;
		addFrist();
	}

	public void setDeleteOnclickListener(PhotoDeleteOnclickListener deleteOnclickListener) {
		this.deleteOnclickListener = deleteOnclickListener;
	}

	public ArrayList<ImgData> getUri() {
		return uri;
	}
	
//	public void addFrist(){
//		ImgData img = new ImgData();
//		img.imgPath=R.drawable.photo_add+"";
//		uri.addFirst(img);
//	}

	public void setUri(ArrayList<ImgData> uri) {
		this.uri = uri;
		addFrist();
	}

	public void setmContext(Context mContext) {
		this.mContext = mContext;
	}

	ArrayList<ImgData> uri = new ArrayList<ImgData>(); 
	@Override
	public int getCount() {
		return uri.size();
	}
	public void addFrist(){
		ImgData imgdata=new ImgData();
		imgdata.imgPath= R.mipmap.photo_add+"";
		uri.add(0,imgdata);
	}

	@Override
	public Object getItem(int position) {
		return uri.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView==null) {
			holder= new ViewHolder();
			LayoutInflater inf = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView= inf.inflate(R.layout.main_image_itme, null);
			holder.iv_itme=(ImageView) convertView.findViewById(R.id.iv_itme);
			holder.iv_itme.setDrawingCacheEnabled(true);
			holder.iv_delete=(ImageView) convertView.findViewById(R.id.image_itme_delete);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		if (position==0) {
			holder.iv_delete.setVisibility(View.GONE);
			holder.iv_itme.setImageResource(Integer.parseInt(uri.get(position).imgPath));
		}else{
			holder.iv_delete.setVisibility(View.VISIBLE);
			holder.iv_delete.setOnClickListener(new View.OnClickListener() {//删除监听
				@Override
				public void onClick(View v) {
					if (deleteOnclickListener!=null)
					deleteOnclickListener.deleteItme(v,position);
				}
			});
			try {
				ImageLoader.getInstance(3, ImageLoader.Type.LIFO).loadImage(uri.get(position).imgPath, holder.iv_itme, true, new ImageLoadingListener() {
					@Override
					public void onLoadingStarted(String imageUri, View view) {
						((ImageView)view).setImageResource(R.mipmap.common_album_imgbg);
					}

					@Override
					public void onLoadingFailed(String imageUri, View view) {

					}

					@Override
					public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
						if ((uri.get(position).oldRotate!=0||uri.get(position).oldRotate%360!=0)){
							new RotateAsyncTaskFrament((ImageView)view).execute(loadedImage,uri.get(position).oldRotate);
						}else {
							uri.get(position).oldRotate=0;
							((ImageView)view).setImageBitmap(loadedImage);
						}
					}
				});
			}catch(Exception e){
				System.out.println(e.getMessage());
			}
		}
		
		return convertView;
	}

	private class ViewHolder{
		ImageView iv_itme;
		ImageView iv_delete;
	}
	public interface PhotoDeleteOnclickListener{
		 void deleteItme(View view, int position);
	}
}
