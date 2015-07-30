package yb.com.exchangeviewlocation.deamo;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import yb.com.exchangeviewlocation.R;
import yb.com.exchangeviewlocation.deamo.ui.ListviewHeaderImage;


public class ListViewHeaderImageActivity extends ActionBarActivity {

    private ListviewHeaderImage lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view_header_image);
        init();
    }

    private void init() {
        String[] from = new String[]{"title"};
        int[] to = new int[]{R.id.itme_tv};
        lv=(ListviewHeaderImage)findViewById(R.id.header_image_listview);
        List<Map<String,Integer>> map = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Map<String,Integer> m=new HashMap<>();
            m.put("title",i);
            map.add(m);
        }

        SimpleAdapter adapter = new SimpleAdapter(this,map,R.layout.list_itme,from,to);
        lv.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list_view_header_image, menu);
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
}
