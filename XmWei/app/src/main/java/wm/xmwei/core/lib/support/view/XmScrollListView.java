package wm.xmwei.core.lib.support.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 这个可以实现在scrollView中放置listView问题，原理是直接重新计算高度
 */
public class XmScrollListView extends ListView {

    public XmScrollListView(Context context) {
        super(context);
    }

    public XmScrollListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public XmScrollListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);

    }
}
