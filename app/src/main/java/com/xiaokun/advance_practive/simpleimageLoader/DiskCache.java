package com.xiaokun.advance_practive.simpleimageLoader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.xiaokun.baselib.util.MD5;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * <pre>
 *     作者   : 肖坤
 *     时间   : 2018/05/08
 *     描述   : 硬盘缓存
 *     版本   : 1.0
 * </pre>
 */
public class DiskCache
{
    private static DiskLruCache diskLruCache;
    public static final long M = 1024 * 1024;

    /**
     * 创建DiskLruCache
     *
     * @param context
     */
    public static DiskLruCache openCache(Context context)
    {
        if (diskLruCache == null)
        {
            try
            {
                if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ||
                        !Environment.isExternalStorageRemovable())
                {
                    diskLruCache = DiskLruCache.open(context.getExternalCacheDir(), 1, 1, 10 * M);
                } else
                {
                    diskLruCache = DiskLruCache.open(context.getCacheDir(), 1, 1, 10 * M);
                }
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return diskLruCache;
    }

    /**
     * 将图片写入disk内存中
     *
     * @param bitmap
     * @param url
     */
    public void putBitmapToDisk(Bitmap bitmap, String url)
    {
        if (diskLruCache == null)
        {
            throw new IllegalStateException("必须先调用openCache()方法");
        }
        DiskLruCache.Editor editor = null;
        try
        {
            editor = diskLruCache.edit(MD5.md5String(url));
            if (editor != null)
            {
                OutputStream outputStream = editor.newOutputStream(0);
                boolean compress = bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                if (compress)
                {
                    editor.commit();
                } else
                {
                    editor.abort();
                }
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 从disk中读取出bitmap
     *
     * @param url
     * @return
     * @throws IOException
     */
    public Bitmap getBitmapFromDisk(String url)
    {
        if (diskLruCache == null)
        {
            throw new IllegalStateException("必须先调用openCache()方法");
        }
        DiskLruCache.Snapshot snapshot = null;
        try
        {
            snapshot = diskLruCache.get(MD5.md5String(url));
            if (snapshot != null)
            {
                InputStream inputStream = snapshot.getInputStream(0);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 判断disk是否有缓存
     *
     * @param url
     * @return
     */
    public static boolean hasCache(String url)
    {
        try
        {
            return diskLruCache.get(MD5.md5String(url)) != null;
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 同步日志
     */
    public static void syncLog()
    {
        try
        {
            diskLruCache.flush();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 关闭DiskLruCache
     */
    public static void closeCache()
    {
        syncLog();
    }
}
