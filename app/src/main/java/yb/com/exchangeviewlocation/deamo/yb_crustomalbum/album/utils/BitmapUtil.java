package yb.com.exchangeviewlocation.deamo.yb_crustomalbum.album.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class BitmapUtil {
	
	
	
	 public static Bitmap setOption(String path){
			return decodeBitmap(null,path,null);
	   }
	 
	 public static Bitmap setOption(String path,int[] xy){
		 return decodeBitmap(null,path,xy);
	 }
	 
	 
	 private static Bitmap decodeBitmap(Bitmap orbitmap,String path,int[] xy){
		 
		 BitmapFactory.Options options = new BitmapFactory.Options();
		 Bitmap bitmap;
		 int w;
		 int h;
		 if (orbitmap!=null) {
			bitmap=orbitmap;
			w=bitmap.getWidth();
			h=bitmap.getHeight();
		}else{
			options.inJustDecodeBounds = true;
			bitmap = BitmapFactory.decodeFile(path, options);
			w = options.outWidth;
			h = options.outHeight;
		}
		 // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
		 float hh;// 这里默认高度为800f
		 float ww;// 这里默认宽度为480f
		 if (xy!=null) {
			ww=xy[0];
			hh=xy[1];
		}else{
			hh=800f;
			ww=400f;
		}
		 
		 // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		 int be = 1;// be=1表示不缩放
		 if (w>ww||h>hh) {
			 if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
				 be = (int) (options.outWidth / ww);
			 } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
				 be = (int) (options.outHeight / hh);
			 }
		 }
		 if (be <= 0)
			 be = 1;
		 options.inSampleSize = be;// 设置缩放比例
		 options.inJustDecodeBounds = false;
		 bitmap =  BitmapFactory.decodeFile(path, options);
		 return bitmap;
	 }

	/**
	 * @deprecated  use saveThumbnailImage(Bitmap bitmap,String path)
	 * @param bitmap
	 * @param path
	 * @return
	 * @throws IOException
	 */

	@Deprecated
	public static Bitmap saveImage(Bitmap bitmap,String path) throws IOException{
		if(bitmap != null){
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int quality = 100;  //--设置压缩大小
			int fileSize = 500;
			bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);

			while (baos.toByteArray().length / 1024 > fileSize) {
				quality -= 10;
				if (quality < 50) {
					fileSize += 50;
					quality = 100;
				}else{
					baos.reset();
					bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
				}
			}
			File f=new File(path);
			if (f.exists()) {
				f.delete();
			}
			FileOutputStream fos = new FileOutputStream(f);
			fos.write(baos.toByteArray());
			fos.flush();
			fos.close();
		}
		return bitmap;
	}
	public static Bitmap saveThumbnailImage(Bitmap bitmap,String path) throws IOException{
		  if(bitmap != null){
			  ByteArrayOutputStream baos = new ByteArrayOutputStream();
			  int quality = 100;  //--设置压缩大小
			  int fileSize = 50;
			  bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);


			  while (baos.toByteArray().length / 1024 > fileSize) {
				  quality -= 10;
				  if (quality < 50) {
					  fileSize +=20;
					  quality = 100;
					  continue;
				  }
				  baos.reset();
				  bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
			  }
			  File f=new File(path);
			  if (f.exists()) {
				  f.delete();
			  }
			  FileOutputStream fos = new FileOutputStream(f);
			  fos.write(baos.toByteArray());
			  fos.flush();
			  fos.close();
		  }
		  return bitmap;
	  }

	  
		public static Bitmap imageRotate(String originalPath,String TargetPath,int mRotate){
			return BitmapRotate(null, originalPath, TargetPath, mRotate);
		}
		
		
		public static Bitmap imageRotate(Bitmap bitmap,int mRotate){
			return BitmapRotate(bitmap, null, null, mRotate);
		}
		
		
		public static Bitmap imageRotate(Bitmap bitmap,int mRotate ,String TargetPath){
			return BitmapRotate(bitmap, null, TargetPath, mRotate);
		}
		
		public static Bitmap imageRotate(String originalPath,int mRotate ,String TargetPath){
			return BitmapRotate(null, originalPath, TargetPath, mRotate);
		}
		
		
		
		public static Bitmap BitmapRotate(Bitmap orBitmap,String originalPath,String TargetPath,int mRotate){
			Bitmap mBitmap;
			if (orBitmap==null) {
				mBitmap = setOption(originalPath);
			}else
				mBitmap=orBitmap;
			if (mRotate > 0) {
				int width = mBitmap.getWidth();
				int height = mBitmap.getHeight();
				Matrix m = new Matrix();
				m.setRotate(mRotate);
				try {
					mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, width, height,
							m, true);
				} catch (OutOfMemoryError ooe) {
					m.postScale((float) 1 / 1, (float) 1 / 1);
					mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, width, height,
							m, true);
				}
			}
			try {
				if (TargetPath!=null) {
					saveThumbnailImage(mBitmap, TargetPath);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			return mBitmap;
		}
}
