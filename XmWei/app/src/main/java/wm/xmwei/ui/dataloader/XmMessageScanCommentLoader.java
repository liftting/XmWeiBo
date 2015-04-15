package wm.xmwei.ui.dataloader;

import android.content.Context;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import wm.xmwei.bean.DataCommentListDomain;
import wm.xmwei.bean.DataMessageListDomain;
import wm.xmwei.core.lib.support.error.XmWeiboException;
import wm.xmwei.datadao.netway.messagescan.NetMessageScanCommentsDao;

/**
 *
 */
public class XmMessageScanCommentLoader extends AbstractAsyncNetRequestTaskLoader<DataCommentListDomain> {

    private static Lock lock = new ReentrantLock();

    private String token;
    private String sinceId;
    private String maxId;
    private String id;

    public XmMessageScanCommentLoader(Context context, String id, String token, String sinceId,
                                      String maxId) {
        super(context);
        this.token = token;
        this.sinceId = sinceId;
        this.maxId = maxId;
        this.id = id;
    }

    @Override
    protected DataCommentListDomain loadData() throws XmWeiboException {
        NetMessageScanCommentsDao dao = new NetMessageScanCommentsDao(token, id);
        dao.setSince_id(sinceId);
        dao.setMax_id(maxId);
        DataCommentListDomain result = null;
        lock.lock();
        try {
            result = dao.getGSONMsgList();
        } finally {
            lock.unlock();
        }

        return result;
    }


}
