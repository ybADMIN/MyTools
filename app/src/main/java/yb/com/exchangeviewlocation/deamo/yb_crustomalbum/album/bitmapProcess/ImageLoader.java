package yb.com.exchangeviewlocation.deamo.yb_crustomalbum.album.bitmapProcess;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

import yb.com.exchangeviewlocation.deamo.yb_crustomalbum.album.extar.AlbumSettingBase;
import yb.com.exchangeviewlocation.deamo.yb_crustomalbum.album.extar.L;
import yb.com.exchangeviewlocation.deamo.yb_crustomalbum.album.listener.ImageLoadingListener;
import yb.com.exchangeviewlocation.deamo.yb_crustomalbum.album.utils.BitmapUtil;
import yb.com.exchangeviewlocation.deamo.yb_crustomalbum.album.utils.CustomAlbumUtil;
import yb.com.exchangeviewlocation.deamo.yb_crustomalbum.album.utils.Md5FileNameGenerator;


public class ImageLoader
{
	protected static final int PROCESSCOMPLETE = 100;
	protected static final int PROCESSSTART = 0;
	protected static final int PROCESSFAILD = 1;
	/**
	 * 图片缓存的核心类
	 */
	private LruMemoryCache mLruCache;
	/**
	 * 线程池
	 */
	private ExecutorService mThreadPool;
	/**
	 * 队列的调度方式
	 */
	private Type mType = Type.LIFO;
	/**
	 * 任务队列
	 */
	private LinkedList<Runnable> mTasks;
	/**
	 * 轮询的线程
	 */
	private Thread mPoolThread;
	private Handler mPoolThreadHander;

	/**
	 * 运行在UI线程的handler，用于给ImageView设置图片
	 */
	private Handler mHandler;

	/**
	 * 引入一个值为1的信号量，防止mPoolThreadHander未初始化完成
	 */
	private volatile Semaphore mSemaphore = new Semaphore(0);

	/**
	 * 引入一个值为1的信号量，由于线程池内部也有一个阻塞线程，防止加入任务的速度过快，使LIFO效果不明显
	 */
	public volatile Semaphore mPoolSemaphore;

	private static ImageLoader mInstance;
	private static final String TAG="Imageloader:";

	/**
	 * 队列的调度方式
	 * 
	 * @author zhy
	 * 
	 */
	public enum Type
	{
		FIFO, LIFO
	}


	public LruMemoryCache getMemoryCache() {
		return mLruCache;
	}

	/**
	 * 单例获得该实例对象
	 * 
	 * @return
	 */
	public static ImageLoader getInstance()
	{

		if (mInstance == null)
		{
			synchronized (ImageLoader.class)
			{
				if (mInstance == null)
				{
					mInstance = new ImageLoader(1, Type.LIFO);
				}
			}
		}
		return mInstance;
	}

	private ImageLoader(int threadCount, Type type)
	{
		init(threadCount, type);
	}

	private void init(int threadCount, Type type)
	{
		// loop thread
		mPoolThread = new Thread()
		{
			@Override
			public void run()
			{
				Looper.prepare();

				mPoolThreadHander = new Handler()
				{
					@Override
					public void handleMessage(Message msg)
					{
						try
						{
							mThreadPool.execute(getTask());
							mPoolSemaphore.acquire();
						} catch (InterruptedException e)
						{
							Log.e(TAG, e.getMessage()==null?"Task is cancle":e.getMessage());
						}
						catch (NullPointerException e2){
							Log.e(TAG, e2.getMessage()==null?"Task is null":e2.getMessage());
						}
					}
				};
				// 释放一个信号量
				mSemaphore.release();
				Looper.loop();
			}
		};
		mPoolThread.start();
		// 获取应用程序最大可用内存
		int maxMemory = (int) Runtime.getRuntime().maxMemory();
		int cacheSize = maxMemory / 8;
		mLruCache = new LruMemoryCache(cacheSize);
		mThreadPool = Executors.newFixedThreadPool(threadCount);
		mPoolSemaphore = new Semaphore(threadCount);
		mTasks = new LinkedList<Runnable>();
		mType = type == null ? Type.LIFO : type;

	}

	public void loadImage( String path,  ImageView imageView, boolean iscach){
		loadImage(path, imageView, iscach, null,null);
	}
	public void loadImage( String path,  ImageView imageView, boolean iscach,ImageLoadingListener listener){
		loadImage(path, imageView, iscach, listener,null);
	}
	public void loadImage( String path,  ImageView imageView){
		loadImage(path, imageView, false, null,null);
	}
	public void loadImage( String path,  ImageView imageView,ImageLoadingListener listener,int [] size){
		loadImage(path, imageView, false, listener,size);
	}
	public void loadImage( String path,  ImageView imageView,ImageLoadingListener listener){
		loadImage(path, imageView, false, listener,null);
	}
	
	
	/**
	 * 加载图片
	 * @param  iscache 如果设置为false 内存缓存和外部缓存都不会有
	 * @param path
	 * @param imageView
	 */
	public void loadImage(final String path, final ImageView imageView,final boolean iscache,final ImageLoadingListener listener,final int[] wh)
	{
		
		Md5FileNameGenerator md5 = new Md5FileNameGenerator();
		String name = md5.generate(path);
		final String cachepath = AlbumSettingBase.getALBUM_PATH(imageView.getContext())+"/"+name;
		File f = new File(AlbumSettingBase.getALBUM_PATH(imageView.getContext()));
		if (!f.exists()&&!f.mkdirs()) {
			Log.e(ImageLoader.class.getSimpleName(),"不能创建文件");
		}
		// set tag
		imageView.setTag(path);
		// UI线程
		if (mHandler == null)
		{
			mHandler = new Handler()
			{
				@Override
				public void handleMessage(Message msg)
				{
					ImgBeanHolder holder = (ImgBeanHolder) msg.obj;
					ImageView imageView = holder.imageView;
					String path = holder.path;
					ImageLoadingListener listener=holder.listener;
					switch (msg.what) {
					case PROCESSSTART:
						if (imageView.getTag().toString().equals(path) && listener!=null)
						{
							listener.onLoadingStarted(path, imageView);
						}
						break;
					case PROCESSFAILD:
						if (imageView.getTag().toString().equals(path) && listener!=null)
						{
							listener.onLoadingFailed(path, imageView);
						}
						break;
					case PROCESSCOMPLETE:
						Bitmap bm = holder.bitmap;
						if (imageView.getTag().toString().equals(path))
						{
							if (listener!=null) {
								listener.onLoadingComplete(path, imageView, bm);
							}else
							imageView.setImageBitmap(bm);
						}
						break;

					default:
						break;
					}
					
				}
			};
		}
		if (listener!=null) {
			ImgBeanHolder holder = new ImgBeanHolder();
			holder.imageView = imageView;
			holder.listener=listener;
			holder.path = path;
			Message message = Message.obtain();
			message.obj = holder;
			message.what=PROCESSSTART;
			mHandler.sendMessage(message);
		}
		Bitmap bm= getBitmapFromLruCache(path);
		if (bm != null && iscache)
		{
			ImgBeanHolder holder = new ImgBeanHolder();
			holder.bitmap = bm;
			holder.imageView = imageView;
			holder.path = path;
			holder.listener=listener;
			Message message = Message.obtain();
			message.obj = holder;
			message.what=PROCESSCOMPLETE;
			mHandler.sendMessage(message);
		}else
		{
			addTask(new Runnable()
			{
				@Override
				public void run()
				{
					if (waitIfPaused()) return;
					Log.w(TAG, mTasks.size() + "--" + paused.get() + "---isInterrupted"+mPoolThread.isInterrupted()+"---");
					ImageSize imageSize;
					if (wh!=null) {
						imageSize = new ImageSize();
						imageSize.width=wh[0];
						imageSize.height=wh[1];
					}else
						imageSize= getImageViewWidth(imageView);

//					if (listener!=null) {
//						ImgBeanHolder holder = new ImgBeanHolder();
//						holder.imageView = imageView;
//						holder.listener=listener;
//						holder.path = path;
//						Message message = Message.obtain();
//						message.obj = holder;
//						message.what=PROCESSSTART;
//						mHandler.sendMessage(message);
//					}
					int reqWidth = imageSize.width;
					int reqHeight = imageSize.height;
					Bitmap bm;
					if (iscache) {
					if (new File(cachepath).exists()) {
					 bm = BitmapUtil.setOption(cachepath);
					}else{
						bm= CustomAlbumUtil.getImageThumbnail(path, reqWidth, reqHeight);
						try {
							BitmapUtil.saveThumbnailImage(bm, cachepath);
						} catch (IOException e) {
							L.e(TAG, e.getMessage());
						}
					}
						addBitmapToLruCache(path, bm);//添加到内存缓存
					}else{
						if (wh!=null)
							bm= CustomAlbumUtil.getImageThumbnail(path, wh[0], wh[1]);
						else
							bm= CustomAlbumUtil.getImageThumbnail(path, reqWidth, reqHeight);
					}

					ImgBeanHolder holder = new ImgBeanHolder();
					holder.bitmap = bm;
					holder.imageView = imageView;
					holder.path = path;
					holder.listener=listener;
					Message message = Message.obtain();
					message.obj = holder;
					if (bm==null) {
						message.what=PROCESSFAILD;
					}else
					message.what=PROCESSCOMPLETE;
					// Log.e("TAG", "mHandler.sendMessage(message);");
					mHandler.sendMessage(message);
					mPoolSemaphore.release();
				}
			});
		}

	}
	
//	private Bitmap createDaskCache(String path,int whidth,int height){
//		Bitmap mBitmap;
//		Md5FileNameGenerator md5 = new Md5FileNameGenerator();
//		String name = md5.generate(path);
//		String filepathe = AlbumSettingBase.ALBUM_PATH+"/"+name;
//		File f = new File(AlbumSettingBase.ALBUM_PATH);
//		if (!f.exists() &&! f.mkdirs()) {
//			AlbumSettingBase.ALBUM_PATH=AlbumSettingBase.ALBUM_TEMPPATH;
//			f=new File(AlbumSettingBase.ALBUM_PATH);
//			f.mkdirs();
//		}
////		UnlimitedDiscCache diskcache =new UnlimitedDiscCache(new File(AlbumSettingBase.ALBUM_PATH));
//		if (new File(filepathe).exists()) {
////			new File(filepathe).delete();
//			mBitmap=BitmapUtil.setOption(new File(filepathe).getAbsolutePath());
////			mBitmap=ImageLoader.getInstance().loadImageSync("file://"+filepathe);
//		}else{
//		mBitmap=CustomAlbumUtil.getImageThumbnail(path, whidth, height);
//		try {
//			BitmapUtil.saveThumbnailImage(mBitmap, filepathe);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		}
//		return mBitmap;
//	}
	
	
	/**
	 * 添加一个任务
	 * 
	 * @param runnable
	 */
	private synchronized void addTask(Runnable runnable)
	{
		try
		{
			// 请求信号量，防止mPoolThreadHander为null
			if (mPoolThreadHander == null)
				mSemaphore.acquire();
		} catch (InterruptedException e)
		{
			L.e(TAG, e.getMessage());
		}
		mTasks.add(runnable);
		
		mPoolThreadHander.sendEmptyMessage(0x110);
	}
	/**
	 * 取出一个任务
	 * 
	 * @return
	 */
	private synchronized Runnable getTask()
	{
		if (mTasks.size()>0){
		if (mType == Type.FIFO)
		{
			return mTasks.removeFirst();
		} else if (mType == Type.LIFO)
		{
			return mTasks.removeLast();
		}
		}
		return null;
	}
	
	/**
	 * 单例获得该实例对象
	 * 
	 * @return
	 */
	public static ImageLoader getInstance(int threadCount, Type type)
	{

		if (mInstance == null)
		{
			synchronized (ImageLoader.class)
			{
				if (mInstance == null)
				{
					mInstance = new ImageLoader(threadCount, type);
				}
			}
		}
		return mInstance;
	}


	/**
	 * 根据ImageView获得适当的压缩的宽和高
	 * 
	 * @param imageView
	 * @return
	 */
	private ImageSize getImageViewWidth(ImageView imageView)
	{
		ImageSize imageSize = new ImageSize();
		final DisplayMetrics displayMetrics = imageView.getContext()
				.getResources().getDisplayMetrics();
		final LayoutParams params = imageView.getLayoutParams();

		int width = params.width == LayoutParams.WRAP_CONTENT ? 0 : imageView
				.getWidth(); // Get actual image width
		if (width <= 0)
			width = params.width; // Get layout width parameter
		if (width <= 0)
			width = getImageViewFieldValue(imageView, "mMaxWidth"); // Check
																	// maxWidth
																	// parameter
		if (width <= 0)
			width = displayMetrics.widthPixels;
		int height = params.height == LayoutParams.WRAP_CONTENT ? 0 : imageView
				.getHeight(); // Get actual image height
		if (height <= 0)
			height = params.height; // Get layout height parameter
		if (height <= 0)
			height = getImageViewFieldValue(imageView, "mMaxHeight"); // Check
																		// maxHeight
																		// parameter
		if (height <= 0)
			height = displayMetrics.heightPixels;
		imageSize.width = width;
		imageSize.height = height;
		return imageSize;

	}

	/**
	 * 从LruCache中获取一张图片，如果不存在就返回null。
	 */
	private Bitmap getBitmapFromLruCache(String key)
	{
		return mLruCache.get(key);
	}

	/**
	 * 往LruCache中添加一张图片
	 * 
	 * @param key
	 * @param bitmap
	 */
	private void addBitmapToLruCache(String key, Bitmap bitmap)
	{
		if (getBitmapFromLruCache(key) == null)
		{
			if (bitmap != null)
				mLruCache.put(key, bitmap);
		}
	}


	private class ImgBeanHolder
	{
		Bitmap bitmap;
		ImageView imageView;
		String path;
		ImageLoadingListener listener;
	}

	public class ImageSize
	{
		int width;
		int height;
	}

	/**
	 * 反射获得ImageView设置的最大宽度和高度
	 * 
	 * @param object
	 * @param fieldName
	 * @return
	 */
	private static int getImageViewFieldValue(Object object, String fieldName)
	{
		int value = 0;
		try
		{
			Field field = ImageView.class.getDeclaredField(fieldName);
			field.setAccessible(true);
			int fieldValue = (Integer) field.get(object);
			if (fieldValue > 0 && fieldValue < Integer.MAX_VALUE)
			{
				value = fieldValue;

				Log.e(TAG+fieldName, value + "");
			}
		} catch (Exception e)
		{
			Log.e(TAG,  e.getMessage());
		}
		return value;
	}
	private final AtomicBoolean paused = new AtomicBoolean(false);

	private final Object pauseLock = new Object();
	


	public synchronized void pause() {
		paused.set(true);
	}

	public void resume() {
		paused.set(false);
		synchronized (pauseLock) {
			Log.w(TAG, "paused:"+paused.get() + "");
			pauseLock.notifyAll();
		}
	}
	public synchronized void cancleTask(){
		mTasks.clear();
	}

	public void stop() {
			mThreadPool.shutdownNow();
//			mPoolThread.interrupt();
			getMemoryCache().clear();
			mTasks.clear();
			mSemaphore.release();
		try {
			mPoolThreadHander.getLooper().quit();
			mPoolThread.interrupt();
			}catch (Exception e){}
		mInstance=null;
	}
	
	AtomicBoolean getPause() {
		return paused;
	}

	Object getPauseLock() {
		return pauseLock;
	}


	private boolean waitIfPaused() {
		AtomicBoolean pause = getPause();
		if (pause.get()) {
			synchronized (getPauseLock()) {
				if (pause.get()) {
					L.d(TAG, "线程开始暂停");
					try {
						getPauseLock().wait();
					} catch (InterruptedException e) {
						L.d(TAG, "线程出错");
						return true;
					}
					L.d(TAG, "暂停成功");
				}
			}
		}
		return pause.get();
	}
	public void removeCacheFile(Context context,String path){
		Md5FileNameGenerator md5 = new Md5FileNameGenerator();
		String name = md5.generate(path);
		final String cachepath = AlbumSettingBase.getALBUM_PATH(context)+"/"+name;
		File f= new File(cachepath);
		if (f.exists()){
			f.delete();
		}
	}
//	private boolean delayIfNeed() {
//		if (options.shouldDelayBeforeLoading()) {
//			L.d(LOG_DELAY_BEFORE_LOADING, options.getDelayBeforeLoading(), memoryCacheKey);
//			try {
//				Thread.sleep(options.getDelayBeforeLoading());
//			} catch (InterruptedException e) {
//				L.e(LOG_TASK_INTERRUPTED, memoryCacheKey);
//				return true;
//			}
//			return isTaskNotActual();
//		}
//		return false;
//	}
}
