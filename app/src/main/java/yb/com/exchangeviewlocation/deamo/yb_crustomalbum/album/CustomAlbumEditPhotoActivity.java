package yb.com.exchangeviewlocation.deamo.yb_crustomalbum.album;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;

import java.util.ArrayList;
import java.util.List;

import yb.com.exchangeviewlocation.R;
import yb.com.exchangeviewlocation.deamo.yb_crustomalbum.album.adapter.CustomAlbum_ViewPageAdapter;
import yb.com.exchangeviewlocation.deamo.yb_crustomalbum.album.bitmapProcess.ImageLoader;
import yb.com.exchangeviewlocation.deamo.yb_crustomalbum.album.extar.AlbumSettingBase;
import yb.com.exchangeviewlocation.deamo.yb_crustomalbum.album.extar.CustomAlbumFileTraversal;
import yb.com.exchangeviewlocation.deamo.yb_crustomalbum.album.extar.ImgData;
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
public class CustomAlbumEditPhotoActivity extends FragmentActivity implements OnClickListener ,OnPageChangeListener {
    public static int screenWidth;
    public static int screenHeight;
    private CustomAlbumFileTraversal files;
    private ViewPager gallery;
    private CustomAlbum_ViewPageAdapter adapter;
    private ArrayList<Integer> removePosition = new ArrayList<Integer>();
    private List<ImgData> imgs;
    private AlbumSettingBase setting;
    private CheckBox checkbox;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_album_edit_photo);
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
        if (setting.isExtarChoose()){
            findViewById(R.id.RelativeLayout1).setVisibility(View.GONE);
        }
        adapter.setExtarChoose(setting.isExtarChoose());
        adapter.setData(imgs);
        gallery.setCurrentItem(setting.getPosition());//选择照片位置
        setting=null;

    }

    private void initViews() {
        Rect frame = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        screenWidth = getResources().getDisplayMetrics().widthPixels;
        screenHeight =  getResources().getDisplayMetrics().heightPixels- DensityUtils.dip2px(this, 52)-statusBarHeight;
        gallery=(ViewPager)findViewById(R.id.pic_gallery);
        checkbox=((CheckBox)findViewById(R.id.top_checkBox));
        checkbox.setOnClickListener(this);
        gallery.setOnPageChangeListener(this);
        adapter=new CustomAlbum_ViewPageAdapter(getSupportFragmentManager());
        gallery.setAdapter(adapter);
        findViewById(R.id.left).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left:
                onBackPressed();
                break;
            case R.id.top_checkBox:
                imgs.get(gallery.getCurrentItem()).isClick=checkbox.isChecked();//选择照片的状态改变
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        ImageLoader.getInstance().getMemoryCache();
        for (int i=0;i<imgs.size();i++){
            if (!imgs.get(i).isClick){
               removePosition.add(imgs.remove(i)._ID);
                --i;
            }
        }
        adapter.notifyDataSetChanged();
        Intent intent = new Intent();
        intent.putIntegerArrayListExtra(AlbumSettingBase.DATA_REMOVEPOSITION, removePosition);
        intent.putParcelableArrayListExtra(AlbumSettingBase.DATA_IMAGES, (ArrayList<ImgData>) imgs);
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
        checkbox.setChecked(imgs.get(arg0).isClick);
    }
}
