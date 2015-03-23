package wm.xmwei.ui.dataloader.datatask;

import wm.xmwei.bean.DataGroupListDomain;
import wm.xmwei.core.lib.support.XmAsyncTask;
import wm.xmwei.core.lib.support.error.XmWeiboException;
import wm.xmwei.datadao.netway.group.NetUserGroupDao;

/**
 */
public class XmUserGroupTask extends XmAsyncTask<Void, DataGroupListDomain, DataGroupListDomain> {

    private XmWeiboException e;
    private String mToken;
    private String mBingUserId;

    public XmUserGroupTask(String token, String bingUserId) {
        mToken = token;
        mBingUserId = bingUserId;
    }

    @Override
    protected DataGroupListDomain doInBackground(Void... params) {

        //要先去本地查询，再从网络查询，

        try {
            return new NetUserGroupDao(mToken).getGroup();
        } catch (XmWeiboException e1) {
            cancel(true);
            e1.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(DataGroupListDomain dataGroupListDomain) {
        // 网络查询执行完成后，需要将数据更新到db数据库中，
        super.onPostExecute(dataGroupListDomain);

        mDataLoadTaskListener.dataLoadComplete(dataGroupListDomain);
    }
}
