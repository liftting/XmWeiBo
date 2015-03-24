package wm.xmwei.ui.dataloader;

import android.content.Context;

import wm.xmwei.bean.DataMessageListDomain;
import wm.xmwei.core.lib.support.error.XmWeiboException;

/**
 *
 *
 */
public class XmHomeMessageLoader extends AbstractAsyncNetRequestTaskLoader<DataMessageListDomain> {


    public XmHomeMessageLoader(Context context) {
        super(context);
    }

    @Override
    protected DataMessageListDomain loadData() throws XmWeiboException {
        return null;
    }
}
