package wm.xmwei.ui.dataloader;

import android.content.Context;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import wm.xmwei.bean.DataCommentListDomain;
import wm.xmwei.core.lib.support.error.XmWeiboException;
import wm.xmwei.datadao.netway.comments.NetCommentsToUserDao;

/**
 *
 */
public class XmCommentsToMeLoader extends AbstractAsyncNetRequestTaskLoader<DataCommentListDomain> {

    private String token;
    private String sinceId;
    private String maxId;
    private String accountId;

    private static Lock lock = new ReentrantLock();

    public XmCommentsToMeLoader(Context context, String accountId, String token, String sinceId,
                                String maxId) {
        super(context);
        this.token = token;
        this.sinceId = sinceId;
        this.maxId = maxId;
        this.accountId = accountId;
    }

    @Override
    protected DataCommentListDomain loadData() throws XmWeiboException {
        //真正去异步的加载数据
        NetCommentsToUserDao dao = new NetCommentsToUserDao(token);
        dao.setSince_id(sinceId);
        dao.setMax_id(maxId);
        DataCommentListDomain result = null;
        lock.lock();

        try {
            result = dao.getDataListByGson();
        } finally {
            lock.unlock();
        }

        return result;
    }
}
