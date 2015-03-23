package wm.xmwei.util;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.URLSpan;
import android.text.util.Linkify;

import wm.xmwei.bean.DataMessageDomain;
import wm.xmwei.bean.UserDomain;
import wm.xmwei.core.lib.support.XmURLSpan;

/**
 * Created by wm on 15-3-23.
 */
public class DataUtils {

    public static void addJustHighLightLinks(DataMessageDomain bean) {
        bean.setListViewSpannableString(convertNormalStringToSpannableString(bean.getText()));
        bean.getSourceString();

        if (bean.getRetweeted_status() != null) {
            bean.getRetweeted_status().setListViewSpannableString(
                    buildOriWeiboSpannalString(bean.getRetweeted_status()));
            bean.getRetweeted_status().getSourceString();
        }
    }

    public static SpannableString convertNormalStringToSpannableString(String txt) {
        //hack to fix android imagespan bug,see http://stackoverflow.com/questions/3253148/imagespan-is-cut-off-incorrectly-aligned
        //if string only contains emotion tags,add a empty char to the end
        String hackTxt;
        if (txt.startsWith("[") && txt.endsWith("]")) {
            hackTxt = txt + " ";
        } else {
            hackTxt = txt;
        }
        SpannableString value = SpannableString.valueOf(hackTxt);
//        Linkify.addLinks(value, XmDataPatterns.MENTION_URL, XmDataPatterns.MENTION_SCHEME);
        Linkify.addLinks(value, XmDataPatterns.WEB_URL, XmDataPatterns.WEB_SCHEME);
//        Linkify.addLinks(value, XmDataPatterns.TOPIC_URL, XmDataPatterns.TOPIC_SCHEME);

        URLSpan[] urlSpans = value.getSpans(0, value.length(), URLSpan.class);
        XmURLSpan weiboSpan = null;
        for (URLSpan urlSpan : urlSpans) {
            weiboSpan = new XmURLSpan(urlSpan.getURL());
            int start = value.getSpanStart(urlSpan);
            int end = value.getSpanEnd(urlSpan);
            value.removeSpan(urlSpan);
            value.setSpan(weiboSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

//        DataUtils.addEmotions(value); // add emotion animation
        return value;
    }

    private static SpannableString buildOriWeiboSpannalString(DataMessageDomain oriMsg) {
        String name = "";
        UserDomain oriUser = oriMsg.getUser();
        if (oriUser != null) {
            name = oriUser.getScreen_name();
            if (TextUtils.isEmpty(name)) {
                name = oriUser.getId();
            }
        }

        SpannableString value;

        if (!TextUtils.isEmpty(name)) {
            value = DataUtils
                    .convertNormalStringToSpannableString("@" + name + "：" + oriMsg.getText());
        } else {
            value = DataUtils.convertNormalStringToSpannableString(oriMsg.getText());
        }
        return value;
    }

}
