package wm.xmwei.core.lib.support;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcel;
import android.provider.Browser;
import android.support.v4.app.FragmentActivity;
import android.text.ParcelableSpan;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.view.View;

import wm.xmwei.R;
import wm.xmwei.util.XmThemeUtils;
import wm.xmwei.util.XmTimeUtils;

/**
 */
public class XmURLSpan extends ClickableSpan implements ParcelableSpan {

    private final String mURL;

    public XmURLSpan(String url) {
        mURL = url;
    }

    public XmURLSpan(Parcel src) {
        mURL = src.readString();
    }

    public int getSpanTypeId() {
        return 11;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mURL);
    }

    public String getURL() {
        return mURL;
    }

    public void onClick(View widget) {

    }

    public void onLongClick(View widget) {

    }

    @Override
    public void updateDrawState(TextPaint tp) {
        tp.setColor(XmThemeUtils.getColor(R.attr.link_color));
//        tp.setUnderlineText(true);
    }
}
