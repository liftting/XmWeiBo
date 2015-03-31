package wm.xmwei.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by wm on 15-3-30.
 */
public class XmAnimationUtil {

    public static Rect getBitmapRectFromImageView(ImageView imageView) {
        Drawable drawable = imageView.getDrawable();
        Bitmap bitmap = null;
        if (drawable instanceof BitmapDrawable) {
            bitmap = ((BitmapDrawable) drawable).getBitmap();
        }

        Rect rect = new Rect();
        boolean isVisible = imageView.getGlobalVisibleRect(rect);
        if (!isVisible) {
            int[] location = new int[2];
            imageView.getLocationOnScreen(location);

            rect.left = location[0];
            rect.top = location[1];
            rect.right = rect.left + imageView.getWidth();
            rect.bottom = rect.top + imageView.getHeight();
        }

        if (bitmap != null) {

            int bitmapWidth = bitmap.getWidth();
            int bitmapHeight = bitmap.getHeight();

            int imageViewWidth = imageView.getWidth() - imageView.getPaddingLeft() - imageView
                    .getPaddingRight();
            int imageviewHeight = imageView.getHeight() - imageView.getPaddingTop() - imageView
                    .getPaddingBottom();

            float startScale;
            if ((float) imageViewWidth / bitmapWidth
                    > (float) imageviewHeight / bitmapHeight) {
                // Extend start bounds horizontally
                startScale = (float) imageviewHeight / bitmapHeight;
            } else {
                startScale = (float) imageViewWidth / bitmapWidth;
            }

            bitmapHeight = (int) (bitmapHeight * startScale);
            bitmapWidth = (int) (bitmapWidth * startScale);

            int deltaX = (imageViewWidth - bitmapWidth) / 2;
            int deltaY = (imageviewHeight - bitmapHeight) / 2;

            rect.set(rect.left + deltaX, rect.top + deltaY, rect.right - deltaX,
                    rect.bottom - deltaY);

            return rect;
        } else {
            return null;
        }
    }

    public static View getAppContentView(Activity activity) {
        final View appView = activity.findViewById(android.R.id.content);
        return appView;
    }

}
