package wm.xmwei.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import wm.xmwei.R;
import wm.xmwei.XmApplication;
import wm.xmwei.bean.UserBingDomain;
import wm.xmwei.ui.adapter.XmCommentsPagerAdapter;
import wm.xmwei.ui.fragment.base.XmBaseFragment;
import wm.xmwei.ui.view.indicator.PageSlidingIndicator;
import wm.xmwei.ui.view.indicator.PagerSlidingTabIndicator;

/**
 * this is commment
 */
public class XmCommentsFragment extends XmBaseFragment {

    private ViewPager mViewPager;
    private PageSlidingIndicator mPageIndicator;

    private SparseArray<Fragment> mChildFragments = new SparseArray<Fragment>();


    public static final int COMMENTS_TO_ME_CHILD_POSITION = 0; // 评论我的，
    public static final int COMMENTS_BY_ME_CHILD_POSITION = 1; // 我评论的

    public static XmCommentsFragment newInstance(Bundle bundle) {

        XmCommentsFragment fragment = new XmCommentsFragment();

        if (bundle != null) {
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.layer_comments, null);
        initView(mainView);
        return mainView;

    }

    private void initView(View mainView) {
        mPageIndicator = (PageSlidingIndicator) mainView.findViewById(R.id.v_comment_indicator);
        mViewPager = (ViewPager) mainView.findViewById(R.id.v_comment_pager);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewPager.setOverScrollMode(View.OVER_SCROLL_NEVER);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setOnPageChangeListener(mOnPageChangeListener);

        buildChildFragment();

        String left = getResources().getString(R.string.all_people_send_to_me);
        String right = getResources().getString(R.string.my_comment);
        String[] title = new String[]{left, right};

        mViewPager.setAdapter(new XmCommentsPagerAdapter(getChildFragmentManager(), mChildFragments, title));
        mPageIndicator.setViewPager(mViewPager);
        mPageIndicator.setOnPageChangeListener(mOnPageChangeListener);

    }

    private void buildChildFragment() {

        mChildFragments.append(XmCommentsFragment.COMMENTS_TO_ME_CHILD_POSITION,
                getXmCommentsToMeFragment());
        mChildFragments.append(XmCommentsFragment.COMMENTS_BY_ME_CHILD_POSITION,
                getXmCommentsByMeFragment());

    }

    public XmCommentsToMeFragment getXmCommentsToMeFragment() {
        XmCommentsToMeFragment fragment
                = ((XmCommentsToMeFragment) getChildFragmentManager().findFragmentByTag(
                XmCommentsToMeFragment.class.getName()));
        if (fragment == null) {

            UserBingDomain bingDomain = XmApplication.getInstance().getUserBingDomain();

            fragment = XmCommentsToMeFragment.newInstance(bingDomain);
        }

        return fragment;
    }

    public XmCommentsByMeFragment getXmCommentsByMeFragment() {
        XmCommentsByMeFragment fragment
                = ((XmCommentsByMeFragment) getChildFragmentManager().findFragmentByTag(
                XmCommentsByMeFragment.class.getName()));
        if (fragment == null) {
            fragment = XmCommentsByMeFragment.newInstance(null);
        }

        return fragment;
    }

    private ViewPager.SimpleOnPageChangeListener mOnPageChangeListener
            = new ViewPager.SimpleOnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);

        }

        @Override
        public void onPageScrollStateChanged(int state) {
            super.onPageScrollStateChanged(state);

        }

    };
}
