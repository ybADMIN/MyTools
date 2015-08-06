package yb.com.exchangeviewlocation.deamo.yb_crustomalbum.album.adapter;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import yb.com.exchangeviewlocation.R;
import yb.com.exchangeviewlocation.deamo.yb_crustomalbum.album.bitmapProcess.ImageLoader;
import yb.com.exchangeviewlocation.deamo.yb_crustomalbum.album.extar.ImgData;
import yb.com.exchangeviewlocation.deamo.yb_crustomalbum.album.listener.ImageLoadingListener;
import yb.com.exchangeviewlocation.deamo.yb_crustomalbum.album.utils.Md5FileNameGenerator;


public  class CustomAlbumEditPhotoFragment extends Fragment {

    public static CustomAlbumEditPhotoFragment getInstance(ImgData imageId ) {
        final CustomAlbumEditPhotoFragment instance = new CustomAlbumEditPhotoFragment();
        final Bundle params = new Bundle();
        params.putParcelable("imageId", imageId);
        instance.setArguments(params);
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.gallery_view_pager_edit_item, null);
        final ImgData imageId = getArguments().getParcelable("imageId");
        final ImageView image;
        final ProgressBar progress = (ProgressBar) v.findViewById(R.id.editphoto_progressbar);
            image = (ImageView) v.findViewById(R.id.iv_editphoto_itme);

//			final InputStream is = getResources().openRawResource(imageId);

        Md5FileNameGenerator md5 = new Md5FileNameGenerator();
        String name = md5.generate(imageId.imgPath);

//        final String cachepath = AlbumSettingBase.getALBUM_PATH(image.getContext())+"/"+name;
//        if (new File(cachepath).exists()) {
//            image.setImageBitmap(BitmapFactory.decodeFile(cachepath));
//        }
        ImageLoader.getInstance(3, ImageLoader.Type.LIFO).loadImage(imageId.original.equals("")?imageId.imgPath:imageId.original, image, false, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

                 progress.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                progress.setVisibility(View.GONE);
                if (imageId.isChange == 0)
                    ((ImageView) view).setImageBitmap(loadedImage);
                else
                    new RotateAsyncTaskFrament((ImageView) view).execute(loadedImage, imageId.oldRotate);
            }
        }, new int[]{getActivity().getResources().getDisplayMetrics().widthPixels, getActivity().getResources().getDisplayMetrics().widthPixels});
        return v;
    }
    @Override
    public void onDestroyView() {
         ImageLoader.getInstance().getMemoryCache().clear();
        super.onDestroyView();
    }


}