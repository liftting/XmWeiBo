package wm.xmwei.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import wm.xmwei.XmApplication;
import wm.xmwei.bean.DataGroupDomain;
import wm.xmwei.bean.DataMessageListDomain;
import wm.xmwei.bean.UserBingDomain;
import wm.xmwei.core.lib.support.XmAsyncTask;
import wm.xmwei.core.selftest.XmStickMessageActivity;
import wm.xmwei.datadao.DataLoadResult;
import wm.xmwei.datadao.dbway.home.DbHomeDefaultMessageTask;
import wm.xmwei.ui.activity.messagescan.XmMessageScanActivity;
import wm.xmwei.ui.activity.messagescan.XmMessageScanActivity2;
import wm.xmwei.ui.activity.messagescan.XmMessageStickScanActivity;
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
    private String mCurrentGroupId;

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
        mCurrentGroupId = mDataType.getId();

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        //load data
        int currentState = getCurrentState(savedInstanceState);
        switch (currentState) {
            case FIRST_TIME_START:
            case ACTIVITY_DESTROY_AND_CREATE:
                if (getDataList().getSize() == 0) {
                    // load data from db
                    DbLoadDataAsyncTask dbTask = new DbLoadDataAsyncTask(XmApplication.getInstance().getUserBingDomain().getUid());
                    dbTask.executeOnIO();
                } else {
                    getAdapter().notifyDataSetChanged();
                    refreshLayout(getDataList());
                }
                break;
        }

        super.onActivityCreated(savedInstanceState);
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
    protected void onListViewItemClick(AdapterView parent, View view, int position, long id) {
        Intent intent = XmMessageStickScanActivity.newIntent(getDataList().getItem(position),
                XmApplication.getInstance().getUserBingDomain().getAccess_token());
        startActivity(intent);
    }

    @Override
    public void onPause() {
        super.onPause();    //To change body of overridden methods use File | Settings | File Templates.

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

        XmHomeMessageLoader messageLoader = new XmHomeMessageLoader(this.getActivity(), accountId, token, mCurrentGroupId, sinceId, null);
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
        return new XmHomeMessageLoader(getActivity(), accountId, token, mCurrentGroupId, null, maxId);
    }

    @Override
    protected void onNewDataLoaderSuccessCallback(DataMessageListDomain newValue, Bundle loaderArgs) {
        // there data has success
        getDataList().addNewData(newValue);

        // put into db cache
        updateDbData();

    }

    @Override
    protected void onOldDataLoaderSuccessCallback(DataMessageListDomain oldValue) {
        getDataList().addOldData(oldValue);
        updateDbData();

    }

    private void updateDbData() {
        UserBingDomain userBingDomain = XmApplication.getInstance().getUserBingDomain();
        DbHomeDefaultMessageTask.asyncReplace(getDataList(), userBingDomain.getUid(), mCurrentGroupId);
    }

    private void handleDBCacheOnProgressUpdateData(List<DataMessageListDomain> result) {
        if (result != null && result.size() > 0) {
            DataMessageListDomain recentData = result.get(0);
            getDataList().replaceData(recentData);
//            putToGroupDataMemoryCache(recentData.groupId, recentData.msgList);
//            positionCache.put(recentData.groupId, recentData.position);
//            mCurrentGroupId = Constants.ALL_GROUP_ID;
        }
        getAdapter().notifyDataSetChanged();
        refreshLayout(getDataList());
        /**
         * when this account first open app,if he don't have any data in database,fetch data from server automally
         */
        if (getDataList().getSize() == 0) {
            getSwipeRefreshLayout().setRefreshing(true);
            super.loadNewData();
        }
    }

    private class DbLoadDataAsyncTask extends XmAsyncTask<Void, Void, List<DataMessageListDomain>> {

        private String mBingUserId;

        private DbLoadDataAsyncTask(String accountId) {
            this.mBingUserId = accountId;
        }

        @Override
        protected List<DataMessageListDomain> doInBackground(Void... params) {
            List<DataMessageListDomain> recentList = new ArrayList<DataMessageListDomain>();
            recentList.add(DbHomeDefaultMessageTask.getCurrentGroupData(mBingUserId, mCurrentGroupId));
            return recentList;
        }

        @Override
        protected void onPostExecute(List<DataMessageListDomain> dataMessageListDomains) {
            // cache data has loaded
            handleDBCacheOnProgressUpdateData(dataMessageListDomains);
        }
    }


}
