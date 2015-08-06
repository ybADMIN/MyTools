package yb.com.exchangeviewlocation.deamo.Toolbar;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Administrator on 2015/6/30.
 */
public class About_US_Frament extends Fragment {
    private String url;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Bundle parms = getArguments();
        if (parms!=null) {
            url = parms.getString("url");
        }
        System.out.println("url:yb"+url);
        TextView tv = new TextView(this.getActivity());
        tv.setText(url);
        tv.setTextSize(20);
        tv.setBackgroundColor(Color.parseColor("#88333399"));
        return tv;
    }
}
