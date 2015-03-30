package wm.xmwei.ui.fragment.photogallery;


import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;

import wm.xmwei.R;
import wm.xmwei.core.image.universalimageloader.XmImageLoader;
import wm.xmwei.ui.view.lib.XmPhotoViewData;
import wm.xmwei.ui.view.photoview.PhotoView;
import wm.xmwei.ui.view.photoview.PhotoViewAttacher;
import wm.xmwei.util.XmAnimationUtil;
import wm.xmwei.util.XmSettingUtil;


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

}
