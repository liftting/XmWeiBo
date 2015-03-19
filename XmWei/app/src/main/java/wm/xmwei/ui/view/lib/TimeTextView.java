package wm.xmwei.ui.view.lib;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import wm.xmwei.util.XmTimeUtils;

/**
 *
 *
 */
public class TimeTextView extends TextView {

    public TimeTextView(Context context) {
        super(context);
    }

    public TimeTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public TimeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setTime(long mills) {
        String time = XmTimeUtils.getListTime(mills);
        if (!getText().toString().equals(time)) {
            setText(time);
        }
    }
}
