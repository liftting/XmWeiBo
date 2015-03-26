package wm.xmwei.bean;

/**
 * Created with IntelliJ IDEA.
 * User: think
 * Date: 15-3-26
 * Time: 下午11:03
 * this is load db mesage data for group
 */
public class DataMessageGroupDomain {

    public DataMessageListDomain msgList;
    public TimeLinePosition position;
    public String groupId;

    public DataMessageGroupDomain(String groupId, DataMessageListDomain msgList, TimeLinePosition position) {
        this.groupId = groupId;
        this.msgList = msgList;
        this.position = position;
    }

}
