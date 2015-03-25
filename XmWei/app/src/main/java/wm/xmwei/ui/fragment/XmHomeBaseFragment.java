package wm.xmwei.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import wm.xmwei.R;
import wm.xmwei.XmApplication;
import wm.xmwei.bean.DataGroupDomain;
import wm.xmwei.bean.DataMessageListDomain;
import wm.xmwei.bean.UserBingDomain;
import wm.xmwei.bean.base.DataListDomain;
import wm.xmwei.datadao.DataLoadResult;
import wm.xmwei.ui.adapter.data.XmHomeDataAdapter;
import wm.xmwei.ui.dataloader.XmHomeMessageLoader;
import wm.xmwei.ui.fragment.base.XmBaseListFragment;

/**
 * this is home item fragment
 */
public class XmHomeBaseFragment extends XmBaseListFragment<DataMessageListDomain> {

    private TextView mTvInfo;

    private DataGroupDomain mDataType;
    private DataMessageListDomain mCurrentMessageList = new DataMessageListDomain();

    public static final String HOME_FRAGMENT_GROUP_KEY = "home_group_key";


    public static XmHomeBaseFragment newInstance(Bundle bundle) {
        XmHomeBaseFragment fragment = new XmHomeBaseFragment();
        if (bundle != null) {
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();

        mDataType = bundle.getParcelable(HOME_FRAGMENT_GROUP_KEY);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //load data

    }

    @Override
    protected void createDataListAdapter() {
        XmHomeDataAdapter adapter = new XmHomeDataAdapter(this.getActivity(),
                getDataList().getItemList());
        mBaseDataAdapter = adapter;
        getListView().setAdapter(mBaseDataAdapter);
    }

    @Override
    public DataMessageListDomain getDataList() {
        return mCurrentMessageList;
    }

    @Override
    protected void onItemClick(AdapterView parent, View view, int position, long id) {

    }

    @Override
    protected Loader<DataLoadResult<DataMessageListDomain>> onCreateNewDataLoader(int id, Bundle args) {
        // create data loader
        UserBingDomain userBingDomain = XmApplication.getInstance().getUserBingDomain();
        String accountId = userBingDomain.getUid();
        String token = userBingDomain.getAccess_token();
        String sinceId = null;
        if (getDataList().getItemList().size() > 0) {
            sinceId = getDataList().getItemList().get(0).getId();
        }

        XmHomeMessageLoader messageLoader = new XmHomeMessageLoader(this.getActivity(), accountId, token, mDataType.getId(), sinceId, null);
        return messageLoader;
    }

    protected Loader<DataLoadResult<DataMessageListDomain>> onCreateOldDataLoader(int id, Bundle args) {
        UserBingDomain userBingDomain = XmApplication.getInstance().getUserBingDomain();
        String accountId = userBingDomain.getUid();
        String token = userBingDomain.getAccess_token();
        String maxId = null;
        if (getDataList().getItemList().size() > 0) {
            maxId = getDataList().getItemList().get(getDataList().getItemList().size() - 1).getId();
        }
        return new XmHomeMessageLoader(getActivity(), accountId, token, mDataType.getId(), null, maxId);
    }

    @Override
    protected void onNewDataLoaderSuccessCallback(DataMessageListDomain newValue, Bundle loaderArgs) {
        // there data has success
        getDataList().addNewData(newValue);

    }

    @Override
    protected void onOldDataLoaderSuccessCallback(DataMessageListDomain oldValue) {
        getDataList().addOldData(oldValue);
    }

}
