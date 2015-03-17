package wm.xmwei.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import wm.xmwei.R;
import wm.xmwei.XmApplication;
import wm.xmwei.ui.fragment.base.XmBaseFragment;
import wm.xmwei.ui.view.dynagridview.BaseDynamicGridAdapter;
import wm.xmwei.ui.view.dynagridview.DynamicGridView;
import wm.xmwei.ui.view.indicator.PageSlidingIndicator;
import wm.xmwei.ui.view.lib.DragGridView;

/**
 * this is home fragment
 */
public class XmHomeFragment extends XmBaseFragment implements View.OnClickListener {

    private Context mContext;
    private ViewPager mHomeViewPager;
    private PageSlidingIndicator mHomePageIndicator;


    private TextView mTvConvertItem;
    private RelativeLayout mRlyDragContainer;
    private DynamicGridView mDragGridView;
    private GridItemAdapter mGridItemAdapter;
    private List<String> mItemDatas = new ArrayList<String>();

    private SparseArray<Fragment> mHomeChildFragments = new SparseArray<Fragment>();
    private boolean isBuildCategory = false;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.layer_frag_home, null);

        initView(mainView);

        return mainView;
    }

    private void initView(View mainView) {
        mRlyDragContainer = (RelativeLayout) mainView.findViewById(R.id.rly_gridview_container);
        mDragGridView = (DynamicGridView) mainView.findViewById(R.id.v_drag_gridview);
        mTvConvertItem = (TextView) mainView.findViewById(R.id.tv_home_conver_griditem);

        mTvConvertItem.setOnClickListener(this);
    }

    // 分类数据构建
    private void buildCategoryData() {

        mItemDatas.add("a");
        mItemDatas.add("b");
        mItemDatas.add("c");
        mItemDatas.add("d");
        mItemDatas.add("e");
        mItemDatas.add("f");
        mItemDatas.add("g");
        mItemDatas.add("h");


        mGridItemAdapter = new GridItemAdapter(mContext, mItemDatas, 3);

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
                String item = (String) parent.getAdapter().getItem(position);
                mItemDatas.remove(item);
                mGridItemAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onClick(View v) {
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
            holder.build(getItem(position).toString());
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
}
