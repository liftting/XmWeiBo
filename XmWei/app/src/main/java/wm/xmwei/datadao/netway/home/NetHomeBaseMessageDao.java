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
import wm.xmwei.util.XmSettingUtil;

/**
 *
 *
 */
public class NetHomeBaseMessageDao {

    protected String getUrl() {
        return URLHelper.FRIENDS_TIMELINE;
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

        String jsonData = NetWorker.getInstance().executeNormalTask(HttpMethod.Get, url, map);
//        try {
//            new ClearUnreadDao(access_token, ClearUnreadDao.STATUS).clearUnread();
//        } catch (WeiboException ignored) {
//
//        }
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
            throw new WeiboException(e.getMessage());
        }
//        if (value != null && value.getItemList().size() > 0) {
//            TimeLineUtility.filterMessage(value);
//            TimeLineUtility.filterHomeTimeLineSinaWeiboAd(value);
//        }

        return value;
    }

    protected String access_token;
    protected String since_id;
    protected String max_id;
    protected String count;
    protected String page;
    protected String base_app;
    protected String feature;
    protected String trim_user;

    public NetHomeBaseMessageDao(String access_token) {

        this.access_token = access_token;
        this.count = XmSettingUtil.getMsgCount();
    }

    public NetHomeBaseMessageDao setSince_id(String since_id) {
        this.since_id = since_id;
        return this;
    }

    public NetHomeBaseMessageDao setMax_id(String max_id) {
        this.max_id = max_id;
        return this;
    }

    public NetHomeBaseMessageDao setCount(String count) {
        this.count = count;
        return this;
    }

    public NetHomeBaseMessageDao setPage(String page) {
        this.page = page;
        return this;
    }

    public NetHomeBaseMessageDao setBase_app(String base_app) {
        this.base_app = base_app;
        return this;
    }

    public NetHomeBaseMessageDao setFeature(String feature) {
        this.feature = feature;
        return this;
    }

    public NetHomeBaseMessageDao setTrim_user(String trim_user) {
        this.trim_user = trim_user;
        return this;
    }

}
