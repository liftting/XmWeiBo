package wm.xmwei.bean.base;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wm on 15-3-24.
 */
public class DataAdDomain implements Parcelable {
    private String id;
    private String mark;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(mark);
    }

    public static final Parcelable.Creator<DataAdDomain> CREATOR =
            new Parcelable.Creator<DataAdDomain>() {
                public DataAdDomain createFromParcel(Parcel in) {
                    DataAdDomain DataAdDomain = new DataAdDomain();
                    DataAdDomain.id = in.readString();
                    DataAdDomain.mark = in.readString();
                    return DataAdDomain;
                }

                public DataAdDomain[] newArray(int size) {
                    return new DataAdDomain[size];
                }
            };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }
}
