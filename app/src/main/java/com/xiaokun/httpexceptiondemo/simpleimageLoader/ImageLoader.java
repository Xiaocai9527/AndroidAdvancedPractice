package com.xiaokun.httpexceptiondemo.simpleimageLoader;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

/**
 * <pre>
 *     作者   : 肖坤
 *     时间   : 2018/05/08
 *     描述   : 图片加载器
 *     版本   : 1.0
 * </pre>
 */
public class ImageLoader
{
    private DiskCache diskCache;
    private MemoryCache memoryCache;
    private NetCache netCache;
    private Context mContext;

    public static ImageLoader init(Context context)
    {
        return new ImageLoader(context);
    }

    private ImageLoader(Context context)
    {
        mContext = context.getApplicationContext();
        DiskCache.openCache(mContext);
        diskCache = new DiskCache();
        memoryCache = new MemoryCache();
        netCache = new NetCache(memoryCache, diskCache);
    }

    /**
     * 加载图片
     *
     * @param imageView
     * @param url
     */
    public void displayImg(ImageView imageView, String url)
    {
        Bitmap bitmap;
        bitmap = memoryCache.getBitmapFromMemory(url);
        if (bitmap != null)
        {
            imageView.setImageBitmap(bitmap);
            Log.d("xiaocai", "从内存中获取图片");
            return;
        }
        bitmap = diskCache.getBitmapFromDisk(url);
        if (bitmap != null)
        {
            imageView.setImageBitmap(bitmap);
            Log.d("xiaocai", "从硬盘中获取图片");
            memoryCache.putBitmapToMemory(url, bitmap);
            return;
        }
        netCache.getBitmapFromNet(imageView, url);
    }

}
