package com.xiaokun.httpexceptiondemo.imageLoader;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * <pre>
 *     作者   : 肖坤
 *     时间   : 2018/05/08
 *     描述   : 内存缓存类
 *     版本   : 1.0
 * </pre>
 */
public class MemoryCache
{
    private LruCache<String, Bitmap> mMemoryCache = null;

    public MemoryCache()
    {
        //获得jvm虚拟机能用到的最大内存值
        long maxMemory = Runtime.getRuntime().maxMemory();
        //取最大内存的8分之一作为图片内存缓存的临界值
        long maxSize = maxMemory / 8;
        mMemoryCache = new LruCache<String, Bitmap>((int) maxSize)
        {
            @Override
            protected int sizeOf(String key, Bitmap value)
            {
                //计算bitmap对象的大小,并将其单位转换成KB
                return value.getRowBytes() * value.getHeight() / 1024;
            }
        };
    }

    //从内存中读bitmap
    public Bitmap getBitmapFromMemory(String key)
    {
        return mMemoryCache.get(key);
    }

    //bitmap写进内存中
    public void putBitmapToMemory(String key, Bitmap bitmap)
    {
        mMemoryCache.put(key, bitmap);
    }
}
