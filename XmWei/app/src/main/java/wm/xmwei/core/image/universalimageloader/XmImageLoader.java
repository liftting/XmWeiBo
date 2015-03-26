package wm.xmwei.core.image.universalimageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import wm.xmwei.R;
import wm.xmwei.XmApplication;
import wm.xmwei.core.image.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import wm.xmwei.core.image.universalimageloader.core.DisplayImageOptions;
import wm.xmwei.core.image.universalimageloader.core.ImageLoader;
import wm.xmwei.core.image.universalimageloader.core.ImageLoaderConfiguration;
import wm.xmwei.core.image.universalimageloader.core.assist.QueueProcessingType;
import wm.xmwei.core.image.universalimageloader.core.display.FadeInBitmapDisplayer;

/**
 *
 *
 */
public class XmImageLoader {

    private static XmImageLoader inst;

    public static XmImageLoader getInstance() {
        if (inst == null) {
            inst = new XmImageLoader();
            inst.initImageLoader(XmApplication.getInstance(), R.color.floralwhite, R.color.floralwhite, R.color.floralwhite);
        }
        return inst;
    }

    private ImageLoader imageLoader;
    private DisplayImageOptions options;

    private static int FADE_IN_DURATION = 50;// 淡入效果的持续时间

    /**
     * 初始化图片加载类配置信息*
     */
    public void initImageLoader(Context context, int mg_default_list, int img_default_listUri, int nomal_mg_default_list) {
        ImageLoaderConfiguration.Builder bin = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.MIN_PRIORITY)//加载图片的线程数
                .denyCacheImageMultipleSizesInMemory() //解码图像的大尺寸将在内存中缓存先前解码图像的小尺寸。
                .discCacheFileNameGenerator(new Md5FileNameGenerator())//设置磁盘缓存文件名称
                .tasksProcessingOrder(QueueProcessingType.LIFO)//设置加载显示图片队列进程  后进先出
                .discCacheFileCount(500);

//        if (!AppConfig.LOG_CLOSED&&DevRunningTime.isShowImageErr)bin.enableLogging();

        ImageLoaderConfiguration config = bin.build();
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);

        DisplayImageOptions.Builder disBin = new DisplayImageOptions.Builder()
                .showStubImage(mg_default_list)
                .showImageForEmptyUri(img_default_listUri)
                .cacheInMemory(true)// .cacheInMemory()
                .cacheOnDisc(true)
                .displayer(new FadeInBitmapDisplayer(FADE_IN_DURATION))// 添加淡入效果
                .bitmapConfig(Bitmap.Config.ARGB_8888);

        disBin.showImageOnFail(nomal_mg_default_list);
        options = disBin.build();
    }

    public void loadImage(String msgImgUrl, ImageView image) {
        if (msgImgUrl == null) return;
        if (msgImgUrl == image.getTag()) return;
        imageLoader.displayImage(msgImgUrl, image, options);
    }

    public void pause() {
        imageLoader.pause();
    }

    public void resume() {
        imageLoader.resume();
    }

}
