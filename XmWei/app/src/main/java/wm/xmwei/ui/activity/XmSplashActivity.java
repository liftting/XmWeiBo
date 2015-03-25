package wm.xmwei.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;

import wm.xmwei.R;
import wm.xmwei.core.lib.support.view.swipebacklayout.SwipeBackLayout;
import wm.xmwei.core.lib.support.view.swipebacklayout.app.SwipeBackActivity;
import wm.xmwei.ui.activity.login.XmUserBingActivity;


public class XmSplashActivity extends SwipeBackActivity implements View.OnClickListener {

    private ImageView mLastImg;

    private SwipeBackLayout mSwipeBackLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layer_splash_guide);
        mLastImg = (ImageView) findViewById(R.id.lastView3);
        mLastImg.setOnClickListener(this);

        mSwipeBackLayout = getSwipeBackLayout();
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, XmUserBingActivity.class);
        startActivity(intent);
    }
}
