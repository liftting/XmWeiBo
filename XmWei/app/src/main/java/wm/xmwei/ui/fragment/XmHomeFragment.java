package wm.xmwei.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wm.xmwei.R;
import wm.xmwei.bean.DataGroupDomain;
import wm.xmwei.bean.DataGroupListDomain;
import wm.xmwei.bean.DataMessageDomain;
import wm.xmwei.core.data.Constants;
import wm.xmwei.core.net.http.OnDataLoadTaskListener;
import wm.xmwei.ui.adapter.XmHomeFragmentAdapter;
import wm.xmwei.ui.fragment.base.XmBaseFragment;
import wm.xmwei.ui.view.dynagridview.BaseDynamicGridAdapter;
import wm.xmwei.ui.view.dynagridview.DynamicGridView;
import wm.xmwei.ui.view.indicator.PageSlidingIndicator;
import wm.xmwei.ui.view.indicator.XmTwoPageSlidingIndicator;

/**
 * this is home fragment
 */
public class XmHomeFragment extends XmBaseFragment implements View.OnClickListener, OnDataLoadTaskListener<DataGroupListDomain> {

    private Context mContext;
    private ViewPager mHomeViewPager;
    private PageSlidingIndicator mHomePageIndicator;
    private ProgressBar mDataLoadProgress;


    private ImageView mTvConvertItemDown;
    private ImageView mTvConvertItemUp;
    private RelativeLayout mRlyDragContainer;
    private DynamicGridView mDragGridView;
    private GridItemAdapter mGridItemAdapter;
    private TextView mTvDragFinish;


    private List<Fragment> mHomeChildFragments = new ArrayList<Fragment>();
    private List<String> mTagFragments = new ArrayList<String>();
    private String[] mTitleArray;
    private XmHomeFragmentAdapter mHomeFragmentAdapter;
    private boolean isBuildCategory = false;

    private Map<String, Fragment> mChildFragMap = new HashMap<String, Fragment>();
    private Map<String, String> mGroupTagMap = new HashMap<String, String>();

    private List<DataGroupDomain> mUserGroupDomain = new ArrayList<DataGroupDomain>();

    private int mCurrentPagePosition = 0;

    private String currentPageGroupId = Constants.ALL_GROUP_ID;

    public static XmHomeFragment newInstance(Bundle bundle) {

        XmHomeFragment fragment = new XmHomeFragment();

        if (bundle != null) {
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mContext = getActivity();
        // load the group data
        createGroupAdapter();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.layer_frag_home, null);

        initView(mainView);

        return mainView;
    }

    private void initView(View mainView) {

        mHomeViewPager = (ViewPager) mainView.findViewById(R.id.v_home_pager);
        mHomePageIndicator = (PageSlidingIndicator) mainView.findViewById(R.id.v_home_indicator);

        mRlyDragContainer = (RelativeLayout) mainView.findViewById(R.id.rly_gridview_container);
        mDragGridView = (DynamicGridView) mainView.findViewById(R.id.v_drag_gridview);
        mTvConvertItemDown = (ImageView) mainView.findViewById(R.id.tv_home_conver_griditem_down);
        mTvConvertItemUp = (ImageView) mainView.findViewById(R.id.tv_home_conver_griditem_up);
        mTvDragFinish = (TextView) mainView.findViewById(R.id.tv_home_conver_griditem_finish);

        mDataLoadProgress = (ProgressBar) mainView.findViewById(R.id.pro_content_show);


        mTvConvertItemDown.setOnClickListener(this);
        mTvConvertItemUp.setOnClickListener(this);
        mTvDragFinish.setOnClickListener(this);
    }

    private void buildHomeFragments() {

        for (DataGroupDomain groupDomain : mUserGroupDomain) {
            Fragment currentFrag = createFragment(groupDomain);
            mHomeChildFragments.add(currentFrag);
            mChildFragMap.put(groupDomain.getId(), currentFrag);


            String tag = createTag(groupDomain);
            mTagFragments.add(tag);
            mGroupTagMap.put(groupDomain.getId(), tag);
        }


        initDataAdapter();
        mHomePageIndicator.setViewPager(mHomeViewPager);

        mHomePageIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {

            }

            @Override
            public void onPageSelected(int i) {
                mCurrentPagePosition = i;
                currentPageGroupId = mUserGroupDomain.get(i).getId();
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

    }

    private void initDataAdapter() {
        // update the three data sort
        mHomeFragmentAdapter = new XmHomeFragmentAdapter(getChildFragmentManager(), mHomeChildFragments, mTagFragments, buildTitleData());
        mHomeViewPager.setAdapter(mHomeFragmentAdapter);
    }

    private String[] buildTitleData() {
        List<DataGroupDomain> dataGroupDomainList = null;
        if (mGridItemAdapter == null || mGridItemAdapter.getItems() == null) {
            dataGroupDomainList = mUserGroupDomain;
        } else {
            dataGroupDomainList = mGridItemAdapter.getItems();
        }
        String[] titleArray = new String[dataGroupDomainList.size()];

        for (int i = 0; i < dataGroupDomainList.size(); i++) {
            DataGroupDomain domain = dataGroupDomainList.get(i);
            titleArray[i] = domain.getName();
        }
        return titleArray;
    }

    private String createTag(DataGroupDomain groupDomain) {
        return XmHomeBaseFragment.class.getName() + "_" + groupDomain.getId();
    }

    private XmHomeBaseFragment createFragment(DataGroupDomain groupDomain) {
        Bundle oneBundle = new Bundle();
        oneBundle.putParcelable(XmHomeBaseFragment.HOME_FRAGMENT_GROUP_KEY, groupDomain);
        return XmHomeBaseFragment.newInstance(oneBundle);
    }

    // 分类数据构建
    private void buildCategoryData() {


        mGridItemAdapter = new GridItemAdapter(mContext, mUserGroupDomain, 3);

        mDragGridView.setAdapter(mGridItemAdapter);
        mDragGridView.setOnDragListener(new DynamicGridView.OnDragListener() {
            @Override
            public void onDragStarted(int position) {
                setDragTopStatus(true);
            }

            @Override
            public void onDragPositionsChanged(int oldPosition, int newPosition) {

            }
        });
        mDragGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                mDragGridView.startEditMode(position);
                return true;
            }
        });
        mDragGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String item = (String) parent.getAdapter().getItem(position);
//                mUserGroupDomain.remove(item);
//                mGridItemAdapter.notifyDataSetChanged();

                // enter this tab viewpager


            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_home_conver_griditem_down:

                if (!isBuildCategory) {
                    buildCategoryData();
                    isBuildCategory = true;
                }

                showDragContainerView();

                break;
            case R.id.tv_home_conver_griditem_up:

                hideDragContainerView();

                handleDataSort();

                break;

            case R.id.tv_home_conver_griditem_finish:
                // 结束
                setDragTopStatus(false);
                if (mDragGridView.isEditMode()) {
                    mDragGridView.stopEditMode();
                }

                break;
        }
    }

    //当用户调整了数据顺序时， 刷新viewpager
    private void handleDataSort() {

        refreshFragment();
        refreshFragmentTag();

        initDataAdapter();

        List<DataGroupDomain> newDatas = mGridItemAdapter.getItems();
        int updatePosition = mCurrentPagePosition;
        for (int i = 0; i < newDatas.size(); i++) {
            if (newDatas.get(i).getId().equals(currentPageGroupId)) {
                updatePosition = i;
                break;
            }
        }

        mHomeViewPager.setCurrentItem(updatePosition);
        mHomePageIndicator.notifyDataSetChanged();

    }

    private void refreshFragment() {
        mHomeChildFragments = new ArrayList<Fragment>();
        for (DataGroupDomain groupDomain : mGridItemAdapter.getItems()) {
            mHomeChildFragments.add(mChildFragMap.get(groupDomain.getId()));
        }
    }

    private void refreshFragmentTag() {
        mTagFragments = new ArrayList<String>();
        for (DataGroupDomain groupDomain : mGridItemAdapter.getItems()) {
            mTagFragments.add(mGroupTagMap.get(groupDomain.getId()));
        }
    }

    private void setDragTopStatus(boolean isDragBegin) {
        if (isDragBegin) {
            mTvDragFinish.setVisibility(View.VISIBLE);
            mTvConvertItemUp.setVisibility(View.GONE);
        } else {
            mTvDragFinish.setVisibility(View.GONE);
            mTvConvertItemUp.setVisibility(View.VISIBLE);
        }
    }

    private void showDragContainerView() {
        ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 1.0f, 0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0f);
        scaleAnimation.setDuration(500);
        mRlyDragContainer.setAnimation(scaleAnimation);
        scaleAnimation.start();
        mRlyDragContainer.setVisibility(View.VISIBLE);
    }

    private void hideDragContainerView() {
        ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 1.0f, 1.0f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0f);
        scaleAnimation.setDuration(500);
        mRlyDragContainer.setAnimation(scaleAnimation);
        scaleAnimation.start();

        mRlyDragContainer.setVisibility(View.GONE);
    }


    public class GridItemAdapter extends BaseDynamicGridAdapter<DataGroupDomain> {

        public GridItemAdapter(Context context, List<DataGroupDomain> items, int columnCount) {
            super(context, items, columnCount);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            CheeseViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.layer_drag_gridview_item, null);
                holder = new CheeseViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (CheeseViewHolder) convertView.getTag();
            }
            DataGroupDomain domain = (DataGroupDomain) getItem(position);
            holder.build(domain.getName());
            return convertView;
        }

        private class CheeseViewHolder {
            private TextView titleText;

            private CheeseViewHolder(View view) {
                titleText = (TextView) view.findViewById(R.id.textView1);
            }

            void build(String title) {
                titleText.setText(title);
            }
        }

    }

    @Override
    public boolean handleBackPressed() {
        if (mDragGridView.isEditMode()) {
            mDragGridView.stopEditMode();
            return true;
        }
        return false;
    }

    //构建分组adapter
    public void createGroupAdapter() {

//        UserBingDomain bingUser = XmApplication.getInstance().getUserBingDomain();
//        XmUserGroupTask task = new XmUserGroupTask(bingUser.getAccess_token(), bingUser.getUid());
//        task.setDataLoadTaskListener(this);
//        task.executeOnExecutor(XmAsyncTask.THREAD_POOL_EXECUTOR);

        createTestDomain();
        groupDataComplete();
        buildHomeFragments();

    }

    @Override
    public void dataLoadComplete(DataGroupListDomain data) {
        if (data != null) {
            groupDataComplete();
            mUserGroupDomain = data.getLists();
            buildHomeFragments();
        }
    }

    private void groupDataComplete() {
        mDataLoadProgress.setVisibility(View.GONE);
    }

    private void createTestDomain() {
        mUserGroupDomain.add(createGroupDomain(Constants.ALL_GROUP_ID, "0", "所有人"));
        mUserGroupDomain.add(createGroupDomain(Constants.BILATERAL_GROUP_ID, "1", "互相的"));

    }

    private DataGroupDomain createGroupDomain(String id, String idStr, String name) {
        DataGroupDomain groupDomain = new DataGroupDomain();
        groupDomain.setId(id);
        groupDomain.setIdstr(idStr);
        groupDomain.setName(name);
        return groupDomain;
    }

}
