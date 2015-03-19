package wm.xmwei.ui.fragment.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import wm.xmwei.R;
import wm.xmwei.bean.DataListDomain;
import wm.xmwei.ui.view.pulltorefresh.PullToRefreshBase;
import wm.xmwei.ui.view.pulltorefresh.PullToRefreshListView;
import wm.xmwei.ui.view.pulltorefresh.extras.SoundPullEventListener;

/**
 *
 */
public abstract class XmBaseListFragment<T extends DataListDomain> extends XmBaseFragment {

    protected PullToRefreshListView mPullToRefreshListView;
    private ProgressBar mProgressBar;
    protected View mFooterView;
    protected BaseAdapter mBaseDataAdapter;

    private View mViewEmpty;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
        mPullToRefreshListView = (PullToRefreshListView) containerView.findViewById(R.id.listView);

        getListView().setHeaderDividersEnabled(false);
        getListView().setScrollingCacheEnabled(false);

        mFooterView = inflater.inflate(R.layout.layer_listview_footer, null);
        getListView().addFooterView(mFooterView);
        dismissFooterView();
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mPullToRefreshListView.setOnRefreshListener(listViewOnRefreshListener);
        mPullToRefreshListView.setOnLastItemVisibleListener(listViewOnLastItemVisibleListener);
        mPullToRefreshListView.setOnScrollListener(listViewOnScrollListener);
        mPullToRefreshListView.setOnItemClickListener(listViewOnItemClickListener);
        mPullToRefreshListView.setOnPullEventListener(getPullEventListener());
//
//        createDataListAdapter();

    }

    private PullToRefreshBase.OnLastItemVisibleListener listViewOnLastItemVisibleListener
            = new PullToRefreshBase.OnLastItemVisibleListener() {
        @Override
        public void onLastItemVisible() {

        }
    };

    private PullToRefreshBase.OnRefreshListener<ListView> listViewOnRefreshListener
            = new PullToRefreshBase.OnRefreshListener<ListView>() {
        @Override
        public void onRefresh(PullToRefreshBase<ListView> refreshView) {

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

    public PullToRefreshListView getPullToRefreshListView() {
        return mPullToRefreshListView;
    }

    public ListView getListView() {
        return mPullToRefreshListView.getRefreshableView();
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

    protected abstract void createDataListAdapter();

    public abstract T getDataList();

    protected abstract void onItemClick(AdapterView parent, View view, int position, long id);


}
