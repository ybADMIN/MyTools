package yb.com.exchangeviewlocation.deamo.yb_crustomalbum.album.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import yb.com.exchangeviewlocation.deamo.yb_crustomalbum.album.extar.ImgData;


public class CustomAlbum_ViewPageAdapter  extends FragmentPagerAdapter {
	private FragmentManager fm;
	private List<ImgData> mItems=new ArrayList<ImgData>();
private boolean isExtarChoose;
	private Fragment mCurrentFrament;

	public CustomAlbum_ViewPageAdapter(FragmentManager fm,List<ImgData> items) {
		super(fm);
		this.mItems = items;
	}

	public boolean isExtarChoose() {
		return isExtarChoose;
	}

	public void setExtarChoose(boolean isExtarChoose) {
		this.isExtarChoose = isExtarChoose;
	}

	public void setData(List<ImgData> data) {
		this.mItems = data;
		notifyDataSetChanged();
	}

	public List<ImgData> getmItems() {
		return mItems;
	}

	public CustomAlbum_ViewPageAdapter(FragmentManager fm) {
		super(fm);
		this.fm=fm;
	}
//	@Override
//	public boolean isViewFromObject(View view, Object obj) {
//		return view == ((Fragment) obj).getView();
//	}


	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		return super.instantiateItem(container, position);
	}

	@Override
	public Fragment getItem(int position) {
		if (!isExtarChoose){
				mCurrentFrament=CustomAlbumGalleryFragment.getInstance(this.mItems.get(position));
			return mCurrentFrament;
		}
		else
		{
			mCurrentFrament= CustomAlbumEditPhotoFragment.getInstance(this.mItems.get(position));
			return mCurrentFrament;
		}
	}
	public Fragment currenteFrament(){
		return mCurrentFrament;
	}

	@Override
	public int getCount() {
		return this.mItems.size();
	}

	@Override
	public int getItemPosition(Object object) {
		if (!isExtarChoose)
			return POSITION_UNCHANGED;
		else
			return POSITION_NONE;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		Fragment fragment = ((Fragment) object);
		container.removeView(fragment.getView());
		fm.beginTransaction().remove(fragment).commit();
		super.destroyItem(container, position, object);
	}


//	public void removeItme(int position){
////		this.fm.beginTransaction().remove(getItem(position)).commit();
////		fm.beginTransaction().remove(fm.findFragmentByTag(position+"")).commit();
//		mItems.remove(position);
//
//		notifyDataSetChanged();
//	}

}
