package wm.xmwei.core.lib.support.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.ProgressBar;

import wm.xmwei.bean.UserDomain;
import wm.xmwei.ui.view.lib.asyncpicture.IXmDrawable;

/**
 *
 */
public class MultiPicturesChildImageView extends ImageView implements IXmDrawable {

    private Paint paint = new Paint();
    private boolean pressed = false;
    private boolean showGif = false;
    private Bitmap gif;

    public MultiPicturesChildImageView(Context context) {
        this(context, null);
    }

    public MultiPicturesChildImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultiPicturesChildImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                pressed = true;
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                pressed = false;
                invalidate();
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public ImageView getImageView() {
        return this;
    }

    @Override
    public void setProgress(int value, int max) {

    }

    @Override
    public ProgressBar getProgressBar() {
        return null;
    }

    @Override
    public void setGifFlag(boolean value) {
        if (showGif != value) {
            showGif = value;
            invalidate();
        }
    }

    @Override
    public void checkVerified(UserDomain user) {

    }

    @Override
    public void setPressesStateVisibility(boolean value) {

    }

}
