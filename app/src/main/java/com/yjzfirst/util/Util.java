package com.yjzfirst.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.yzq.zxinglibrary.android.CaptureActivity;

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
}
