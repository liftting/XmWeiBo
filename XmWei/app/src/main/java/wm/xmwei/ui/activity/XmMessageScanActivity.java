package wm.xmwei.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import wm.xmwei.R;
import wm.xmwei.XmApplication;
import wm.xmwei.bean.DataMessageDomain;
import wm.xmwei.ui.activity.base.XmBaseActivity;

/**
 * this is handle the operation
 */
public class XmMessageScanActivity extends XmBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layer_sticky_test);
    }

    public static Intent newIntent() {
        Intent intent = new Intent();
        intent.setClass(XmApplication.getInstance(), XmMessageScanActivity.class);
        XmMessageScanActivity.TestFragment fragment =null;
        return intent;
    }

    private static class TestFragment extends Fragment {
        public TestFragment() {
            super();
        }
    }

}
