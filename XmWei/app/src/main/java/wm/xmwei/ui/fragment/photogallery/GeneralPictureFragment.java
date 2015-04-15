package wm.xmwei.ui.fragment.photogallery;


import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.sina.weibo.sdk.utils.Utility;

import java.lang.reflect.Field;

import wm.xmwei.R;
import wm.xmwei.core.debug.AppLogger;
import wm.xmwei.core.image.universalimageloader.XmImageLoader;
import wm.xmwei.ui.view.lib.XmPhotoViewData;
import wm.xmwei.ui.view.photoview.PhotoView;
import wm.xmwei.ui.view.photoview.PhotoViewAttacher;
import wm.xmwei.util.XmAnimationUtil;
import wm.xmwei.util.XmSettingUtil;
import wm.xmwei.util.XmUtils;


/**
 * User: qii
 * Date: 14-4-30
 */
public class GeneralPictureFragment extends Fragment {

    private PhotoView photoView;

    public static final int ANIMATION_DURATION = 300;

    public static GeneralPictureFragment newInstance(String largePicPath, XmPhotoViewData rect,
                                                     boolean animationIn) {
        GeneralPictureFragment fragment = new GeneralPictureFragment();
        Bundle bundle = new Bundle();
        bundle.putString("large_path", largePicPath);
        bundle.putParcelable("rect", rect);
        bundle.putBoolean("animationIn", animationIn);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layer_photo_scan_general, container, false);

        AppLogger.w("imagescan:general pictural fragment: enter oncreateview ");

        photoView = (PhotoView) view.findViewById(R.id.animation);

        if (XmSettingUtil.allowClickToCloseGallery()) {

            photoView.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
                @Override
                public void onViewTap(View view, float x, float y) {
                    getActivity().onBackPressed();
                }
            });
        }

        final String largePicPath = getArguments().getString("large_path");
        boolean animateIn = getArguments().getBoolean("animationIn");
        final XmPhotoViewData rect = getArguments().getParcelable("rect");

        if (!animateIn) {
            XmImageLoader.getInstance().loadImage(largePicPath, photoView);
            return view;
        }

        final Runnable endAction = new Runnable() {
            @Override
            public void run() {
                Bundle bundle = getArguments();
                bundle.putBoolean("animationIn", false);
            }
        };

        photoView.getViewTreeObserver()
                .addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {

                        if (rect == null) {
                            photoView.getViewTreeObserver().removeOnPreDrawListener(this);
                            endAction.run();
                            return true;
                        }

                        final Rect startBounds = new Rect(rect.scaledBitmapRect);
                        final Rect finalBounds = XmAnimationUtil
                                .getBitmapRectFromImageView(photoView);

                        if (finalBounds == null) {
                            photoView.getViewTreeObserver().removeOnPreDrawListener(this);
                            endAction.run();
                            return true;
                        }

                        float startScale = (float) finalBounds.width() / startBounds.width();

                        if (startScale * startBounds.height() > finalBounds.height()) {
                            startScale = (float) finalBounds.height() / startBounds.height();
                        }

                        int deltaTop = startBounds.top - finalBounds.top;
                        int deltaLeft = startBounds.left - finalBounds.left;

                        photoView.setPivotY(
                                (photoView.getHeight() - finalBounds.height()) / 2);
                        photoView.setPivotX((photoView.getWidth() - finalBounds.width()) / 2);

                        photoView.setScaleX(1 / startScale);
                        photoView.setScaleY(1 / startScale);

                        photoView.setTranslationX(deltaLeft);
                        photoView.setTranslationY(deltaTop);

                        photoView.animate().translationY(0).translationX(0)
                                .scaleY(1)
                                .scaleX(1).setDuration(ANIMATION_DURATION)
                                .setInterpolator(
                                        new AccelerateDecelerateInterpolator())
                                .withEndAction(endAction);

                        AnimatorSet animationSet = new AnimatorSet();
                        animationSet.setDuration(ANIMATION_DURATION);
                        animationSet
                                .setInterpolator(new AccelerateDecelerateInterpolator());

                        animationSet.playTogether(ObjectAnimator.ofFloat(photoView,
                                "clipBottom",
                                XmPhotoViewData.getClipBottom(rect, finalBounds), 0));
                        animationSet.playTogether(ObjectAnimator.ofFloat(photoView,
                                "clipRight",
                                XmPhotoViewData.getClipRight(rect, finalBounds), 0));
                        animationSet.playTogether(ObjectAnimator.ofFloat(photoView,
                                "clipTop", XmPhotoViewData.getClipTop(rect, finalBounds), 0));
                        animationSet.playTogether(ObjectAnimator.ofFloat(photoView,
                                "clipLeft", XmPhotoViewData.getClipLeft(rect, finalBounds), 0));

                        animationSet.start();

                        photoView.getViewTreeObserver().removeOnPreDrawListener(this);
                        return true;
                    }
                });

        return view;
    }

    public void animationExit(ObjectAnimator backgroundAnimator) {

        if (Math.abs(photoView.getScale() - 1.0f) > 0.1f) {
            photoView.setScale(1, true);
            return;
        }

        getActivity().overridePendingTransition(0, 0);
        animateClose(backgroundAnimator);
    }

    private void animateClose(ObjectAnimator backgroundAnimator) {

        XmPhotoViewData rect = getArguments().getParcelable("rect");

        if (rect == null) {
            photoView.animate().alpha(0);
            backgroundAnimator.start();
            return;
        }

        final Rect startBounds = rect.scaledBitmapRect;
        final Rect finalBounds = XmAnimationUtil.getBitmapRectFromImageView(photoView);

        if (finalBounds == null) {
            photoView.animate().alpha(0);
            backgroundAnimator.start();
            return;
        }

        if (XmUtils.isDevicePort() != rect.isScreenPortrait) {
            photoView.animate().alpha(0);
            backgroundAnimator.start();
            return;
        }

        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            startScale = (float) startBounds.height() / finalBounds.height();
        } else {
            startScale = (float) startBounds.width() / finalBounds.width();
        }

        final float startScaleFinal = startScale;

        int deltaTop = startBounds.top - finalBounds.top;
        int deltaLeft = startBounds.left - finalBounds.left;

        photoView.setPivotY((photoView.getHeight() - finalBounds.height()) / 2);
        photoView.setPivotX((photoView.getWidth() - finalBounds.width()) / 2);

        photoView.animate().translationX(deltaLeft).translationY(deltaTop)
                .scaleY(startScaleFinal)
                .scaleX(startScaleFinal).setDuration(ANIMATION_DURATION)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {

                        photoView.animate().alpha(0.0f).setDuration(200).withEndAction(
                                new Runnable() {
                                    @Override
                                    public void run() {

                                    }
                                }
                        );
                    }
                });

        AnimatorSet animationSet = new AnimatorSet();
        animationSet.setDuration(ANIMATION_DURATION);
        animationSet.setInterpolator(new AccelerateDecelerateInterpolator());

        animationSet.playTogether(backgroundAnimator);

        animationSet.playTogether(ObjectAnimator.ofFloat(photoView,
                "clipBottom", 0, XmPhotoViewData
                        .getClipBottom(rect, finalBounds)
        ));
        animationSet.playTogether(ObjectAnimator.ofFloat(photoView,
                "clipRight", 0,
                XmPhotoViewData.getClipRight(rect, finalBounds)));
        animationSet.playTogether(ObjectAnimator.ofFloat(photoView,
                "clipTop", 0, XmPhotoViewData.getClipTop(rect, finalBounds)));
        animationSet.playTogether(ObjectAnimator.ofFloat(photoView,
                "clipLeft", 0, XmPhotoViewData.getClipLeft(rect, finalBounds)));

        animationSet.start();
    }

}
