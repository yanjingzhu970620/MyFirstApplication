//package com.yjzfirst.app;
//
//import android.app.Activity;
//import android.app.AlarmManager;
//import android.app.AlertDialog.Builder;
//import android.app.Application;
//import android.app.PendingIntent;
//import android.app.ProgressDialog;
//import android.content.ActivityNotFoundException;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.content.SharedPreferences;
//import android.content.pm.PackageInfo;
//import android.content.pm.PackageManager.NameNotFoundException;
//import android.content.res.Configuration;
//import android.graphics.Shader.TileMode;
//import android.graphics.drawable.BitmapDrawable;
//import android.os.Build;
//import android.os.Handler;
//import android.os.Message;
//import android.provider.Settings;
//import android.text.format.DateFormat;
//import android.util.Log;
//import android.view.View;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import java.io.BufferedReader;
//import java.io.BufferedWriter;
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.FileReader;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.io.PrintStream;
//import java.lang.Thread.UncaughtExceptionHandler;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Locale;
//
//public class YjzApplication extends Application {
//	public static final String EXCEPTION_PATH = "exception.log"; //$NON-NLS-1$
////	public static final String mypackagename="yjzfoc.hifleet.com.studionavigationaisnew";
//	private List<String> startingWarnings;
//
//	// start variables
//	private Handler uiHandler;
//	// private GPXFile gpxFileToDisplay;
//
//	private boolean applicationInitializing = false;
//	private Locale prefferedLocale = null;
//
////	private MapActivity_Messure activity_Messure;
////	private MapActivity_Navigation activity_Navigation;
////	UploadlocService uploadlocService;
//	private PendingIntent restartIntent;
//
//	public boolean isInmeasure() {
//		return inmeasure;
//	}
//
//	public void setInmeasure(boolean inmeasure) {
//		this.inmeasure = inmeasure;
//	}
//
//	private boolean inmeasure=false;
//
//	public boolean isInNavigationMode() {
//		return isInNavigationMode;
//	}
//
//	public void setInNavigationMode(boolean inNavigationMode) {
//		isInNavigationMode = inNavigationMode;
//	}
//
//	private boolean isInNavigationMode=false;
//
//	public boolean isInNavigationeditMode() {
//		return isInNavigationeditMode;
//	}
//
//	public void setInNavigationeditMode(boolean inNavigationeditMode) {
//		isInNavigationeditMode = inNavigationeditMode;
//	}
//
//	private boolean isInNavigationeditMode=false;
//
//	public int getCanclealert() {
//		return canclealert;
//	}
//
//	public void setCanclealert(int canclealert) {
//		this.canclealert = canclealert;
//	}
//
//	private int canclealert = 0;
//	public static SharedPreferences myPreferences;
//	public static SharedPreferences.Editor mEditor;
//
////	KEYCODE_SOS 键码300
////	短按：com.android.call.sos
////	长按：android.intent.action.SOS
////
////	KEYCODE_AIS 键码301
////	短按：com.android.call.ais
////	长按：android.intent.action.AIS
//	public String SOS_SHORT_PRESS="com.android.call.sos";
//	public String SOS_LONG_PRESS="android.intent.action.SOS";
//	public String AIS_SHORT_PRESS="com.android.call.ais";
//	public String AIS_LONG_PRESS="android.intent.action.AIS";
//	public String shipstargetname="";
//	 private List<Activity> activities = new ArrayList<Activity>();
//	 public void removeActivity(Activity a){
//	        activities.remove(a);
//	    }
//
//	    /**
//	     * 向Activity列表中添加Activity对象*/
//	    public void addActivity(Activity a){
//	        activities.add(a);
//	    }
//
//	    /**
//	     * 关闭Activity列表中的所有Activity*/
//	    public void finishActivity(){
//	        for (Activity activity : activities) {
//	            if (null != activity) {
//	                activity.finish();
//	            }
//	        }
//	        //杀死该应用进程
//	       android.os.Process.killProcess(android.os.Process.myPid());
//	    }
//
//
//	@Override
//	public void onCreate() {
//		long timeToStart = System.currentTimeMillis();
//		super.onCreate();
//
//		uiHandler = new Handler();
//
//
//		startApplication();
//
//		timeToStart = System.currentTimeMillis();
//		// OsmandPlugin.initPlugins(this);
//
//
//		myPreferences = getSharedPreferences("EasyNavigationAIS",
//				Activity.MODE_PRIVATE);
//		mEditor = myPreferences.edit();
//
//
//	}
//
////	public static void keep(Context cxt){
////		Intent intent = new Intent(cxt,KeepReceive.class);
////		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////		cxt.startService(intent);
////	}
//	 public UncaughtExceptionHandler restartHandler = new UncaughtExceptionHandler() {
//	        @Override
//	        public void uncaughtException(Thread thread, Throwable ex) {
//	            AlarmManager mgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//	            mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000,
//	                    restartIntent); // 1秒钟后重启应用
//	        }
//	    };
//	@Override
//	public void onTerminate() {
//		super.onTerminate();
//
//	}
//
//
//
//
//	@Override
//	public void onLowMemory() {
//		super.onLowMemory();
//	}
//
//	@Override
//	public void onConfigurationChanged(Configuration newConfig) {
//		if (prefferedLocale != null
//				&& !newConfig.locale.getLanguage().equals(
//						prefferedLocale.getLanguage())) {
//			super.onConfigurationChanged(newConfig);
//			// ugly fix ! On devices after 4.0 screen is blinking when you
//			// rotate device!
//			if (Build.VERSION.SDK_INT < 14) {
//				newConfig.locale = prefferedLocale;
//			}
//			getBaseContext().getResources().updateConfiguration(newConfig,
//					getBaseContext().getResources().getDisplayMetrics());
//			Locale.setDefault(prefferedLocale);
//		} else {
//			super.onConfigurationChanged(newConfig);
//		}
//	}
//
//
//
//	public static final int PROGRESS_DIALOG = 5;
//
//
//
//	public boolean isApplicationInitializing() {
//		return startDialog != null;
//	}
//
//
//	private void fullExit() {
//		// http://stackoverflow.com/questions/2092951/how-to-close-android-application
//		System.runFinalizersOnExit(true);
//		System.exit(0);
//	}
//
//	public synchronized void closeApplication(final Activity activity) {
//
//		closeApplicationAnyway(activity, true);
//
//	}
//
//	private void closeApplicationAnyway(final Activity activity,
//			boolean disableService) {
//		if (applicationInitializing) {
////			resourceManager.close();
//		}
//		applicationInitializing = false;
//
//		activity.finish();
//
//		fullExit();
//
//	}
//
//	public synchronized void startApplication() {
//		if (applicationInitializing) {
//			return;
//		}
//		applicationInitializing = true;
//
//	}
//
//
//	public static String txt2String(File file){
//		StringBuilder result = new StringBuilder();
//		try{
//			BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
//			String s = null;
//			while((s = br.readLine())!=null){//使用readLine方法，一次读一行
//				result.append(System.lineSeparator()+s);
//			}
//			br.close();
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//		return result.toString();
//	}
//
//	public void runInUIThread(Runnable run) {
//		uiHandler.post(run);
//	}
//
//	public void runInUIThread(Runnable run, long delay) {
//		uiHandler.postDelayed(run, delay);
//	}
//
//	public void runMessageInUIThreadAndCancelPrevious(final int messageId,
//	                                                  final Runnable run, long delay) {
//		Message msg = Message.obtain(uiHandler, new Runnable() {
//
//			@Override
//			public void run() {
//				if (!uiHandler.hasMessages(messageId)) {
//					run.run();
//				}
//			}
//		});
//		msg.what = messageId;
//		uiHandler.removeMessages(messageId);
//		uiHandler.sendMessageDelayed(msg, delay);
//	}
//
//
//
//	public static void print(String msg) {
//		Log.i(TAG, msg);
//	}
//
//	private static final String TAG = "application";
//
//
//	public void setLanguage(Context context) {
//		if (prefferedLocale != null) {
//			Configuration config = context.getResources().getConfiguration();
//			String lang = prefferedLocale.getLanguage();
//			if (!"".equals(lang) && !config.locale.getLanguage().equals(lang)) {
//				prefferedLocale = new Locale(lang);
//				Locale.setDefault(prefferedLocale);
//				config.locale = prefferedLocale;
//				context.getResources().updateConfiguration(config,
//						context.getResources().getDisplayMetrics());
//			}
//		}
//	}
//
//
//	// public boolean accessibilityEnabled() {
//	// final AccessibilityMode mode = getSettings().ACCESSIBILITY_MODE.get();
//	// if(OsmandPlugin.getEnabledPlugin(AccessibilityPlugin.class) == null) {
//	// return false;
//	// }
//	// if (mode == AccessibilityMode.ON) {
//	// return true;
//	// } else if (mode == AccessibilityMode.OFF) {
//	// return false;
//	// }
//	// return ((AccessibilityManager)
//	// getSystemService(Context.ACCESSIBILITY_SERVICE)).isEnabled();
//	// }
//
//	public String getVersionName() {
//		try {
//			PackageInfo info = getPackageManager().getPackageInfo(
//					getPackageName(), 0);
//			return info.versionName;
//		} catch (NameNotFoundException e) {
//			return "";
//		}
//	}
//
//	public int getVersionCode() {
//		try {
//			PackageInfo info = getPackageManager().getPackageInfo(
//					getPackageName(), 0);
//			return info.versionCode;
//		} catch (NameNotFoundException e) {
//			return 0;
//		}
//	}
//
//}
