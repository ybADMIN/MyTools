package yb.com.exchangeviewlocation.deamo.adapter.bean;

import android.content.Context;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/7/30.
 */
public class ViewModel implements Serializable{
    public String card_name="";
    public String imgname="";

    @Override
    public String toString() {
        return "ViewModel{" +
                "card_name='" + card_name + '\'' +
                ", imgID=" + imgname +
                '}';
    }
    public int getImageResourceId( Context context )
    {
        try
        {
            return context.getResources().getIdentifier(this.imgname, "mipmap", context.getPackageName());

        }
        catch (Exception e)
        {
            e.printStackTrace();
            return -1;
        }
    }
}
