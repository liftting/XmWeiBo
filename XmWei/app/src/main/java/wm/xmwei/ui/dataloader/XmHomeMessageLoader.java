package wm.xmwei.ui.dataloader;

import android.content.Context;
import android.text.TextUtils;

import com.sina.weibo.sdk.utils.Utility;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import wm.xmwei.bean.DataMessageListDomain;
import wm.xmwei.core.data.Constants;
import wm.xmwei.core.lib.support.error.XmWeiboException;
import wm.xmwei.datadao.netway.home.NetHomeBilateralMessageDao;
import wm.xmwei.datadao.netway.home.NetHomeDefaultMessageDao;
import wm.xmwei.datadao.netway.home.NetHomeGroupMessageDao;
import wm.xmwei.ui.fragment.XmHomeFragment;

/**
 *
 *
 */
public class XmHomeMessageLoader extends AbstractAsyncNetRequestTaskLoader<DataMessageListDomain> {

    private static Lock lock = new ReentrantLock();

    private String token;
    private String sinceId;
    private String maxId;
    private String accountId;
    private String currentGroupId;

    private final int MAX_RETRY_COUNT = 6;  //1*50+6*49=344 new messages count

    public XmHomeMessageLoader(Context context, String accountId, String token, String groupId,
                               String sinceId, String maxId) {
        super(context);
        this.token = token;
        this.sinceId = sinceId;
        this.maxId = maxId;
        this.accountId = accountId;
        this.currentGroupId = groupId;
    }

    public DataMessageListDomain loadData() throws XmWeiboException {
        DataMessageListDomain result = null;
        DataMessageListDomain tmp = get(token, currentGroupId, sinceId, maxId);
        result = tmp;
//        if (isLoadNewData() && Utility.isWifi(getContext()) && SettingUtility
//                .isWifiUnlimitedMsgCount()) {
//            int retryCount = 0;
//            while (tmp.getReceivedCount() >= Integer.valueOf(SettingUtility.getMsgCount())
//                    && retryCount < MAX_RETRY_COUNT) {
//                String tmpMaxId = tmp.getItemList().get(tmp.getItemList().size() - 1).getId();
//                tmp = get(token, currentGroupId, sinceId, tmpMaxId);
//                result.addOldData(tmp);
//                retryCount++;
//            }
//            if (tmp.getReceivedCount() >= Integer.valueOf(SettingUtility.getMsgCount())) {
//                MessageBean middleUnreadItem = new MessageBean();
//                middleUnreadItem.setId(String.valueOf(System.currentTimeMillis()));
//                middleUnreadItem.setMiddleUnreadItem(true);
//                result.getItemList().add(middleUnreadItem);
//            }
//        } else {
//            return result;
//        }

        return result;
    }

    private boolean isLoadNewData() {
        return !TextUtils.isEmpty(sinceId) && TextUtils.isEmpty(maxId);
    }

    private DataMessageListDomain get(String token, String groupId, String sinceId, String maxId)
            throws XmWeiboException {
        NetHomeDefaultMessageDao dao;
        if (currentGroupId.equals(Constants.BILATERAL_GROUP_ID)) {
            dao = new NetHomeBilateralMessageDao(token);
        } else if (currentGroupId.equals(Constants.ALL_GROUP_ID)) {
            dao = new NetHomeDefaultMessageDao(token);
        } else {
            dao = new NetHomeGroupMessageDao(token, currentGroupId); //
        }

        dao.setSince_id(sinceId);
        dao.setMax_id(maxId);
        DataMessageListDomain result = null;

        lock.lock();

        try {
            result = dao.getGSONMsgList();
        } finally {
            lock.unlock();
        }
        return result;
    }
}
