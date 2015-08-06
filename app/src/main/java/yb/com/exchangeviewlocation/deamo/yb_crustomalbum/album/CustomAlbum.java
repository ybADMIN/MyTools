package yb.com.exchangeviewlocation.deamo.yb_crustomalbum.album;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import yb.com.exchangeviewlocation.R;
import yb.com.exchangeviewlocation.deamo.yb_crustomalbum.album.adapter.CustomAlbumAdapter;
import yb.com.exchangeviewlocation.deamo.yb_crustomalbum.album.adapter.CustomAlbumItmeAdapter;
import yb.com.exchangeviewlocation.deamo.yb_crustomalbum.album.bitmapProcess.ImageLoader;
import yb.com.exchangeviewlocation.deamo.yb_crustomalbum.album.extar.AlbumException;
import yb.com.exchangeviewlocation.deamo.yb_crustomalbum.album.extar.AlbumSettingBase;
import yb.com.exchangeviewlocation.deamo.yb_crustomalbum.album.extar.CustomAlbumFileTraversal;
import yb.com.exchangeviewlocation.deamo.yb_crustomalbum.album.extar.ImgData;
import yb.com.exchangeviewlocation.deamo.yb_crustomalbum.album.utils.CustomAlbumUtil;

/**
 * 调用相册返回选中的图片对象{@linkplain yb.com.exchangeviewlocation.deamo.yb_crustomalbum.album.extar.ImgData} imgData<br>
 * 返回参数在Intent.getParcelableArrayListExtra (CustomAlbumImageSee.MODE_IMAGES)<br>
 * <b>参数设置</b>
 * 	选择数量：Intent.setExtras().putInt(TAG_CHOOSESIZE,size);
 * @author yb
 *
 */
public class CustomAlbum extends Activity implements CustomAlbumAdapter.OnItemClickCallBack, OnClickListener, CustomAlbumItmeAdapter.OnAlbumItmeClick {

	private CustomAlbumAdapter adapter;
	private GridView gv;
	private CustomAlbumUtil util;
	private TextView chooseAlbum;// 选择相册的文本框
	private ListView pop_lv;// 相册列表
	private PopupWindow pop;// 相册弹出框
	private CustomAlbumItmeAdapter albumItemAadpter;// 相册适配器
	private LinearLayout toolbar;// 下边工具条
	private ArrayList<ImgData> chooseImage = new ArrayList<ImgData>();
	private ArrayList<Integer> removePosition = new ArrayList<Integer>();
	private  int chooseSize = 0;// 控制选择数量
	private boolean isonBackReturn;//判断是否返回选择的图片列表
	/**
	 * 相册列表
	 */
	private List<CustomAlbumFileTraversal> files;
	private TextView finish;
	private AlbumSettingBase setting;
	private boolean emptyChoose=true;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_common_album);
		InitView();
		InitDate();
	}

	/**
	 * 初始化布局
	 */
	private void InitView() {
		findViewById(R.id.left).setOnClickListener(this);
		gv = (GridView) findViewById(R.id.gv_costom_album);
//		gv.setOnScrollListener(new PauseOnScrollListener(ImageLoader.getInstance(3, ImageLoader.Type.LIFO),false,true));
		chooseAlbum = (TextView) findViewById(R.id.tv_choosephoto_costom_album);
		chooseAlbum.setOnClickListener(this);
		toolbar = (LinearLayout) findViewById(R.id.common_album_buttom_toolbar);
		finish = (TextView) findViewById(R.id.tv_choosephoto_costom_finish);
		finish.setOnClickListener(this);
		findViewById(R.id.textView_editImg).setOnClickListener(this);
	}
	private void InitDate() {
//		if (new File(AlbumSettingBase.FILEPATH).getUsableSpace()/1024>1024*1024*2) {
//			
//		}
		
		if (getIntent().hasExtra(AlbumSettingBase.MODE_INITALBUM)) {//初始化相册数据
			setting=(AlbumSettingBase) getIntent().getParcelableExtra(AlbumSettingBase.MODE_INITALBUM);
			this.chooseSize=setting.getChoosesize()>0?setting.getChoosesize():0;
			
			if (setting.getFiles()!=null&&setting.getFiles().size()>0) {
				emptyChoose=true;
				files=setting.getFiles();
			}else if(chooseSize>0 && setting.getChooseImage()!=null){//过滤选择已经选择的数据
				emptyChoose=false;
				util = new CustomAlbumUtil();
				chooseImage=setting.getChooseImage();
				files = util.getChoosedImgFileList(this,chooseImage);
				isonBackReturn=true;
			}else{//默认载入数据
				emptyChoose=true;
				util = new CustomAlbumUtil();
				try {
					files = util.getLocalImgFileList(this);
				} catch (AlbumException e) {
				Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
				}
			}
			
//			gv.setOnScrollListener(new PauseOnScrollListener(ImageLoaderUtil.getOtherConfig(), setting.isPauseOnScroll(), setting.isPauseOnFling()));
		}
		finish.setText("完成("+chooseImage.size()+"/" + chooseSize + ")");
		if (files!=null) {
			adapter = new CustomAlbumAdapter(this, files.get(0).filecontent,files.get(0).filename);
			adapter.setOnItemClickCallBack(this);
			gv.setAdapter(adapter);
		}
	}

	@Override
	public void OnItemClick(View v, int Position, View client) {
		if (client instanceof CheckBox) {
			CheckBox checkBox = (CheckBox) client;
			boolean flag = !checkBox.isChecked();
			if (!checkBox.isChecked() && chooseImage.size() == chooseSize) {
				Toast.makeText(this, "最多选择" + chooseSize + "张", Toast.LENGTH_SHORT).show();
				flag = false;
			}
			if (!checkBox.isChecked() && chooseImage.size() < chooseSize) {
				chooseImage.add(adapter.getData().get(Position));
				for (int i=0 ; i<removePosition.size() ;i++) {//存在效率问题如需改造可以试着将数据removePosition改成map形式
					if (removePosition.get(i).equals(adapter.getData().get(Position)._ID)) {
						removePosition.remove(i);
						--i;
						break;
					}
				}
			} else if (checkBox.isChecked()) {
				for (int i = 0; i < chooseImage.size(); i++) {//存在效率问题如需改造可以试着将数据chooseimgae改成map形式
					if (adapter.getData().get(Position)._ID.equals(chooseImage.get(i)._ID)) {
						removePosition.add(chooseImage.get(i)._ID);
						chooseImage.remove(i);
						--i;
						break;
					}
				}
			}
			adapter.getData().get(Position).isClick = flag;
			checkBox.setChecked(flag);
			finish.setText("完成(" + chooseImage.size() + "/" + chooseSize + ")");
		}else {
			Intent intent = new Intent(this, CustomAlbumImageSee.class);
			AlbumSettingBase asb = new AlbumSettingBase();
			CustomAlbumFileTraversal f = new CustomAlbumFileTraversal();
			f.filename = adapter.getAlbumName();
			f.filecontent = adapter.getData();
			asb.setF(f);
			asb.setChooseImage(chooseImage);
			asb.setPosition(Position);
			intent.putExtra(AlbumSettingBase.MODE_PREVIEW, asb);
			startActivityForResult(intent, 0);
		}

	}

	@Override
	public void onClick(View v) {

		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.tv_choosephoto_costom_album:
			if (adapter.getData().size()==0) {
				return;
			}
			if (pop == null) {// 初始化popwindow
				setPopWindow();
			}
			if (pop.isShowing()) {
				// 隐藏窗口，如果设置了点击窗口外小时即不需要此方式隐藏
				pop.dismiss();
			} else {
				// 显示窗口
				if (albumItemAadpter.getOldposition() != -1) {
					pop_lv.setSelection(albumItemAadpter.getOldposition());
				}
				pop.showAsDropDown(toolbar, 0, 0);
			}
			break;
		case R.id.tv_choosephoto_costom_finish:
			isonBackReturn=true;
			onBackPressed();
			break;
		case R.id.v_mode:
			pop.dismiss();
			break;
		case R.id.left:
			onBackPressed();
			break;
		case R.id.textView_editImg:
			if (chooseImage.size()==0) {
				Toast.makeText(this, "请选择照片", Toast.LENGTH_SHORT).show();
				return;
			}
			AlbumSettingBase asb = new AlbumSettingBase();
			asb.setChooseImage(chooseImage);
			intent = new Intent(this, CustomAlbumImageSee.class);
			intent.putExtra(AlbumSettingBase.MODE_EDITPHOTOFORALBUM	, asb);
			startActivityForResult(intent, 0);
			break;
		default:
			break;
		}
	}
	@Override
	public void onBackPressed() {
		if (isonBackReturn) {
			Intent intent = new Intent();
			intent.putParcelableArrayListExtra(AlbumSettingBase.DATA_IMAGES, chooseImage);
			intent.putIntegerArrayListExtra(AlbumSettingBase.DATA_REMOVEPOSITION, removePosition);
			setResult(RESULT_OK, intent);
		}
		finish();
	}

	/**
	 * 弹出相册选择Popwindown
	 */
	@SuppressWarnings("deprecation")
	private void setPopWindow() {
		LayoutInflater inflater = LayoutInflater.from(this);
		// 引入窗口配置文件
		View view = inflater.inflate(R.layout.activity_common_album_itme, null);
		view.findViewById(R.id.v_mode).setOnClickListener(this);
		pop_lv = (ListView) view.findViewById(R.id.lv_album);
		albumItemAadpter = new CustomAlbumItmeAdapter(this, files);
		albumItemAadpter.setAlbumItmeClick(this);
		pop_lv.setAdapter(albumItemAadpter);
		// 创建PopupWindow对象
		Rect frame = new Rect();
		getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);

		int statusBarHeight = frame.top;
		pop = new PopupWindow(view, android.view.ViewGroup.LayoutParams.MATCH_PARENT, (getResources().getDisplayMetrics().heightPixels - (toolbar.getHeight()
				+ findViewById(R.id.album_top_toolbar).getHeight() + statusBarHeight)), false);
		// 需要设置一下此参数，点击外边可消失
		pop.setBackgroundDrawable(new BitmapDrawable());
		// 设置点击窗口外边窗口消失
		pop.setOutsideTouchable(true);
		// 设置此参数获得焦点，否则无法点击
		pop.setFocusable(true);
	}
	@Override
	protected void onPause() {
		ImageLoader.getInstance().pause();
		super.onPause();
	}
	@Override
	protected void onResume() {
		ImageLoader.getInstance().resume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		ImageLoader.getInstance().resume();
//		ImageLoader.getInstance().getMemoryCache().clear();
		super.onDestroy();
	}

	@Override
	public void ItmeClick(View v, CheckBox cb, CustomAlbumFileTraversal fileTraversal) {
		if (!cb.isChecked()) {
			adapter = null;
			adapter = new CustomAlbumAdapter(this, fileTraversal.filecontent, fileTraversal.filename);
			chooseAlbum.setText(fileTraversal.filename);
			gv.setAdapter(adapter);
			adapter.setOnItemClickCallBack(this);
		}
		pop.dismiss();
		cb.setChecked(true);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data != null && resultCode == RESULT_OK && requestCode == 0) {
			ArrayList<ImgData> chooseimg = data.getParcelableArrayListExtra(AlbumSettingBase.DATA_IMAGES);
			if (!emptyChoose) {
				this.removePosition.addAll(data.getIntegerArrayListExtra(AlbumSettingBase.DATA_REMOVEPOSITION));
			}
			
			for (ImgData imgData : chooseImage) {
				files.get(0).filecontent.get(imgData.position).isClick=false;
			}
			chooseImage.clear();
			for (ImgData imgData : chooseimg) {
				imgData.isClick=true;
				files.get(0).filecontent.get(imgData.position).isClick=true;
			}
			chooseImage.addAll(chooseimg);
			adapter.notifyDataSetChanged();
			finish.setText("完成(" + chooseImage.size() + "/" + chooseSize + ")");
		}
	}
}
