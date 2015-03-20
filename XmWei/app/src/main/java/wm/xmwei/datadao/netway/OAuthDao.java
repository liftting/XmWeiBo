package wm.xmwei.datadao.netway;


import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import wm.xmwei.bean.UserDomain;
import wm.xmwei.core.debug.AppLogger;
import wm.xmwei.core.lib.support.error.XmWeiboException;
import wm.xmwei.core.net.http.HttpMethod;
import wm.xmwei.core.net.http.NetWorker;
import wm.xmwei.util.URLHelper;

/**
 *
 */
public class OAuthDao {

    private String access_token;

    public OAuthDao(String access_token) {

        this.access_token = access_token;
    }

    public UserDomain getOAuthUserInfo() throws XmWeiboException {

        String uidJson = getOAuthUserUIDJsonData();
        String uid = "";

        try {
            JSONObject jsonObject = new JSONObject(uidJson);
            uid = jsonObject.optString("uid");
        } catch (JSONException e) {
            AppLogger.e(e.getMessage());
        }

        Map<String, String> map = new HashMap<String, String>();
        map.put("uid", uid);
        map.put("access_token", access_token);

        String url = URLHelper.USER_SHOW;
        String result = NetWorker.getInstance().executeNormalTask(HttpMethod.Get, url, map);

        Gson gson = new Gson();
        UserDomain user = new UserDomain();
        try {
            user = gson.fromJson(result, UserDomain.class);
        } catch (JsonSyntaxException e) {
            AppLogger.e(result);
        }
        return user;
    }

    private String getOAuthUserUIDJsonData() throws XmWeiboException {
        String url = URLHelper.UID;
        Map<String, String> map = new HashMap<String, String>();
        map.put("access_token", access_token);
        return NetWorker.getInstance().executeNormalTask(HttpMethod.Get, url, map);
    }
}
