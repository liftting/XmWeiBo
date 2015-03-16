package wm.xmwei.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import wm.xmwei.R;
import wm.xmwei.ui.fragment.base.BaseFragment;

/**
 *
 *
 */
public class XmMenuFragment extends BaseFragment {


    public static XmMenuFragment newInstance() {
        XmMenuFragment fragment = new XmMenuFragment();
        fragment.setArguments(new Bundle());
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View containView = inflater.inflate(R.layout.layer_left_menu_contents, null);
        return containView;
    }
}
