package com.android.utils;

import android.util.Log;

/**
 * Created by Administrator on 2017/9/13.
 */

public class LogUtils {

    private static final boolean DEBUG = true;

    private static final String DEFAULT_TAG = "===>";

    public static final void e(Object obj, String... strs) {
        if (!DEBUG) return;
        Log.e(getTag(strs), obj.toString());
    }

    public static final void d(Object obj, String... strs) {
        if (!DEBUG) return;
        Log.d(getTag(strs), obj.toString());
    }

    public static final void i(Object obj, String... strs) {
        if (!DEBUG) return;
        Log.i(getTag(strs), obj.toString());
    }

    public static final void w(Object obj, String... strs) {
        if (!DEBUG) return;
        Log.w(getTag(strs), obj.toString());
    }

    private static final String getTag(String... strs) {
        if (strs == null || strs.length <= 0) return DEFAULT_TAG;
        if (strs[0] == null || strs[0].length() <= 0) return DEFAULT_TAG;
        return strs[0];
    }
}
