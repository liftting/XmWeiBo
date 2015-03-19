package wm.xmwei.ui.view.lib.asyncpicture;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import wm.xmwei.bean.UserDomain;

/**
 *
 */
public interface IXmDrawable {

    public void setImageDrawable(Drawable drawable);

    public void setImageBitmap(Bitmap bm);

    public ImageView getImageView();

    public void setProgress(int value, int max);

    public ProgressBar getProgressBar();

    public void setGifFlag(boolean value);

    public void checkVerified(UserDomain user);

    public void setPressesStateVisibility(boolean value);

    public void setVisibility(int visibility);

    public int getVisibility();

    public void setOnClickListener(View.OnClickListener onClickListener);

    public void setOnLongClickListener(View.OnLongClickListener onLongClickListener);

    public void setLayoutParams(ViewGroup.LayoutParams layoutParams);
}
