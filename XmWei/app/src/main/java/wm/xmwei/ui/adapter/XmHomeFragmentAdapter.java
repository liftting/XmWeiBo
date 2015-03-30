package wm.xmwei.ui.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.util.List;

import wm.xmwei.bean.DataGroupDomain;
import wm.xmwei.ui.fragment.XmHomeBaseFragment;

/**
 * Created by wm on 15-3-17.
 */
public class XmHomeFragmentAdapter extends XmFragmentPagerAdapter {

    private List<String> mTagList;
    private String[] mTitle;
    private int dataSize;
    private List<DataGroupDomain> mGroupList;

    public XmHomeFragmentAdapter(FragmentManager fm, List<String> tagList, List<DataGroupDomain> groupDomainList) {
        super(fm);

        mTagList = tagList;
        mGroupList = groupDomainList;
        dataSize = groupDomainList.size();

        buildTitle();
    }

    private void buildTitle() {
        mTitle = new String[mGroupList.size()];

        for (int i = 0; i < mGroupList.size(); i++) {
            DataGroupDomain domain = mGroupList.get(i);
            mTitle[i] = domain.getName();
        }
    }

    @Override
    protected String getTag(int position) {
        return mTagList.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        //在base adapter会判断是否需要重新构建fragment还是，findTag 之前的进行复用
        return createFragment(mGroupList.get(position));
    }

    @Override
    public int getCount() {
        return dataSize;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitle[position];
    }

    private XmHomeBaseFragment createFragment(DataGroupDomain groupDomain) {
        Bundle oneBundle = new Bundle();
        oneBundle.putParcelable(XmHomeBaseFragment.HOME_FRAGMENT_GROUP_KEY, groupDomain);
        return XmHomeBaseFragment.newInstance(oneBundle);
    }

}
