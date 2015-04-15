package wm.xmwei.datadao.netway.messagescan;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import wm.xmwei.bean.DataCommentDomain;
import wm.xmwei.bean.DataCommentListDomain;
import wm.xmwei.core.debug.AppLogger;
import wm.xmwei.core.lib.support.error.XmWeiboException;
import wm.xmwei.core.net.http.HttpMethod;
import wm.xmwei.core.net.http.NetWorker;
import wm.xmwei.util.URLHelper;
import wm.xmwei.util.XmSettingUtil;

/**
 * Created by wm on 15-4-15.
 */
public class NetMessageScanCommentsDao {

    private String access_token;
    private String id;
    private String since_id;
    private String max_id;
    private String count;
    private String page;
    private String filter_by_author;

    public DataCommentListDomain getGSONMsgList() throws XmWeiboException {

        String url = URLHelper.COMMENTS_TIMELINE_BY_MSGID;

        Map<String, String> map = new HashMap<String, String>();
        map.put("access_token", access_token);
        map.put("id", id);
        map.put("since_id", since_id);
        map.put("max_id", max_id);
        map.put("count", count);
        map.put("page", page);
        map.put("filter_by_author", filter_by_author);

        String jsonData = NetWorker.getInstance().executeNormalTask(HttpMethod.Get, url, map);
        Gson gson = new Gson();

        DataCommentListDomain value = null;
        try {
            value = gson.fromJson(jsonData, DataCommentListDomain.class);
        } catch (JsonSyntaxException e) {

            AppLogger.e(e.getMessage());
        }

        if (value != null && value.getItemList().size() > 0) {
            List<DataCommentDomain> msgList = value.getItemList();
            Iterator<DataCommentDomain> iterator = msgList.iterator();
            while (iterator.hasNext()) {

                DataCommentDomain msg = iterator.next();
                if (msg.getUser() == null) {
                    iterator.remove();
                } else {
                    msg.getListViewSpannableString();
//                    TimeUtility.dealMills(msg);
                }
            }
        }

        return value;
    }

    public NetMessageScanCommentsDao(String token, String id) {

        this.access_token = token;
        this.id = id;
        this.count = XmSettingUtil.getMsgCount();
    }

    public void setSince_id(String since_id) {
        this.since_id = since_id;
    }

    public void setMax_id(String max_id) {
        this.max_id = max_id;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public void setFilter_by_author(String filter_by_author) {
        this.filter_by_author = filter_by_author;
    }

}
