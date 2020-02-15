package com.android.customplayer.utils;

import android.util.Log;

public class LogUtils {
    private static final boolean IS_DEBUG = true;
    private static final String TAG = "CustomPlayer";

    public static void e(String msg) {
        if (IS_DEBUG) {
            Log.e(TAG, msg);
        }
    }

    public static void w(String msg) {
        if (IS_DEBUG) {
            Log.w(TAG, msg);
        }
    }

    public static void i(String msg) {
        if (IS_DEBUG) {
            Log.i(TAG, msg);
        }
    }

    public static void d(String msg) {
        if (IS_DEBUG) {
            Log.d(TAG, msg);
        }
    }

    public static void v(String msg) {
        if (IS_DEBUG) {
            Log.v(TAG, msg);
        }
    }
}
