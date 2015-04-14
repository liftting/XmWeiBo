package wm.xmwei.core.data;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.util.DisplayMetrics;
import android.view.Window;


import wm.xmwei.XmApplication;

/**
 *
 */
public class ScreenUtil {

    public static int HEIGHT;
    public static int WIDTH;
    public static int DENSITY_DPI;
    public static int DENSITY_SIZE = 0;

    public static int STATUSUIBAR = 0;

    public static final int HIGH_MODE = 0;
    public static final int MIDDLE_MODE = 1;
    public static final int LOW_MODE = 2;

    public static final int DENSITY_480 = 0;
    public static final int DENSITY_720 = 1;
    public static final int DENSITY_1080 = 2;


    public static final int IMAGE_TYRPE_480 = 0;
    public static final int IMAGE_TYRPE_1080_720_WIFI = 1;


    public static int getTitleDisplayMode() {
        int highHeight = 800;
        int lowHeight = 320;

        if (highHeight > HEIGHT && lowHeight < HEIGHT) {
            return MIDDLE_MODE;
        } else if (HEIGHT >= highHeight) {
            return HIGH_MODE;
        } else {
            return LOW_MODE;
        }
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int getStatusBarHeight(Activity activity) {
        Rect rect = new Rect();
        Window win = activity.getWindow();
        win.getDecorView().getWindowVisibleDisplayFrame(rect);

        return rect.top;
    }

    public static int getTitleBarHeight(Activity activity) {
        Rect rect = new Rect();
        Window win = activity.getWindow();
        win.getDecorView().getWindowVisibleDisplayFrame(rect);
        return win.findViewById(Window.ID_ANDROID_CONTENT).getTop() - rect.top;
    }

    public static void setDisplay(Activity activity) {
        setContextDisplay(activity);
        Rect rect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        STATUSUIBAR = rect.top;
    }

    public static void setContextDisplay(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        WIDTH = dm.widthPixels;
        HEIGHT = dm.heightPixels;
        DENSITY_DPI = dm.densityDpi;
        if (WIDTH >= 1080) {
            DENSITY_SIZE = DENSITY_1080;
        } else if (WIDTH >= 720) {
            DENSITY_SIZE = DENSITY_720;
        } else {
            DENSITY_SIZE = DENSITY_480;
        }
    }

    public static int getScreenDensity(Context context) {
        return DENSITY_SIZE;
    }

}
