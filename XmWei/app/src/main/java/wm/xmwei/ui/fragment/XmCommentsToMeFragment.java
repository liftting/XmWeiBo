package wm.xmwei.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import wm.xmwei.R;
import wm.xmwei.bean.DataListDomain;
import wm.xmwei.ui.fragment.base.XmBaseListFragment;

/**
 * Created by wm on 15-3-17.
 */
public class XmCommentsToMeFragment extends XmBaseListFragment {


    public static XmCommentsToMeFragment newInstance(Bundle bundle) {
        XmCommentsToMeFragment fragment = new XmCommentsToMeFragment();
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
        View mainView = inflater.inflate(R.layout.layer_comments_to_me, null);
        return mainView;
    }

    @Override
    protected void createDataListAdapter() {

    }

    @Override
    public DataListDomain getDataList() {
        return null;
    }

    @Override
    protected void onItemClick(AdapterView parent, View view, int position, long id) {

    }


}
