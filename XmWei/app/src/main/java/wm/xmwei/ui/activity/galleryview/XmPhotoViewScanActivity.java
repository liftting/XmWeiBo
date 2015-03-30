package wm.xmwei.ui.activity.galleryview;


import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import wm.xmwei.R;
import wm.xmwei.XmApplication;
import wm.xmwei.bean.DataMessageDomain;
import wm.xmwei.core.lib.support.animation.ZoomOutPageTransformer;
import wm.xmwei.ui.fragment.photogallery.XmPhotoViewFragment;
import wm.xmwei.ui.view.jazzyviewpager.JazzyViewPager;
import wm.xmwei.ui.view.lib.XmPhotoViewData;

/**
 */
public class XmPhotoViewScanActivity extends FragmentActivity {

    private static final int STATUS_BAR_HEIGHT_DP_UNIT = 25;

    private ArrayList<XmPhotoViewData> rectList;
    private ArrayList<String> mLargeUrls = new ArrayList<String>();
    private List<String> mOriginUrls = new ArrayList<String>();

    private JazzyViewPager pager;
    private int initPosition;

    private HashMap<Integer, XmPhotoViewFragment> fragmentMap
            = new HashMap<Integer, XmPhotoViewFragment>();

    private boolean alreadyAnimateIn = false;

    public static Intent newIntent(DataMessageDomain msg, ArrayList<XmPhotoViewData> rectList,
                                   int initPosition) {
        Intent intent = new Intent(XmApplication.getInstance(), XmPhotoViewScanActivity.class);
        intent.putExtra("msg", msg);
        intent.putExtra("rect", rectList);
        intent.putExtra("position", initPosition);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layer_photoview_list);

        rectList = getIntent().getParcelableArrayListExtra("rect");
        DataMessageDomain msg = getIntent().getParcelableExtra("msg");

        mOriginUrls = msg.getThumbnailPicUrls();
        for (int i = 0; i < mOriginUrls.size(); i++) {
            mLargeUrls.add(mOriginUrls.get(i).replace("thumbnail", "large"));
        }

        pager = (JazzyViewPager) findViewById(R.id.pager);
        pager.setTransitionEffect(JazzyViewPager.TransitionEffect.ZoomIn);

        pager.setAdapter(new ImagePagerAdapter(getSupportFragmentManager()));
        pager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int scrollState) {
//                if (scrollState != ViewPager.SCROLL_STATE_IDLE && finalDisableHardwareLayerType) {
//                    final int childCount = pager.getChildCount();
//                    for (int i = 0; i < childCount; i++) {
//                        View child = pager.getChildAt(i);
//                        if (child.getLayerType() != View.LAYER_TYPE_NONE) {
//                            child.setLayerType(View.LAYER_TYPE_NONE, null);
//                        }
//                    }
//                }
            }
        });
        pager.setCurrentItem(getIntent().getIntExtra("position", 0));
        pager.setOffscreenPageLimit(1);
//        pager.setPageTransformer(true, new ZoomOutPageTransformer());


    }

    private class ImagePagerAdapter extends FragmentPagerAdapter {

        public ImagePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            XmPhotoViewFragment fragment = fragmentMap.get(position);
            if (fragment == null) {

                boolean animateIn = (initPosition == position) && !alreadyAnimateIn;
                fragment = XmPhotoViewFragment.newInstance(mOriginUrls.get(position), mLargeUrls.get(position),
                        rectList.get(position), animateIn, initPosition == position);
                alreadyAnimateIn = true;
                fragmentMap.put(position, fragment);
            }

            return fragment;
        }

        //when activity is recycled, ViewPager will reuse fragment by theirs name, so
        //getItem wont be called, but we need fragmentMap to animate close operation
        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            super.setPrimaryItem(container, position, object);
            if (object instanceof Fragment) {
                fragmentMap.put(position, (XmPhotoViewFragment) object);
            }
        }

        @Override
        public int getCount() {
            return mOriginUrls.size();
        }
    }


}
