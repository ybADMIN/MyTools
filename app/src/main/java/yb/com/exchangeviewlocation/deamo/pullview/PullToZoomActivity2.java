package yb.com.exchangeviewlocation.deamo.pullview;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import yb.com.exchangeviewlocation.R;

public class PullToZoomActivity2 extends ActionBarActivity {

    private PullToZoomListView2 listView;
    private String[] adapterData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull_to_zoom_activity2);
        listView = (PullToZoomListView2)findViewById(R.id.listview);
        adapterData = new String[] { "Activity","Service","Content Provider","Intent","BroadcastReceiver","ADT","Sqlite3","HttpClient","DDMS","Android Studio","Fragment","Loader","Eclipse","Gradle","XML","Java" };
        listView.setAdapter(new ArrayAdapter<String>(PullToZoomActivity2.this,
                android.R.layout.simple_list_item_1, adapterData));
        listView.getHeaderView().setImageResource(R.mipmap.a1);
        listView.getHeaderView().setScaleType(ImageView.ScaleType.CENTER_CROP);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pull_to_zoom_activity2, menu);
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
