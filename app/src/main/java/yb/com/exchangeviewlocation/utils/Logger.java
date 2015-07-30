package yb.com.exchangeviewlocation.utils;

import android.util.Log;

public class Logger {
  private static int Level = 6;
  private static int VERBOSE = 1;
  private static int DEBUG = 2;
  private static int INFO = 3;
  private static int WARN = 4;
  private static int ERROR = 5;
  
  
  public static void v(String tag,String msg){
    if (Level >VERBOSE) {
      Log.v(tag, msg);
    }
  }
  public static void d(String tag,String msg){
    if (Level >DEBUG) {
      Log.d(tag, msg);
    }
  }
  public static void i(String tag,String msg){
    if (Level >INFO) {
      Log.i(tag, msg);
    }
  }
  public static void w(String tag,String msg){
    if (Level >WARN) {
      Log.w(tag, msg);
    }
  }
  public static void e(String tag,String msg){
    if (Level >ERROR) {
      Log.e(tag, msg);
    }
  }
  public static void e(String tag,String msg,Throwable e){
    if (Level >ERROR) {
      Log.e(tag, msg,e);
    }
  }

}