package wm.xmwei.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import wm.xmwei.bean.base.DataAdDomain;
import wm.xmwei.bean.base.DataListDomain;
import wm.xmwei.util.XmSettingUtil;

/**
 * Created by wm on 15-3-24.
 */
public class DataMessageListDomain extends DataListDomain<DataMessageDomain, DataMessageListDomain> implements Parcelable {

    private List<DataMessageDomain> statuses = new ArrayList<DataMessageDomain>();
    private List<DataAdDomain> ad = new ArrayList<DataAdDomain>();
    private int removedCount = 0;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(total_number);
        dest.writeString(previous_cursor);
        dest.writeString(next_cursor);

        dest.writeTypedList(statuses);
        dest.writeTypedList(ad);
        dest.writeInt(removedCount);
    }

    public static final Parcelable.Creator<DataMessageListDomain> CREATOR =
            new Parcelable.Creator<DataMessageListDomain>() {
                public DataMessageListDomain createFromParcel(Parcel in) {
                    DataMessageListDomain DataMessageListDomain = new DataMessageListDomain();

                    DataMessageListDomain.total_number = in.readInt();
                    DataMessageListDomain.previous_cursor = in.readString();
                    DataMessageListDomain.next_cursor = in.readString();

                    DataMessageListDomain.statuses = new ArrayList<DataMessageDomain>();
                    in.readTypedList(DataMessageListDomain.statuses, DataMessageDomain.CREATOR);

                    DataMessageListDomain.ad = new ArrayList<DataAdDomain>();
                    in.readTypedList(DataMessageListDomain.ad, DataAdDomain.CREATOR);

                    DataMessageListDomain.removedCount = in.readInt();

                    return DataMessageListDomain;
                }

                public DataMessageListDomain[] newArray(int size) {
                    return new DataMessageListDomain[size];
                }
            };

    private List<DataMessageDomain> getStatuses() {
        return statuses;
    }

    public List<DataAdDomain> getAd() {
        return ad;
    }

    public void setStatuses(List<DataMessageDomain> statuses) {
        this.statuses = statuses;
    }

    @Override
    public int getSize() {
        return statuses.size();
    }

    @Override
    public DataMessageDomain getItem(int position) {
        return getStatuses().get(position);
    }

    @Override
    public List<DataMessageDomain> getItemList() {
        return getStatuses();
    }

    public int getReceivedCount() {
        return getSize() + removedCount;
    }

    public void removedCountPlus() {
        removedCount++;
    }

    @Override
    public void addNewData(DataMessageListDomain newValue) {

        if (newValue == null || newValue.getSize() == 0) {
            return;
        }

        boolean receivedCountBelowRequestCount = newValue.getReceivedCount() < Integer
                .valueOf(XmSettingUtil.getMsgCount());
        boolean receivedCountEqualRequestCount = newValue.getReceivedCount() >= Integer
                .valueOf(XmSettingUtil.getMsgCount());
        if (receivedCountEqualRequestCount && this.getSize() > 0) {
            DataMessageDomain middleUnreadItem = new DataMessageDomain();
            middleUnreadItem.setId(String.valueOf(System.currentTimeMillis()));
            middleUnreadItem.setMiddleUnreadItem(true);
            newValue.getItemList().add(middleUnreadItem);
        }
        this.getItemList().addAll(0, newValue.getItemList());
        this.setTotal_number(newValue.getTotal_number());

        //remove duplicate null flag, [x,y,null,null,z....]
        List<DataMessageDomain> msgList = getItemList();
        ListIterator<DataMessageDomain> listIterator = msgList.listIterator();

        boolean isLastItemNull = false;
        while (listIterator.hasNext()) {
            DataMessageDomain msg = listIterator.next();
            if (msg == null || msg.isMiddleUnreadItem()) {
                if (isLastItemNull) {
                    listIterator.remove();
                }
                isLastItemNull = true;
            } else {
                isLastItemNull = false;
            }
        }
    }

    @Override
    public void addOldData(DataMessageListDomain oldValue) {
        if (oldValue != null && oldValue.getSize() > 1) {
            getItemList().addAll(oldValue.getItemList().subList(1, oldValue.getSize()));
            setTotal_number(oldValue.getTotal_number());
        }
    }

    public void addMiddleData(int position, DataMessageListDomain middleValue, boolean towardsBottom) {
        if (middleValue == null) {
            return;
        }

        if (middleValue.getSize() == 0 || middleValue.getSize() == 1) {
            getItemList().remove(position);
            return;
        }

        List<DataMessageDomain> middleData = middleValue.getItemList().subList(1, middleValue.getSize());

        String beginId = getItem(position + 1).getId();
        String endId = getItem(position - 1).getId();
        Iterator<DataMessageDomain> iterator = middleData.iterator();
        while (iterator.hasNext()) {
            DataMessageDomain msg = iterator.next();
            boolean notNull = !TextUtils.isEmpty(msg.getId());
            if (notNull) {
                if (msg.getId().equals(beginId) || msg.getId().equals(endId)) {
                    iterator.remove();
                }
            }
        }

        getItemList().addAll(position, middleData);
    }

    public void replaceData(DataMessageListDomain value) {
        if (value == null) {
            return;
        }
        getItemList().clear();
        getItemList().addAll(value.getItemList());
        setTotal_number(value.getTotal_number());
    }

    public DataMessageListDomain copy() {
        DataMessageListDomain object = new DataMessageListDomain();
        object.replaceData(DataMessageListDomain.this);
        return object;
    }

    @Override
    public String toString() {
        return super.toString();
    }

}
