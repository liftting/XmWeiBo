package wm.xmwei.bean.base;

import android.text.SpannableString;

import wm.xmwei.bean.UserDomain;

/**
 * 这是每一个数据项提供统一方法
 */
public abstract class DataItemDomain {

    public abstract SpannableString getListViewSpannableString();

    public abstract String getListviewItemShowTime();

    public abstract String getText();

    public abstract String getCreated_at();

    public abstract void setMills(long mills);

    public abstract long getMills();

    public abstract String getId();

    public abstract long getIdLong();

    public abstract UserDomain getUser();

    public abstract boolean isMiddleUnreadItem();

    @Override
    public boolean equals(Object o) {
        if (o instanceof DataItemDomain && ((DataItemDomain) o).getId().equals(getId())) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

}
