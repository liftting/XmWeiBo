package wm.xmwei.core.lib.support.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import wm.xmwei.R;
import wm.xmwei.core.lib.support.error.XmWeiboException;

/**
 *
 *
 */
public class XmBaseTipLayout extends RelativeLayout {

    private Context mContext;
    private TextView mTvTip;
    private RelativeLayout mRlyTipView;

    public XmBaseTipLayout(Context context, int contextLayoutId, View contextView) {
        super(context);

        mContext = context;
        initView(context, contextLayoutId, contextView);
    }

    private void initView(Context context, int contextLayoutId, View contextView) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        // this is tipView
        View tipView = inflater.inflate(R.layout.layer_base_tip, null);

        View childView = contextView == null ? inflater.inflate(contextLayoutId, null) : contextView;
        this.addView(childView, params);

        LayoutParams tipParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.addView(tipView, tipParams);


        mTvTip = (TextView) findViewById(R.id.tv_tip_info_view);
        mRlyTipView = (RelativeLayout) findViewById(R.id.rly_tip_container);


    }

    public XmBaseTipLayout setTipInfo(String info) {
        mTvTip.setText(info);
        return this;
    }

    public void showTip() {
        mRlyTipView.clearAnimation();
        TranslateAnimation showAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f);
        showAnimation.setDuration(400);
        mRlyTipView.setAnimation(showAnimation);
        mRlyTipView.setVisibility(View.VISIBLE);

        mRlyTipView.postDelayed(new Runnable() {
            @Override
            public void run() {
                hideTip();
            }
        }, 2000);
    }

    public void hideTip() {
        mRlyTipView.clearAnimation();

        TranslateAnimation hideAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, -1.0f,
                Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f);
        hideAnimation.setDuration(400);

        mRlyTipView.setAnimation(hideAnimation);
        mRlyTipView.setVisibility(View.GONE);
    }

    public void loadDataFail() {
        setTipInfo("数据加载失败").showTip();
    }

    public void loadDataSuccess(int dataCount) {
        if (dataCount > 0) {
            setTipInfo("成功加载" + dataCount + "条动态信息").showTip();
        } else {
            setTipInfo("没有新的动态了").showTip();
        }
    }

    public void loadError(XmWeiboException e) {
        setTipInfo("出错了~").showTip();
    }

}
