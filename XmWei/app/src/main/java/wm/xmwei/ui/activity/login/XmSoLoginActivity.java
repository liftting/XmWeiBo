package wm.xmwei.ui.activity.login;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuth;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;

import java.lang.ref.WeakReference;

import wm.xmwei.R;
import wm.xmwei.bean.UserBingDomain;
import wm.xmwei.bean.UserDomain;
import wm.xmwei.core.data.Constants;
import wm.xmwei.datadao.dbway.login.DbUserBingTask;
import wm.xmwei.core.debug.AppLogger;
import wm.xmwei.core.lib.support.error.XmWeiboException;
import wm.xmwei.datadao.netway.login.NetOAuthDao;
import wm.xmwei.ui.activity.base.XmBaseActivity;

/**
 *
 */
public class XmSoLoginActivity extends XmBaseActivity {

    /**
     * 微博 Web 授权类，提供登陆等功能
     */
    private WeiboAuth mWeiboAuth;

    /**
     * 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能
     */
    private Oauth2AccessToken mAccessToken;

    /**
     * 注意：SsoHandler 仅当 SDK 支持 SSO 时有效
     */
    private SsoHandler mSsoHandler;

    private SoLoginTask mSoLoginTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layer_so_login);


        // 创建微博实例
        mWeiboAuth = new WeiboAuth(this, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);
        mSoLoginTask = new SoLoginTask(this);
    }


    public void soLogin(View view) {
        mSsoHandler = new SsoHandler(XmSoLoginActivity.this, mWeiboAuth);
        mSsoHandler.authorize(new AuthListener());
    }

    /**
     * 微博认证授权回调类。
     * 1. SSO 授权时，需要在 {@link #onActivityResult} 中调用 {@link SsoHandler#authorizeCallBack} 后，
     * 该回调才会被执行。
     * 2. 非 SSO 授权时，当授权结束后，该回调就会被执行。
     * 当授权成功后，请保存该 access_token、expires_in、uid 等信息到 SharedPreferences 中。
     */
    class AuthListener implements WeiboAuthListener {

        @Override
        public void onComplete(Bundle values) {
            // 从 Bundle 中解析 Token
            mAccessToken = Oauth2AccessToken.parseAccessToken(values);
            if (mAccessToken.isSessionValid()) {
                // 显示 Token

                // 保存 Token 到 SharedPreferences
                AccessTokenKeeper.writeAccessToken(XmSoLoginActivity.this, mAccessToken);

                //利用token去读取用户信息
                mSoLoginTask.execute("");

            } else {
                // 当您注册的应用程序签名不正确时，就会收到 Code，请确保签名正确
                String code = values.getString("code");
                String message = "授权失败";
                if (!TextUtils.isEmpty(code)) {
                    message = message + "\nObtained the code: " + code;
                }
                Toast.makeText(XmSoLoginActivity.this, message, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onCancel() {
        }

        @Override
        public void onWeiboException(WeiboException e) {
            Toast.makeText(XmSoLoginActivity.this,
                    "Auth exception : " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    class SoLoginTask extends AsyncTask<String, String, OauthDbResult> {

        private WeakReference<XmSoLoginActivity> oAuthActivityWeakReference;

        private SoLoginTask(XmSoLoginActivity activity) {
            oAuthActivityWeakReference = new WeakReference<XmSoLoginActivity>(activity);
        }

        @Override
        protected void onPostExecute(OauthDbResult oauthDbResult) {

            if (oauthDbResult == null) {
                return;
            }

            XmSoLoginActivity activity = oAuthActivityWeakReference.get();
            if (activity == null) {
                return;
            }
            switch (oauthDbResult) {
                case add_successfuly:
                    Toast.makeText(activity, activity.getString(R.string.login_success),
                            Toast.LENGTH_SHORT).show();
                    break;
                case update_successfully:
                    Toast.makeText(activity, activity.getString(R.string.update_account_success),
                            Toast.LENGTH_SHORT).show();
                    break;
            }
            setResult(RESULT_OK);
            activity.finish();

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected OauthDbResult doInBackground(String... params) {
            return createUserData();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // SSO 授权回调
        // 重要：发起 SSO 登陆的 Activity 必须重写 onActivityResult
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    /**
     * 主要是根据token来获取用户信息
     *
     * @return
     */
    private OauthDbResult createUserData() {
        try {

            String token = mAccessToken.getToken();
            long expiresInSeconds = mAccessToken.getExpiresTime();
            UserDomain user = new NetOAuthDao(token).getOAuthUserInfo();
            UserBingDomain bingDomain = new UserBingDomain();
            bingDomain.setAccess_token(token);
            bingDomain.setExpires_time(System.currentTimeMillis() + expiresInSeconds * 1000);
            bingDomain.setInfo(user);

            return DbUserBingTask.addOrUpdateBingInfo(bingDomain, false);
        } catch (XmWeiboException e) {
            AppLogger.e(e.getError());
        }
        return null;
    }

    public static enum OauthDbResult {
        add_successfuly, update_successfully
    }

}
