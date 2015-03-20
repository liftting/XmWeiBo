package wm.xmwei.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.Html;
import android.text.SpannableString;
import android.text.TextUtils;

import wm.xmwei.bean.base.DataItemDomain;

/**
 *
 */
public class DataCommentDomain extends DataItemDomain implements Parcelable {

    private String created_at;
    private long id;
    private String idstr;
    private String text;
    private String source;
    private String mid;
    private long mills;

    private UserDomain user;
    private DataCommentDomain reply_comment;

    private String sourceString;

    private boolean isMiddleUnreadItem = false;
    private transient SpannableString listViewSpannableString;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(created_at);
        dest.writeLong(id);

        dest.writeString(idstr);
        dest.writeString(text);
        dest.writeString(source);
        dest.writeString(mid);
        dest.writeLong(mills);

        dest.writeParcelable(user, flags);
//        dest.writeParcelable(status, flags);
        dest.writeParcelable(reply_comment, flags);

        dest.writeString(sourceString);

        dest.writeBooleanArray(new boolean[]{this.isMiddleUnreadItem});
    }

    public static final Parcelable.Creator<DataCommentDomain> CREATOR =
            new Parcelable.Creator<DataCommentDomain>() {
                public DataCommentDomain createFromParcel(Parcel in) {
                    DataCommentDomain commentBean = new DataCommentDomain();
                    commentBean.created_at = in.readString();
                    commentBean.id = in.readLong();
                    commentBean.idstr = in.readString();
                    commentBean.text = in.readString();
                    commentBean.source = in.readString();
                    commentBean.mid = in.readString();

                    commentBean.mills = in.readLong();

                    commentBean.user = in.readParcelable(UserDomain.class.getClassLoader());
//                    commentBean.status = in.readParcelable(MessageBean.class.getClassLoader());
                    commentBean.reply_comment = in.readParcelable(DataCommentDomain.class.getClassLoader());

                    commentBean.sourceString = in.readString();

                    boolean[] booleans = new boolean[1];
                    in.readBooleanArray(booleans);
                    commentBean.isMiddleUnreadItem = booleans[0];

                    return commentBean;
                }

                public DataCommentDomain[] newArray(int size) {
                    return new DataCommentDomain[size];
                }
            };

    public DataCommentDomain getReply_comment() {
        return reply_comment;
    }

    public void setReply_comment(DataCommentDomain reply_comment) {
        this.reply_comment = reply_comment;
    }

    //comment timeline show comment
    public SpannableString getListViewSpannableString() {
        if (!TextUtils.isEmpty(listViewSpannableString)) {
            return listViewSpannableString;
        } else {
//            TimeLineUtility.addJustHighLightLinks(this);
            return listViewSpannableString;
        }
    }

    public void setListViewSpannableString(SpannableString listViewSpannableString) {
        this.listViewSpannableString = listViewSpannableString;
    }

    public long getMills() {
//        if (mills == 0L) {
//            TimeUtility.dealMills(this);
//        }
        return mills;
    }

    public void setMills(long mills) {
        this.mills = mills;
    }

    public String getCreated_at() {

        return created_at;
    }

    public String getListviewItemShowTime() {
//        return TimeUtility.getListTime(this);
        return "";
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getId() {
        return idstr;
    }

    public void setId(String id) {
        this.idstr = id;
    }

    public long getIdLong() {
        return this.id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSourceString() {
        if (!TextUtils.isEmpty(sourceString)) {
            return sourceString;
        } else {
            if (!TextUtils.isEmpty(source)) {
                sourceString = Html.fromHtml(this.source).toString();
            }
            return sourceString;
        }
    }

    public void setSourceString(String sourceString) {
        this.sourceString = sourceString;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public UserDomain getUser() {
        return user;
    }

    @Override
    public boolean isMiddleUnreadItem() {
        return this.isMiddleUnreadItem;
    }

    public void setUser(UserDomain user) {
        this.user = user;
    }

//    public MessageBean getStatus() {
//        return status;
//    }
//
//    public void setStatus(MessageBean status) {
//        this.status = status;
//    }

    public void setMiddleUnreadItem(boolean isMiddleUnreadItem) {
        this.isMiddleUnreadItem = isMiddleUnreadItem;
    }

    @Override
    public String toString() {
        return super.toString();
//        return ObjectToStringUtility.toString(this);
    }

}
