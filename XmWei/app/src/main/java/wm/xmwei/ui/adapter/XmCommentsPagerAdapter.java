package wm.xmwei.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;

import wm.xmwei.ui.fragment.XmCommentsFragment;

/**
 * Created by wm on 15-3-17.
 */
public class XmCommentsPagerAdapter extends XmFragmentPagerAdapter {

    private SparseArray<Fragment> mFragmentDataList;
    private String[] title;

    public XmCommentsPagerAdapter(FragmentManager fm,SparseArray<Fragment> dataList, String[] pagerTitle) {
        super(fm);
        this.mFragmentDataList = dataList;
        title = pagerTitle;
    }

    @Override
    protected String getTag(int position) {
        SparseArray<String> tagList = new SparseArray<String>();
        tagList.append(XmCommentsFragment.COMMENTS_TO_ME_CHILD_POSITION,
                XmCommentsFragment.class.getName());
        tagList.append(XmCommentsFragment.COMMENTS_BY_ME_CHILD_POSITION,
                XmCommentsFragment.class.getName());

        return tagList.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return title[position];
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentDataList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentDataList.size();
    }
}
