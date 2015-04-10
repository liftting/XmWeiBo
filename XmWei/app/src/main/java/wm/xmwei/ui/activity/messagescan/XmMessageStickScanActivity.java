package wm.xmwei.ui.activity.messagescan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import wm.xmwei.R;
import wm.xmwei.XmApplication;
import wm.xmwei.core.lib.support.eventbus.EventBus;
import wm.xmwei.core.selftest.TabFragment;
import wm.xmwei.ui.activity.base.XmBaseActivity;
import wm.xmwei.ui.view.draptopout.DragTopLayout;
import wm.xmwei.ui.view.indicator.XmTwoPageSlidingIndicator;

/**
 * Created by wm on 15-4-10.
 */
public class XmMessageStickScanActivity extends XmBaseActivity {

    private String[] mTitles = new String[]{"评论", "转发"};
    private XmTwoPageSlidingIndicator mIndicator;
    private ViewPager mViewPager;
    private FragmentPagerAdapter mAdapter;
    private TabFragment[] mFragments = new TabFragment[mTitles.length];

    private DragTopLayout dragLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layer_message_scan_staick_main);

        initViews();
        initDatas();
    }

    public static Intent newIntent() {
        Intent intent = new Intent();
        intent.setClass(XmApplication.getInstance(), XmMessageStickScanActivity.class);
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
        mIndicator = (XmTwoPageSlidingIndicator) findViewById(R.id.v_message_stick_indicator);
        mViewPager = (ViewPager) findViewById(R.id.v_message_stick_pager);
        dragLayout = (DragTopLayout) findViewById(R.id.v_message_stick_drag_layout);
    }


    // Handle scroll event from fragments
    public void onEvent(Boolean b){
        dragLayout.setTouchMode(b);
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }


}
