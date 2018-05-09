package com.xiaokun.httpexceptiondemo.artimgloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;

import com.xiaokun.httpexceptiondemo.simpleimageLoader.DiskLruCache;
import com.xiaokun.httpexceptiondemo.util.MD5;
import com.xiaokun.httpexceptiondemo.util.Utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * <pre>
 *     作者   : 肖坤
 *     时间   : 2018/05/09
 *     描述   : 参考自Android开发艺术探索书籍。硬盘缓存
 *     版本   : 1.0
 * </pre>
 */
public class DiskCache
{
    private static final String TAG = "DiskCache";
    private static DiskLruCache diskLruCache;
    private static final long M = 1024 * 1024;
    private static final long DISK_CACHE_SIZE = 50 * M;
    private static final int IO_BUFFER_SIZE = 8 * 1024;
    private ImageResizer imageResizer;
    public static boolean mIsDiskLruCacheCreated = false;

    public DiskCache(ImageResizer imageResizer)
    {
        this.imageResizer = imageResizer;
    }

    /**
     * 创建DiskLruCache
     *
     * @param context
     */
    public static DiskLruCache openCache(Context context)
    {
        if (diskLruCache == null)
        {
            File diskCacheDir = getDiskCacheDir(context, "bitmap");
            if (!diskCacheDir.exists())
            {
                diskCacheDir.mkdirs();
            }
            if (diskCacheDir.getUsableSpace() > DISK_CACHE_SIZE)
            {
                try
                {
                    diskLruCache = DiskLruCache.open(diskCacheDir, 1, 1, DISK_CACHE_SIZE);
                    mIsDiskLruCacheCreated = true;
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return diskLruCache;
    }

    public Bitmap loadBitmapFromDiskCache(String url, int reqWidth, int reqHeight)
    {
        if (Looper.myLooper() == Looper.getMainLooper())
        {
            Log.w(TAG, "load bitmap from UI Thread, it's not recommended!");
        }
        if (diskLruCache == null)
        {
            return null;
        }

        Bitmap bitmap = null;
        DiskLruCache.Snapshot snapshot = null;
        try
        {
            snapshot = diskLruCache.get(MD5.md5String(url));
            if (snapshot != null)
            {
                FileInputStream inputStream = (FileInputStream) snapshot.getInputStream(0);
                FileDescriptor fd = inputStream.getFD();
                bitmap = imageResizer.decodeSampledBitmapFromFileDescriptor(fd, reqWidth, reqHeight);
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return bitmap;
    }

    public void putBitmapToDisk(String url, BufferedInputStream inputStream)
    {
        BufferedOutputStream out = null;
        String key = MD5.md5String(url);
        try
        {
            DiskLruCache.Editor edit = diskLruCache.edit(key);
            if (edit != null)
            {
                OutputStream outputStream = edit.newOutputStream(0);
                out = new BufferedOutputStream(outputStream, IO_BUFFER_SIZE);
                int b;
                while ((b = inputStream.read()) != -1)
                {
                    out.write(b);
                }
                diskLruCache.flush();
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        } finally
        {
            Utils.close(out);
        }
    }

    public void putBitmapToDisk(String url)
    {
        String key = MD5.md5String(url);
        try
        {
            DiskLruCache.Editor edit = diskLruCache.edit(key);
            if (edit != null)
            {
                OutputStream outputStream = edit.newOutputStream(0);
                if (downloadUrlToStream(url, outputStream))
                {
                    edit.commit();
                } else
                {
                    edit.abort();
                }
                diskLruCache.flush();
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public boolean downloadUrlToStream(String urlString, OutputStream outputStream)
    {
        HttpURLConnection urlConnection = null;
        BufferedOutputStream out = null;
        BufferedInputStream in = null;
        try
        {
            final URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedInputStream(urlConnection.getInputStream(),
                    IO_BUFFER_SIZE);
            out = new BufferedOutputStream(outputStream, IO_BUFFER_SIZE);

            int b;
            while ((b = in.read()) != -1)
            {
                out.write(b);
            }
            return true;
        } catch (IOException e)
        {
            Log.e(TAG, "downloadBitmap failed." + e);
        } finally
        {
            if (urlConnection != null)
            {
                urlConnection.disconnect();
            }
            Utils.close(out);
            Utils.close(in);
        }
        return false;
    }

    public static File getDiskCacheDir(Context context, String fileName)
    {
        final String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ||
                !Environment.isExternalStorageRemovable())
        {
            cachePath = context.getExternalCacheDir().getPath();
        } else
        {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + fileName);
    }
}
