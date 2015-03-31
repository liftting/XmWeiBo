package wm.xmwei.ui.adapter.data;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import wm.xmwei.R;
import wm.xmwei.XmApplication;
import wm.xmwei.bean.DataMessageDomain;
import wm.xmwei.bean.UserDomain;
import wm.xmwei.bean.base.DataItemDomain;
import wm.xmwei.core.image.universalimageloader.XmImageLoader;
import wm.xmwei.core.image.universalimageloader.core.ImageLoader;
import wm.xmwei.ui.activity.galleryview.XmPhotoViewScanActivity;
import wm.xmwei.ui.view.lib.TimeLineAvatarImageView;
import wm.xmwei.ui.view.lib.TimeTextView;
import wm.xmwei.ui.view.lib.XmPhotoViewData;
import wm.xmwei.ui.view.lib.asyncpicture.IXmDrawable;
import wm.xmwei.util.XmSettingUtil;

/**
 *
 *
 *
 */
public abstract class XmBaseDataAdapter<T extends DataItemDomain> extends BaseAdapter {

    protected List<T> mDataList;
    private Activity mContext;
    private LayoutInflater mInflater;

    // view type
    private final int TYPE_NORMAL = 0;
    private final int TYPE_NORMAL_BIG_PIC = 1;
    private final int TYPE_MIDDLE = 2;
    private final int TYPE_SIMPLE = 3;
    private static int VIEW_TYPE_COUNT = 4;

    public XmBaseDataAdapter(Activity activity, List<T> datas) {
        mContext = activity;
        mDataList = datas;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public T getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return -1;
    }

    public List<T> getDataList() {
        return mDataList;
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        int itemViewType = getItemViewType(position);
        if (convertView == null) {


//            View view = null;
//            switch (itemViewType) {
//                case TYPE_SIMPLE:
//                    view = buildSimpleView();
//                    break;
//                case TYPE_NORMAL:
//
//                    if (view == null) {
//                        view = buildNormalView();
//                    }
//                    break;
//                default:
//                    view = buildNormalView();
//                    break;
//            }
            convertView = buildNormalView();
            holder = new ViewHolder();
            convertView.setTag(holder);
            buildHolder(holder, convertView);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        createViewData(holder, position);

        return convertView;
    }

    private View buildSimpleView() {
        return mInflater.inflate(R.layout.layer_timeline_listview_item_simple, null);
    }

    private View buildNormalView() {
        return mInflater.inflate(R.layout.layer_timeline_listview_item_normal, null);
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
        holder.repost_content_pic_multi = (GridLayout) convertView
                .findViewById(R.id.repost_content__pic_multi);

        holder.content_pic = holder.repost_content_pic;
        holder.content_pic_multi = holder.repost_content_pic_multi;

        holder.listview_root = (ViewGroup) convertView.findViewById(R.id.listview_root);
//        holder.repost_layout = convertView.findViewById(R.id.repost_layout);
        holder.repost_flag = convertView.findViewById(R.id.repost_flag);
        holder.count_layout = (LinearLayout) convertView.findViewById(R.id.count_layout);
        holder.repost_count = (TextView) convertView.findViewById(R.id.repost_count);
        holder.comment_count = (TextView) convertView.findViewById(R.id.comment_count);
        holder.timeline_gps = (ImageView) convertView.findViewById(R.id.timeline_gps_iv);
        holder.timeline_pic = (ImageView) convertView.findViewById(R.id.timeline_pic_iv);
        holder.replyIV = (ImageView) convertView.findViewById(R.id.replyIV);
        holder.source = (TextView) convertView.findViewById(R.id.source);
        return holder;
    }

    protected void buildAvatar(IXmDrawable view, int position, final UserDomain user) {
        view.setVisibility(View.VISIBLE);
        view.checkVerified(user);
        String image_url = user.getProfile_image_url();
        if (!TextUtils.isEmpty(image_url)) {
            view.setVisibility(View.VISIBLE);
            XmImageLoader.getInstance().loadImage(image_url, view.getImageView());
        } else {
            view.setVisibility(View.GONE);
        }
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
        View repost_layout;
        View repost_flag;
        LinearLayout count_layout;
        TextView repost_count;
        TextView comment_count;
        TextView source;
        ImageView timeline_gps;
        ImageView timeline_pic;
        ImageView replyIV;
    }


    protected void buildMultiPic(final DataMessageDomain msg, final GridLayout gridLayout) {
        if (XmSettingUtil.isEnablePic()) {
            gridLayout.setVisibility(View.VISIBLE);

            final int count = msg.getPicCount();
            for (int i = 0; i < count; i++) {
                final IXmDrawable pic = (IXmDrawable) gridLayout.getChildAt(i);
                pic.setVisibility(View.VISIBLE);


                final int selectPos = i;
                pic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // add onclick

                        // 这里在构造时，因为有些图片没有显示完整，所以点击后这里的
                        ArrayList<XmPhotoViewData> animationRectArrayList
                                = new ArrayList<XmPhotoViewData>();
                        for (int i = 0; i < count; i++) {
                            final IXmDrawable pic = (IXmDrawable) gridLayout
                                    .getChildAt(i);
                            ImageView imageView = (ImageView) pic;
                            if (imageView.getVisibility() == View.VISIBLE) {
                                XmPhotoViewData rect = XmPhotoViewData.buildFromImageView(imageView);
                                animationRectArrayList.add(rect);
                            }
                        }

                        Intent intent = XmPhotoViewScanActivity
                                .newIntent(msg, animationRectArrayList, selectPos);
                        getActivity().startActivity(intent);

                    }
                });

                if (XmSettingUtil.getEnableBigPic()) {
                    XmImageLoader.getInstance().loadImage(msg.getHighPicUrls().get(i), pic.getImageView());
                } else {
                    XmImageLoader.getInstance().loadImage(msg.getThumbnailPicUrls().get(i), pic.getImageView());
                }

            }

            if (count < 9) {
                ImageView pic;
                switch (count) {
                    case 8:
                        pic = (ImageView) gridLayout.getChildAt(8);
                        pic.setVisibility(View.INVISIBLE);
                        break;
                    case 7:
                        for (int i = 8; i > 6; i--) {
                            pic = (ImageView) gridLayout.getChildAt(i);
                            pic.setVisibility(View.INVISIBLE);
                        }
                        break;
                    case 6:
                        for (int i = 8; i > 5; i--) {
                            pic = (ImageView) gridLayout.getChildAt(i);
                            pic.setVisibility(View.GONE);
                        }

                        break;
                    case 5:
                        for (int i = 8; i > 5; i--) {
                            pic = (ImageView) gridLayout.getChildAt(i);
                            pic.setVisibility(View.GONE);
                        }
                        pic = (ImageView) gridLayout.getChildAt(5);
                        pic.setVisibility(View.INVISIBLE);
                        break;
                    case 4:
                        for (int i = 8; i > 5; i--) {
                            pic = (ImageView) gridLayout.getChildAt(i);
                            pic.setVisibility(View.GONE);
                        }
                        pic = (ImageView) gridLayout.getChildAt(5);
                        pic.setVisibility(View.INVISIBLE);
                        pic = (ImageView) gridLayout.getChildAt(4);
                        pic.setVisibility(View.INVISIBLE);
                        break;
                    case 3:
                        for (int i = 8; i > 2; i--) {
                            pic = (ImageView) gridLayout.getChildAt(i);
                            pic.setVisibility(View.GONE);
                        }
                        break;
                    case 2:
                        for (int i = 8; i > 2; i--) {
                            pic = (ImageView) gridLayout.getChildAt(i);
                            pic.setVisibility(View.GONE);
                        }
                        pic = (ImageView) gridLayout.getChildAt(2);
                        pic.setVisibility(View.INVISIBLE);
                        break;
                }
            }
        } else {
            gridLayout.setVisibility(View.GONE);
        }
    }

    protected void buildPic(final DataMessageDomain msg, final IXmDrawable view, int position) {
        if (XmSettingUtil.isEnablePic()) {
            view.setVisibility(View.VISIBLE);

            buildPic(msg, view);
        } else {
            view.setVisibility(View.GONE);
        }
    }

    private void buildPic(final DataMessageDomain msg, IXmDrawable view) {
        view.setVisibility(View.VISIBLE);
        String picUrl = msg.getThumbnail_pic();
        if (XmSettingUtil.getEnableBigPic()) {
            picUrl = msg.getOriginal_pic();
        }
        XmImageLoader.getInstance().loadImage(picUrl, view.getImageView());
    }

    private Activity getActivity() {
        return mContext;
    }

    /**
     * 根据指定位置，来填充adapter的数据
     *
     * @param holder
     * @param position
     */
    protected abstract void createViewData(ViewHolder holder, int position);

}
