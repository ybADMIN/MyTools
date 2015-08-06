package yb.com.exchangeviewlocation.deamo.yb_crustomalbum.album.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import yb.com.exchangeviewlocation.deamo.yb_crustomalbum.album.extar.AlbumException;
import yb.com.exchangeviewlocation.deamo.yb_crustomalbum.album.extar.CustomAlbumFileTraversal;
import yb.com.exchangeviewlocation.deamo.yb_crustomalbum.album.extar.ImgData;
import yb.com.exchangeviewlocation.deamo.yb_crustomalbum.album.extar.L;

import static android.os.Environment.MEDIA_MOUNTED;


public class CustomAlbumUtil {
	
	public final static String SDCARDPATH = Environment.getExternalStorageDirectory().getAbsolutePath();
	/**
	 * 获取全部图片地址
	 * @return
	 * @throws AlbumException
	 */
	public ArrayList<ImgData>  listAlldir(Context context) throws AlbumException{
		MediaScannerConnection.scanFile(context, new String[]{SDCARDPATH}, null, null);//更新图库
    	Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
    	Uri uri = intent.getData();
    	ArrayList<ImgData> list = new ArrayList<ImgData>();
    	String[] proj ={MediaColumns.DATA,BaseColumns._ID};
    	Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);//managedQuery(uri, proj, null, null, null);
    	if (cursor==null) {
			throw new AlbumException("空间不足");
		}
    	while(cursor.moveToNext()){
    		String path =cursor.getString(0);
    		Integer integer = cursor.getInt(1);
    		ImgData imgData = new ImgData();
    		imgData.imgPath = new File(path).getAbsolutePath();
    		imgData._ID=integer;
    		imgData.position=cursor.getPosition();
    		list.add(imgData);
    	}
    	cursor.close();
		return list;
    }
	/**
	 * 某张图片的信息
	 * @return
	 * @throws AlbumException 
	 */
	public ImgData search(Context context,Uri uri) throws AlbumException{
		//TODO未完待续，需要拍照时传入照片路径或者照片ID
		MediaScannerConnection.scanFile(context, new String[]{SDCARDPATH}, null, null);//更新图库
//		Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//		Uri uri = intent.getData();
		String[] proj ={MediaColumns.DATA,BaseColumns._ID};
		Cursor cursor = context.getContentResolver().query(uri, proj, null, null,null);//managedQuery(uri, proj, null, null, null);
		if (cursor==null) {
			throw new AlbumException("空间不足");
		}
		cursor.moveToLast();
		ImgData imgData = new ImgData();
			String path =cursor.getString(0);
			Integer integer = cursor.getInt(1);
			imgData.imgPath = new File(path).getAbsolutePath();
			imgData._ID=integer;
			imgData.isClick=true;
			imgData.position=cursor.getPosition();
		cursor.close();
		return imgData;
	}
	/**
	 * 得到相册列表
	 * @param context
	 * @return
	 * @throws AlbumException 
	 */
	public List<CustomAlbumFileTraversal> getLocalImgFileList(Context context) throws AlbumException{
		List<ImgData> allimglist=listAlldir(context);
		return 	getAlbumForImages(allimglist);
	}
	/**
	 * 过滤已选择的图片将图片的ischeck设置成true
	 * @param context
	 * @param Choosed
	 * @return
	 */
	public List<CustomAlbumFileTraversal> getChoosedImgFileList(Context context,List<ImgData> Choosed){
		List<ImgData> allimglist=listAlldir(context,Choosed);
		return 	getAlbumForImages(allimglist);
	}
	
	/**
	 * 过滤已选择的相片
	 * @param context
	 * @return
	 */
	public List<ImgData> listAlldir(Context context,List<ImgData> Choosed){
		MediaScannerConnection.scanFile(context, new String[]{SDCARDPATH}, null, null);//更新图库
    	Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
    	Uri uri = intent.getData();
    	ArrayList<ImgData> list = new ArrayList<ImgData>();
    	String[] proj ={MediaColumns.DATA,BaseColumns._ID};
    	Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);//managedQuery(uri, proj, null, null, null);
    	HashMap<Integer, Boolean> filter_map=new HashMap<Integer, Boolean>();
    	
    	
		for (ImgData imgData : Choosed) {
			filter_map.put(imgData._ID, imgData.isClick);
		}
		
    	while(cursor.moveToNext()){
    		String path =cursor.getString(0);
    		Integer integer = cursor.getInt(1);
    		ImgData imgData = new ImgData();
    		imgData.imgPath = new File(path).getAbsolutePath();
    		imgData._ID=integer;
    		imgData.position=cursor.getPosition();
    		Boolean ischeck = filter_map.get(integer);
    		if (ischeck!=null&&ischeck) {
    			imgData.isClick=ischeck;
			}
    		list.add(imgData);
    	}
		return list;
	}
	
	/*通过数据列表返回处理后的相册
	 */
	private List<CustomAlbumFileTraversal> getAlbumForImages(List<ImgData>allimglist){
		LinkedList<CustomAlbumFileTraversal> data=new LinkedList<CustomAlbumFileTraversal>();
		String filename="";
		if (allimglist!=null) {
			Set<String> set = new TreeSet<String>();
			String []str;
			for (int i = 0; i < allimglist.size(); i++) {
				set.add(getfileinfo(allimglist.get(i).imgPath));
			}
			str= set.toArray(new String[0]);
			for (int i = 0; i < str.length; i++) {
				filename=str[i];
				CustomAlbumFileTraversal ftl= new CustomAlbumFileTraversal();
				ftl.filename=filename;
				data.add(ftl);
			}
			
			for (int i = 0; i < data.size(); i++) {
				for (int j = 0; j < allimglist.size(); j++) {
					if (data.get(i).filename.equals(getfileinfo(allimglist.get(j).imgPath))) {
						data.get(i).filecontent.add(allimglist.get(j));
					}
				}
			}
		}
		CustomAlbumFileTraversal allimg = new CustomAlbumFileTraversal();
		allimg.filename="所有照片";
		allimg.filecontent=allimglist;
		data.addFirst(allimg);
		return data;
		}
    /**
     * 根据指定的图像路径和大小来获取缩略图 此方法有两点好处： 1.
     * 使用较小的内存空间，第一次获取的bitmap实际上为null，只是为了读取宽度和高度，
     * 第二次读取的bitmap是根据比例压缩过的图像，第三次读取的bitmap是所要的缩略图。 2.
     * 缩略图对于原图像来讲没有拉伸，这里使用了2.2版本的新工具ThumbnailUtils，使 用这个工具生成的图像不会被拉伸。
     * 
     * @param imagePath
     *            图像的路径
     * @param width
     *            指定输出图像的宽度
     * @param height
     *            指定输出图像的高度
     * @return 生成的缩略图
     */
    public static Bitmap getImageThumbnail(String imagePath, int width, int height)
    {
    Bitmap bitmap = null;
    BitmapFactory.Options options = new BitmapFactory.Options();
    options.inJustDecodeBounds = true;
    // 获取这个图片的宽和高，注意此处的bitmap为null
    bitmap = BitmapFactory.decodeFile(imagePath, options);
    options.inJustDecodeBounds = false; // 设为 false
    // 计算缩放比
    int h = options.outHeight;
    int w = options.outWidth;
    int beWidth = w / width;
    int beHeight = h / height;
    int be = 1;
    if (beWidth < beHeight)
    {
        be = beWidth;
    }
    else
    {
        be = beHeight;
    }
    if (be <= 0)
    {
        be = 1;
    }
    options.inSampleSize = be;
    // 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
    bitmap = BitmapFactory.decodeFile(imagePath, options);
    // 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
    bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
        ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
    return bitmap;
    }

	
	
	public static String getfileinfo(String data){
		String filename[]= data.split("/");
		if (filename!=null) {
			return filename[filename.length-2];
		}
		return null;
	}
	
	public static String getFileName(String data){
		String filename[]= data.split("/");
		if (filename!=null) {
			return filename[filename.length-1];
		}
		return null;
	}
	
	/*
	 * 缓存路径
	 */
	private static final String EXTERNAL_STORAGE_PERMISSION = "android.permission.WRITE_EXTERNAL_STORAGE";
	private static final String INDIVIDUAL_DIR_NAME = "Album";
	/**
	 * Returns application cache directory. Cache directory will be created on SD card
	 * <i>("/Android/data/[app_package_name]/cache")</i> if card is mounted and app has appropriate permission. Else -
	 * Android defines cache directory on device's file system.
	 *
	 * @param context Application context
	 * @return Cache {@link File directory}.<br />
	 * <b>NOTE:</b> Can be null in some unpredictable cases (if SD card is unmounted and
	 * {@link Context#getCacheDir() Context.getCacheDir()} returns null).
	 */
	public static File getCacheDirectory(Context context) {
		return getCacheDirectory(context, true);
	}

	/**
	 * Returns application cache directory. Cache directory will be created on SD card
	 * <i>("/Android/data/[app_package_name]/cache")</i> (if card is mounted and app has appropriate permission) or
	 * on device's file system depending incoming parameters.
	 *
	 * @param context        Application context
	 * @param preferExternal Whether prefer external location for cache
	 * @return Cache {@link File directory}.<br />
	 * <b>NOTE:</b> Can be null in some unpredictable cases (if SD card is unmounted and
	 * {@link Context#getCacheDir() Context.getCacheDir()} returns null).
	 */
	public static File getCacheDirectory(Context context, boolean preferExternal) {
		File appCacheDir = null;
		String externalStorageState;
		try {
			externalStorageState = Environment.getExternalStorageState();
		} catch (NullPointerException e) { // (sh)it happens (Issue #660)
			externalStorageState = "";
		}
		if (preferExternal && MEDIA_MOUNTED.equals(externalStorageState) && hasExternalStoragePermission(context)) {
			appCacheDir = getExternalCacheDir(context);
		}
		if (appCacheDir == null) {
			appCacheDir = context.getCacheDir();
		}
		if (appCacheDir == null) {
			String cacheDirPath = "/data/data/" + context.getPackageName() + "/cache/";
			L.w("Can't define system cache directory! '%s' will be used.", cacheDirPath);
			appCacheDir = new File(cacheDirPath);
		}
		return appCacheDir;
	}

	/**
	 * Returns individual application cache directory (for only image caching from ImageLoader). Cache directory will be
	 * created on SD card <i>("/Android/data/[app_package_name]/cache/uil-images")</i> if card is mounted and app has
	 * appropriate permission. Else - Android defines cache directory on device's file system.
	 *
	 * @param context Application context
	 * @return Cache {@link File directory}
	 */
	public static File getIndividualCacheDirectory(Context context) {
		File cacheDir = getCacheDirectory(context);
		File individualCacheDir = new File(cacheDir, INDIVIDUAL_DIR_NAME);
		if (!individualCacheDir.exists()) {
			if (!individualCacheDir.mkdir()) {
				individualCacheDir = cacheDir;
			}
		}
		return individualCacheDir;
	}

	/**
	 * Returns specified application cache directory. Cache directory will be created on SD card by defined path if card
	 * is mounted and app has appropriate permission. Else - Android defines cache directory on device's file system.
	 *
	 * @param context  Application context
	 * @param cacheDir Cache directory path (e.g.: "AppCacheDir", "AppDir/cache/images")
	 * @return Cache {@link File directory}
	 */
	public static File getOwnCacheDirectory(Context context, String cacheDir) {
		File appCacheDir = null;
		if (MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) && hasExternalStoragePermission(context)) {
			appCacheDir = new File(Environment.getExternalStorageDirectory(), cacheDir);
		}
		if (appCacheDir == null || (!appCacheDir.exists() && !appCacheDir.mkdirs())) {
			appCacheDir = context.getCacheDir();
		}
		return appCacheDir;
	}

	private static File getExternalCacheDir(Context context) {
		File dataDir = new File(new File(Environment.getExternalStorageDirectory(), "Android"), "data");
		File appCacheDir = new File(new File(dataDir, context.getPackageName()), "cache");
		if (!appCacheDir.exists()) {
			if (!appCacheDir.mkdirs()) {
				L.w("Unable to create external cache directory");
				return null;
			}
			try {
				new File(appCacheDir, ".nomedia").createNewFile();
			} catch (IOException e) {
				L.i("Can't create \".nomedia\" file in application external cache directory");
			}
		}
		return appCacheDir;
	}

	private static boolean hasExternalStoragePermission(Context context) {
		int perm = context.checkCallingOrSelfPermission(EXTERNAL_STORAGE_PERMISSION);
		return perm == PackageManager.PERMISSION_GRANTED;
	}
	// 递归   判断文件大小
	public static  long  getFileSize(File f)
			throws  Exception
	{
		long  size =  0 ;
		File flist[] = f.listFiles();
		for  ( int  i =  0 ; i < flist.length; i++)
		{
			if  (flist[i].isDirectory())
			{
				size = size + getFileSize(flist[i]);
			} else
			{
				size = size + flist[i].length();
			}
		}
		return  size;
	}
}
