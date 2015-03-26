package wm.xmwei.bean;

import wm.xmwei.bean.base.DataItemDomain;
import wm.xmwei.bean.base.DataListDomain;

import java.util.TreeSet;

/**
 * Created with IntelliJ IDEA.
 * User: think
 * Date: 15-3-26
 * Time: 下午11:04
 * To change this template use File | Settings | File Templates.
 */
public class TimeLinePosition {

    public TimeLinePosition(long firstItemId, int top, int position) {
        this.firstItemId = firstItemId;
        this.top = top;
        this.position = position;
    }

    public long firstItemId = 0L;

    //Fragment contain ListView, when Fragment is invisible (user dont open Fragment), ListView children count is zero, getFirstVisiblePosition return zero
    //App have to use firstItemId to calc correct position
    public int position = 0;
    public int top = 0;

    public TreeSet<Long> newMsgIds = new TreeSet<Long>();

    private boolean empty = false;

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder().append("first item id :")
                .append(firstItemId)
                .append("; top=").append(top);
        return stringBuilder.toString();
    }

    public int getPosition(DataListDomain source) {
        for (int i = 0; i < source.getSize(); i++) {
            DataItemDomain item = source.getItem(i);
            if (item != null && item.getIdLong() == this.firstItemId) {
                return i;
            }
        }

        return 0;
    }

    public static TimeLinePosition empty() {
        TimeLinePosition position = new TimeLinePosition(0, 0, 0);
        position.empty = true;
        return position;
    }

    public boolean isEmpty() {
        return empty;
    }


}
