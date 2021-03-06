package wm.xmwei.ui.fragment.base;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import wm.xmwei.R;
import wm.xmwei.bean.base.DataItemDomain;
import wm.xmwei.bean.base.DataListDomain;
import wm.xmwei.core.image.universalimageloader.XmImageLoader;
import wm.xmwei.core.lib.support.error.XmWeiboException;
import wm.xmwei.core.lib.support.view.XmBaseTipLayout;
import wm.xmwei.datadao.DataLoadResult;
import wm.xmwei.ui.view.pulltorefresh.PullToRefreshBase;
import wm.xmwei.ui.view.pulltorefresh.PullToRefreshListView;
import wm.xmwei.ui.view.pulltorefresh.extras.SoundPullEventListener;
import wm.xmwei.util.XmUtils;

/**
 *
 */
public abstract class XmBaseListFragment<T extends DataListDomain> extends XmBaseFragment {

    protected SwipeRefreshLayout mSwipeRefreshLayout;
    protected ListView mRefreshListView;
    private ProgressBar mProgressBar;
    protected View mFooterView;
    protected BaseAdapter mBaseDataAdapter;
    private XmBaseTipLayout mBaseTipLayout;

    private RelativeLayout mRlyTipContainer;

    private View mViewEmpty;

    //data load worker manager
    protected static final int DB_CACHE_LOADER_ID = 0; // db cache manager
    protected static final int NEW_MSG_LOADER_ID = 1; // go to load the new message manager
    @Deprecated
    protected static final int MIDDLE_MSG_LOADER_ID = 2;
    protected static final int OLD_MSG_LOADER_ID = 3;

    private static final int DATA_LOAD_NEXT_ITEM_NUM = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //判断如果之前的loader存在，要将其废弃掉
        Loader<T> loader = getLoaderManager().getLoader(NEW_MSG_LOADER_ID);
        if (loader != null) {
            getLoaderManager().initLoader(NEW_MSG_LOADER_ID, null, dataLoaderCallback);
        }
        loader = getLoaderManager().getLoader(MIDDLE_MSG_LOADER_ID);
        if (loader != null) {
            getLoaderManager().initLoader(MIDDLE_MSG_LOADER_ID, null, dataLoaderCallback);
        }
        loader = getLoaderManager().getLoader(OLD_MSG_LOADER_ID);
        if (loader != null) {
            getLoaderManager().initLoader(OLD_MSG_LOADER_ID, null, dataLoaderCallback);
        }

        mSwipeRefreshLayout.setOnRefreshListener(listViewOnRefreshListener);
        mRefreshListView.setOnItemClickListener(mItemClickListener);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View containerView = inflater.inflate(R.layout.layer_base_data_listview, container, false);


        mRlyTipContainer = (RelativeLayout) containerView.findViewById(R.id.rly_tip_message_list_container);
        mBaseTipLayout = new XmBaseTipLayout(getActivity());
        mRlyTipContainer.addView(mBaseTipLayout, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        buildLayout(inflater, containerView);
        return containerView;
    }

    protected void buildLayout(LayoutInflater inflater, View containerView) {
        mViewEmpty = containerView.findViewById(R.id.rly_empty);
        mProgressBar = (ProgressBar) containerView.findViewById(R.id.progressbar);
        mProgressBar.setVisibility(View.GONE);
        mSwipeRefreshLayout = (SwipeRefreshLayout) containerView.findViewById(R.id.listView);
        mRefreshListView = (ListView) containerView.findViewById(R.id.xm_origin_listview);

        getListView().setHeaderDividersEnabled(false);
        getListView().setScrollingCacheEnabled(false);

        mFooterView = inflater.inflate(R.layout.layer_data_load_footer, null);
        getListView().addFooterView(mFooterView);
        dismissFooterView();

        mViewEmpty.setVisibility(View.GONE);

    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        mRefreshListView.setOnScrollListener(onScrollListener);


        createDataListAdapter();

        if (mBaseDataAdapter != null) {
            getListView().setAdapter(mBaseDataAdapter);
        }


    }

    private AbsListView.OnScrollListener onScrollListener = new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

            switch (scrollState) {
                case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                    XmImageLoader.getInstance().pause();
                    break;

                case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                    XmImageLoader.getInstance().resume();
                    break;

                case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                    XmImageLoader.getInstance().pause();
                    break;
                default:
                    break;
            }

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            //
            if (firstVisibleItem > 0 && totalItemCount > 0 && firstVisibleItem + visibleItemCount >= totalItemCount - DATA_LOAD_NEXT_ITEM_NUM) {
                // show load next data
                loadNextData();
            }
        }
    };


    private PullToRefreshBase.OnLastItemVisibleListener listViewOnLastItemVisibleListener
            = new PullToRefreshBase.OnLastItemVisibleListener() {
        @Override
        public void onLastItemVisible() {

        }
    };

    private SwipeRefreshLayout.OnRefreshListener listViewOnRefreshListener
            = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            loadNewData();
        }
    };

    protected void dismissFooterView() {
        if (mFooterView.getVisibility() == View.VISIBLE) {
            mFooterView.setVisibility(View.GONE);
        }
    }

    protected void showFooterView() {
        if (mFooterView.getVisibility() == View.GONE) {
            mFooterView.setVisibility(View.VISIBLE);
        }
    }

    public SwipeRefreshLayout getSwipeRefreshLayout() {
        return mSwipeRefreshLayout;
    }

    public ListView getListView() {
        return mRefreshListView;
    }

    protected void refreshLayout(T bean) {
        if (bean != null && bean.getSize() > 0) {
            mProgressBar.setVisibility(View.INVISIBLE);
        } else if (bean == null || bean.getSize() == 0) {
            mProgressBar.setVisibility(View.INVISIBLE);
        } else if (bean.getSize() == bean.getTotal_number()) {
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    // data load  this is create the from 0 load data
    public void loadNewData() {

        if (getLoaderManager().getLoader(NEW_MSG_LOADER_ID) != null) {
            return;
        }

        getLoaderManager().destroyLoader(MIDDLE_MSG_LOADER_ID);
        getLoaderManager().destroyLoader(OLD_MSG_LOADER_ID);
        dismissFooterView();
        getLoaderManager().restartLoader(NEW_MSG_LOADER_ID, null, dataLoaderCallback);
    }

    private void loadNextData() {
        if (getLoaderManager().getLoader(OLD_MSG_LOADER_ID) != null) {
            return;
        }
        getLoaderManager().destroyLoader(NEW_MSG_LOADER_ID);
        mSwipeRefreshLayout.setRefreshing(false);
        getLoaderManager().destroyLoader(MIDDLE_MSG_LOADER_ID);
        getLoaderManager().restartLoader(OLD_MSG_LOADER_ID, null, dataLoaderCallback);
    }

    //数据load，result要封装一下，
    private LoaderManager.LoaderCallbacks<DataLoadResult<T>> dataLoaderCallback = new LoaderManager.LoaderCallbacks<DataLoadResult<T>>() {
        @Override
        public Loader<DataLoadResult<T>> onCreateLoader(int id, Bundle args) {
            Loader<DataLoadResult<T>> loader = null;
            switch (id) {
                case NEW_MSG_LOADER_ID:
                    loader = createNewDataLoader(id, args);
                    break;
                case OLD_MSG_LOADER_ID:
                    showFooterView(); // 只有可以创建加载时，才去现实footerView
                    loader = createOldDataLoader(id, args);
                    break;
            }
            return loader;
        }

        @Override
        public void onLoadFinished(Loader<DataLoadResult<T>> loader, DataLoadResult<T> data) {
            //这里做异常的列表处理
            T result = data != null ? data.data : null;

            if (data == null || result == null) {
                mBaseTipLayout.loadDataFail();
                loadCompleteStatus();
                getLoaderManager().destroyLoader(loader.getId());
                return;
            }

            XmWeiboException ect = data.exception;
            if (ect != null) {
                loadCompleteStatus();
                mBaseTipLayout.loadError(ect);
                getLoaderManager().destroyLoader(loader.getId());
                return;
            }

            int count = result.getItemList().size();
            mBaseTipLayout.loadDataSuccess(count);

            if (count > 0) {
                mViewEmpty.setVisibility(View.GONE);
            } else {
                mViewEmpty.setVisibility(View.VISIBLE);
            }

            switch (loader.getId()) {
                case NEW_MSG_LOADER_ID:
                    getSwipeRefreshLayout().setRefreshing(false);
                    refreshLayout(getDataList());
                    onNewDataLoaderSuccessCallback(result, null);

                    break;
                case OLD_MSG_LOADER_ID:
                    refreshLayout(getDataList());
                    if (data != null) {
                        onOldDataLoaderSuccessCallback(result);
                        dismissFooterView();
                    }
                    break;
            }

            getLoaderManager().destroyLoader(loader.getId());
            mBaseDataAdapter.notifyDataSetChanged();

        }

        @Override
        public void onLoaderReset(Loader<DataLoadResult<T>> loader) {

        }
    };

    private AdapterView.OnItemClickListener mItemClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            int headerViewsCount = getListView().getHeaderViewsCount();
            if (isPositionBetweenHeaderViewAndFooterView(position)) {
                int indexInDataSource = position - headerViewsCount;
                DataItemDomain dataItemDomain = getDataList().getItem(indexInDataSource);


                onListViewItemClick(parent, view, indexInDataSource, id);
            } else if (isLastItem(position)) {
                loadNextData();
            }
        }

        boolean isLastItem(int position) {
            //click the last footer view load more
            return position - 1 >= getDataList().getSize();
        }

        boolean isPositionBetweenHeaderViewAndFooterView(int position) {
            return position - getListView().getHeaderViewsCount() < getDataList().getSize()
                    && position - getListView().getHeaderViewsCount() >= 0;
        }

    };

    private void loadCompleteStatus() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    private Loader<DataLoadResult<T>> createNewDataLoader(int id, Bundle args) {
        return onCreateNewDataLoader(id, args);
    }

    private Loader<DataLoadResult<T>> createOldDataLoader(int id, Bundle args) {
        return onCreateOldDataLoader(id, args);
    }

    public BaseAdapter getAdapter() {
        return mBaseDataAdapter;
    }

    protected Loader<DataLoadResult<T>> onCreateNewDataLoader(int id, Bundle args) {
        return null;
    }

    protected Loader<DataLoadResult<T>> onCreateOldDataLoader(int id, Bundle args) {
        return null;
    }

    /**
     * create the list data adapter
     */
    protected abstract void createDataListAdapter();

    public abstract T getDataList();

    protected abstract void onListViewItemClick(AdapterView parent, View view, int position, long id);

    protected abstract void onNewDataLoaderSuccessCallback(T newValue, Bundle loaderArgs);

    protected abstract void onOldDataLoaderSuccessCallback(T oldValue);


}
