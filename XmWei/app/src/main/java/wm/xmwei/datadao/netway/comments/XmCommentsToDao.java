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
        map.put("since_id", since_id);
        map.put("max_id", max_id);
        map.put("count", count);
        map.put("page", page);
        map.put("filter_by_author", filter_by_author);
//        map.put("filter_by_source", filter_by_source);

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

/**
 * 根据微博ID返回某条微博的评论列表
 * @param id 需要查询的微博ID。
 * @param since_id 若指定此参数，则返回ID比since_id大的评论（即比since_id时间晚的评论），默认为0。
 * @param max_id 若指定此参数，则返回ID小于或等于max_id的评论，默认为0。
 * @param count 单页返回的记录条数，默认为50
 * @param page 返回结果的页码，默认为1。
 * @param filter_by_author 作者筛选类型，0：全部、1：我关注的人、2：陌生人，默认为0。
 * @param listener
 */

    protected String access_token;
    private String since_id = "0";
    private String max_id = "0";
    private String count = "50";
    private String page = "1";
    private String filter_by_author = "0";
    private String filter_by_source = "";

    public XmCommentsToDao(String access_token) {
        this.access_token = access_token;
        this.count = "20";
    }

    protected String getUrl() {
        return URLHelper.COMMENTS_TO_ME_TIMELINE;
    }

}
