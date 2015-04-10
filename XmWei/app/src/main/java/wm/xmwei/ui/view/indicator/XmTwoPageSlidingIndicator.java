package wm.xmwei.ui.view.indicator;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.text.Layout;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Locale;

import wm.xmwei.R;
import wm.xmwei.core.lib.support.view.XmIndicatorItemView;

/**
 * Created by wm on 15-3-25.
 */
public class XmTwoPageSlidingIndicator extends HorizontalScrollView {

    private static String TAG = "XmTwoPageSlidingIndicator";

    private final PageListener pageListener = new PageListener();
    private final RelativeLayout.LayoutParams defaultLayoutParams;
    public ViewPager.OnPageChangeListener delegatePageListener;

    private RelativeLayout tabsContainer;
    private ViewPager pager;

    private int tabCount = 2;

    private int lastPosition = -1;
    private int currentPosition = 0;

    private Paint rectPaint;


    private Typeface tabTypeface = null;
    private int tabTypefaceStyle = Typeface.NORMAL;


    private XmIndicatorItemView firstView;
    private XmIndicatorItemView secondView;

    private int mSlidingWidth;
    private int mFirstViewWidth;

    private boolean isScrollToRight = false;


    public XmTwoPageSlidingIndicator(Context context) {
        this(context, null);
    }

    public XmTwoPageSlidingIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XmTwoPageSlidingIndicator(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        setFillViewport(true);//定义滚动视图是否可以伸缩其内容以填充视口.
        setWillNotDraw(false);


        tabsContainer = new RelativeLayout(context);
        tabsContainer.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addView(tabsContainer);

        DisplayMetrics dm = getResources().getDisplayMetrics();

        // get system attrs (android:textSize and android:textColor)


        rectPaint = new Paint();
        rectPaint.setAntiAlias(true);
        rectPaint.setStyle(Paint.Style.FILL);

        defaultLayoutParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 默认如果不等比占用的话，直接默认测量测量即可
        mSlidingWidth = getMeasuredWidth();

        int count = tabsContainer.getChildCount();
        if (count >= 2) {
            firstView = (XmIndicatorItemView) tabsContainer.getChildAt(0);
            secondView = (XmIndicatorItemView) tabsContainer.getChildAt(1);

            mFirstViewWidth = firstView.getTextWidth();

        }

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        scrollToCenter(secondView, true);

        if (isInEditMode() || tabCount == 0) {
            return;
        }

//        final int height = getHeight();
//
//        rectPaint.setColor(indicatorColor); // 设置边线的颜色
//
//        // default: line below current tab
//        // 获取到当前选中的tab页
//        View currentTab = tabsContainer.getChildAt(currentPosition);
//        float lineLeft = currentTab.getLeft();
//        float lineRight = currentTab.getRight();
//
//        if (currentPositionOffset > 0f && currentPosition < tabCount - 1) {
//
//            View nextTab = tabsContainer.getChildAt(currentPosition + 1);
//            final float nextTabLeft = nextTab.getLeft();
//            final float nextTabRight = nextTab.getRight();
//
//            lineLeft = (currentPositionOffset * nextTabLeft + (1f - currentPositionOffset) * lineLeft);
//            lineRight = (currentPositionOffset * nextTabRight + (1f - currentPositionOffset) * lineRight);
//        }
//        canvas.drawRect(lineLeft, height - indicatorHeight, lineRight, height, rectPaint);

        //上面是绘制完边线

    }

    private boolean isScrolling;
    private boolean right, left;
    private int lastValue = -1;

    private class PageListener implements ViewPager.OnPageChangeListener {

        /**
         * @param position             当前可见的第一个页的position, index of the first page currently being displayed
         * @param positionOffset       当前页面偏移的百分比
         * @param positionOffsetPixels 当前页面偏移的像素位置
         */
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            Log.w(TAG, "TAG:onPageScrolled-position:" + position + "-positionOffset:" + positionOffset + "-positionOffsetPixels:" + positionOffsetPixels);

            currentPosition = position;
            lastValue = positionOffsetPixels;

            onScrollToChild(position, positionOffsetPixels);

//            invalidate();

            if (delegatePageListener != null) {
                delegatePageListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
        }

        /**
         * public static final int SCROLL_STATE_IDLE = 0;
         * public static final int SCROLL_STATE_DRAGGING = 1;
         * public static final int SCROLL_STATE_SETTLING = 2;
         *
         * @param state 有三种状态（0，1，2）。arg0 ==1的时辰默示正在滑动，arg0==2的时辰默示滑动完毕了，arg0==0的时辰默示什么都没做
         */
        @Override
        public void onPageScrollStateChanged(int state) {

            Log.w(TAG, "TAG:onPageScrollStateChanged-state:" + state);

            mCurrentState = state;

            if (state == ViewPager.SCROLL_STATE_IDLE) {
                scrollToChild(pager.getCurrentItem(), 0);
            }

            if (delegatePageListener != null) {
                delegatePageListener.onPageScrollStateChanged(state);
            }
        }

        @Override
        public void onPageSelected(int position) {
            setTextStatus(position);

            Log.w(TAG, "TAG:onPageSelected-position:" + position);

            mCurrentFragmentPosition = position;

            lastValue = -1;

            if (delegatePageListener != null) {
                delegatePageListener.onPageSelected(position);
            }

        }

    }

    private int mCurrentFragmentPosition;


    private int mCurrentState;

    /**
     * @param position 要进行滚动操作的page 的position
     * @param offset   滚动的距离值
     */
    private void scrollToChild(int position, int offset) {

        if (tabCount == 0) {
            return;
        }

//        int newScrollX = tabsContainer.getChildAt(position).getLeft() + offset;
//
//        if (newScrollX != lastScrollX) {
//            lastScrollX = newScrollX;
//            scrollTo(newScrollX, 0);
//        }

        int scrollDis = mSlidingWidth / 2 - mFirstViewWidth / 2;

    }

    private void onScrollToChild(int position, int offsetDis) {

//
//        if (tabCount == 0) {
//            return;
//        }
//
//        int newScrollX = tabsContainer.getChildAt(position).getLeft() + offset;
//
//        Log.w(TAG, "scrolling:" + isScrolling + "-- right:" + right);
//
//
//        int firstScrollX = newScrollX;
//        int secondScrollX = newScrollX;
//
//        int scrollStopDis = mSlidingWidth / 2 - mFirstViewWidth / 2;
//
//        Log.w(TAG, "scrollX:" + newScrollX);
//        Log.w(TAG, "position:" + currentPosition);
//        Log.w(TAG, "offset:" + offset);
//
//        if (right) {
//            int dis = newScrollX - (mSlidingWidth / 2 + mFirstViewWidth / 2);
//            if (dis <= 0) dis = 0;
//            firstView.scrollTo(-dis, 0);
//            secondView.scrollTo(-dis, 0);
//
//        } else {
//
//            if (newScrollX <= 0) return;
//
//            // right
//            if (firstScrollX > scrollStopDis) {
//                firstScrollX = scrollStopDis;
//            }
//
//            if (secondScrollX > scrollStopDis) {
//                secondScrollX = scrollStopDis;
//            }
//
//            firstView.scrollTo(-firstScrollX, 0);
//            secondView.scrollTo(-secondScrollX, 0);
//        }


        Log.w(TAG, "offsetPis:" + offsetDis);

        if (offsetDis <= 0) return;

//        if (mCurrentState == ViewPager.SCROLL_STATE_DRAGGING || mCurrentState == ViewPager.SCROLL_STATE_SETTLING) {
        //正在滑动
        // 滑动完毕，手指离开了界面，可能下一页，或者前面一页
        if (mCurrentFragmentPosition == 0 || (mCurrentFragmentPosition == 1 && mCurrentState == ViewPager.SCROLL_STATE_SETTLING)) {
            int scrollX = offsetDis;
            if (scrollX >= (mSlidingWidth / 2 - mFirstViewWidth / 2)) {
                scrollX = (mSlidingWidth / 2 - mFirstViewWidth / 2);
            }

            Log.w(TAG, "enter right logic :scrollX-" + scrollX + "-offsetDis-" + offsetDis);
            int hasScroll = firstView.getScrollX();

            int scrollByDis = scrollX - hasScroll;

            firstView.scrollBy(scrollByDis, 0);
            secondView.scrollBy(scrollByDis, 0);

            Log.w(TAG, "rightscroll:" + offsetDis);

            // 上面的=1 滑动释放状态有两种情况  释放后向左侧滑动，释放后向右侧滑动两种，  释放后向右侧滑动有问题

        } else if (mCurrentFragmentPosition == 1) {
            // left
            // offsetDis  598 - 0  scrollX
            int scrollX = mSlidingWidth / 2 - offsetDis; // 移动像素

            int dis = mSlidingWidth / 2;

            if (scrollX <= mSlidingWidth / 2 - mFirstViewWidth / 2) {
                int scrollDis = (mSlidingWidth / 2 - mFirstViewWidth / 2) - scrollX - dis;
                if (scrollDis >= 0) {

                    int hasScroll = firstView.getScrollX();

                    int scrollByDis = scrollDis - hasScroll;

                    firstView.scrollBy(scrollByDis, 0);
                    secondView.scrollBy(scrollByDis, 0);
                    Log.w(TAG, "leftscroll:firstView has scroll:" + firstView.getScrollX());
                    Log.w(TAG, "leftscroll:offsetDis:" + scrollX);
                }
            }
        }

//        }


    }

    private void scrollToCenter(View view, boolean isToLeftScroll) {
        int viewWidth = view.getMeasuredWidth();
        int centerWidth = mSlidingWidth / 2;
        int scrollDis = centerWidth - viewWidth / 2;

        int leftDis = view.getLeft();
        if ((leftDis + viewWidth / 2 == centerWidth)) {
            return;
        }

        scrollDis = isToLeftScroll ? -scrollDis : scrollDis;
        view.scrollBy(scrollDis, 0);


    }

    private void scrollToLeft(View view) {
        int leftDis = view.getLeft();
        if (leftDis > 0) {
            view.scrollBy(leftDis, 0);
        }
    }

    private void scrollToRight(View view) {
        int rightDis = view.getRight();
        view.scrollBy(-rightDis, 0);
    }


    private void setTextStatus(int position) {
        if (position != lastPosition) {
            if (lastPosition != -1) {
                setTabTextColor(lastPosition, false);
            }

            setTabTextColor(position, true);
            lastPosition = position;
        }

    }

    private void setTabTextColor(int position, boolean isSelect) {
        XmIndicatorItemView selectView = ((XmIndicatorItemView) tabsContainer.getChildAt(position));
        if (isSelect) {
            selectView.setTextColor(getResources().getColor(R.color.v_title_bg));
        } else {
            selectView.setTextColor(getResources().getColor(R.color.v_title_black_color));
        }

    }

    public void setViewPager(ViewPager pager) {
        this.pager = pager;

        if (pager.getAdapter() == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }

        pager.setOnPageChangeListener(pageListener);

        notifyDataSetChanged();
    }

    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        this.delegatePageListener = listener;
    }

    public void notifyDataSetChanged() {

        tabsContainer.removeAllViews();

        tabCount = pager.getAdapter().getCount();

        for (int i = 0; i < tabCount; i++) {
            addTextTab(i, pager.getAdapter().getPageTitle(i).toString());
        }

        setDefaultStatus();

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @SuppressLint("NewApi")
            @Override
            public void onGlobalLayout() {

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }

                currentPosition = pager.getCurrentItem();
                scrollToChild(currentPosition, 0);
            }
        });

    }

    private void addTextTab(final int position, String title) {
        XmIndicatorItemView currentView = null;
        TextView tab = null;


        currentView = new XmIndicatorItemView(getContext());
        tab = currentView.getTextView();

        tab.setText(title);
        tab.setFocusable(true);
        tab.setSingleLine();

        if (position == 0) {
            currentView.setToCenter();
        } else {
            currentView.setToRight();
        }

//        tab.setTextSize(TypedValue.COMPLEX_UNIT_DIP, tabTextSize);
        tab.setTypeface(tabTypeface, tabTypefaceStyle);
        tab.setTextColor(getResources().getColor(R.color.v_title_black_color));


        tab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(position);
            }
        });

        tabsContainer.addView(currentView, defaultLayoutParams);

    }

    private void setDefaultStatus() {
        setTextStatus(pager.getCurrentItem());
    }

}
