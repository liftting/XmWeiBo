package wm.xmwei.util;

import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.io.Closeable;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Set;

import wm.xmwei.XmApplication;
import wm.xmwei.core.lib.support.XmAsyncTask;

/**
 * Created by wm on 15-3-16.
 */
public class XmUtils {

    public static int dip2px(int dipValue) {
        float reSize = XmApplication.getInstance().getResources().getDisplayMetrics().density;
        return (int) ((dipValue * reSize) + 0.5);
    }

    public static int px2dip(int pxValue) {
        float reSize = XmApplication.getInstance().getResources().getDisplayMetrics().density;
        return (int) ((pxValue / reSize) + 0.5);
    }

    public static float sp2px(int spValue) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue,
                XmApplication.getInstance().getResources().getDisplayMetrics());
    }

    /**
     * https://svn.apache.org/repos/asf/cayenne/main/branches/cayenne-jdk1.5-generics-unpublished/src/main/java/org/apache/cayenne/conf/Rot47PasswordEncoder.java
     */
    public static String rot47(String value) {
        int length = value.length();
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < length; i++) {
            char c = value.charAt(i);

            // Process letters, numbers, and symbols -- ignore spaces.
            if (c != ' ') {
                // Add 47 (it is ROT-47, after all).
                c += 47;

                // If character is now above printable range, make it printable.
                // Range of printable characters is ! (33) to ~ (126).  A value
                // of 127 (just above ~) would therefore get rotated down to a
                // 33 (the !).  The value 94 comes from 127 - 33 = 94, which is
                // therefore the value that needs to be subtracted from the
                // non-printable character to put it into the correct printable
                // range.
                if (c > '~') {
                    c -= 94;
                }
            }

            result.append(c);
        }

        return result.toString();
    }

    public static String encodeUrl(Map<String, String> param) {
        if (param == null) {
            return "";
        }

        StringBuilder sb = new StringBuilder();

        Set<String> keys = param.keySet();
        boolean first = true;

        for (String key : keys) {
            String value = param.get(key);
            //pain...EditMyProfileDao params' values can be empty
            if (!TextUtils.isEmpty(value) || key.equals("description") || key.equals("url")) {
                if (first) {
                    first = false;
                } else {
                    sb.append("&");
                }
                try {
                    sb.append(URLEncoder.encode(key, "UTF-8")).append("=")
                            .append(URLEncoder.encode(param.get(key), "UTF-8"));
                } catch (UnsupportedEncodingException e) {

                }
            }
        }

        return sb.toString();
    }

    public static void cancelTasks(XmAsyncTask... tasks) {
        for (XmAsyncTask task : tasks) {
            if (task != null) {
                task.cancel(true);
            }
        }
    }

    /**
     * 流关闭，统一处理
     *
     * @param closeable 流实现了接口
     */
    public static void closeSilently(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException ignored) {

            }
        }
    }

    /**
     * Parse a URL query and fragment parameters into a key-value bundle.
     */
    public static Bundle parseUrl(String url) {
        // hack to prevent MalformedURLException
        url = url.replace("weiboconnect", "http");
        try {
            URL u = new URL(url);
            Bundle b = decodeUrl(u.getQuery());
            b.putAll(decodeUrl(u.getRef()));
            return b;
        } catch (MalformedURLException e) {
            return new Bundle();
        }
    }

    public static Bundle decodeUrl(String s) {
        Bundle params = new Bundle();
        if (s != null) {
            String array[] = s.split("&");
            for (String parameter : array) {
                String v[] = parameter.split("=");
                try {
                    params.putString(URLDecoder.decode(v[0], "UTF-8"),
                            URLDecoder.decode(v[1], "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        return params;
    }

    // 判断设备方向
    public static boolean isDevicePort() {
        return XmApplication.getInstance().getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_PORTRAIT;
    }


    // view utils

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    /**
     * 获取Listview的高度，然后设置ViewPager的高度
     *
     * @param listView
     * @return
     */
    public static int setListViewHeightBasedOnChildren1(ListView listView) {
        //获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return 0;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) { //listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0); //计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); //统计所有子项的总高度
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        //listView.getDividerHeight()获取子项间分隔符占用的高度
        //params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
        return params.height;
    }


}
