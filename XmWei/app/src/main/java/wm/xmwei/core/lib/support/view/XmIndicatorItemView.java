package wm.xmwei.core.lib.support.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import wm.xmwei.R;

/**
 * Created by wm on 15-3-25.
 */
public class XmIndicatorItemView extends RelativeLayout {

    private TextView mTvInfo;

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
    }

    public TextView getTextView(){
        return mTvInfo;
    }

    public void setTextColor(int color){
        mTvInfo.setTextColor(color);
    }
}
