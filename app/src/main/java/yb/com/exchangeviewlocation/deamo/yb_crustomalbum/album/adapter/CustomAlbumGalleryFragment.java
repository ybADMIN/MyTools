package yb.com.exchangeviewlocation.deamo.yb_crustomalbum.album.adapter;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.File;

import yb.com.exchangeviewlocation.R;
import yb.com.exchangeviewlocation.deamo.yb_crustomalbum.album.bitmapProcess.ImageLoader;
import yb.com.exchangeviewlocation.deamo.yb_crustomalbum.album.extar.AlbumSettingBase;
import yb.com.exchangeviewlocation.deamo.yb_crustomalbum.album.extar.ImgData;
import yb.com.exchangeviewlocation.deamo.yb_crustomalbum.album.uitools.lib.TileBitmapDrawable;
import yb.com.exchangeviewlocation.deamo.yb_crustomalbum.album.uitools.lib.TouchImageView;
import yb.com.exchangeviewlocation.deamo.yb_crustomalbum.album.utils.Md5FileNameGenerator;

public  class CustomAlbumGalleryFragment extends Fragment {

	public static CustomAlbumGalleryFragment getInstance(ImgData imageId) {
		final CustomAlbumGalleryFragment instance = new CustomAlbumGalleryFragment();
			final Bundle params = new Bundle();
			params.putParcelable("imageId", imageId);
			instance.setArguments(params);
			return instance;
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			final View v = inflater.inflate(R.layout.gallery_view_pager_sample_item, null);

			final ImgData imageId = getArguments().getParcelable("imageId");
			final ImageView image;
			final ProgressBar progress = (ProgressBar) v.findViewById(R.id.gallery_view_pager_sample_item_progress);
				image = (TouchImageView) v.findViewById(R.id.gallery_view_pager_sample_item_image);
//			final InputStream is = getResources().openRawResource(imageId);

			Md5FileNameGenerator md5 = new Md5FileNameGenerator();
			String name = md5.generate(imageId.imgPath);

			final String cachepath = AlbumSettingBase.getALBUM_PATH(image.getContext())+"/"+name;
			if (new File(cachepath).exists()) {
				image.setImageBitmap(BitmapFactory.decodeFile(cachepath));
			}
				TileBitmapDrawable.attachTileBitmapDrawable(image, imageId.imgPath, null, new TileBitmapDrawable.OnInitializeListener() {

					@Override
					public void onStartInitialization() {
						progress.setVisibility(View.VISIBLE);
					}

					@Override
					public void onEndInitialization() {
						progress.setVisibility(View.GONE);
					}

					@Override
					public void onError(Exception ex) {

					}
				});

			return v;
		}


	@Override
	public void onDestroyView() {
		ImageLoader.getInstance().getMemoryCache().clear();
		super.onDestroyView();
	}

}