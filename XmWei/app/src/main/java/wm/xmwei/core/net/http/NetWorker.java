package wm.xmwei.core.net.http;

import android.content.Context;

import java.util.Map;

import wm.xmwei.XmApplication;
import wm.xmwei.core.lib.support.error.XmWeiboException;
import wm.xmwei.core.net.RequestQueue;
import wm.xmwei.core.net.toolbox.Volley;

/**
 *
 *
 */
public class NetWorker {

    private static NetWorker netWorker = new NetWorker();

    private static RequestQueue mVolleyQueue;

    private NetWorker() {
    }

    public static NetWorker getInstance() {
        initVolley();
        return netWorker;
    }

    public String executeNormalTask(HttpMethod httpMethod, String url, Map<String, String> param)
            throws XmWeiboException {
        return HttpNetWorker.executeNormalTask(httpMethod, url, param);
    }

    private static void initVolley() {
        mVolleyQueue = Volley.newRequestQueue(XmApplication.getInstance());
    }

    public void request() {
        
    }

}
