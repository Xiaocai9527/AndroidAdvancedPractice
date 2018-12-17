package com.xiaokun.advance_practive.simpleimageLoader;

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
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        //取最大内存的8分之一作为图片内存缓存的临界值
        int maxSize = maxMemory / 8;
        mMemoryCache = new LruCache<String, Bitmap>(maxSize)
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
