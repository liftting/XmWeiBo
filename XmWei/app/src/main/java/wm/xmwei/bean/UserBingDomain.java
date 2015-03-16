package wm.xmwei.bean;


import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import wm.xmwei.util.DomainTextUtil;


/**
 *
 * this is user login bing info
 *
 */
public class UserBingDomain implements Parcelable {

    private String access_token;
    private long expires_time;
    private UserDomain info;
    private boolean black_magic;
    private int navigationPosition;

    public String getUid() {
        return (info != null ? info.getId() : "");
    }

    public String getUsernick() {
        return (info != null ? info.getScreen_name() : "");
    }

    public String getAvatar_url() {
        return (info != null ? info.getProfile_image_url() : "");
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public long getExpires_time() {
        return expires_time;
    }

    public void setExpires_time(long expires_time) {
        this.expires_time = expires_time;
    }

    public UserDomain getInfo() {
        return info;
    }

    public void setInfo(UserDomain info) {
        this.info = info;
    }

    public boolean isBlack_magic() {
        return black_magic;
    }

    public void setBlack_magic(boolean black_magic) {
        this.black_magic = black_magic;
    }

    public int getNavigationPosition() {
        return navigationPosition;
    }

    public void setNavigationPosition(int navigationPosition) {
        this.navigationPosition = navigationPosition;
    }

    @Override
    public String toString() {
        return DomainTextUtil.toString(this);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(access_token);
        dest.writeLong(expires_time);
        dest.writeInt(navigationPosition);
        dest.writeBooleanArray(new boolean[]{this.black_magic});
        dest.writeParcelable(info, flags);
    }

    public static final Creator<UserBingDomain> CREATOR =
            new Creator<UserBingDomain>() {
                public UserBingDomain createFromParcel(Parcel in) {
                    UserBingDomain accountBean = new UserBingDomain();
                    accountBean.access_token = in.readString();
                    accountBean.expires_time = in.readLong();
                    accountBean.navigationPosition = in.readInt();

                    boolean[] booleans = new boolean[1];
                    in.readBooleanArray(booleans);
                    accountBean.black_magic = booleans[0];

                    accountBean.info = in.readParcelable(UserDomain.class.getClassLoader());

                    return accountBean;
                }

                public UserBingDomain[] newArray(int size) {
                    return new UserBingDomain[size];
                }
            };

    @Override
    public boolean equals(Object o) {

        return o instanceof UserBingDomain
                && !TextUtils.isEmpty(((UserBingDomain) o).getUid())
                && ((UserBingDomain) o).getUid().equalsIgnoreCase(getUid());
    }

    @Override
    public int hashCode() {
        return info.hashCode();
    }
}
