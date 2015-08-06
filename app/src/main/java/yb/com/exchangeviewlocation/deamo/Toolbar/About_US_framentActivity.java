package yb.com.exchangeviewlocation.deamo.Toolbar;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;

import java.util.ArrayList;
import java.util.List;

import yb.com.exchangeviewlocation.R;


public class About_US_framentActivity extends FragmentActivity implements OnClickListener,OnPageChangeListener {
    private ViewPager mViewPager;
    private List<Fragment> fragmentList = new ArrayList<>();
    public  final String[] TITLE = new String[]{
            "企业简介","工程案列","工程师","招贤纳才"
    };
    private  About_Us_FramentAdapter adapter;
    private List<WJHToolbar> toobarItme = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about__us_frament);
        initView();
        initDate();
    }

    private void initDate() {
        for (int i =0 ;i<TITLE.length;i++){
            About_US_Frament frament = new About_US_Frament();
            Bundle bundle = new Bundle();
            bundle.putString("url",TITLE[i]);
            frament.setArguments(bundle);
            fragmentList.add(frament);
        }
        adapter.setmFragments(fragmentList);
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(0);
    }

    private void initView() {
        mViewPager= (ViewPager) findViewById(R.id.about_us_vp);
        mViewPager.setOnPageChangeListener(this);
        adapter = new About_Us_FramentAdapter(this.getSupportFragmentManager());
        WJHToolbar company_profile = (WJHToolbar) findViewById(R.id.about_us_tools_company_profile);
        WJHToolbar engineering_case = (WJHToolbar) findViewById(R.id.about_us_tools_engineering_case);
        WJHToolbar engineers = (WJHToolbar) findViewById(R.id.about_us_tools_engineers);
        WJHToolbar talent_recruitment = (WJHToolbar) findViewById(R.id.about_us_tools_talent_recruitment);
        toobarItme.add(company_profile);
        toobarItme.add(engineering_case);
        toobarItme.add(engineers);
        toobarItme.add(talent_recruitment);
        company_profile.setIconAlpha(1.0f);
    }


    @Override
    public void onClick(View v) {

    }

    //导航栏的Itme点击事件
    public void toobarClick(View v){
        resetState();
        switch (v.getId()){
            case R.id.about_us_tools_company_profile:
                toobarItme.get(0).setIconAlpha(1.0f);
                toobarItme.get(0).setCircleTranslation(0.0f);
                mViewPager.setCurrentItem(0,false);

                break;
            case R.id.about_us_tools_engineering_case:
                toobarItme.get(1).setIconAlpha(1.0f);
                toobarItme.get(1).setCircleTranslation(0.0f);
                mViewPager.setCurrentItem(1,false);
                break;
            case R.id.about_us_tools_engineers:
                toobarItme.get(2).setIconAlpha(1.0f);
                toobarItme.get(2).setCircleTranslation(0.0f);
                mViewPager.setCurrentItem(2,false);
                break;
            case R.id.about_us_tools_talent_recruitment:
                toobarItme.get(3).setIconAlpha(1.0f);
                toobarItme.get(3).setCircleTranslation(0.0f);
                mViewPager.setCurrentItem(3,false);
                break;
        }
    }

    private void resetState() {
        for (int i = 0; i < toobarItme.size(); i++) {
            toobarItme.get(i).setIconAlpha(0.0f);
            toobarItme.get(i).setCircleTranslation(0.0f);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        if (positionOffset>0){
            WJHToolbar lift = toobarItme.get(position);
            WJHToolbar right = toobarItme.get(position+1);

            //注意：Xml布局文件中属性对应
            // 1.tb_modo="onaly_color"设置后需要设置tb_choose_color
            //2. tb_modo="onaly_circle_bg"设置后需要设置 tb_circle_bg
            //3. 如果设置tb_modo="both" 需要设置上面两种属性

           /**
            * 设置布局属性tb_modo为 onaly_circle_bg 或者 both
            *  只有平移动画
            */
            lift.setOnalyCircleTranslation(positionOffset);
            right.setOnalyCircleTranslation(positionOffset-1);

            /**
             *  设置布局属性tb_modo为 onaly_color 或者 both
             * Alpha动画**/
//            lift.setIconAlpha(1-positionOffset);
//            right.setIconAlpha(positionOffset);
            /**两种动画都有
             * 设置布局属性tb_modo为  任何一种都可以
             * **/
//            lift.setCircleTranslationAndAlpha(positionOffset,true);
//            right.setCircleTranslationAndAlpha(positionOffset,false);

        }
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }


    private  class  About_Us_FramentAdapter extends FragmentPagerAdapter {

        List<Fragment> mFragments  = new ArrayList<>();

        public About_Us_FramentAdapter(FragmentManager fm,  List<Fragment> fragments) {
            super(fm);
            this.mFragments=fragments;
        }

        public About_Us_FramentAdapter(FragmentManager fm) {
            super(fm);
        }

        public void setmFragments(List<Fragment> mFragments) {
            this.mFragments = mFragments;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }
}
