package wm.xmwei.ui.activity.base;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.ViewConfiguration;


import java.lang.reflect.Field;

import wm.xmwei.XmApplication;

public class XmBaseActivity extends FragmentActivity {

    protected int theme = 0; // 当前的主题样式

    @Override
    protected void onResume() {
        super.onResume();

        XmApplication.getInstance().setCurrentRunningAct(this);


    }

    @Override
    protected void onPause() {
        super.onPause();

        if (XmApplication.getInstance().getCurrentRunningAct() == this) {
            XmApplication.getInstance().setCurrentRunningAct(null);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("theme", theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        if (savedInstanceState == null) {
////            theme = SettingUtility.getAppTheme();
//        } else {
//            theme = savedInstanceState.getInt("theme");
//        }
//        setTheme(theme);
        super.onCreate(savedInstanceState);
        forceShowActionBarOverflowMenu(); //强制显示actionBar menu
        XmApplication.getInstance().setActivity(this);
    }

    private void forceShowActionBarOverflowMenu() {
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception ignored) {

        }
    }


}
