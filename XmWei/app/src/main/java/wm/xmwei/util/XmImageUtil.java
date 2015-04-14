package wm.xmwei.util;

import android.media.ExifInterface;
import android.text.TextUtils;

import java.io.IOException;

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

}
