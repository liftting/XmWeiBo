package wm.xmwei.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import wm.xmwei.R;
import wm.xmwei.bean.DataCommentListDomain;
import wm.xmwei.bean.UserBingDomain;
import wm.xmwei.bean.base.DataListDomain;
import wm.xmwei.datadao.DataLoadResult;
import wm.xmwei.ui.adapter.data.XmCommentsDataAdapter;
import wm.xmwei.ui.dataloader.XmCommentsToMeLoader;
import wm.xmwei.ui.fragment.base.XmBaseListFragment;

/**
 * Created by
 */
public class XmCommentsToMeFragment extends XmBaseListFragment<DataCommentListDomain> {

    private static final String ARGUMENTS_ACCOUNT_EXTRA = XmCommentsToMeFragment.class.getName() + ":account_extra";
    private static final String ARGUMENTS_USER_EXTRA = XmCommentsToMeFragment.class.getName() + ":userBean_extra";
    private static final String ARGUMENTS_TOKEN_EXTRA = XmCommentsToMeFragment.class.getName() + ":token_extra";
    private static final String ARGUMENTS_DATA_EXTRA = XmCommentsToMeFragment.class.getName() + ":msg_extra";
    private static final String ARGUMENTS_TIMELINE_POSITION_EXTRA = XmCommentsToMeFragment.class.getName()
            + ":timeline_position_extra";

    private Context mContext;
    private DataCommentListDomain mCommentsToMeDataList = new DataCommentListDomain();

    private UserBingDomain mUserBingDomain;

    public static XmCommentsToMeFragment newInstance(UserBingDomain userBingDomain) {
        XmCommentsToMeFragment fragment = new XmCommentsToMeFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARGUMENTS_ACCOUNT_EXTRA, userBingDomain);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getActivity();
        mUserBingDomain = getArguments().getParcelable(ARGUMENTS_ACCOUNT_EXTRA);


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
        getListView().setAdapter(mBaseDataAdapter);
    }

    @Override
    public DataCommentListDomain getDataList() {
        //进行数据加载，
        return mCommentsToMeDataList;
    }

    @Override
    protected Loader<DataLoadResult<DataCommentListDomain>> onCreateNewDataLoader(int id, Bundle args) {

        // 去构建loader
        String accountId = mUserBingDomain.getUid();
        String token = mUserBingDomain.getAccess_token();
        String sinceId = null;
        if (getDataList().getItemList().size() > 0) {
            sinceId = getDataList().getItemList().get(0).getId();
        }
        return new XmCommentsToMeLoader(getActivity(), accountId, token, sinceId, null);

    }

    @Override
    protected void onItemClick(AdapterView parent, View view, int position, long id) {

    }

    @Override
    protected void onNewDataLoaderSuccessCallback(DataCommentListDomain newValue, Bundle loaderArgs) {
        getDataList().addNewData(newValue);
    }

    @Override
    protected void onOldDataLoaderSuccessCallback(DataCommentListDomain newValue) {

    }


}
