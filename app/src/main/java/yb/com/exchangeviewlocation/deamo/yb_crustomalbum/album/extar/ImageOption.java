package yb.com.exchangeviewlocation.deamo.yb_crustomalbum.album.extar;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @Description: TODO(--) 
 * @author wsx - heikepianzi@qq.com 
 * @date 2015-4-3 下午6:13:12 
 */

public class ImageOption implements Parcelable{
	public int rotate;//旋转角度
	public ImageOption(int roate) {
		this.rotate = roate;
	}
	public ImageOption() {
	}
	
	@Override
	public String toString() {
		return "ImageOption [rotate=" + rotate + "]";
	}
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(rotate);
	}
	
	public static final Creator<ImageOption> CREATOR=new Creator<ImageOption>() {
		
		@Override
		public ImageOption[] newArray(int size) {
			return new ImageOption[size];
		}
		@Override
		public ImageOption createFromParcel(Parcel source) {
			ImageOption ft=new ImageOption();
			ft.rotate=source.readInt();
			return ft;
		}
	};
}
