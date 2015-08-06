package yb.com.exchangeviewlocation.deamo.yb_crustomalbum.album.adapter;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

import yb.com.exchangeviewlocation.deamo.yb_crustomalbum.album.utils.BitmapUtil;


/**
 * 照片旋转处理
 */
public class  RotateAsyncTaskFrament extends AsyncTask<Object,Void,Bitmap> {
    private final ImageView mView;

    public RotateAsyncTaskFrament(ImageView view) {
        this.mView = view;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        mView.setImageBitmap(bitmap);
    }


    @Override
    protected Bitmap doInBackground(Object... params) {
        Bitmap img= (Bitmap) params[0];
        int rotate = (int) params[1];
        return  BitmapUtil.imageRotate(img, rotate);
    }
}