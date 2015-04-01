package wm.xmwei.ui.activity;

import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import wm.xmwei.R;
import wm.xmwei.XmApplication;
import wm.xmwei.bean.DataMessageDomain;
import wm.xmwei.core.lib.support.view.StickyScrollView;
import wm.xmwei.ui.activity.base.XmBaseActivity;
import wm.xmwei.util.XmUtils;

/**
 * this is handle the operation
 */
public class XmMessageScanActivity extends XmBaseActivity implements StickyScrollView.ScrollViewListener {

    private ViewPager mPager;
    private StickyScrollView mScrollView;

    private ListView mDataListView;
    private List<String> datas = new ArrayList<String>();
    private List<Fragment> mDataList = new ArrayList<Fragment>();

    private View headView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layer_sticky_test);

        mScrollView = (StickyScrollView) findViewById(R.id.v_message_scan_scrollview);
        mScrollView.setScrollViewListener(this);
        mPager = (ViewPager) findViewById(R.id.v_message_scan_pager);
        headView = findViewById(R.id.v_head_view);

//        for (int i = 1; i < 50; i++) {
//            datas.add(i + "");
//        }

        mDataList.add(new TestFragment());
        mDataList.add(new TestFragment());
        mPager.setAdapter(new TestFragmentAdapter(getSupportFragmentManager()));
        registerPagerScroll();

        mScrollView.setScrollViewListener(this);

    }

    public static Intent newIntent() {
        Intent intent = new Intent();
        intent.setClass(XmApplication.getInstance(), XmMessageScanActivity.class);
        return intent;
    }

    private void registerPagerScroll() {

//        mPager.setOnTouchListener(new View.OnTouchListener() {
//
//            int dragthreshold = 30;
//            int downX;
//            int downY;
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        downX = (int) event.getRawX();
//                        downY = (int) event.getRawY();
//                        break;
//                    case MotionEvent.ACTION_MOVE:
//                        int distanceX = Math.abs((int) event.getRawX() - downX);
//                        int distanceY = Math.abs((int) event.getRawY() - downY);
//
//                        if (distanceY > distanceX && distanceY > dragthreshold) {
//                            mPager.getParent().requestDisallowInterceptTouchEvent(false);
//                            mScrollView.getParent().requestDisallowInterceptTouchEvent(true);
//                        } else if (distanceX > distanceY && distanceX > dragthreshold) {
//                            mPager.getParent().requestDisallowInterceptTouchEvent(true);
//                            mScrollView.getParent().requestDisallowInterceptTouchEvent(false);
//                        }
//                        break;
//                    case MotionEvent.ACTION_UP:
//                        mScrollView.getParent().requestDisallowInterceptTouchEvent(false);
//                        mPager.getParent().requestDisallowInterceptTouchEvent(false);
//                        break;
//                }
//                return false;
//            }
//        });

    }

    @Override
    public void onScrollChanged(StickyScrollView scrollView, int x, int y, int oldx, int oldy) {
        int height = headView.getMeasuredHeight();
        mScrollView.scrollTo(0,mScrollView.getScrollY());
        if (mScrollView.getScrollY() >= height) {
            mPager.getParent().requestDisallowInterceptTouchEvent(true);
        }
    }


    public class ItemAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public Object getItem(int position) {
            return datas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return -1;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View content = LayoutInflater.from(XmMessageScanActivity.this).inflate(R.layout.layer_frag_home_item, null);
            return content;
        }
    }


    public class TestFragmentAdapter extends FragmentPagerAdapter {

        public TestFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return mDataList.get(i);
        }

        @Override
        public int getCount() {
            return mDataList.size();
        }
    }

    public static class TestFragment extends Fragment {
        public TestFragment() {
            super();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View content = inflater.inflate(R.layout.layer_frag_home_item, container, false);
            return content;
        }


    }

}
