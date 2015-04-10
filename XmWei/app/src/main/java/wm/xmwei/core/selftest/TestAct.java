package wm.xmwei.core.selftest;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;

import wm.xmwei.R;
import wm.xmwei.ui.activity.messagescan.XmMessageScanActivity;
import wm.xmwei.ui.view.observablescrollview.CacheFragmentStatePagerAdapter;

/**
 * Created by wm on 15-4-10.
 */
public class TestAct extends FragmentActivity {

    private static final String TAG = "TestAct";
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        viewPager = (ViewPager) findViewById(R.id.viewpager);

        viewPager.setAdapter(new TestFragmentAdapter(getSupportFragmentManager()));

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.w(TAG, "TAG:onPageScrolled-position:" + position + "-positionOffset:" + positionOffset + "-positionOffsetPixels:" + positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                Log.w(TAG, "TAG:onPageSelected-position:" + position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.w(TAG, "TAG:onPageScrollStateChanged-state:" + state);
            }
        });

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
            Fragment f = new XmMessageScanActivity.TestFragment();
            if (0 <= mScrollY) {
                Bundle args = new Bundle();
                args.putInt(XmMessageScanActivity.TestFragment.ARG_SCROLL_Y, mScrollY);
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

    private void init() {

    }


}
