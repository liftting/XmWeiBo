package wm.xmwei.datadao.netway.comments;

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

public class XmCommentsToDao implements ICommentsDao {

    @Override
    public DataCommentListDomain getDataListByGson() throws XmWeiboException {
        String url = getUrl();

        Map<String, String> map = new HashMap<String, String>();
        map.put("access_token", access_token);
        map.put("since_id", "0");
        map.put("max_id", "100");
        map.put("count", count);
        map.put("page", page);
        map.put("filter_by_author", filter_by_author);
        map.put("filter_by_source", filter_by_source);

        String jsonData = NetWorker.getInstance().executeNormalTask(HttpMethod.Get, url, map);

        Gson gson = new Gson();

        DataCommentListDomain value = null;
        try {
            value = gson.fromJson(jsonData, DataCommentListDomain.class);
        } catch (JsonSyntaxException e) {
            AppLogger.e(e.getMessage());
        }

        if (value != null && value.getSize() > 0) {
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

    public void setSince_id(String since_id) {
        this.since_id = since_id;
    }

    public void setMax_id(String max_id) {
        this.max_id = max_id;
    }

    public XmCommentsToDao setCount(String count) {
        this.count = count;
        return this;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public void setFilter_by_author(String filter_by_author) {
        this.filter_by_author = filter_by_author;
    }

    protected String access_token;
    private String since_id;
    private String max_id;
    private String count;
    private String page;
    private String filter_by_author = "";
    private String filter_by_source = "";

    public XmCommentsToDao(String access_token) {
        this.access_token = access_token;
        this.count = "20";
        this.page = "1";
    }

    protected String getUrl() {
        return URLHelper.COMMENTS_TO_ME_TIMELINE;
    }

}
