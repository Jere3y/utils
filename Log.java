package jeremy.com.utils;

/**
 * Created by Xin on 2017/4/1 0001,20:44.
 */

import android.util.Log;

public class Log {
    private Log() {
    }

    private static final boolean DEBUG_E = true;
    private static final boolean DEBUG_W = true;
    private static final boolean DEBUG_I = true;
    private static final boolean DEBUG_D = true;
    private static final boolean DEBUG_V = true;

    public static final void e(String tag, String msg) {
        if (DEBUG_E)
            Log.e(tag, msg);
    }

    public static final void e(String tag, String msg, Throwable tr) {
        if (DEBUG_E)
            Log.e(tag, msg, tr);
    }


    public static final void w(String tag, String msg) {
        if (DEBUG_W)
            Log.w(tag, msg);
    }

    public static final void w(String tag, String msg, Throwable tr) {
        if (DEBUG_W)
            Log.w(tag, msg, tr);
    }


    public static final void i(String tag, String msg) {
        if (DEBUG_I)
            Log.i(tag, msg);
    }

    public static final void i(String tag, String msg, Throwable tr) {
        if (DEBUG_I)
            Log.i(tag, msg, tr);
    }


    public static final void d(String tag, String msg) {
        if (DEBUG_D)
            Log.d(tag, msg);
    }

    public static final void d(String tag, String msg, Throwable tr) {
        if (DEBUG_D)
            Log.d(tag, msg, tr);
    }

    public static final void v(String tag, String msg) {
        if (DEBUG_V)
            Log.v(tag, msg);
    }

    public static final void v(String tag, String msg, Throwable tr) {
        if (DEBUG_V)
            Log.v(tag, msg, tr);
    }
}