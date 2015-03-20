package wm.xmwei.ui.adapter.data;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.List;

import wm.xmwei.bean.DataCommentDomain;
import wm.xmwei.bean.UserDomain;
import wm.xmwei.core.image.universalimageloader.XmImageLoader;
import wm.xmwei.ui.view.lib.asyncpicture.IXmDrawable;

/**
 *
 *
 */
public class XmCommentsDataAdapter extends XmBaseDataAdapter<DataCommentDomain> {

    public XmCommentsDataAdapter(Context context, List<DataCommentDomain> datas) {
        super(context, datas);
    }

    @Override
    protected void createViewData(ViewHolder holder, int position) {

        final DataCommentDomain comment = getDataList().get(position);

        UserDomain user = comment.getUser();
        if (user != null) {
            holder.username.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(user.getRemark())) {
                holder.username.setText(new StringBuilder(user.getScreen_name()).append("(")
                        .append(user.getRemark()).append(")").toString());
            } else {
                holder.username.setText(user.getScreen_name());
            }
            buildAvatar(holder.avatar, position, user);
        } else {
            holder.username.setVisibility(View.INVISIBLE);
            holder.avatar.setVisibility(View.INVISIBLE);
        }

        holder.content.setText(comment.getListViewSpannableString());

        holder.time.setTime(comment.getMills());

        holder.repost_content.setVisibility(View.GONE);
        holder.repost_content_pic.setVisibility(View.GONE);

        DataCommentDomain reply = comment.getReply_comment();
        if (holder.replyIV != null) {
            holder.replyIV.setVisibility(View.GONE);
        }
        if (reply != null) {
            holder.repost_flag.setVisibility(View.VISIBLE);
            holder.repost_content.setVisibility(View.VISIBLE);
            holder.repost_content.setText(reply.getListViewSpannableString());
            holder.repost_content.setTag(reply.getId());
        }
    }


    protected void buildAvatar(IXmDrawable view, int position, final UserDomain user) {
        view.setVisibility(View.VISIBLE);
        view.checkVerified(user);
        buildAvatar(view.getImageView(), position, user);
    }

    protected void buildAvatar(ImageView view, int position, final UserDomain user) {
        String image_url = user.getProfile_image_url();
        if (!TextUtils.isEmpty(image_url)) {
            view.setVisibility(View.VISIBLE);
            XmImageLoader.getInstance().loadImage(image_url, view);
        } else {
            view.setVisibility(View.GONE);
        }
    }
}
