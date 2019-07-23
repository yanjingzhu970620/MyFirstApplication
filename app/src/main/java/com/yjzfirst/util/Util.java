package com.yjzfirst.util;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;


import com.yzq.zxinglibrary.android.CaptureActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import app.yjzfirst.com.activity.R;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by 94012 on 2018/1/17.
 */

public class Util {

    public static void showToastMessage(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public static void showShortToastMessage(Context context,String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
    // 开始扫码
    public  static int REQUEST_CODE_SCAN = 111;
    public static void startQrCode(Activity context) {
        // 申请相机权限
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            Util.showShortToastMessage(context,"请开启相机权限，用于扫码");
            return;
        }
//        // 申请文件读写权限（部分朋友遇到相册选图需要读写权限的情况，这里一并写一下）
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            // 申请权限
//            ActivityCompat.requestPermissions(EntryActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Constant.REQ_PERM_EXTERNAL_STORAGE);
//            return;
//        }
        // 二维码扫码
        Intent intent = new Intent(context, CaptureActivity.class);
        context.startActivityForResult(intent, REQUEST_CODE_SCAN);
    }

    /**
     182.     * 把输入流转换成字符数组
     183.     * @param inputStream   输入流
     184.     * @return  字符数组
     185.     * @throws Exception
     186.     */
    public static byte[] readStream(InputStream inputStream) throws Exception {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            bout.write(buffer, 0, len);
        }
        bout.close();
        inputStream.close();

        return bout.toByteArray();
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        //获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }
        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {   //listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);  //计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight();  //统计所有子项的总高度
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        //listView.getDividerHeight()获取子项间分隔符占用的高度
        //params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

    public static String CheckNullString(String s) {

        if (s == null || s.equals("null")) {
            return "";
        } else {
            return s;
        }
    }

    static String defaultLocation = Environment.getExternalStorageDirectory()
            .getAbsolutePath();
    public static final String APP_DIR = "yjzerp/";
    public static File getAppPath(String path) {
        if (path == null) {
            path = "";
        }
        return new File(defaultLocation,
                APP_DIR + path);
    }
    public static void initNotification(Context context, boolean sound,String msg){
//
        String NOTIFICATION_CHANNEL_ID="1002";
        // 获取NotificationManager的引用
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_DEFAULT);

            // Configure the notification channel.
            notificationChannel.setDescription("Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }
//        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);//设置提示音
//        if(sound){
////            Uri uri = null;
//            File f=new File(getAppPath("SOUNDS/")+"alarm2.mp3");
//            if(!f.exists()){
//        	System.err.println("!!!f.exists 111 getName ???"+f.getName());
//                try {
//                    f.createNewFile();
//                    InputStream ins = null;
//                    try {
//                        ins = context.getAssets().open("alarm2.mp3");
//                        System.err.println("!!!f.exists 222 alarm2.mp3"+f.getName());
//                    } catch (IOException e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }
//                    if(ins!=null){
//                        inputstreamtofile(ins, f);
//                        uri = Uri.fromFile(f);
////			        uri = Uri.parse(view.getApplication().getAppPath("SOUNDS/")+"alert.mp3");
//                    }else{
//                        uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//                    }
//                } catch (IOException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//            }else{
////        	System.err.println("f.exists"+f.getName());
//                uri=Uri.parse(getAppPath("SOUNDS/")+"alarm2.mp3");
//            }
//
////            m_builder.setSound(uri);
//        }//设置提示音

//        AssetFileDescriptor fileDescriptor=null;
//        try {
//             fileDescriptor = context.getAssets().openFd("alarm2.mp3");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        Uri uri = Uri.parse("file:///android_asset/alarm2.mp3");
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
//                .setVibrate(new long[]{0, 100 })
                .setSound(uri)
                .setSmallIcon(R.mipmap.icon_safe_message)
                .setContentTitle("信息验证错误")
                .setContentText(msg);
        notificationManager.notify(1001, builder.build());


    }

    public static void inputstreamtofile(InputStream ins,File file) {
        try {


            OutputStream os = new FileOutputStream(file);
            int bytesWritten = 0;
            int byteCount = 0;

            byte[] b = new byte[1024];
            while ((byteCount=ins.read(b)) != -1) {
//				   System.err.println("FileOutputStream write"+b);
                os.write(b, bytesWritten, byteCount);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static void setsound(Context context){
         SoundPool soundPool = null;
        List<Integer> soundIdList = new ArrayList<>();
        AudioAttributes audioAttributes = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            audioAttributes = new AudioAttributes.Builder()
                    // 设置场景
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION )
                    // 设置类型
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            soundPool = new SoundPool.Builder()
                    // 设置上面的属性
                    .setAudioAttributes(audioAttributes)
                    // 设置最多10个音频流文件
                    .setMaxStreams(10).build();
        }

        // 加载音频流到soundPool中去，并且用List存储起来
        soundIdList.add(soundPool.load(context , R.raw.alarm2 , 1));
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int i, int i2) {
                soundPool.play(1,  //声音id
                        1, //左声道
                        1, //右声道
                        1, //优先级
                        0, // 0表示不循环，-1表示循环播放
                        1);//播放比率，0.5~2，一般为1
            }
        });
//        soundPool.play( soundIdList.get(0) , 1 ,1 , 0 , 0 , 1);

    }
    public static void textsetError(Context context,EditText text,String msg) {
        text.requestFocus();
        text.setError(msg);
//        initNotification(context,true,msg);
        setsound(context);
    }
}
