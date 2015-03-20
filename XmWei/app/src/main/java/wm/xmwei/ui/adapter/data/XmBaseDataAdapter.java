package wm.xmwei.ui.adapter.data;

import android.content.Context;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import wm.xmwei.R;
import wm.xmwei.bean.base.DataItemDomain;
import wm.xmwei.ui.view.lib.TimeLineAvatarImageView;
import wm.xmwei.ui.view.lib.TimeTextView;
import wm.xmwei.ui.view.lib.asyncpicture.IXmDrawable;

/**
 *
 *
 *
 */
public abstract class XmBaseDataAdapter<T extends DataItemDomain> extends BaseAdapter {

    protected List<T> mDataList;
    private Context mContext;

    public XmBaseDataAdapter(Context context, List<T> datas) {
        mContext = context;
        mDataList = datas;
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

    public List<T> getDataList() {
        return mDataList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.layer_timeline_listview_item_simple, parent, false);
            holder = new ViewHolder();
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        buildHolder(holder, convertView);

        createViewData(holder, position);

        return convertView;
    }

    private ViewHolder buildHolder(ViewHolder holder, View convertView) {

        holder.username = (TextView) convertView.findViewById(R.id.username);
        TextPaint tp = holder.username.getPaint();
        if (tp != null) {
            tp.setFakeBoldText(true);
        }
        holder.content = (TextView) convertView.findViewById(R.id.content);
        holder.repost_content = (TextView) convertView.findViewById(R.id.repost_content);
        holder.time = (TimeTextView) convertView.findViewById(R.id.time);
        holder.avatar = (TimeLineAvatarImageView) convertView.findViewById(R.id.avatar);

        holder.repost_content_pic = (IXmDrawable) convertView
                .findViewById(R.id.repost_content_pic);

        holder.content_pic = holder.repost_content_pic;
        holder.content_pic_multi = holder.repost_content_pic_multi;

        holder.listview_root = (ViewGroup) convertView.findViewById(R.id.listview_root);
        holder.repost_flag = convertView.findViewById(R.id.repost_flag);
        holder.replyIV = (ImageView) convertView.findViewById(R.id.replyIV);
        return holder;
    }

    public static class ViewHolder {
        TextView username;
        TextView content;
        TextView repost_content;
        TimeTextView time;
        IXmDrawable avatar;
        IXmDrawable content_pic;
        GridLayout content_pic_multi;
        IXmDrawable repost_content_pic;
        GridLayout repost_content_pic_multi;
        ViewGroup listview_root;
        View repost_flag;
        ImageView replyIV;
    }

    /**
     * 根据指定位置，来填充adapter的数据
     *
     * @param holder
     * @param position
     */
    protected abstract void createViewData(ViewHolder holder, int position);

}
