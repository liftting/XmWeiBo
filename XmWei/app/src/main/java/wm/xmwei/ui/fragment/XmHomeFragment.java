package wm.xmwei.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import wm.xmwei.ui.fragment.base.XmBaseFragment;
import wm.xmwei.ui.view.indicator.PageSlidingIndicator;

/**
 * this is home fragment
 */
public class XmHomeFragment extends XmBaseFragment {

    private ViewPager mHomeViewPager;
    private PageSlidingIndicator mHomePageIndicator;

    private SparseArray<Fragment> mHomeChildFragments = new SparseArray<Fragment>();

    public static XmHomeFragment newInstance(Bundle bundle) {

        XmHomeFragment fragment = new XmHomeFragment();

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }




}
