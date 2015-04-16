package wm.xmwei.ui.view.lib;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import wm.xmwei.R;
import wm.xmwei.bean.DataMessageDomain;
import wm.xmwei.bean.UserDomain;
import wm.xmwei.core.image.universalimageloader.XmImageLoader;
import wm.xmwei.ui.activity.galleryview.XmPhotoViewScanActivity;
import wm.xmwei.ui.view.lib.asyncpicture.IXmDrawable;
import wm.xmwei.util.XmSettingUtil;

/**
 *
 *
 */
public class XmMessageScanHeaderView extends LinearLayout {

    private Context mContext;
    private MessageScanHeaderLayout mHeaderLayout;
    private DataMessageDomain mDataMessageDomain;

    private static class MessageScanHeaderLayout {
        TextView username;
        TextView content;
        TextView recontent;
        TextView time;
        TextView source;
        TimeLineAvatarImageView avatar;
        GridLayout content_pic_multi;


        GridLayout repost_pic_multi;
        LinearLayout repost_layout;
    }

    public XmMessageScanHeaderView(Context context, DataMessageDomain dataMessageDomain) {
        super(context);
        mDataMessageDomain = dataMessageDomain;
        init(context);
    }

    private void init(Context context) {
        mContext = context;

        if (mDataMessageDomain == null) {
            throw new NullPointerException("data message is null");
        }

        View containerView = LayoutInflater.from(mContext).inflate(R.layout.layer_message_scan_header, this);
        initView(containerView);
        buildViewData(false);
    }


    private void initView(View view) {
        mHeaderLayout = new MessageScanHeaderLayout();
        mHeaderLayout.username = (TextView) view.findViewById(R.id.username);
        mHeaderLayout.content = (TextView) view.findViewById(R.id.content);
        mHeaderLayout.recontent = (TextView) view.findViewById(R.id.repost_content);
        mHeaderLayout.time = (TextView) view.findViewById(R.id.time);
        mHeaderLayout.source = (TextView) view.findViewById(R.id.source);


        mHeaderLayout.avatar = (TimeLineAvatarImageView) view.findViewById(R.id.avatar);
        mHeaderLayout.content_pic_multi = (GridLayout) view.findViewById(R.id.content_pic_multi);

//        mHeaderLayout.content_pic = (WeiboDetailImageView) view.findViewById(R.id.content_pic);
//        mHeaderLayout.repost_pic = (WeiboDetailImageView) view.findViewById(R.id.repost_content_pic);
        mHeaderLayout.repost_pic_multi = (GridLayout) view.findViewById(R.id.repost_content_pic_multi);
        mHeaderLayout.repost_layout = (LinearLayout) view.findViewById(R.id.repost_layout);


    }

    public void buildViewData(final boolean refreshPic) {
        mHeaderLayout.avatar.checkVerified(mDataMessageDomain.getUser());
        UserDomain user = mDataMessageDomain.getUser();
        if (mDataMessageDomain.getUser() != null) {
            if (TextUtils.isEmpty(mDataMessageDomain.getUser().getRemark())) {
                mHeaderLayout.username.setText(mDataMessageDomain.getUser().getScreen_name());
            } else {
                mHeaderLayout.username.setText(
                        mDataMessageDomain.getUser().getScreen_name() + "(" + user.getRemark() + ")");
            }

            // 用户图标
            String userUrl = null;
            if (XmSettingUtil.getEnableBigAvatar()) {
                userUrl = user.getAvatar_large();
            } else {
                userUrl = user.getProfile_image_url();
            }

            XmImageLoader.getInstance().loadImage(userUrl, mHeaderLayout.avatar.getImageView());
        }
        mHeaderLayout.content.setText(mDataMessageDomain.getListViewSpannableString());
        mHeaderLayout.time.setText(mDataMessageDomain.getTimeInFormat());


        if (!TextUtils.isEmpty(mDataMessageDomain.getSource())) {
            mHeaderLayout.source.setText(Html.fromHtml(mDataMessageDomain.getSource()).toString());
        }


        mHeaderLayout.content_pic_multi.setVisibility(View.GONE);

        //sina weibo official account can send repost message with picture, fuck sina weibo
        if (mDataMessageDomain.havePicture() && mDataMessageDomain.getRetweeted_status() == null) {
            displayPictures(mDataMessageDomain, mHeaderLayout.content_pic_multi);
        }

        // 回复的
        final DataMessageDomain repostmDataMessageDomain = mDataMessageDomain.getRetweeted_status();

        mHeaderLayout.repost_layout.setVisibility(repostmDataMessageDomain != null ? View.VISIBLE : View.GONE);

        if (repostmDataMessageDomain != null) {
            //sina weibo official account can send repost message with picture, fuck sina weibo

            mHeaderLayout.repost_layout.setVisibility(View.VISIBLE);
            mHeaderLayout.recontent.setVisibility(View.VISIBLE);
            if (repostmDataMessageDomain.getUser() != null) {
                mHeaderLayout.recontent.setText(repostmDataMessageDomain.getListViewSpannableString());
            } else {
                mHeaderLayout.recontent.setText(repostmDataMessageDomain.getListViewSpannableString());
            }

            mHeaderLayout.repost_pic_multi.setVisibility(View.GONE);

            if (repostmDataMessageDomain.havePicture()) {
                displayPictures(repostmDataMessageDomain, mHeaderLayout.repost_pic_multi);
            }
        }

    }

    //处理多图显示，
    private void displayPictures(final DataMessageDomain dataMessageDomain, final GridLayout gridLayout) {
        if (!dataMessageDomain.isMultiPics()) {
            //不是多图时，

        } else {
            gridLayout.setVisibility(View.VISIBLE);

            final int count = dataMessageDomain.getPicCount();
            for (int i = 0; i < count; i++) {
                final IXmDrawable pic = (IXmDrawable) gridLayout.getChildAt(i);
                pic.setVisibility(View.VISIBLE);

                if (XmSettingUtil.getEnableBigPic()) {
                    XmImageLoader.getInstance().loadImage(dataMessageDomain.getHighPicUrls().get(i), pic.getImageView());
                } else {
                    XmImageLoader.getInstance().loadImage(dataMessageDomain.getThumbnailPicUrls().get(i), pic.getImageView());
                }

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
                                .newIntent(dataMessageDomain, animationRectArrayList, selectPos);
                        ((Activity) mContext).startActivity(intent);

                    }
                });
            }

            if (count < 9) {
                for (int i = count; i < 9; i++) {
                    ImageView pic = (ImageView) gridLayout.getChildAt(i);
                    pic.setVisibility(View.GONE);
                }
            }
        }

    }

}
