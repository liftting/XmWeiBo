package wm.xmwei.core.lib.support.sinasso;

import android.app.Activity;
import android.content.*;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;

import com.sina.sso.RemoteSSO;

import wm.xmwei.util.URLHelper;


/**
 *
 *
 */
public class SsoHandler {
    private ServiceConnection conn = null;
    private static final int DEFAULT_AUTH_ACTIVITY_CODE = 32973;
    //签名的信息
    private static final String WEIBO_SIGNATURE = "308202333082019ca00302010202045508ebe0300d06092a864886f70d01010" +
            "50500305d310b30090603550406130243483110300e060355040813076265696a696e673110300e060355040713076" +
            "265696a696e67310b3009060355040a1302786d310e300c060355040b1305756e697479310d300b060355040313047" +
            "7616e673020170d3135303331383033303731325a180f32303639313231393033303731325a305d310b30090603550" +
            "406130243483110300e060355040813076265696a696e673110300e060355040713076265696a696e67310b3009060" +
            "355040a1302786d310e300c060355040b1305756e697479310d300b0603550403130477616e6730819f300d06092a8" +
            "64886f70d010101050003818d0030818902818100e0313d1723a1c5dba933713dc3aadc65c366c6a78294cfb6b8d5f" +
            "2a4c64963da934cd9aeb636ad959677b1255346d9e6a8d9416589f204c5b272799a70627011b0a33ea1f87628a1f49" +
            "7b1851bd4cc01c6d3efe63e30062d113564fbff046390d04a0445ae3baa3abda7f62149b4f5fc267b8785486bec556" +
            "cb375ee5c2a46b50203010001300d06092a864886f70d0101050500038181003c7614c1e883f37c9c41892ef8d6fde" +
            "cf42d7b443466d0f9f31f24e4f6d03c37e1c19bdc6616a0fa6fe47c097332eed193a17653cde8f8885489b00644db09" +
            "c31abb7e1f700a03bbfafb900a1e21f7eed3f696f727f486bb353694b8e053b1824ca576a3a979ac707d047e8352bfdf" +
            "1fa22aaf4c9333a411fa0b7665e5747a68";

    private int mAuthActivityCode;
    private static String ssoPackageName = "";// "com.sina.weibo";
    private static String ssoActivityName = "";// "com.sina.weibo.MainTabActivity";
    private Activity mAuthActivity;
    private String[] authPermissions = {"friendships_groups_read", "friendships_groups_write"};


    public SsoHandler(Activity activity) {
        mAuthActivity = activity;
        conn = new ServiceConnection() {
            @Override
            public void onServiceDisconnected(ComponentName name) {

            }

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                RemoteSSO remoteSSOservice = RemoteSSO.Stub
                        .asInterface(service);
                try {
                    ssoPackageName = remoteSSOservice.getPackageName();
                    ssoActivityName = remoteSSOservice.getActivityName();
                    boolean singleSignOnStarted = startSingleSignOn(
                            mAuthActivity, authPermissions,
                            mAuthActivityCode);

                } catch (RemoteException e) {
                    e.printStackTrace();
                }

            }
        };
    }


    public void authorize() {
        authorize(DEFAULT_AUTH_ACTIVITY_CODE);
    }

    private void authorize(
            int activityCode) {
        mAuthActivityCode = activityCode;

        boolean bindSucced = false;

        // Prefer single sign-on, where available.
        bindSucced = bindRemoteSSOService(mAuthActivity);

    }

    private boolean bindRemoteSSOService(Activity activity) {
        Context context = activity.getApplicationContext();
        Intent intent = new Intent("com.sina.weibo.remotessoservice");
        return context.bindService(intent, conn, Context.BIND_AUTO_CREATE);
    }

    private boolean startSingleSignOn(Activity activity, String[] permissions, int activityCode) {
        boolean didSucceed = true;
        Intent intent = new Intent();
        intent.setClassName(ssoPackageName, ssoActivityName);
        intent.putExtra("appKey", URLHelper.APP_KEY);
        intent.putExtra("redirectUri", URLHelper.DIRECT_URL);

        if (permissions.length > 0) {
            intent.putExtra("scope", TextUtils.join(",", permissions));
        }

        // validate Signature
        if (!validateAppSignatureForIntent(activity, intent)) {
            return false;
        }

        try {
            activity.startActivityForResult(intent, activityCode);
        } catch (ActivityNotFoundException e) {
            didSucceed = false;
        }

        activity.getApplication().unbindService(conn);
        return didSucceed;
    }

    private boolean validateAppSignatureForIntent(Activity activity,
                                                  Intent intent) {
        ResolveInfo resolveInfo = activity.getPackageManager().resolveActivity(
                intent, 0);
        if (resolveInfo == null) {
            return false;
        }

        String packageName = resolveInfo.activityInfo.packageName;
        try {
            PackageInfo packageInfo = activity.getPackageManager()
                    .getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
            for (Signature signature : packageInfo.signatures) {
                if (WEIBO_SIGNATURE.equals(signature.toCharsString())) {
                    return true;
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }

        return false;
    }


}
