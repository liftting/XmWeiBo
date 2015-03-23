package wm.xmwei.ui.fragment.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import wm.xmwei.R;
import wm.xmwei.bean.DataGroupDomain;
import wm.xmwei.bean.base.DataListDomain;

/**
 * this is home item fragment
 */
public class XmHomeBaseFragment extends XmBaseListFragment {

    private DataGroupDomain mDataType;
    private TextView mTvInfo;

    public static final String HOME_FRAGMENT_GROUP_KEY = "home_group_key";

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

        mDataType = bundle.getParcelable(HOME_FRAGMENT_GROUP_KEY);

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

    @Override
    protected void onNewDataLoaderSuccessCallback(DataListDomain newValue, Bundle loaderArgs) {

    }

    private void initView(View containView) {
        mTvInfo = (TextView) containView.findViewById(R.id.tv_home_info);
    }

    private void initData() {
        String text = "this is not found";
        mTvInfo.setText(text);
    }


}
