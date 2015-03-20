package wm.xmwei.ui.activity.login;


import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import wm.xmwei.R;
import wm.xmwei.bean.UserBingDomain;
import wm.xmwei.bean.UserDomain;
import wm.xmwei.core.debug.AppLogger;
import wm.xmwei.core.lib.support.error.XmWeiboException;
import wm.xmwei.datadao.netway.OAuthDao;
import wm.xmwei.ui.activity.BaseActivity;
import wm.xmwei.util.URLHelper;
import wm.xmwei.util.XmUtils;

/**
 * 网页的认证
 */
public class OAuthActivity extends BaseActivity {

    private WebView webView;
    private MenuItem refreshItem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layer_oauth_web_main);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setTitle(getString(R.string.login));
        webView = (WebView) findViewById(R.id.webView);
        webView.setWebViewClient(new WeiboWebViewClient());

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setSaveFormData(false);
        settings.setSavePassword(false);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH);

        CookieSyncManager.createInstance(this);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.clearCache(true);

    }

    // 创建选项菜单时，回调操作
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_menu_oauthactivity, menu);
        refreshItem = menu.findItem(R.id.menu_refresh); // 添加选项actionBar
        refresh();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
//                Intent intent = AccountActivity.newIntent();
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
                return true;
            case R.id.menu_refresh:
                refresh();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void refresh() {
        webView.clearView();
        webView.loadUrl("about:blank");
        LayoutInflater inflater = (LayoutInflater) getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);

        // 设置actionBar的view进行旋转操作view
        ImageView iv = (ImageView) inflater.inflate(R.layout.layer_refresh_action_view, null);
        Animation rotation = AnimationUtils.loadAnimation(this, R.anim.refresh);
        iv.startAnimation(rotation);
        refreshItem.setActionView(iv);


        webView.loadUrl(getWeiboOAuthUrl());
    }

    private void completeRefresh() {
        if (refreshItem.getActionView() != null) {
            refreshItem.getActionView().clearAnimation();
            refreshItem.setActionView(null);
        }
    }

    private String getWeiboOAuthUrl() {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("client_id", URLHelper.APP_KEY);
        parameters.put("response_type", "token");
        parameters.put("redirect_uri", URLHelper.DIRECT_URL);
        parameters.put("display", "mobile");
        return URLHelper.URL_OAUTH2_ACCESS_AUTHORIZE + "?" + XmUtils.encodeUrl(parameters)
                + "&scope=friendships_groups_read,friendships_groups_write";
    }

    private class WeiboWebViewClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            //页面加载完成，要停止掉actionBar的旋转效果
            if (!url.equals("about:blank")) {
                completeRefresh();
            }
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {

            // 这里如果成功，会进行回调到，我们在web配置的回调url
            if (url.startsWith(URLHelper.DIRECT_URL)) {
                handleRedirectUrl(view, url);
                view.stopLoading();
                return;
            }
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
        }


    }

    private void handleRedirectUrl(WebView view, String url) {
        Bundle values = XmUtils.parseUrl(url);
        String error = values.getString("error");
        String error_code = values.getString("error_code");

        Intent intent = new Intent();
        intent.putExtras(values);

        if (error == null && error_code == null) {

            String access_token = values.getString("access_token");
            String expires_time = values.getString("expires_in");
            setResult(RESULT_OK, intent);
            new OAuthTask(this).execute(access_token, expires_time);
        } else {
            Toast.makeText(OAuthActivity.this, getString(R.string.you_cancel_login),
                    Toast.LENGTH_SHORT).show();
//            finish();
        }
    }

    private static class OAuthTask extends AsyncTask<String, String, String> {

        private XmWeiboException e;
        private WeakReference<OAuthActivity> oAuthActivityWeakReference;

        private OAuthTask(OAuthActivity activity) {
            oAuthActivityWeakReference = new WeakReference<OAuthActivity>(activity);
        }


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {
            String token = params[0];
            long expiresInSeconds = Long.valueOf(params[1]);

            try {
                UserDomain user = new OAuthDao(token).getOAuthUserInfo();
                UserBingDomain account = new UserBingDomain();
                account.setAccess_token(token);
                account.setExpires_time(System.currentTimeMillis() + expiresInSeconds * 1000);
                account.setInfo(user);

                return "UserBingDomain";

            } catch (XmWeiboException e) {
                AppLogger.e(e.getError());
                this.e = e;
                cancel(true);
                return null;
            }
        }

        @Override
        protected void onCancelled(String dbResult) {
            super.onCancelled(dbResult);
            OAuthActivity activity = oAuthActivityWeakReference.get();
            if (activity == null) {
                return;
            }

            if (e != null) {
                Toast.makeText(activity, e.getError(), Toast.LENGTH_SHORT).show();
            }
            activity.webView.loadUrl(activity.getWeiboOAuthUrl());
        }

        @Override
        protected void onPostExecute(String dbResult) {
            OAuthActivity activity = oAuthActivityWeakReference.get();
            if (activity == null) {
                return;
            }

            Toast.makeText(activity, "dbResult",
                    Toast.LENGTH_SHORT).show();
        }
    }


}
