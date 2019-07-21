package com.yjzfirst.util;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
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
import java.io.InputStream;
import java.util.HashMap;

import app.yjzfirst.com.activity.R;

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

    private void setsound(Context context) {
        //发送通知
        NotificationCompat.Builder notifyBuilder =
                new NotificationCompat.Builder(context)
                        //设置可以显示多行文本
                        .setContentTitle("信息错误")
                        .setContentText("信息错误")
                        .setSmallIcon(R.mipmap.mainview)
                        //设置大图标
                        .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.mainview))
                        // 点击消失
                        .setAutoCancel(true)
                        // 设置该通知优先级
                        .setPriority(Notification.PRIORITY_MAX)
                        .setTicker("悬浮通知")
                        // 通知首次出现在通知栏，带上升动画效果的
                        .setWhen(System.currentTimeMillis())
                        // 通知产生的时间，会在通知信息里显示
                        // 向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合：
                        .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_ALL | Notification.DEFAULT_SOUND);
        NotificationManager mNotifyMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = notifyBuilder.build();
        mNotifyMgr.notify(01, notification);
    }
    public static void textsetError(EditText text,String msg) {
        text.requestFocus();
        text.setError(msg);
    }
}
