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

    public XmHomeFragmentAdapter(FragmentManager fm, List<Fragment> dataList, List<String> tagList) {
        super(fm);

        mTagList = tagList;
        mFragmentList = dataList;
    }

    @Override
    protected String getTag(int position) {
        return null;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return 0;
    }
}
