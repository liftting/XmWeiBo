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
import android.widget.ListView;
import android.widget.ProgressBar;

import wm.xmwei.R;
import wm.xmwei.bean.base.DataListDomain;
import wm.xmwei.datadao.DataLoadResult;
import wm.xmwei.ui.view.pulltorefresh.PullToRefreshBase;
import wm.xmwei.ui.view.pulltorefresh.PullToRefreshListView;
import wm.xmwei.ui.view.pulltorefresh.extras.SoundPullEventListener;
import wm.xmwei.util.XmUtils;

/**
 *
 */
public abstract class XmBaseListFragment<T extends DataListDomain> extends XmBaseContainerFragment {

    protected SwipeRefreshLayout mSwipeRefreshLayout;
    protected ListView mRefreshListView;
    private ProgressBar mProgressBar;
    protected View mFooterView;
    protected BaseAdapter mBaseDataAdapter;

    private View mViewEmpty;

    //data load worker manager
    protected static final int DB_CACHE_LOADER_ID = 0; // db cache manager
    protected static final int NEW_MSG_LOADER_ID = 1; // go to load the new message manager
    protected static final int MIDDLE_MSG_LOADER_ID = 2;
    protected static final int OLD_MSG_LOADER_ID = 3;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Loader<T> loader = getLoaderManager().getLoader(NEW_MSG_LOADER_ID);
        if (loader != null) {
            getLoaderManager().initLoader(NEW_MSG_LOADER_ID, null, dataLoaderCallback);
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View containerView = inflater.inflate(R.layout.layer_base_data_listview, container, false);
        buildLayout(inflater, containerView);
        return containerView;
    }

    protected void buildLayout(LayoutInflater inflater, View containerView) {
        mViewEmpty = containerView.findViewById(R.id.empty);
        mProgressBar = (ProgressBar) containerView.findViewById(R.id.progressbar);
        mProgressBar.setVisibility(View.GONE);
        mSwipeRefreshLayout = (SwipeRefreshLayout) containerView.findViewById(R.id.listView);
        mRefreshListView = (ListView) containerView.findViewById(R.id.xm_origin_listview);

        getListView().setHeaderDividersEnabled(false);
        getListView().setScrollingCacheEnabled(false);

        mFooterView = inflater.inflate(R.layout.layer_listview_footer, null);
        getListView().addFooterView(mFooterView);
        dismissFooterView();

    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mSwipeRefreshLayout.setOnRefreshListener(listViewOnRefreshListener);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        createDataListAdapter();

    }

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

    private SoundPullEventListener<ListView> getPullEventListener() {
        SoundPullEventListener<ListView> listener = new SoundPullEventListener<ListView>(
                getActivity());
        return listener;
    }

    private AdapterView.OnItemClickListener listViewOnItemClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        }

    };

    private AbsListView.OnScrollListener listViewOnScrollListener
            = new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                             int totalItemCount) {
        }
    };

    protected void dismissFooterView() {
        final View progressbar = mFooterView.findViewById(R.id.loading_progressbar);
        progressbar.animate().scaleX(0).scaleY(0).alpha(0.5f).setDuration(300)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        progressbar.setVisibility(View.GONE);
                    }
                });
        mFooterView.findViewById(R.id.laod_failed).setVisibility(View.GONE);
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

        getLoaderManager().destroyLoader(MIDDLE_MSG_LOADER_ID);
        getLoaderManager().destroyLoader(OLD_MSG_LOADER_ID);
        dismissFooterView();
        getLoaderManager().restartLoader(NEW_MSG_LOADER_ID, null, dataLoaderCallback);
    }

    //数据load，result要封装一下，
    private LoaderManager.LoaderCallbacks<DataLoadResult<T>> dataLoaderCallback = new LoaderManager.LoaderCallbacks<DataLoadResult<T>>() {
        @Override
        public Loader<DataLoadResult<T>> onCreateLoader(int id, Bundle args) {
            return createNewDataLoader(id, args);
        }

        @Override
        public void onLoadFinished(Loader<DataLoadResult<T>> loader, DataLoadResult<T> data) {

            //这里做异常的列表处理
            T result = data != null ? data.data : null;

            getSwipeRefreshLayout().setRefreshing(false);
            refreshLayout(getDataList());
            onNewDataLoaderSuccessCallback(result, null);

            getLoaderManager().destroyLoader(loader.getId());

            mBaseDataAdapter.notifyDataSetChanged();

        }

        @Override
        public void onLoaderReset(Loader<DataLoadResult<T>> loader) {

        }
    };

    private Loader<DataLoadResult<T>> createNewDataLoader(int id, Bundle args) {
        return onCreateNewDataLoader(id, args);
    }

    public BaseAdapter getAdapter() {
        return mBaseDataAdapter;
    }

    protected Loader<DataLoadResult<T>> onCreateNewDataLoader(int id, Bundle args) {
        return null;
    }

    protected abstract void createDataListAdapter();

    public abstract T getDataList();

    protected abstract void onItemClick(AdapterView parent, View view, int position, long id);

    protected abstract void onNewDataLoaderSuccessCallback(T newValue, Bundle loaderArgs);


}
