package wm.xmwei.ui.fragment.photogallery;

import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import wm.xmwei.R;
import wm.xmwei.XmApplication;
import wm.xmwei.core.debug.AppLogger;
import wm.xmwei.core.image.universalimageloader.XmImageLoader;
import wm.xmwei.core.image.universalimageloader.core.assist.FailReason;
import wm.xmwei.core.image.universalimageloader.core.listener.ImageLoadingListener;
import wm.xmwei.core.image.universalimageloader.core.listener.ImageLoadingProgressListener;
import wm.xmwei.core.lib.support.view.material.ProgressWheel;
import wm.xmwei.ui.activity.galleryview.XmPhotoViewScanActivity;
import wm.xmwei.ui.view.lib.XmPhotoViewData;
import wm.xmwei.util.XmImageUtil;

/**
 * this is photo container and include different gif or common fragment view
 */
public class XmPhotoViewFragment extends Fragment {

    private ProgressWheel progressView;
    private TextView wait;
    private TextView error;
    private RelativeLayout mRlyImageOrigin;
    private ImageView mImgOrigin;

    private XmPhotoViewData mPhotoData;
    private String largeUrl;
    private String originUrl;

    private int imagePosition;

    private static int OFFSET_IMAGE_ORIGIN = 100;

    public static XmPhotoViewFragment newInstance(String originUrl, String largeUrl, XmPhotoViewData rect,
                                                  boolean animationIn, boolean firstOpenPage, int position) {
        XmPhotoViewFragment fragment = new XmPhotoViewFragment();
        Bundle bundle = new Bundle();
        bundle.putString("origin_url", originUrl);
        bundle.putString("large_url", largeUrl);
        bundle.putParcelable("rect", rect);
        bundle.putBoolean("animationIn", animationIn);
        bundle.putBoolean("firstOpenPage", firstOpenPage);
        bundle.putInt("image_pos", position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layer_gallery_container, container, false);
        progressView = (ProgressWheel) view.findViewById(R.id.loading);
        error = (TextView) view.findViewById(R.id.error);
        mRlyImageOrigin = (RelativeLayout) view.findViewById(R.id.rly_gallery_origin_view);
        mImgOrigin = (ImageView) view.findViewById(R.id.v_img_origin);

        initData();

        AppLogger.w("imagescan:photoView fragment: enter oncreateview-" + imagePosition);


        // load the large bitmap
        XmImageLoader.getInstance().loadImage(largeUrl, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                progressView.setVisibility(View.GONE);
                error.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                progressView.setVisibility(View.GONE);
                error.setVisibility(View.GONE);
                mImgOrigin.setVisibility(View.INVISIBLE);
                displayPicture(false);

            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        }, new ImageLoadingProgressListener() {
            @Override
            public void onProgressUpdate(String imageUri, View view, int current, int total) {
                progressView.setProgress((float) (current * 1.0 / total));
            }
        });


        XmPhotoViewScanActivity activity = (XmPhotoViewScanActivity) getActivity();
        activity.showBackgroundImmediately();

        return view;
    }

    private void initData() {
        Bundle bundle = getArguments();
        largeUrl = bundle.getString("large_url");
        originUrl = bundle.getString("origin_url");
        mPhotoData = bundle.getParcelable("rect");
        imagePosition = bundle.getInt("image_pos");

        boolean animateIn = bundle.getBoolean("animationIn");
        bundle.putBoolean("animationIn", false);

        // mphotoData fail null, beause when image not loaded success
        RelativeLayout.LayoutParams imgParams = (RelativeLayout.LayoutParams) mImgOrigin.getLayoutParams();
        if (mPhotoData != null && imgParams != null) {
            updateOriginImageParams(imgParams);
            XmImageLoader.getInstance().loadImage(originUrl, mImgOrigin);

        } else {
            // not get data and set visibility gone
            mImgOrigin.setVisibility(View.GONE);
        }
    }

    private void updateOriginImageParams(RelativeLayout.LayoutParams imgParams) {
        int imageWidth = mPhotoData.thumbnailWidth;
        int imageHeight = mPhotoData.thumbnailHeight;

        DisplayMetrics displayMetrics = XmApplication.getInstance().getDisplayMetrics();
        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;

        if (imageWidth >= imageHeight) {
            imgParams.width = screenWidth;
            imgParams.height = (int) ((imgParams.width / (float) imageWidth) * imageHeight) - OFFSET_IMAGE_ORIGIN;
        } else {
            imgParams.height = screenHeight / 2;
            imgParams.width = (int) ((imgParams.height / (float) imageHeight) * imageWidth);
        }

        mImgOrigin.setLayoutParams(imgParams);
        mImgOrigin.setVisibility(View.VISIBLE);
    }

    private void displayPicture(boolean animateIn) {
        XmPhotoViewScanActivity activity = (XmPhotoViewScanActivity) getActivity();

        XmPhotoViewData rect = getArguments().getParcelable("rect");
        boolean firstOpenPage = getArguments().getBoolean("firstOpenPage");

        if (firstOpenPage) {
            if (animateIn) {
                ObjectAnimator animator = activity.showBackgroundAnimate();
                animator.start();
            } else {
                activity.showBackgroundImmediately();
            }
            getArguments().putBoolean("firstOpenPage", false);
        }

        Fragment fragment = getChildFragmentManager().findFragmentByTag("imagePosition-" + imagePosition);

        if (fragment == null) {
            if (XmImageUtil.isGifPicture(largeUrl)) {
                fragment = GifPictureFragment.newInstance(largeUrl, rect, animateIn);
            } else {
                fragment = GeneralPictureFragment.newInstance(largeUrl, rect, animateIn);
            }
        }
        getChildFragmentManager().beginTransaction().replace(R.id.child, fragment, "imagePosition-" + imagePosition)
                .commitAllowingStateLoss();

    }

    public boolean canAnimateCloseActivity() {
//        Fragment fragment = getChildFragmentManager().findFragmentById(R.id.child);
//        if (fragment instanceof GeneralPictureFragment) {
//            return true;
//        } else if (fragment instanceof GifPictureFragment) {
//            return true;
//        } else {
//            return false;
//        }
        return true;
    }


    public void animationExit(ObjectAnimator backgroundAnimator) {
        Fragment fragment = getChildFragmentManager().findFragmentById(R.id.child);
        if (fragment instanceof GeneralPictureFragment) {
            GeneralPictureFragment child = (GeneralPictureFragment) fragment;
            child.animationExit(backgroundAnimator);
        }
    }


}
