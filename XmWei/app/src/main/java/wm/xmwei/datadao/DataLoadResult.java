package wm.xmwei.datadao;

import android.os.Bundle;

import wm.xmwei.core.lib.support.error.XmWeiboException;

/**
 *
 *
 */
public class DataLoadResult<T> {

    // 请求回来的数据 listBean
    public T data;

    // 加载数据出现了异常
    public XmWeiboException exception;

    // 传递的一些参数信息
    public Bundle args;

}
