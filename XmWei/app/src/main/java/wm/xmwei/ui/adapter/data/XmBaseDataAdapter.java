package wm.xmwei.ui.adapter.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import wm.xmwei.R;
import wm.xmwei.bean.DataItemDomain;

/**
 *
 *
 *
 */
public class XmBaseDataAdapter<T extends DataItemDomain> extends BaseAdapter {

    protected List<T> mDataList;
    private Context mContext;

    public XmBaseDataAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View containView = LayoutInflater.from(mContext).inflate(R.layout.layer_timeline_listview_item_simple, parent, false);
        return containView;
    }
}
