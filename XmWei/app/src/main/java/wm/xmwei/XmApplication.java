package wm.xmwei;

import android.app.Activity;
import android.app.Application;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Display;

import java.util.List;

import wm.xmwei.bean.UserBingDomain;
import wm.xmwei.datadao.dbway.login.DbUserBingTask;

public class XmApplication extends Application {

    private static XmApplication mGlobalContext;

    private Activity mCurrentRunningAct;
    private Activity mActivity;
    private DisplayMetrics displayMetrics = null;
    private Handler handler = new Handler();


    //一些用户数据
    private UserBingDomain mUserBingDomain;

    @Override
    public void onCreate() {
        super.onCreate();
        mGlobalContext = this;
    }

    public static XmApplication getInstance() {
        return mGlobalContext;
    }

    public void setCurrentRunningAct(Activity activity) {
        this.mCurrentRunningAct = activity;
    }

    public Activity getCurrentRunningAct() {
        return mCurrentRunningAct;
    }


    public DisplayMetrics getDisplayMetrics() {
        if (displayMetrics != null) {
            return displayMetrics;
        } else {
            Activity a = getActivity();
            if (a != null) {
                Display display = getActivity().getWindowManager().getDefaultDisplay();
                DisplayMetrics metrics = new DisplayMetrics();
                display.getMetrics(metrics);
                this.displayMetrics = metrics;
                return metrics;
            } else {
                //default screen is 800x480
                DisplayMetrics metrics = new DisplayMetrics();
                metrics.widthPixels = 480;
                metrics.heightPixels = 800;
                return metrics;
            }
        }
    }

    public Activity getActivity() {
        return mActivity;
    }

    public void setActivity(Activity activity) {
        this.mActivity = activity;
    }

    public UserBingDomain getUserBingDomain() {
        if (mUserBingDomain == null) {
            // load bing data from db
            List<UserBingDomain> userBingList = DbUserBingTask.getUserBingList();
            if (userBingList != null && userBingList.size() > 0) {
                mUserBingDomain = userBingList.get(0);
            }
        }
        return mUserBingDomain;
    }

}
