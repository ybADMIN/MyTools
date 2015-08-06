package yb.com.exchangeviewlocation.deamo.skin;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.simon.catkins.skin.SkinService;
import com.simon.catkins.skin.external.ExternalSkin;
import com.simon.catkins.skin.impl.NightSkin;

import yb.com.exchangeviewlocation.R;

public class SkinMainActivity extends Activity {

    private ExternalSkin mExternalSkin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getLayoutInflater().setFactory(SkinService.getInflatorFactory(this));
        super.onCreate(savedInstanceState);

        mExternalSkin = new ExternalSkin(Environment.getExternalStorageDirectory()+"/main.apk",
                getResources().getDisplayMetrics(),
                getResources().getConfiguration());

        SkinService.addSkin(mExternalSkin);

        setContentView(R.layout.skin_activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SkinService.applySkin(this, ExternalSkin.NAME);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mExternalSkin.updateConfiguration(getResources().getDisplayMetrics(), newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_settings:
//                SkinFactory.getFactory().unregister( ExternalSkin.NAME);
//                mExternalSkin = new ExternalSkin(Environment.getExternalStorageDirectory()+"/app-release.apk",
//                        getResources().getDisplayMetrics(),
//                        getResources().getConfiguration());
                SkinService.addSkin(new NightSkin());
                SkinService.applySkin(this, NightSkin.NAME);
                break;
        }
        return super.onMenuItemSelected(featureId, item);
    }

    public void tvclick(View view) {
    }
}
