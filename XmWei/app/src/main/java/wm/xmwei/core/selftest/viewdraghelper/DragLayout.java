package wm.xmwei.core.selftest.viewdraghelper;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import wm.xmwei.core.lib.support.view.swipebacklayout.ViewDragHelper;

/**
 * Created by wm on 15-4-16.
 */
public class DragLayout extends LinearLayout {

    private ViewDragHelper mDragHelper;
    private View dragContentView; // 内容页面
    private View topView;// top view


    public DragLayout(Context context) {
        this(context, null, 0);
    }

    public DragLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mDragHelper = ViewDragHelper.create(this, 1.0f, new DragHelperCallBack());

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();


        if (getChildCount() < 2) {
            throw new RuntimeException("Content view must contains two child views at least.");
        }


        topView = getChildAt(0);
        dragContentView = getChildAt(1);


    }

    private class DragHelperCallBack extends ViewDragHelper.Callback {

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return false;
        }

        // 水平拖动效果
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            return super.clampViewPositionHorizontal(child, left, dx);
        }

        // 竖直方向拖动
        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            return super.clampViewPositionVertical(child, top, dy);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = MotionEventCompat.getActionMasked(ev);
        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            mDragHelper.cancel();
            return false;
        }
        return mDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        mDragHelper.processTouchEvent(ev);
        return true;
    }

}
