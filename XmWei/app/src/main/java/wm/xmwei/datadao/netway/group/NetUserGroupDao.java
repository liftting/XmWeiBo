package wm.xmwei.datadao.netway.group;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.HashMap;
import java.util.Map;

import wm.xmwei.bean.DataGroupListDomain;
import wm.xmwei.core.debug.AppLogger;
import wm.xmwei.core.lib.support.error.XmWeiboException;
import wm.xmwei.core.net.http.HttpMethod;
import wm.xmwei.core.net.http.NetWorker;
import wm.xmwei.util.URLHelper;

/**
 *
 *
 *
 */
public class NetUserGroupDao {

    public DataGroupListDomain getGroup() throws XmWeiboException {

        String url = URLHelper.FRIENDSGROUP_INFO;

        Map<String, String> map = new HashMap<String, String>();
        map.put("access_token", access_token);
//        map.put("source", URLHelper.APP_KEY);

        String jsonData = NetWorker.getInstance().executeNormalTask(HttpMethod.Get, url, map);
        Gson gson = new Gson();
        DataGroupListDomain value = null;
        try {
            value = gson.fromJson(jsonData, DataGroupListDomain.class);
        } catch (JsonSyntaxException e) {
            AppLogger.e(e.getMessage());
        }

        return value;
    }

    public NetUserGroupDao(String token) {
        this.access_token = token;
    }

    private String access_token;

}
