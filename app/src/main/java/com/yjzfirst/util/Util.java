package com.yjzfirst.util;

import android.content.Context;
import android.widget.Toast;

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
}
