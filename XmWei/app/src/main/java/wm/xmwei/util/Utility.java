package wm.xmwei.util;

import android.util.TypedValue;

import wm.xmwei.XmApplication;

/**
 * Created by wm on 15-3-16.
 */
public class Utility {

    public static int dip2px(int dipValue) {
        float reSize = XmApplication.getInstance().getResources().getDisplayMetrics().density;
        return (int) ((dipValue * reSize) + 0.5);
    }

    public static int px2dip(int pxValue) {
        float reSize = XmApplication.getInstance().getResources().getDisplayMetrics().density;
        return (int) ((pxValue / reSize) + 0.5);
    }

    public static float sp2px(int spValue) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue,
                XmApplication.getInstance().getResources().getDisplayMetrics());
    }

}
