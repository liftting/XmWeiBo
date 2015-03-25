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

    private RelativeLayout.LayoutParams defaultTabLayoutParams;

    private final PageListener pageListener = new PageListener();
    public ViewPager.OnPageChangeListener delegatePageListener;

    private RelativeLayout tabsContainer;
    private ViewPager pager;

    private int tabCount = 2;

    private int lastPosition = -1;
    private int currentPosition = 0;
    private float currentPositionOffset = 0f;

    private Paint rectPaint;


    private int indicatorColor = 0xFF666666;

    private int scrollOffset = 52;
    private int indicatorHeight = 8;
    private int tabPadding = 0;

    private int tabTextSize = 14; // this is dp
    private Typeface tabTypeface = null;
    private int tabTypefaceStyle = Typeface.NORMAL;

    private int lastScrollX = 0;
    private boolean checkedTabWidths = false;


    private XmIndicatorItemView firstView;
    private XmIndicatorItemView secondView;


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
//        tabsContainer.setOrientation(LinearLayout.HORIZONTAL);
        tabsContainer.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addView(tabsContainer);

        DisplayMetrics dm = getResources().getDisplayMetrics();

        // get system attrs (android:textSize and android:textColor)


        rectPaint = new Paint();
        rectPaint.setAntiAlias(true);
        rectPaint.setStyle(Paint.Style.FILL);

        defaultTabLayoutParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 默认如果不等比占用的话，直接默认测量测量即可

        int myWidth = getMeasuredWidth();
        int childWidth = 0;
        for (int i = 0; i < tabCount; i++) {
            childWidth += tabsContainer.getChildAt(i).getMeasuredWidth();
        }

        if (!checkedTabWidths && childWidth > 0 && myWidth > 0) {

            if (childWidth <= myWidth) {
                for (int i = 0; i < tabCount; i++) {
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                    if (i == 0) {
                        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                    } else {
                        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
                    }

                    tabsContainer.getChildAt(i).setLayoutParams(layoutParams);
                }
            }

            checkedTabWidths = true;
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

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

    private class PageListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            currentPosition = position;
            currentPositionOffset = positionOffset;

            scrollToChild(position, (int) (positionOffset * tabsContainer.getChildAt(position).getWidth()));

            invalidate();

            if (delegatePageListener != null) {
                delegatePageListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
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

            if (delegatePageListener != null) {
                delegatePageListener.onPageSelected(position);
            }
        }

    }

    private void scrollToChild(int position, int offset) {

        if (tabCount == 0) {
            return;
        }

        int newScrollX = tabsContainer.getChildAt(position).getLeft() + offset;

        if (position > 0 || offset > 0) {
            newScrollX -= scrollOffset;
        }

        if (newScrollX != lastScrollX) {
            lastScrollX = newScrollX;
            scrollTo(newScrollX, 0);
        }

        firstView.scrollTo(-newScrollX,0);
        secondView.scrollTo(-newScrollX,0);

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

        checkedTabWidths = false;

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
        if (position == 0) {
            firstView = new XmIndicatorItemView(getContext());
            currentView = firstView;
            tab = firstView.getTextView();
        } else {
            secondView = new XmIndicatorItemView(getContext());
            currentView = secondView;
            tab = secondView.getTextView();
        }

        tab.setText(title);
        tab.setFocusable(true);
        tab.setGravity(Gravity.CENTER);
        tab.setSingleLine();

//        tab.setTextSize(TypedValue.COMPLEX_UNIT_DIP, tabTextSize);
        tab.setTypeface(tabTypeface, tabTypefaceStyle);
        tab.setTextColor(getResources().getColor(R.color.v_title_black_color));

        RelativeLayout.LayoutParams layoutParams = defaultTabLayoutParams;

        tab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(position);
            }
        });

        tabsContainer.addView(currentView, layoutParams);
    }

    private void setDefaultStatus() {
        setTextStatus(pager.getCurrentItem());
    }

}
