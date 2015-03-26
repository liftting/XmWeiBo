package wm.xmwei.core.lib.support.error;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Looper;

import wm.xmwei.core.debug.AppLogger;

/**
 * Created by wm on 15-3-26.
 */
public class XmCarshHandler implements Thread.UncaughtExceptionHandler {

    public static final String TAG = "XmCarshHandler";
    private static XmCarshHandler instance = new XmCarshHandler();
    private Context mContext;
    private Thread.UncaughtExceptionHandler mDefaultHandler;

    private XmCarshHandler() {
    }

    public static XmCarshHandler getInstance() {
        return instance;
    }

    public void init(Context ctx) {
        mContext = ctx;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        System.out.println("uncaughtException");
        AppLogger.e(TAG, ex);
    }
}
