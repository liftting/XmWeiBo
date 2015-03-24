package wm.xmwei.datadao.netway.home;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sina.weibo.sdk.exception.WeiboException;

import java.util.HashMap;
import java.util.Map;

import wm.xmwei.bean.DataMessageListDomain;
import wm.xmwei.core.debug.AppLogger;
import wm.xmwei.core.lib.support.error.XmWeiboException;
import wm.xmwei.core.net.http.HttpMethod;
import wm.xmwei.core.net.http.NetWorker;
import wm.xmwei.util.URLHelper;

/**
 *
 *
 */
public class NetHomeGroupMessageDao extends NetHomeBaseMessageDao {

    protected String getUrl() {
        return URLHelper.FRIENDSGROUP_TIMELINE;
    }

    private String getMsgListJson() throws XmWeiboException {
        String url = getUrl();

        Map<String, String> map = new HashMap<String, String>();
        map.put("access_token", access_token);
        map.put("since_id", since_id);
        map.put("max_id", max_id);
        map.put("count", count);
        map.put("page", page);
        map.put("base_app", base_app);
        map.put("feature", feature);
        map.put("trim_user", trim_user);
        map.put("list_id", list_id);

        String jsonData = NetWorker.getInstance().executeNormalTask(HttpMethod.Get, url, map);

        return jsonData;
    }

    public DataMessageListDomain getGSONMsgList() throws XmWeiboException {

        String json = getMsgListJson();
        Gson gson = new Gson();

        DataMessageListDomain value = null;
        try {
            value = gson.fromJson(json, DataMessageListDomain.class);
        } catch (JsonSyntaxException e) {

            AppLogger.e(e.getMessage());
            return null;
        }
//        if (value != null && value.getItemList().size() > 0) {
//            TimeLineUtility.filterMessage(value);
//        }

        return value;
    }

    public NetHomeGroupMessageDao(String access_token, String list_id) {

        super(access_token);
        this.list_id = list_id;
    }

    private String list_id;
}
