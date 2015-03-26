package wm.xmwei.ui.fragment.base;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import wm.xmwei.core.lib.support.view.XmBaseTipLayout;

/**
 * this is handle info tip(error or data load tip)
 */
public class XmBaseContainerFragment extends XmBaseFragment {

    protected XmBaseTipLayout baseTipLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void setView(Context context, int layoutId) {
        this.baseTipLayout = new XmBaseTipLayout(context, layoutId, null);
    }


    protected void setView(Context context, View childView) {
        this.baseTipLayout = new XmBaseTipLayout(context, 0, childView);
    }


}
