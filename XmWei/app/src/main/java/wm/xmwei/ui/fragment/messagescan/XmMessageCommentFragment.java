package wm.xmwei.ui.fragment.messagescan;

import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import wm.xmwei.XmApplication;
import wm.xmwei.bean.DataCommentListDomain;
import wm.xmwei.bean.DataMessageDomain;
import wm.xmwei.bean.base.DataListDomain;
import wm.xmwei.datadao.DataLoadResult;
import wm.xmwei.ui.adapter.data.XmHomeDataAdapter;
import wm.xmwei.ui.adapter.data.XmMessageScanCommentDataAdapter;
import wm.xmwei.ui.dataloader.XmMessageScanCommentLoader;
import wm.xmwei.ui.fragment.base.XmBaseListFragment;

/**
 * Created by wm on 15-4-15.
 */
public class XmMessageCommentFragment extends XmBaseListFragment<DataCommentListDomain> {

    private DataCommentListDomain mCommentsToMeDataList = new DataCommentListDomain();
    private DataMessageDomain mDataMessageDomain;

    public static XmMessageCommentFragment newInstance(DataMessageDomain dataMessageDomain) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("message_domain", dataMessageDomain);
        XmMessageCommentFragment fragment = new XmMessageCommentFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        mDataMessageDomain = bundle.getParcelable("message_domain");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View containerView = super.onCreateView(inflater, container, savedInstanceState);


//        if (mCommentsToMeDataList.getItemList().size() <= 0) {
//            getSwipeRefreshLayout().setRefreshing(true);
//        }


        return containerView;

    }

    @Override
    protected void createDataListAdapter() {

        XmMessageScanCommentDataAdapter adapter = new XmMessageScanCommentDataAdapter(getActivity(), mCommentsToMeDataList.getItemList());
        mBaseDataAdapter = adapter;

    }

    @Override
    public DataCommentListDomain getDataList() {
        return mCommentsToMeDataList;
    }

    @Override
    protected void onListViewItemClick(AdapterView parent, View view, int position, long id) {

    }

    @Override
    protected Loader<DataLoadResult<DataCommentListDomain>> onCreateNewDataLoader(int id, Bundle args) {
        return new XmMessageScanCommentLoader(getActivity(), mDataMessageDomain.getId(), XmApplication.getInstance().getUserBingDomain().getAccess_token(), null, null);
    }

    @Override
    protected Loader<DataLoadResult<DataCommentListDomain>> onCreateOldDataLoader(int id, Bundle args) {
        String maxId = null;
        if (mCommentsToMeDataList.getItemList().size() > 0) {
            maxId = mCommentsToMeDataList.getItemList().get(mCommentsToMeDataList.getItemList().size() - 1)
                    .getId();
        }
        return new XmMessageScanCommentLoader(getActivity(), mDataMessageDomain.getId(), XmApplication.getInstance().getUserBingDomain().getAccess_token(), null,
                maxId);
    }


    @Override
    protected void onNewDataLoaderSuccessCallback(DataCommentListDomain newValue, Bundle loaderArgs) {
        getDataList().addNewData(newValue);
    }

    @Override
    protected void onOldDataLoaderSuccessCallback(DataCommentListDomain oldValue) {
        getDataList().addOldData(oldValue);
    }
}
