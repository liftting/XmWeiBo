package wm.xmwei.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import wm.xmwei.R;
import wm.xmwei.ui.fragment.base.XmBaseListFragment;

/**
 * Created by wm on 15-3-17.
 */
public class XmCommentsByMeFragment extends XmBaseListFragment {

    public static XmCommentsByMeFragment newInstance(Bundle bundle) {
        XmCommentsByMeFragment fragment = new XmCommentsByMeFragment();
        if (bundle != null) {
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.layer_comments_by_me, null);
        return mainView;
    }
}
