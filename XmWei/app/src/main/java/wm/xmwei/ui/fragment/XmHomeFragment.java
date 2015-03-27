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
import java.util.List;

import wm.xmwei.R;
import wm.xmwei.bean.DataGroupDomain;
import wm.xmwei.bean.DataGroupListDomain;
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


    private ImageView mTvConvertItem;
    private RelativeLayout mRlyDragContainer;
    private DynamicGridView mDragGridView;
    private GridItemAdapter mGridItemAdapter;

    private List<Fragment> mHomeChildFragments = new ArrayList<Fragment>();
    private List<String> mTagFragments = new ArrayList<String>();
    private boolean isBuildCategory = false;

    private List<DataGroupDomain> mUserGroupDomain = new ArrayList<DataGroupDomain>();


    private String currentGroupId = Constants.ALL_GROUP_ID;

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
        mTvConvertItem = (ImageView) mainView.findViewById(R.id.tv_home_conver_griditem);

        mDataLoadProgress = (ProgressBar) mainView.findViewById(R.id.pro_content_show);


        mTvConvertItem.setOnClickListener(this);
    }

    private void buildHomeFragments() {

        for (DataGroupDomain groupDomain : mUserGroupDomain) {
            mHomeChildFragments.add(createFragment(groupDomain));
            mTagFragments.add(createTag(groupDomain));
        }


        mHomeViewPager.setAdapter(new XmHomeFragmentAdapter(getChildFragmentManager(), mHomeChildFragments, mTagFragments, buildTitleData()));
        mHomePageIndicator.setViewPager(mHomeViewPager);

    }

    private String[] buildTitleData() {

        String[] titleArray = new String[mUserGroupDomain.size()];

        for (int i = 0; i < mUserGroupDomain.size(); i++) {
            DataGroupDomain domain = mUserGroupDomain.get(i);
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
            }
        });
    }

    @Override
    public void onClick(View v) {
//        try {
//            PackageInfo packageInfo = mContext.getPackageManager()
//                    .getPackageInfo("wm.xmwei", PackageManager.GET_SIGNATURES);
//
//            for (Signature signature : packageInfo.signatures) {
//                System.out.print(signature.toCharsString());
//            }
//
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
        if (!isBuildCategory) {
            buildCategoryData();
            isBuildCategory = true;
        }
        if (mRlyDragContainer.getVisibility() == View.GONE) {
            showDragContainerView();
        } else {
            hideDragContainerView();
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


    public class GridItemAdapter extends BaseDynamicGridAdapter {
        public GridItemAdapter(Context context, List<?> items, int columnCount) {
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
