package com.xiaokun.advance_practive.artimgloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

import com.xiaokun.advance_practive.R;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <pre>
 *     作者   : 肖坤
 *     时间   : 2018/05/09
 *     描述   : 参考自Android开发艺术探索书籍。很完善的图片加载工具
 *     版本   : 1.0
 * </pre>
 */
public class ArtImageLoader
{
    private static final String TAG = "ArtImageLoader";
    public static final int TAG_KEY_URI = R.id.imageloader_uri;
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
    public static final long KEEP_ALIVE = 10L;
    public static final int MESSAGE_POST_RESULT = 1;
    private DiskCache mDiskCache;
    private MemoryCache mMemoryCache;
    private NetCache mNetCache;
    private static ArtImageLoader imageLoader;

    private static final ThreadFactory sThreadFactory = new ThreadFactory()
    {
        private final AtomicInteger mCount = new AtomicInteger(1);

        public Thread newThread(Runnable r)
        {
            return new Thread(r, "ImageLoader#" + mCount.getAndIncrement());
        }
    };

    public static final Executor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(CORE_POOL_SIZE,
            MAXIMUM_POOL_SIZE, KEEP_ALIVE, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), sThreadFactory);

    private Handler mMainHandler = new Handler(Looper.getMainLooper())
    {
        @Override
        public void handleMessage(Message msg)
        {
            LoaderResult result = (LoaderResult) msg.obj;
            ImageView imageView = result.imageView;
            String uri = (String) imageView.getTag(TAG_KEY_URI);
            if (uri.equals(result.uri))
            {
                imageView.setImageBitmap(result.bitmap);
            } else
            {
                Log.w(TAG, "set image bitmap,but url has changed, ignored!");
            }
        }
    };

    public static ArtImageLoader init(Context context)
    {
        if (imageLoader == null)
        {
            imageLoader = new ArtImageLoader(context);
        }
        return imageLoader;
    }

    private ArtImageLoader(Context context)
    {
        DiskCache.openCache(context.getApplicationContext());
        mMemoryCache = new MemoryCache();
        mDiskCache = new DiskCache(new ImageResizer());
        mNetCache = new NetCache(mMemoryCache, mDiskCache);
    }

    public void displayImg(String url, ImageView imageView)
    {
        displayImg(url, imageView, 0, 0);
    }

    public void displayImg(final String url, final ImageView imageView, final int reqWidth, final int reqHeight)
    {
        imageView.setTag(TAG_KEY_URI, url);
        Bitmap bitmap = mMemoryCache.getBitmapFromMemory(url);
        if (bitmap != null)
        {
            imageView.setImageBitmap(bitmap);
            return;
        }

        Runnable loadBitmapTask = new Runnable()
        {
            @Override
            public void run()
            {
                Bitmap bitmap = loadBitmap(url, reqWidth, reqHeight);
                if (bitmap != null)
                {
                    LoaderResult result = new LoaderResult(imageView, url, bitmap);
                    mMainHandler.obtainMessage(MESSAGE_POST_RESULT, result).sendToTarget();
                }
            }
        };
        THREAD_POOL_EXECUTOR.execute(loadBitmapTask);
    }

    private Bitmap loadBitmap(String url, int reqWidth, int reqHeight)
    {
        Bitmap bitmap = mMemoryCache.getBitmapFromMemory(url);
        if (bitmap != null)
        {
            return bitmap;
        }

        bitmap = mDiskCache.loadBitmapFromDiskCache(url, reqWidth, reqHeight);
        if (bitmap != null)
        {
            Log.d(TAG, "loadBitmapFromDisk,url:" + url);
            return bitmap;
        }

        bitmap = mNetCache.loadBitmapFromHttp(url, reqWidth, reqHeight);

        if (bitmap == null && !DiskCache.mIsDiskLruCacheCreated)
        {
            bitmap = mNetCache.loadBitmapFromUrl(url);
        }
        return bitmap;
    }

    private static class LoaderResult
    {
        public ImageView imageView;
        public String uri;
        public Bitmap bitmap;

        public LoaderResult(ImageView imageView, String uri, Bitmap bitmap)
        {
            this.imageView = imageView;
            this.uri = uri;
            this.bitmap = bitmap;
        }
    }
}
