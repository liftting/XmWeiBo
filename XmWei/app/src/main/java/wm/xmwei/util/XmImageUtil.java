package wm.xmwei.util;

import android.media.ExifInterface;
import android.text.TextUtils;
import android.widget.ImageView;

import java.io.IOException;

import wm.xmwei.bean.UserDomain;
import wm.xmwei.core.image.universalimageloader.XmImageLoader;

import static android.media.ExifInterface.ORIENTATION_NORMAL;
import static android.media.ExifInterface.ORIENTATION_ROTATE_180;
import static android.media.ExifInterface.ORIENTATION_ROTATE_270;
import static android.media.ExifInterface.ORIENTATION_ROTATE_90;
import static android.media.ExifInterface.TAG_ORIENTATION;

/**
 * Created by wm on 15-4-14.
 */
public class XmImageUtil {


    public static boolean isGifPicture(String url) {
        return !TextUtils.isEmpty(url) && url.endsWith(".gif");
    }

    public static void loadAvatarImage(ImageView imageView, UserDomain userDomain) {
        // 用户图标
        String userUrl = null;
        if (XmSettingUtil.getEnableBigAvatar()) {
            userUrl = userDomain.getAvatar_large();
        } else {
            userUrl = userDomain.getProfile_image_url();
        }
        XmImageLoader.getInstance().loadImage(userUrl, imageView);

    }

}
