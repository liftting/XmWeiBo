package wm.xmwei.core.lib.support.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import wm.xmwei.R;

/**
 * Created by wm on 15-3-25.
 */
public class XmIndicatorItemView extends RelativeLayout {

    private TextView mTvInfo;
    private RelativeLayout mRlyContainer;

    public XmIndicatorItemView(Context context) {
        this(context, null, 0);
    }

    public XmIndicatorItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XmIndicatorItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        LayoutInflater.from(context).inflate(R.layout.layer_indicator_inner_view, this);

        mTvInfo = (TextView) this.findViewById(R.id.tv_indicator_title);
        mRlyContainer = (RelativeLayout) this.findViewById(R.id.rly_container);
    }

    public TextView getTextView() {
        return mTvInfo;
    }

    public void setTextColor(int color) {
        mTvInfo.setTextColor(color);
    }

    public void setToLeft() {
        RelativeLayout.LayoutParams params = (LayoutParams) mRlyContainer.getLayoutParams();
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
    }

    public void setToRight() {
        RelativeLayout.LayoutParams params = (LayoutParams) mRlyContainer.getLayoutParams();
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
    }

    public void setToCenter() {
        RelativeLayout.LayoutParams params = (LayoutParams) mRlyContainer.getLayoutParams();
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
    }

    public int getTextWidth() {
        return mRlyContainer.getMeasuredWidth();
    }


}
