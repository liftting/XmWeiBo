package wm.xmwei.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import wm.xmwei.R;
import wm.xmwei.bean.DataCommentListDomain;
import wm.xmwei.bean.base.DataListDomain;
import wm.xmwei.ui.adapter.data.XmCommentsDataAdapter;
import wm.xmwei.ui.fragment.base.XmBaseListFragment;

/**
 * Created by
 */
public class XmCommentsToMeFragment extends XmBaseListFragment {

    private Context mContext;
    private DataListDomain mCommentsToMeDataList = new DataCommentListDomain();

    public static XmCommentsToMeFragment newInstance(Bundle bundle) {
        XmCommentsToMeFragment fragment = new XmCommentsToMeFragment();
        if (bundle != null) {
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    protected void createDataListAdapter() {
        XmCommentsDataAdapter commentsDataAdapter = new XmCommentsDataAdapter(getActivity(),
                getDataList().getItemList());
        mBaseDataAdapter = commentsDataAdapter;
        mPullToRefreshListView.setAdapter(commentsDataAdapter);
    }

    @Override
    public DataListDomain getDataList() {
        //进行数据加载，

        return mCommentsToMeDataList;
    }

    @Override
    protected void onItemClick(AdapterView parent, View view, int position, long id) {

    }


}
