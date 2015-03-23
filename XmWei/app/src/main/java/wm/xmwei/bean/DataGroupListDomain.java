package wm.xmwei.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wm on 15-3-23.
 */
public class DataGroupListDomain implements Parcelable {

    private List<DataGroupDomain> lists = new ArrayList<DataGroupDomain>();
    private String total_number = "0";

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(total_number);
        dest.writeTypedList(lists);
    }

    public static final Parcelable.Creator<DataGroupListDomain> CREATOR =
            new Parcelable.Creator<DataGroupListDomain>() {
                public DataGroupListDomain createFromParcel(Parcel in) {
                    DataGroupListDomain groupListBean = new DataGroupListDomain();

                    groupListBean.total_number = in.readString();

                    groupListBean.lists = new ArrayList<DataGroupDomain>();
                    in.readTypedList(groupListBean.lists, DataGroupDomain.CREATOR);

                    return groupListBean;
                }

                public DataGroupListDomain[] newArray(int size) {
                    return new DataGroupListDomain[size];
                }
            };

    public List<DataGroupDomain> getLists() {
        return lists;
    }

    public void setLists(List<DataGroupDomain> lists) {
        this.lists = lists;
    }

    public String getTotal_number() {
        return total_number;
    }

    public void setTotal_number(String total_number) {
        this.total_number = total_number;
    }

    @Override
    public String toString() {
        return super.toString();
    }

}
