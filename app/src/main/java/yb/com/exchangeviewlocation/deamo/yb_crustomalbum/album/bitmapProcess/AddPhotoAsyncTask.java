package yb.com.exchangeviewlocation.deamo.yb_crustomalbum.album.bitmapProcess;

import android.os.AsyncTask;

import java.util.List;

import yb.com.exchangeviewlocation.deamo.yb_crustomalbum.album.adapter.AddPhotoAdapter;
import yb.com.exchangeviewlocation.deamo.yb_crustomalbum.album.extar.ImgData;


public class AddPhotoAsyncTask extends AsyncTask<Object, Integer, Object>{

	@Override
	protected Object doInBackground(Object... params) {
		@SuppressWarnings("unchecked")
		List<ImgData> imgs = (List<ImgData>) params[0];
		AddPhotoAdapter adapter=(AddPhotoAdapter) params[1];
		adapter.getUri().clear();
		adapter.getUri().addAll(imgs);
		adapter.addFrist();
		return adapter;
	}
	
	
	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
	}
	
	
	@Override
	protected void onPostExecute(Object result) {
		((AddPhotoAdapter)result).notifyDataSetChanged();
		// TODO Auto-generated method stub
		super.onPostExecute(result);
	}
	
	


}
