package wm.xmwei.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;

import wm.xmwei.R;


public class XmSplashActivity extends FragmentActivity implements View.OnClickListener {

    private ImageView mLastImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layer_splash_guide);
        mLastImg = (ImageView) findViewById(R.id.lastView3);
        mLastImg.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, XmMainActivity.class);
        startActivity(intent);
    }
}
