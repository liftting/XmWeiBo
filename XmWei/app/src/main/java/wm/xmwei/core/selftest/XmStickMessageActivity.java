package wm.xmwei.core.selftest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;

import wm.xmwei.R;
import wm.xmwei.XmApplication;
import wm.xmwei.ui.view.indicator.XmTwoPageSlidingIndicator;

/**
 * Created by wm on 15-4-8.
 */
public class XmStickMessageActivity extends FragmentActivity {

    private String[] mTitles = new String[]{"评论", "转发"};
    private XmTwoPageSlidingIndicator mIndicator;
    private ViewPager mViewPager;
    private FragmentPagerAdapter mAdapter;
    private TabFragment[] mFragments = new TabFragment[mTitles.length];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layer_self_stick_layout_test);

        initViews();
        initDatas();
    }

    public static Intent newIntent() {
        Intent intent = new Intent();
        intent.setClass(XmApplication.getInstance(), XmStickMessageActivity.class);
        return intent;
    }

    private void initDatas() {
        for (int i = 0; i < mTitles.length; i++) {
            mFragments[i] = (TabFragment) TabFragment.newInstance(mTitles[i]);
        }

        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return mTitles.length;
            }

            @Override
            public Fragment getItem(int position) {
                return mFragments[position];
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mTitles[position];
            }
        };

        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(0);
        mIndicator.setViewPager(mViewPager);
    }

    private void initViews() {
        mIndicator = (XmTwoPageSlidingIndicator) findViewById(R.id.id_stickynavlayout_indicator);
        mViewPager = (ViewPager) findViewById(R.id.id_stickynavlayout_viewpager);
    }

}
