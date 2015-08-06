package yb.com.exchangeviewlocation.deamo.yb_crustomalbum.album.extar;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

//文件的类
@SuppressLint("ParcelCreator")
public class CustomAlbumFileTraversal implements Parcelable {
	public String filename;//所属图片的文件名称
	public List<ImgData> filecontent=new ArrayList<ImgData>();
	
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(filename);
		dest.writeList(filecontent);
	}
	
	public static final Creator<CustomAlbumFileTraversal> CREATOR=new Creator<CustomAlbumFileTraversal>() {
		
		@Override
		public CustomAlbumFileTraversal[] newArray(int size) {
			return new CustomAlbumFileTraversal[size];
		}
		@SuppressWarnings("unchecked")
		@Override
		public CustomAlbumFileTraversal createFromParcel(Parcel source) {
			CustomAlbumFileTraversal ft=new CustomAlbumFileTraversal();
			ft.filename= source.readString();
			ft.filecontent= source.readArrayList(CustomAlbumFileTraversal.class.getClassLoader());
			return ft;
		}
	};

	@Override
	public String toString() {
		return "CustomAlbumFileTraversal [filename=" + filename
				+ ", filecontent=" + filecontent + "]";
	}
	
	
}
