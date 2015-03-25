package wm.xmwei.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.SpannableString;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import wm.xmwei.bean.base.DataListDomain;

/**
 * Created by wm on 15-3-20.
 */
public class DataCommentListDomain extends DataListDomain<DataCommentDomain, DataCommentListDomain> implements Parcelable {

    private List<DataCommentDomain> comments = new ArrayList<DataCommentDomain>();

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(total_number);
        dest.writeString(previous_cursor);
        dest.writeString(next_cursor);

        dest.writeTypedList(comments);
    }

    public static final Parcelable.Creator<DataCommentListDomain> CREATOR =
            new Parcelable.Creator<DataCommentListDomain>() {
                public DataCommentListDomain createFromParcel(Parcel in) {
                    DataCommentListDomain DataCommentListDomain = new DataCommentListDomain();

                    DataCommentListDomain.total_number = in.readInt();
                    DataCommentListDomain.previous_cursor = in.readString();
                    DataCommentListDomain.next_cursor = in.readString();

                    DataCommentListDomain.comments = new ArrayList<DataCommentDomain>();
                    in.readTypedList(DataCommentListDomain.comments, DataCommentDomain.CREATOR);

                    return DataCommentListDomain;
                }

                public DataCommentListDomain[] newArray(int size) {
                    return new DataCommentListDomain[size];
                }
            };

    private List<DataCommentDomain> getComments() {
        return comments;
    }

    public void setComments(List<DataCommentDomain> comments) {
        this.comments = comments;
    }

    @Override
    public DataCommentDomain getItem(int position) {
        return getComments().get(position);
    }

    @Override
    public List<DataCommentDomain> getItemList() {
        return getComments();
    }

    @Override
    public void addNewData(DataCommentListDomain newValue) {
        if (newValue == null || newValue.getSize() == 0) {
            return;
        }
        this.getItemList().addAll(0, newValue.getItemList());
        this.setTotal_number(newValue.getTotal_number());

        //remove duplicate null flag, [x,y,null,null,z....]
//        List<DataCommentDomain> msgList = getItemList();
//        ListIterator<DataCommentDomain> listIterator = msgList.listIterator();

//        boolean isLastItemNull = false;
//        while (listIterator.hasNext()) {
//            DataCommentDomain msg = listIterator.next();
//            if (msg == null || msg.isMiddleUnreadItem()) {
//                if (isLastItemNull) {
//                    listIterator.remove();
//                }
//                isLastItemNull = true;
//            } else {
//                isLastItemNull = false;
//            }
//        }
    }

    @Override
    public void addOldData(DataCommentListDomain oldValue) {
        if (oldValue != null && oldValue.getSize() > 1) {
            getItemList().addAll(oldValue.getItemList().subList(1, oldValue.getSize()));
            setTotal_number(oldValue.getTotal_number());
        }
    }

    @Override
    public int getSize() {
        return comments.size();
    }

    public void addMiddleData(int position, DataCommentListDomain middleValue, boolean towardsBottom) {
        if (middleValue == null) {
            return;
        }

        if (middleValue.getSize() == 0 || middleValue.getSize() == 1) {
            getItemList().remove(position);
            return;
        }

        List<DataCommentDomain> middleData = middleValue.getItemList().subList(1, middleValue.getSize());

        String beginId = getItem(position + 1).getId();
        String endId = getItem(position - 1).getId();
        Iterator<DataCommentDomain> iterator = middleData.iterator();
        while (iterator.hasNext()) {
            DataCommentDomain msg = iterator.next();
            boolean notNull = !TextUtils.isEmpty(msg.getId());
            if (notNull) {
                if (msg.getId().equals(beginId) || msg.getId().equals(endId)) {
                    iterator.remove();
                }
            }
        }

        getItemList().addAll(position, middleData);
    }

    public void replaceAll(DataCommentListDomain newValue) {
        if (newValue != null && newValue.getSize() > 0) {
            setTotal_number(newValue.getTotal_number());
            getItemList().clear();
            getItemList().addAll(newValue.getItemList());
        }
    }

    public void clear() {
        setTotal_number(0);
        getItemList().clear();
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
