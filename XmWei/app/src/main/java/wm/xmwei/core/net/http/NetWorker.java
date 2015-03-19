package wm.xmwei.core.net.http;

import java.util.Map;

import wm.xmwei.core.lib.support.error.XmWeiboException;

/**
 *
 *
 */
public class NetWorker {

    private static NetWorker netWorker = new NetWorker();

    private NetWorker() {
    }

    public static NetWorker getInstance() {
        return netWorker;
    }

    public String executeNormalTask(HttpMethod httpMethod, String url, Map<String, String> param)
            throws XmWeiboException {
        return HttpNetWorker.executeNormalTask(httpMethod, url, param);
    }

}
