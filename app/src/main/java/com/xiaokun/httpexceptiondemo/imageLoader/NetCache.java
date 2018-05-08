package com.xiaokun.httpexceptiondemo.imageLoader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executors;

/**
 * <pre>
 *     作者   : 肖坤
 *     时间   : 2018/05/08
 *     描述   : 网络缓存
 *     版本   : 1.0
 * </pre>
 */
public class NetCache
{
    public static final int MILLISECOND = 1000;
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private MemoryCache memoryCache;
    private LocalCache localCache;
    private DiskCache diskCache;

    public NetCache(MemoryCache memoryCache, LocalCache localCache)
    {
        this.memoryCache = memoryCache;
        this.localCache = localCache;
    }

    public NetCache(MemoryCache memoryCache, DiskCache diskCache)
    {
        this.memoryCache = memoryCache;
        this.diskCache = diskCache;
    }

    public void getBitmapFromNet(ImageView imageView, String url)
    {
        new BitmapTask().executeOnExecutor(Executors.newFixedThreadPool(CPU_COUNT), imageView, url);
    }

    //继承AsyncTask,下载bitmap
    class BitmapTask extends AsyncTask<Object, Void, Bitmap>
    {

        private ImageView imageView;
        private String url;

        @Override
        protected Bitmap doInBackground(Object... params)
        {
            imageView = (ImageView) params[0];
            url = (String) params[1];
            return downLoadBitmap(url);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap)
        {
            if (bitmap != null)
            {
                //展示并缓存至本地和内存中
                imageView.setImageBitmap(bitmap);
                if (localCache != null)
                {
                    localCache.setBitmapToLocal(url, bitmap);
                }
                if (diskCache != null)
                {
                    diskCache.putBitmapToDisk(bitmap, url);
                }
                memoryCache.putBitmapToMemory(url, bitmap);
                Log.d("xiaocai", "从网络中获取图片");
            }
        }

        private Bitmap downLoadBitmap(String url)
        {
            HttpURLConnection urlConnection = null;
            try
            {
                urlConnection = (HttpURLConnection) new URL(url).openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setConnectTimeout(5 * MILLISECOND);
                urlConnection.setReadTimeout(5 * MILLISECOND);
                int responseCode = urlConnection.getResponseCode();
                if (responseCode == 200)
                {
                    //连接成功
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 2;//宽和高压缩为原来1/2
                    options.inPreferredConfig = Bitmap.Config.ARGB_4444;
                    Bitmap bitmap = BitmapFactory.decodeStream(urlConnection.getInputStream(), null, options);
                    return bitmap;
                }
            } catch (IOException e)
            {
                e.printStackTrace();
            } finally
            {
                if (urlConnection != null)
                {
                    urlConnection.disconnect();
                    urlConnection = null;
                }
            }
            return null;
        }
    }

}
