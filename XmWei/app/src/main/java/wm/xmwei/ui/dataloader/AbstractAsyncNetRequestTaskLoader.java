package wm.xmwei.ui.dataloader;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;

import wm.xmwei.core.lib.support.error.XmWeiboException;
import wm.xmwei.datadao.DataLoadResult;

/**
 */
public abstract class AbstractAsyncNetRequestTaskLoader<T>
        extends AsyncTaskLoader<DataLoadResult<T>> {

    private DataLoadResult<T> result;
    private Bundle args;

    public AbstractAsyncNetRequestTaskLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if (result == null) {
            forceLoad();
        } else {
            deliverResult(result);
        }
    }

    @Override
    public DataLoadResult<T> loadInBackground() {
        T data = null;
        XmWeiboException exception = null;

        try {
            data = loadData();
        } catch (XmWeiboException e) {
            exception = e;
        }

        result = new DataLoadResult<T>();
        result.data = data;
        result.exception = exception;
        result.args = this.args;

        return result;
    }

    protected abstract T loadData() throws XmWeiboException;

    public void setArgs(Bundle args) {
        if (result != null) {
            throw new IllegalArgumentException("can't setArgs after loader executes");
        }
        this.args = args;
    }
}
