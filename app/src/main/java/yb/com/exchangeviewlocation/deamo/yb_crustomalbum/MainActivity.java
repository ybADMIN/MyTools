package yb.com.exchangeviewlocation.deamo.yb_crustomalbum;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import java.util.ArrayList;

import yb.com.exchangeviewlocation.R;
import yb.com.exchangeviewlocation.deamo.yb_crustomalbum.album.CustomAlbum;
import yb.com.exchangeviewlocation.deamo.yb_crustomalbum.album.CustomAlbumImageSee;
import yb.com.exchangeviewlocation.deamo.yb_crustomalbum.album.adapter.AddPhotoAdapter;
import yb.com.exchangeviewlocation.deamo.yb_crustomalbum.album.bitmapProcess.ImageLoader;
import yb.com.exchangeviewlocation.deamo.yb_crustomalbum.album.extar.AlbumSettingBase;
import yb.com.exchangeviewlocation.deamo.yb_crustomalbum.album.extar.ImgData;
import yb.com.exchangeviewlocation.deamo.yb_crustomalbum.album.uitools.CustomGridView;

public class MainActivity extends Activity implements OnItemClickListener,AddPhotoAdapter.PhotoDeleteOnclickListener{

	private static final int ALBUM_CODE = 0;
	private static final int EDITALBUM=1;
	private ArrayList<ImgData> chooseImage= new ArrayList<>();
	private CustomGridView gv;
	private AddPhotoAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.toobar_activity_main);
		initView();
		initDate();
		Toast.makeText(this, "启动", Toast.LENGTH_SHORT).show();

//		findViewById(R.id.sss).setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				Intent intent = new Intent(
//						getApplicationContext(),
//						CustomAlbum.class);
//				AlbumSettingBase asb = new AlbumSettingBase();
//				asb.setChoosesize(100);
////				if (liLayout.getChildCount()>0) {
//					asb.setChooseImage(chooseImage);// 如果已经有选择的照片将其传入避免选择相同照片
////				}
//				intent.putExtra(
//						AlbumSettingBase.MODE_INITALBUM, asb);
//				startActivityForResult(intent, ALBUM_CODE);
//				
//			}
//		});;
	}
	private void initDate() {
		adapter.setDeleteOnclickListener(this);
		gv.setAdapter(adapter);
	}

	@Override
	protected void onDestroy() {
//		File targedir = CustomAlbumUtil.getOwnCacheDirectory(this, "Android/data/" + this.getPackageName() + "/cache/" + "/upImg");
		ImageLoader.getInstance().stop();
//		android.os.Process.killProcess(android.os.Process.myPid());
		super.onDestroy();
	}

	private void initView() {
		gv=(CustomGridView)findViewById(R.id.photo_main_gv);
		gv.setOnItemClickListener(this);
		adapter = new AddPhotoAdapter(this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data==null || resultCode!=RESULT_OK)
			return;

		switch (requestCode){
			case ALBUM_CODE:
				chooseImage=data.getParcelableArrayListExtra(AlbumSettingBase.DATA_IMAGES);

//		new AddPhotoAsyncTask().execute(imgs,adapter);
				adapter.getUri().clear();
				adapter.setUri(chooseImage);
				adapter.notifyDataSetChanged();

				data.getIntegerArrayListExtra(AlbumSettingBase.DATA_REMOVEPOSITION);
				break;
			case EDITALBUM:
				chooseImage=data.getParcelableArrayListExtra(AlbumSettingBase.DATA_IMAGES);

//		new AddPhotoAsyncTask().execute(imgs,adapter);
				adapter.getUri().clear();
				adapter.setUri(chooseImage);
				adapter.notifyDataSetChanged();

				data.getIntegerArrayListExtra(AlbumSettingBase.DATA_REMOVEPOSITION);
				break;
		}
//		if (data != null && resultCode == RESULT_OK && requestCode == 0) {
//			ArrayList<ImgData> chooseimg = data.getParcelableArrayListExtra(AlbumSettingBase.DATA_IMAGES);
//			if (!emptyChoose) {
//				this.removePosition.addAll(data.getIntegerArrayListExtra(AlbumSettingBase.DATA_REMOVEPOSITION));
//			}
//
//			for (ImgData imgData : chooseImage) {
//				files.get(0).filecontent.get(imgData.position).isClick=false;
//			}
//			chooseImage.clear();
//			for (ImgData imgData : chooseimg) {
//				imgData.isClick=true;
//				files.get(0).filecontent.get(imgData.position).isClick=true;
//			}
//			chooseImage.addAll(chooseimg);
//			adapter.notifyDataSetChanged();
//			finish.setText("完成(" + chooseImage.size() + "/" + chooseSize + ")");
//		}

		super.onActivityResult(requestCode, resultCode, data);
	}
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		ArrayList<ImgData> chooseImage= (ArrayList<ImgData>) this.chooseImage.clone();
		Intent intent;
		if (position==0) {
			 intent = new Intent(
					getApplicationContext(),
					CustomAlbum.class);
			AlbumSettingBase asb = new AlbumSettingBase();
			asb.setChoosesize(100);
//			if (liLayout.getChildCount()>0) {
			if (chooseImage.size()>0) {
				chooseImage.remove(0);
			}
			asb.setChooseImage(chooseImage);// 如果已经有选择的照片将其传入避免选择相同照片
//			}
			intent.putExtra(
					AlbumSettingBase.MODE_INITALBUM, asb);
			startActivityForResult(intent, ALBUM_CODE);
		}else{
			ImageLoader.getInstance().resume();
			AlbumSettingBase asb = new AlbumSettingBase();
			chooseImage.remove(0);
			asb.setChooseImage(chooseImage);
			asb.setPosition(position - 1);
			asb.setExtarChoose(true);
			intent = new Intent(this, CustomAlbumImageSee.class);
			intent.putExtra(AlbumSettingBase.MODE_EDITPHOTOFORALBUM	, asb);
			startActivityForResult(intent, EDITALBUM);
		}
	}



	@Override
	public void deleteItme(View view, int position) {
		adapter.getUri().remove(position);
		adapter.notifyDataSetChanged();
	}
}
