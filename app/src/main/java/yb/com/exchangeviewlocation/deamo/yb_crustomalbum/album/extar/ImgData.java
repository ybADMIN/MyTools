package yb.com.exchangeviewlocation.deamo.yb_crustomalbum.album.extar;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @Description: TODO(--) 
 * @author wsx - heikepianzi@qq.com 
 * @date 2015-4-3 下午6:13:12 
 */

public class ImgData implements Parcelable{

	public String imgPath;
	public String original="";
	public boolean isClick;
	public Integer _ID=0;
	public int position;//在相册中的位置
	public int oldRotate;//旋转角度
	public int isChange=0;

	
	
	
	
	@Override
	public String toString() {
		return "ImgData [imgPath=" + imgPath + ", isClick=" + isClick
				+ ", _ID=" + _ID + ", position=" + position + "]";
	}
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(imgPath);
		dest.writeString(original);
		dest.writeValue(isClick);
		dest.writeInt(_ID);
		dest.writeInt(position);
		dest.writeInt(oldRotate);
		dest.writeInt(isChange);
	}
	
	public static final Creator<ImgData> CREATOR=new Creator<ImgData>() {
		
		@Override
		public ImgData[] newArray(int size) {
			return new ImgData[size];
		}
		@Override
		public ImgData createFromParcel(Parcel source) {
			ImgData ft=new ImgData();
			ft.imgPath= source.readString();
			ft.original= source.readString();
			ft.isClick= (Boolean) source.readValue(Boolean.class.getClassLoader());
			ft._ID=source.readInt();
			ft.position=source.readInt();
			ft.oldRotate=source.readInt();
			ft.isChange=source.readInt();
			return ft;
		}
	};
}
