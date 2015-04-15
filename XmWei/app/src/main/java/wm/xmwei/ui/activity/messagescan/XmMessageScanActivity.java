package wm.xmwei.ui.activity.messagescan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

import java.util.ArrayList;
import java.util.List;

import wm.xmwei.R;
import wm.xmwei.XmApplication;
import wm.xmwei.core.lib.support.view.StickyScrollView;
import wm.xmwei.ui.activity.base.XmBaseActivity;
import wm.xmwei.ui.view.indicator.SlidingTabLayout;
import wm.xmwei.ui.view.observablescrollview.CacheFragmentStatePagerAdapter;
import wm.xmwei.ui.view.observablescrollview.ObservableScrollView;
import wm.xmwei.ui.view.observablescrollview.ObservableScrollViewCallbacks;
import wm.xmwei.ui.view.observablescrollview.ScrollState;
import wm.xmwei.ui.view.observablescrollview.ScrollUtils;

/**
 * this is handle the operation
 */

@Deprecated
public class XmMessageScanActivity extends XmBaseActivity implements ObservableScrollViewCallbacks {

    private ViewPager mPager;

    private ListView mDataListView;
    private List<String> datas = new ArrayList<String>();
    private List<Fragment> mDataList = new ArrayList<Fragment>();

    private View mHeaderView;
    private View mToolbarView;
    private TestFragmentAdapter mPagerAdapter;

    private int mBaseTranslationY;

    private int range;

    private FrameLayout mFlyContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layer_sticky_test);

        mPager = (ViewPager) findViewById(R.id.v_message_scan_pager);
        mHeaderView = findViewById(R.id.v_head_view);
        mFlyContent = (FrameLayout) findViewById(R.id.fly_data_content);

        ViewCompat.setElevation(mHeaderView, getResources().getDimension(R.dimen.toolbar_elevation)); //this is set the headview shadow effect

        mToolbarView = findViewById(R.id.v_toobar_view);

        mDataList.add(new TestFragment());
        mDataList.add(new TestFragment());

        mPagerAdapter = new TestFragmentAdapter(getSupportFragmentManager());
        mPagerAdapter.setScrollY(range);

        mPager.setAdapter(mPagerAdapter);

        SlidingTabLayout slidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
        slidingTabLayout.setCustomTabView(R.layout.layer_tab_indicator, android.R.id.text1);
        slidingTabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.lightskyblue));
        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setViewPager(mPager);

        // When the page is selected, other fragments' scrollY should be adjusted
        // according to the toolbar status(shown/hidden)
        slidingTabLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {
            }

            @Override
            public void onPageSelected(int i) {
                propagateToolbarState(toolbarIsShown());
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });

        propagateToolbarState(toolbarIsShown());


    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        if (dragging) {
            int toolbarHeight = mToolbarView.getHeight();
            float currentHeaderTranslationY = ViewHelper.getTranslationY(mHeaderView);
            if (firstScroll) {
                if (-toolbarHeight < currentHeaderTranslationY) {
                    mBaseTranslationY = scrollY;
                }
            }
            float headerTranslationY = ScrollUtils.getFloat(-(scrollY - mBaseTranslationY), -toolbarHeight, 0);
            ViewPropertyAnimator.animate(mHeaderView).cancel();
            ViewHelper.setTranslationY(mHeaderView, headerTranslationY);
        }
    }

    @Override
    public void onDownMotionEvent() {
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        mBaseTranslationY = 0;

        Fragment fragment = getCurrentFragment();
        if (fragment == null) {
            return;
        }
        View view = fragment.getView();
        if (view == null) {
            return;
        }

        int toolbarHeight = mToolbarView.getHeight();
        final ObservableScrollView scrollView = (ObservableScrollView) view.findViewById(R.id.scroll);
        if (scrollView == null) {
            return;
        }
        int scrollY = scrollView.getCurrentScrollY();
        if (scrollState == ScrollState.DOWN) {
            showToolbar();
        } else if (scrollState == ScrollState.UP) {
            if (toolbarHeight <= scrollY) {
                hideToolbar();
            } else {
                showToolbar();
            }
        } else {
            // Even if onScrollChanged occurs without scrollY changing, toolbar should be adjusted
            if (toolbarIsShown() || toolbarIsHidden()) {
                // Toolbar is completely moved, so just keep its state
                // and propagate it to other pages
                propagateToolbarState(toolbarIsShown());
            } else {
                // Toolbar is moving but doesn't know which to move:
                // you can change this to hideToolbar()
                showToolbar();
            }
        }
    }

    private Fragment getCurrentFragment() {
        return mPagerAdapter.getItemAt(mPager.getCurrentItem());
    }


    private boolean toolbarIsShown() {
        return ViewHelper.getTranslationY(mHeaderView) == 0;
    }

    private boolean toolbarIsHidden() {
        return ViewHelper.getTranslationY(mHeaderView) == -mToolbarView.getHeight();
    }

    private void showToolbar() {
        float headerTranslationY = ViewHelper.getTranslationY(mHeaderView);
        if (headerTranslationY != 0) {
            ViewPropertyAnimator.animate(mHeaderView).cancel();
            ViewPropertyAnimator.animate(mHeaderView).translationY(0).setDuration(200).start();
        }
        propagateToolbarState(true);
    }

    private void hideToolbar() {
        float headerTranslationY = ViewHelper.getTranslationY(mHeaderView);
        int toolbarHeight = mToolbarView.getHeight();
        if (headerTranslationY != -toolbarHeight) {
            ViewPropertyAnimator.animate(mHeaderView).cancel();
            ViewPropertyAnimator.animate(mHeaderView).translationY(-toolbarHeight).setDuration(200).start();
        }
        propagateToolbarState(false);
    }

    private void propagateToolbarState(boolean isShown) {
        int toolbarHeight = mToolbarView.getHeight();

        // Set scrollY for the fragments that are not created yet
        mPagerAdapter.setScrollY(isShown ? 0 : toolbarHeight);

        // Set scrollY for the active fragments
        for (int i = 0; i < mPagerAdapter.getCount(); i++) {
            // Skip current item
            if (i == mPager.getCurrentItem()) {
                continue;
            }

            // Skip destroyed or not created item
            Fragment f = mPagerAdapter.getItemAt(i);
            if (f == null) {
                continue;
            }

            ObservableScrollView scrollView = (ObservableScrollView) f.getView().findViewById(R.id.scroll);
            if (isShown) {
                // Scroll up
                if (0 < scrollView.getCurrentScrollY()) {
                    scrollView.scrollTo(0, 0);
                }
            } else {
                // Scroll down (to hide padding)
                if (scrollView.getCurrentScrollY() < toolbarHeight) {
                    scrollView.scrollTo(0, toolbarHeight);
                }
            }
        }
    }

    public static Intent newIntent() {
        Intent intent = new Intent();
        intent.setClass(XmApplication.getInstance(), XmMessageScanActivity.class);
        return intent;
    }


    private static class TestFragmentAdapter extends CacheFragmentStatePagerAdapter {

        private static final String[] TITLES = new String[]{"Applepie", "Butter Cookie", "Cupcake", "Donut", "Eclair", "Froyo", "Gingerbread", "Honeycomb", "Ice Cream Sandwich", "Jelly Bean", "KitKat", "Lollipop"};

        private int mScrollY;

        public TestFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        public void setScrollY(int scrollY) {
            mScrollY = scrollY;
        }

        @Override
        protected Fragment createItem(int position) {
            Fragment f = new TestFragment();
            if (0 <= mScrollY) {
                Bundle args = new Bundle();
                args.putInt(TestFragment.ARG_SCROLL_Y, mScrollY);
                f.setArguments(args);
            }
            return f;
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }
    }

    public static class TestFragment extends Fragment {
        public TestFragment() {
            super();
        }

        public static final String ARG_SCROLL_Y = "ARG_SCROLL_Y";

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_scrollview, container, false);

            final ObservableScrollView scrollView = (ObservableScrollView) view.findViewById(R.id.scroll);
            Activity parentActivity = getActivity();
            if (parentActivity instanceof ObservableScrollViewCallbacks) {
                // Scroll to the specified offset after layout
                Bundle args = getArguments();
                if (args != null && args.containsKey(ARG_SCROLL_Y)) {
                    final int scrollY = args.getInt(ARG_SCROLL_Y, 0);
                    ScrollUtils.addOnGlobalLayoutListener(scrollView, new Runnable() {
                        @Override
                        public void run() {
                            scrollView.scrollTo(0, scrollY);
                        }
                    });
                }
                scrollView.setScrollViewCallbacks((ObservableScrollViewCallbacks) parentActivity); //注册listener
            }
            return view;
        }


    }

}
