package com.xiaokun.httpexceptiondemo.simpleimageLoader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.xiaokun.httpexceptiondemo.util.MD5;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * <pre>
 *     作者   : 肖坤
 *     时间   : 2018/05/08
 *     描述   : 本地缓存(弃用,被硬盘缓存取代)
 *     版本   : 1.0
 * </pre>
 */
public class LocalCache
{
    public static final String CACHE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/localCache";

    //将bitmap写入本地文件
    public void setBitmapToLocal(String url, Bitmap bitmap)
    {
        //防止url中有非法字符，将其md5
        String fileName = MD5.md5String(url);
        File file = new File(CACHE_PATH, fileName);
        File parentFile = file.getParentFile();
        if (!parentFile.exists())
        {
            parentFile.mkdirs();
        }
        //bitmap写入文件
        try
        {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            //判空
            assert bitmap != null;
            //位图的压缩格式有 JPEG/PNG/WEBP
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    //从本地文件中获取bitmap
    public Bitmap getBitmapFromLocal(String url)
    {
        String fileName = MD5.md5String(url);
        File file = new File(CACHE_PATH, fileName);
        //从文件读取bitmap
        try
        {
            FileInputStream fileInputStream = new FileInputStream(file);
            Bitmap bitmap = BitmapFactory.decodeStream(fileInputStream);
            return bitmap;
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return null;
    }

}
