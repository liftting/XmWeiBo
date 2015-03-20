package wm.xmwei.ui.fragment.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import wm.xmwei.R;
import wm.xmwei.bean.base.DataListDomain;

/**
 * this is home item fragment
 */
public class XmHomeBaseFragment extends XmBaseListFragment {

    private int mDataType = -1;
    private TextView mTvInfo;

    public static XmHomeBaseFragment newInstance(Bundle bundle) {
        XmHomeBaseFragment fragment = new XmHomeBaseFragment();
        if (bundle != null) {
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();

        mDataType = bundle.getInt("home_fragment_key");

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View containerView = inflater.inflate(R.layout.layer_frag_home_item, null);
        initView(containerView);
        initData();
        return containerView;
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

    private void initView(View containView) {
        mTvInfo = (TextView) containView.findViewById(R.id.tv_home_info);
    }

    private void initData() {
        String text = "this is not found";
        switch (mDataType) {
            case 0:
                text = "this is the one";
                break;
            case 1:
                text = "this is the two";
                break;
            case 2:
                text = "this is the three";
                break;
            case 3:
                text = "this is the four";
                break;
            default:
                text = "this is the default";
        }

        mTvInfo.setText(text);
    }


}
