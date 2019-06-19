package com.yjzfirst.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.yzq.zxinglibrary.android.CaptureActivity;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

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
}
