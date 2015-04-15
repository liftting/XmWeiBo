package wm.xmwei.ui.activity.messagescan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.FrameLayout;

import wm.xmwei.R;
import wm.xmwei.XmApplication;
import wm.xmwei.bean.DataMessageDomain;
import wm.xmwei.core.lib.support.eventbus.EventBus;
import wm.xmwei.core.selftest.TabFragment;
import wm.xmwei.ui.activity.base.XmBaseActivity;
import wm.xmwei.ui.fragment.messagescan.XmMessageCommentFragment;
import wm.xmwei.ui.view.draptopout.DragTopLayout;
import wm.xmwei.ui.view.indicator.XmTwoPageSlidingIndicator;
import wm.xmwei.ui.view.lib.XmMessageScanHeaderView;

/**
 * Created by wm on 15-4-10.
 */
public class XmMessageStickScanActivity extends XmBaseActivity {

    private static final String ACTION_WITH_ID = "action_with_id";
    private static final String ACTION_WITH_DETAIL = "action_with_detail";

    private String[] mTitles = new String[]{"评论", "转发"};
    private XmTwoPageSlidingIndicator mIndicator;
    private ViewPager mViewPager;
    private FragmentPagerAdapter mAdapter;
    private Fragment[] mFragments = new Fragment[mTitles.length];

    private DragTopLayout dragLayout;

    private String token;
    private String mDataMessageId;
    private DataMessageDomain mDataMessageDomain;

    private FrameLayout mHeaderLayout;
    private XmMessageScanHeaderView mHeadView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layer_message_scan_staick_main);

        String action = getIntent().getAction();
        if (ACTION_WITH_DETAIL.equalsIgnoreCase(action)) {
            Intent intent = getIntent();
            token = intent.getStringExtra("token");
            mDataMessageDomain = intent.getParcelableExtra("msg");
        } else {
            throw new IllegalArgumentException(
                    "activity intent action must be " + ACTION_WITH_DETAIL + " or "
                            + ACTION_WITH_ID
            );
        }

        mHeadView = new XmMessageScanHeaderView(this, mDataMessageDomain);

        initViews();
        initDatas();
    }

    public static Intent newIntent(DataMessageDomain messageDomain, String token) {
        Intent intent = new Intent(XmApplication.getInstance(), XmMessageStickScanActivity.class);
        intent.putExtra("msg", messageDomain);
        intent.putExtra("token", token);
        intent.setAction(ACTION_WITH_DETAIL);
        return intent;
    }

    private void initDatas() {
        mFragments[0] = XmMessageCommentFragment.newInstance(mDataMessageDomain);
        mFragments[1] = TabFragment.newInstance(mTitles[1]);


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

        mHeaderLayout = (FrameLayout) findViewById(R.id.top_view);
        mHeaderLayout.addView(mHeadView);

    }


    // Handle scroll event from fragments
    public void onEvent(Boolean b) {
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
