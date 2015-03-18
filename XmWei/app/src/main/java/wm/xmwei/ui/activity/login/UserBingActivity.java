package wm.xmwei.ui.activity.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

import wm.xmwei.R;
import wm.xmwei.ui.activity.BaseActivity;

/**
 */
public class UserBingActivity extends BaseActivity implements View.OnClickListener {

    private Button mBtnUserBing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layer_user_bing_main);

        mBtnUserBing = (Button) findViewById(R.id.btn_bing_user);
        mBtnUserBing.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent(UserBingActivity.this, OAuthActivity.class);
        startActivity(intent);
    }


}
