package wm.xmwei.core.selftest;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import wm.xmwei.R;
import wm.xmwei.core.lib.support.eventbus.EventBus;
import wm.xmwei.ui.view.draptopout.AttachUtil;

/**
 * Created by wm on 15-4-8.
 */
public class TabFragment extends Fragment {

    public static final String TITLE = "title";
    private String mTitle = "Defaut Value";
    private TextView mTextView;
    private ScrollView mScrollView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTitle = getArguments().getString(TITLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layer_fragment_tab_test, container, false);
        mTextView = (TextView) view.findViewById(R.id.id_info);
        mTextView.setText(mTitle);

        mScrollView = (ScrollView) view.findViewById(R.id.id_stickynavlayout_innerscrollview);

        mScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                EventBus.getDefault().post(AttachUtil.isScrollViewAttach(mScrollView));
                return false;
            }
        });

        return view;

    }

    public static TabFragment newInstance(String title) {
        TabFragment tabFragment = new TabFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TITLE, title);
        tabFragment.setArguments(bundle);
        return tabFragment;
    }

}
