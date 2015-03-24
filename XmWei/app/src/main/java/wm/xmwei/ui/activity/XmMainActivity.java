package wm.xmwei.ui.activity;

import android.app.ActionBar;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import wm.xmwei.R;
import wm.xmwei.XmApplication;
import wm.xmwei.bean.UserBingDomain;
import wm.xmwei.core.data.Constants;
import wm.xmwei.ui.activity.base.XmBaseActivity;
import wm.xmwei.ui.fragment.XmCommentsFragment;
import wm.xmwei.ui.fragment.XmHomeFragment;
import wm.xmwei.ui.fragment.base.XmBaseFragment;
import wm.xmwei.ui.view.ldrawer.ActionBarDrawerToggle;
import wm.xmwei.ui.view.ldrawer.DrawerArrowDrawable;


public class XmMainActivity extends XmBaseActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerArrowDrawable drawerArrow;
    private boolean drawerArrowColor;

    private View mDrawerMenu;
    private FrameLayout mContentContainer;

    private XmBaseFragment mCurrentShowFragment;

    public static Intent newIntent() {
        return new Intent(XmApplication.getInstance(), XmMainActivity.class);
    }

    public static Intent newIntent(UserBingDomain accountBean) {
        Intent intent = newIntent();
        intent.putExtra(Constants.USER_BING_EXTRA, accountBean);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layer_main);
        ActionBar ab = getActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mContentContainer = (FrameLayout) findViewById(R.id.fly_content_container);

        mDrawerMenu = findViewById(R.id.navigation_drawer);


        drawerArrow = new DrawerArrowDrawable(this) {
            @Override
            public boolean isLayoutRtl() {
                return false;
            }
        };
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                drawerArrow, R.string.drawer_open,
                R.string.drawer_close) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        initFragments();

    }

    private void initFragments() {
        Fragment comments = getCommentsFragment();

        Fragment homeFrag = getHomeFragment();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (!comments.isAdded()) {
            fragmentTransaction
                    .add(R.id.fly_content_container, homeFrag, XmCommentsFragment.class.getName());
        }


        mCurrentShowFragment = (XmBaseFragment) homeFrag;

        if (!fragmentTransaction.isEmpty()) {
            fragmentTransaction.commit();
            getSupportFragmentManager().executePendingTransactions();
        }

    }

    public XmHomeFragment getHomeFragment() {
        XmHomeFragment fragment = ((XmHomeFragment) getSupportFragmentManager()
                .findFragmentByTag(
                        XmHomeFragment.class.getName()));
        if (fragment == null) {
            fragment = XmHomeFragment.newInstance(null);
        }
        return fragment;
    }

    public XmCommentsFragment getCommentsFragment() {
        XmCommentsFragment fragment = ((XmCommentsFragment) getSupportFragmentManager()
                .findFragmentByTag(
                        XmCommentsFragment.class.getName()));
        if (fragment == null) {
            fragment = XmCommentsFragment.newInstance(null);
        }
        return fragment;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (mDrawerLayout.isDrawerOpen(mDrawerMenu)) {
                mDrawerLayout.closeDrawer(mDrawerMenu);
            } else {
                mDrawerLayout.openDrawer(mDrawerMenu);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {

        boolean isHandle = mCurrentShowFragment.handleBackPressed();
        if (!isHandle) {
            super.onBackPressed();
        }

    }

}
