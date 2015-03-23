package wm.xmwei.core.net.http;

/**
 * Created by wm on 15-3-23.
 */
public interface OnDataLoadTaskListener<T> {

    public void dataLoadComplete(T data);

}
