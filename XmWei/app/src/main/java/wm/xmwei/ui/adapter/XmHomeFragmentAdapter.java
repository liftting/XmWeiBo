package wm.xmwei.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.util.List;

/**
 * Created by wm on 15-3-17.
 */
public class XmHomeFragmentAdapter extends XmFragmentPagerAdapter {

    private List<Fragment> mFragmentList;
    private List<String> mTagList;
    private String[] mTitle;

    public XmHomeFragmentAdapter(FragmentManager fm, List<Fragment> dataList, List<String> tagList, String[] title) {
        super(fm);

        mTagList = tagList;
        mFragmentList = dataList;
        mTitle = title;
    }

    @Override
    protected String getTag(int position) {
        return mTagList.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitle[position];
    }
}
