package wm.xmwei.datadao.netway.comments;


import wm.xmwei.bean.DataCommentListDomain;
import wm.xmwei.core.lib.support.error.XmWeiboException;

/**
 *
 */
public interface ICommentsDao {


    public DataCommentListDomain getDataListByGson() throws XmWeiboException;

    public void setSince_id(String since_id);

    public void setMax_id(String max_id);


}
