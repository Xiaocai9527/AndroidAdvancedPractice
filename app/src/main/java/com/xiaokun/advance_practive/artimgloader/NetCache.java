package com.xiaokun.advance_practive.artimgloader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Looper;
import android.util.Log;

import com.xiaokun.baselib.util.Utils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * <pre>
 *     作者   : 肖坤
 *     时间   : 2018/05/09
 *     描述   : 网络缓存
 *     版本   : 1.0
 * </pre>
 */
public class NetCache
{
    private static final String TAG = "NetCache";
    private static final int IO_BUFFER_SIZE = 8 * 1024;
    private MemoryCache memoryCache;
    private DiskCache diskCache;

    public NetCache()
    {

    }

    public NetCache(MemoryCache memoryCache, DiskCache diskCache)
    {
        this.memoryCache = memoryCache;
        this.diskCache = diskCache;
    }

    public Bitmap loadBitmapFromHttp(String url, int reqWidth, int reqHeight)
    {
        if (Looper.myLooper() == Looper.getMainLooper())
        {
            throw new RuntimeException("不能在主线程开启网络");
        }
        if (diskCache == null)
        {
            return null;
        }
        diskCache.putBitmapToDisk(url);
        return diskCache.loadBitmapFromDiskCache(url, reqWidth, reqHeight);
    }

    public Bitmap loadBitmapFromUrl(String urlString)
    {
        Bitmap bitmap = null;
        HttpURLConnection urlConnection = null;
        BufferedInputStream in = null;
        try
        {
            final URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedInputStream(urlConnection.getInputStream(),
                    IO_BUFFER_SIZE);
            bitmap = BitmapFactory.decodeStream(in);
        } catch (final IOException e)
        {
            Log.e(TAG, "Error in downloadBitmap: " + e);
        } finally
        {
            if (urlConnection != null)
            {
                urlConnection.disconnect();
            }
            Utils.close(in);
        }
        return bitmap;
    }

    private BufferedInputStream getIsFromHttp(String urlString)
    {
        HttpURLConnection urlConnection = null;
        BufferedInputStream in = null;
        try
        {
            URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedInputStream(urlConnection.getInputStream(), IO_BUFFER_SIZE);
        } catch (MalformedURLException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        } finally
        {
            if (urlConnection != null)
            {
                urlConnection.disconnect();
            }
            Utils.close(in);
        }
        return in;
    }
}
