package com.xiaokun.advance_practive.artimgloader;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.FileDescriptor;

/**
 * <pre>
 *     作者   : 肖坤
 *     时间   : 2018/05/09
 *     描述   : 参考自Android开发艺术探索书籍。主要是用来计算图片采样率
 *     版本   : 1.0
 * </pre>
 */
public class ImageResizer
{
    private static final String TAG = "ImageResizer";

    public ImageResizer()
    {
    }

    public Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight)
    {
        BitmapFactory.Options options = new BitmapFactory.Options();
        //设置为true仅仅为了得到图片尺寸，轻量级操作
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        //计算采样率后重新decode到需要bitmap
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public Bitmap decodeSampledBitmapFromFileDescriptor(FileDescriptor fd, int reqWidth, int reqHeight)
    {
        BitmapFactory.Options options = new BitmapFactory.Options();
        //设置为true仅仅为了得到图片尺寸，轻量级操作
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFileDescriptor(fd, null, options);
        //计算采样率后重新decode到需要bitmap
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFileDescriptor(fd, null, options);
    }

    /**
     * 计算图片采样率
     *
     * @param options
     * @param reqWidth  当reqWidth或reqHeight为0时，默认采样率1
     * @param reqHeight
     * @return
     */
    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight)
    {
        if (reqWidth == 0 || reqHeight == 0)
        {
            return 1;
        }

        //origin height and width of the image
        int originHeight = options.outHeight;
        int originWidth = options.outWidth;
        Log.d(TAG, "origin, w= " + originWidth + " h=" + originHeight);

        int inSampleSize = 1;
        if (originWidth > reqWidth || originHeight > reqHeight)
        {
            int halfWidth = originWidth / 2;
            int halfHeight = originHeight / 2;

            //保证压缩后的宽和高,比展示的宽和高都要大
            while ((halfWidth / inSampleSize) > reqWidth && (halfHeight / inSampleSize) > reqHeight)
            {
                inSampleSize *= 2;
            }
        }

        Log.d(TAG, "sampleSize:" + inSampleSize);
        return inSampleSize;
    }

}
