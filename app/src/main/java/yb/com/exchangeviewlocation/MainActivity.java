package yb.com.exchangeviewlocation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import yb.com.exchangeviewlocation.deamo.Toolbar.About_US_framentActivity;
import yb.com.exchangeviewlocation.deamo.adapter.MainAdapter;
import yb.com.exchangeviewlocation.deamo.adapter.bean.ViewModel;
import yb.com.exchangeviewlocation.deamo.pullview.PullToZoomActivity;
import yb.com.exchangeviewlocation.deamo.pullview.PullToZoomActivity2;
import yb.com.exchangeviewlocation.deamo.readDot.DotActivity;
import yb.com.exchangeviewlocation.deamo.skin.SkinMainActivity;
import yb.com.exchangeviewlocation.utils.BadgeUtil;


public class MainActivity extends ActionBarActivity implements View.OnClickListener,MainAdapter.RecyclerOnItemClickListener{
    private RecyclerView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list=(RecyclerView)findViewById(R.id.list);
        list.setHasFixedSize(true);
        MainAdapter adapter = new MainAdapter(createMockList(), R.layout.main_itme_cardview);
        list.setAdapter(adapter);
        adapter.setListener(this);
        BadgeUtil.setBadgeCount(this,50);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setItemAnimator(new DefaultItemAnimator());
    }

    private List<ViewModel> createMockList() {
        String[] name = new String[]{"ListView头部下拉放大","下拉显示底部图层","自定义底部Bar","自定义相册","皮肤更换1","小红点","",""};


        List<ViewModel> list = new ArrayList();
        for (int i = 0; i < name.length; i++) {
            ViewModel model =new ViewModel();
            model.card_name=name[i];
            model.imgname="a"+(i+1);
            list.add(model);
        }
        return list;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void ItmeclickListener(MainAdapter.ViewHolder tag) {
        Intent intent = new Intent();
        boolean isDo = false;
        switch (tag.getPosition()){
            case 0:
                isDo=true;
                intent.setClass(this, PullToZoomActivity.class);
            break;
            case 1:
                isDo=true;
                intent.setClass(this, PullToZoomActivity2.class);
                break;
            case 2:
                isDo=true;
                intent.setClass(this, About_US_framentActivity.class);
                break;
            case 3:
                isDo=true;
                intent.setClass(this, yb.com.exchangeviewlocation.deamo.yb_crustomalbum.MainActivity.class);
                break;
            case 4:
                isDo=true;
                intent.setClass(this, SkinMainActivity.class);
                break;
            case 5:
                isDo=true;
                intent.setClass(this, DotActivity.class);
                break;
        }
        if (isDo)
        startActivity(intent);
    }
}
