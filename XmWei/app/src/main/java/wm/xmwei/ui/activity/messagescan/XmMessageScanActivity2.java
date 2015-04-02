package wm.xmwei.ui.activity.messagescan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import wm.xmwei.R;
import wm.xmwei.XmApplication;
import wm.xmwei.ui.activity.base.XmBaseActivity;
import wm.xmwei.ui.view.indicator.SlidingTabLayout;
import wm.xmwei.ui.view.observablescrollview.CacheFragmentStatePagerAdapter;
import wm.xmwei.ui.view.observablescrollview.ObservableScrollView;
import wm.xmwei.ui.view.observablescrollview.ObservableScrollViewCallbacks;
import wm.xmwei.ui.view.observablescrollview.ScrollUtils;

/**
 */
public class XmMessageScanActivity2 extends XmBaseActivity {

    private ViewPager mPager;
    private TestFragmentAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layer_message_scan_2);
        mPager = (ViewPager) findViewById(R.id.v_message_scan_pager);
        mPagerAdapter = new TestFragmentAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);

        SlidingTabLayout slidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
        slidingTabLayout.setCustomTabView(R.layout.layer_tab_indicator, android.R.id.text1);
        slidingTabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.lightskyblue));
        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setViewPager(mPager);

    }

    public static Intent newIntent() {
        Intent intent = new Intent();
        intent.setClass(XmApplication.getInstance(), XmMessageScanActivity2.class);
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
