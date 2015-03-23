package wm.xmwei.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 */
public class DataGroupDomain implements Parcelable {

    private String id;
    private String idstr;
    private String name;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(idstr);
        dest.writeString(name);
    }

    public static final Parcelable.Creator<DataGroupDomain> CREATOR =
            new Parcelable.Creator<DataGroupDomain>() {
                public DataGroupDomain createFromParcel(Parcel in) {
                    DataGroupDomain groupBean = new DataGroupDomain();
                    groupBean.id = in.readString();
                    groupBean.idstr = in.readString();
                    groupBean.name = in.readString();
                    return groupBean;
                }

                public DataGroupDomain[] newArray(int size) {
                    return new DataGroupDomain[size];
                }
            };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdstr() {
        return idstr;
    }

    public void setIdstr(String idstr) {
        this.idstr = idstr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return super.toString();
    }

}
