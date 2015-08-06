package yb.com.exchangeviewlocation.deamo.yb_crustomalbum.album;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import yb.com.exchangeviewlocation.R;
import yb.com.exchangeviewlocation.deamo.yb_crustomalbum.album.adapter.CustomAlbum_ViewPageAdapter;
import yb.com.exchangeviewlocation.deamo.yb_crustomalbum.album.extar.AlbumSettingBase;
import yb.com.exchangeviewlocation.deamo.yb_crustomalbum.album.extar.CustomAlbumFileTraversal;
import yb.com.exchangeviewlocation.deamo.yb_crustomalbum.album.extar.ImgData;
import yb.com.exchangeviewlocation.deamo.yb_crustomalbum.album.uitools.lib.GalleryViewPager;
import yb.com.exchangeviewlocation.deamo.yb_crustomalbum.album.utils.DensityUtils;

/**
 * 照片列表查询<br>
 * MODE_CHOOSEFILE<br>
 * Intent.putParcelableExtra(CustomAlbumImageSee.MODE_CHOOSEFILE,{@linkplain yb.com.exchangeviewlocation.deamo.yb_crustomalbum.album.extar.CustomAlbumFileTraversal
 *                          file})相册列表<br>
 *  选择后的照片编辑<br>
 *  MODE_IMAGES    <br> 
 * Intent.putParcelableExtra(CustomAlbumImageSee.MODE_IMAGES,{@linkplain yb.com.exchangeviewlocation.deamo.yb_crustomalbum.album.extar.ImgData
 *                          imgs})照片列表<br>
 * Intent.putIntegerArrayList(CustomAlbumImageSee.TAG_CHOOSEPOSITION,{@linkplain Integer
 *                          positions})照片id列表<br>
 *  该类最后返回为<br>
 *  	intent.putIntegerArrayListExtra("removePosition", removePosition);已经删除的id<br>
 *  	intent.putParcelableArrayListExtra(MODE_IMAGES, imgs);修改后的照片列表<br>
 *		intent.putIntegerArrayListExtra("imgRotate", imgRotate);旋转角度<br>
 *		用于处理后在接收结果端处理
 * @author yb
 *
 */
public class CustomAlbumImageSee extends FragmentActivity implements OnClickListener ,OnPageChangeListener {
	public static int screenWidth;
	public static int screenHeight;
	private CustomAlbumFileTraversal files;
	private GalleryViewPager gallery;
	private CustomAlbum_ViewPageAdapter adapter;
	private List<Integer> removePosition = new ArrayList<Integer>();
	private List<ImgData> chooses= new ArrayList<ImgData>();
	private List<ImgData> imgs;
	private AlbumSettingBase setting;
	private CheckBox checkbox;
	/**
	 * 用于判断是否是外部调用的图片查看
	 */
	public static final int TAG_VALUE=1;//表示显示编辑图层
	private boolean isExtarChoose;//判断是否是外部调用
	private TextView photonumber_position;//编辑照片时候的照片表示 eg：1/2

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_common_album_picture_view);
		initViews();
		initData();
	}
	private void initData() {
		//照片列表查询
		if (getIntent().hasExtra(AlbumSettingBase.MODE_PREVIEW)){
			setting= getIntent().getParcelableExtra(AlbumSettingBase.MODE_PREVIEW);
			files=setting.getF();
			imgs= files.filecontent;
			files=null;
		}
		
		//选择后的照片编辑
		if (getIntent().hasExtra(AlbumSettingBase.MODE_EDITPHOTOFORALBUM)) {
			setting = (getIntent().getParcelableExtra(AlbumSettingBase.MODE_EDITPHOTOFORALBUM));
			imgs=setting.getChooseImage();
		}
			for (ImgData i : setting.getChooseImage()) {
				chooses.add(i);
			}
		if (setting.isExtarChoose()){
			findViewById(R.id.top_checkBox).setVisibility(View.GONE);
			findViewById(R.id.editphoto_toolbare).setVisibility(View.VISIBLE);
			((TextView)findViewById(R.id.title)).setText("编辑");
//			TextView btn_editTextphoto_ok = ((TextView) findViewById(R.id.bartershare_publish_del_img));//右上角确定
//			btn_editTextphoto_ok.setVisibility(View.VISIBLE);
//			btn_editTextphoto_ok.setCompoundDrawables(null, null, null, null);
//			btn_editTextphoto_ok.setText("确定");
			findViewById(R.id.editphoto_del_iv).setOnClickListener(this);//删除右下角
			photonumber_position = (TextView) findViewById(R.id.editphoto_number);//删除下边toolbar用于显示选择照片的数量
			photonumber_position.setText((setting.getPosition() + 1) + "/" + imgs.size());
		}
		adapter.setExtarChoose(setting.isExtarChoose());
		adapter.setData(imgs);
		adapter.notifyDataSetChanged();
		gallery.setCurrentItem(setting.getPosition());//选择照片位置
		setting=null;
		
	}

	private void initViews() {
		Rect frame = new Rect();
		getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		int statusBarHeight = frame.top;
		screenWidth = getResources().getDisplayMetrics().widthPixels;
		screenHeight =  getResources().getDisplayMetrics().heightPixels- DensityUtils.dip2px(this, 52)-statusBarHeight;
		gallery=(GalleryViewPager)findViewById(R.id.pic_gallery);
		checkbox=((CheckBox)findViewById(R.id.top_checkBox));
		checkbox.setOnClickListener(this);
		gallery.setOnPageChangeListener(this);
		adapter = new CustomAlbum_ViewPageAdapter(getSupportFragmentManager());
		gallery.setAdapter(adapter);
		findViewById(R.id.left).setOnClickListener(this);
		findViewById(R.id.edit_photo_rotate).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.left:
			onBackPressed();
			break;
		case R.id.top_checkBox:
			imgs.get(gallery.getCurrentItem()).isClick=checkbox.isChecked();//选择照片的状态改变

			if (checkbox.isChecked()){
				chooses.add(imgs.get(gallery.getCurrentItem()));
				for (int i=0 ; i<removePosition.size() ;i++) {//存在效率问题如需改造可以试着将数据removePosition改成map形式
					if (removePosition.get(i).equals(imgs.get(gallery.getCurrentItem())._ID)){
						removePosition.remove(i);
						--i;
						break;
					}

				}

			}else{
				for(int i=0;i<chooses.size();i++){
					if (chooses.get(i)._ID.equals(imgs.get(gallery.getCurrentItem())._ID)) {
						removePosition.add(chooses.remove(i)._ID);
						--i;
						break;
					}
				}
			}

			break;
			case R.id.editphoto_del_iv:
				int CurrentItem = gallery.getCurrentItem();
				int countNumber=adapter.getCount()-1;
				ImgData imgdate = null;
//					if (CurrentItem==countNumber && countNumber==0){
//						Toast.makeText(this,"只剩下最后一张",Toast.LENGTH_SHORT).show();
//					}else
					if (countNumber>=0){
						imgs.remove(gallery.getCurrentItem());
						imgdate=chooses.remove(gallery.getCurrentItem());
						removePosition.add(imgdate._ID);
						if (countNumber==0){
							onBackPressed();
							}
						else {
							adapter.notifyDataSetChanged();
						}
					}
				photonumber_position.setText(gallery.getCurrentItem() + 1 + "/" + imgs.size());

				break;
				case R.id.edit_photo_rotate:
					int current=gallery.getCurrentItem();
					int rotate = imgs.get(current).oldRotate;
//					imgs.get(current).oldRotate=(rotate+90)==360?0:(rotate+90);
					imgs.get(current).oldRotate=rotate+90;
					imgs.get(current).isChange=1;
//					chooses.get(current).oldRotate=(rotate+90)==360?0:(rotate+90);
					chooses.get(current).oldRotate=rotate+90;
					chooses.get(current).isChange=1;
					adapter.notifyDataSetChanged();
				break;

		default:
			break;
		}
	}



	@Override
	public void onBackPressed() {
			adapter.notifyDataSetChanged();
		Intent intent = new Intent();
		intent.putIntegerArrayListExtra(AlbumSettingBase.DATA_REMOVEPOSITION, (ArrayList<Integer>) removePosition);
		intent.putParcelableArrayListExtra(AlbumSettingBase.DATA_IMAGES, (ArrayList<ImgData>) chooses);
		setResult(RESULT_OK, intent);
		finish();
	}


	private boolean isfirstinpage=true;
	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		if (isfirstinpage && imgs!=null) {
			isfirstinpage=false;
			checkbox.setChecked(imgs.get(arg0).isClick);
		}
	}

	@Override
	public void onPageSelected(int arg0) {
	if (photonumber_position!=null)
		photonumber_position.setText(arg0 + 1 + "/" + imgs.size());
		checkbox.setChecked(imgs.get(arg0).isClick);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
