package wm.xmwei.ui.adapter.data;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import wm.xmwei.R;
import wm.xmwei.bean.DataCommentDomain;
import wm.xmwei.bean.UserDomain;
import wm.xmwei.core.image.universalimageloader.XmImageLoader;
import wm.xmwei.ui.fragment.messagescan.XmMessageCommentFragment;
import wm.xmwei.ui.view.lib.TimeLineAvatarImageView;
import wm.xmwei.ui.view.lib.TimeTextView;
import wm.xmwei.util.XmImageUtil;
import wm.xmwei.util.XmSettingUtil;

/**
 * Created by wm on 15-4-15.
 */
public class XmMessageScanCommentDataAdapter extends BaseAdapter {

    private List<DataCommentDomain> mDataComments;
    private Context mContext;

    public XmMessageScanCommentDataAdapter(Context context, List<DataCommentDomain> datas) {
        mContext = context;
        mDataComments = datas;
    }

    @Override
    public int getCount() {
        return mDataComments.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataComments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return -1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.layer_message_scan_comment_list_item, parent, false);

            viewHolder = buildHolder(convertView);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        bindCommentData(viewHolder, position);

        return convertView;
    }

    private static class ViewHolder {
        TextView username;
        TextView content;
        TimeTextView time;
        TimeLineAvatarImageView avatar;
        ImageView reply;
    }

    private ViewHolder buildHolder(View convertView) {
        ViewHolder holder = new ViewHolder();
        holder.username = (TextView) convertView.findViewById(R.id.username);
        TextPaint tp = holder.username.getPaint();
        tp.setFakeBoldText(true);
        holder.content = (TextView) convertView.findViewById(R.id.content);
        holder.time = (TimeTextView) convertView.findViewById(R.id.time);
        holder.avatar = (TimeLineAvatarImageView) convertView.findViewById(R.id.avatar);
        holder.reply = (ImageView) convertView.findViewById(R.id.replyIV);
        return holder;
    }

    private void bindCommentData(ViewHolder holder, int position) {

        final DataCommentDomain comment = (DataCommentDomain) getItem(position);

        UserDomain user = comment.getUser();
        if (user != null) {
            holder.username.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(user.getRemark())) {
                holder.username.setText(new StringBuilder(user.getScreen_name()).append("(")
                        .append(user.getRemark()).append(")").toString());
            } else {
                holder.username.setText(user.getScreen_name());
            }
            if (!XmSettingUtil.getEnableCommentRepostListAvatar()) {
                holder.avatar.setLayoutParams(new RelativeLayout.LayoutParams(0, 0));
            } else {
                buildAvatar(holder.avatar, user);
            }
        } else {
            holder.username.setVisibility(View.INVISIBLE);
            holder.avatar.setVisibility(View.INVISIBLE);
        }

        holder.avatar.checkVerified(user);
        holder.content.setText(comment.getListViewSpannableString());
        holder.time.setTime(comment.getMills());
        holder.reply.setVisibility(View.VISIBLE);
        holder.reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
    }

    private void buildAvatar(TimeLineAvatarImageView avatar, UserDomain user) {
        avatar.setVisibility(View.VISIBLE);
        avatar.checkVerified(user);
        String image_url = user.getProfile_image_url();
        if (!TextUtils.isEmpty(image_url)) {
            avatar.getImageView().setVisibility(View.VISIBLE);
            XmImageUtil.loadAvatarImage(avatar.getImageView(), user);
        } else {
            avatar.getImageView().setVisibility(View.GONE);
        }
    }
}
