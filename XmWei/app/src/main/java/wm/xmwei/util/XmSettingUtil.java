package wm.xmwei.util;

import com.sina.weibo.sdk.utils.Utility;

/**
 */
public class XmSettingUtil {


    public static String getMsgCount() {
//        String value = SettingHelper
//                .getSharedPreferences(getContext(), SettingActivity.MSG_COUNT, "3");
//
//        switch (Integer.valueOf(value)) {
//            case 1:
//                return String.valueOf(XmDataAppConfig.DEFAULT_MSG_COUNT_25);
//
//            case 2:
//                return String.valueOf(XmDataAppConfig.DEFAULT_MSG_COUNT_50);
//
//            case 3:
//                if (Utility.isConnected(getContext())) {
//                    if (Utility.isWifi(getContext())) {
//                        return String.valueOf(XmDataAppConfig.DEFAULT_MSG_COUNT_50);
//                    } else {
//                        return String.valueOf(XmDataAppConfig.DEFAULT_MSG_COUNT_25);
//                    }
//                }
//        }
        return String.valueOf(XmDataAppConfig.DEFAULT_MSG_COUNT_25);
    }


    public static boolean isEnablePic() {
        return true;
    }

    public static boolean getEnableBigPic() {
        return false;
    }



    // is allow to click the image close the gallery
    public static boolean allowClickToCloseGallery() {
        return true;
    }

    public static boolean getEnableBigAvatar() {
        return true;
    }
}
