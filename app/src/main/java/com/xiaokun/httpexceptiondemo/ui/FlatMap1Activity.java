package com.xiaokun.httpexceptiondemo.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.xiaokun.httpexceptiondemo.R;

import java.io.File;
import java.io.FileOutputStream;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * <pre>
 *     作者   : 肖坤
 *     时间   : 2018/04/24
 *     描述   : flatMap实战1
 *     版本   : 1.0
 * </pre>
 */
public class FlatMap1Activity extends AppCompatActivity implements View.OnClickListener
{

    private static final String TAG = "FlatMap1Activity";
    private Context mContext;
    private ImageView mImageView;
    private Button mButton13;
    private TextView textView;
    private String imgUrl = "https://ws1.sinaimg.cn/large/610dc034ly1fp9qm6nv50j20u00miacg.jpg";
    private File imgFile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flat_map);
        mContext = this;
        initView();
    }

    private void initView()
    {
        mImageView = (ImageView) findViewById(R.id.imageView);
        mButton13 = (Button) findViewById(R.id.button13);
        textView = (TextView) findViewById(R.id.textView);
        initListener(mButton13);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.mipmap.ic_launcher);

        Glide.with(this).load(imgUrl).apply(requestOptions).into(mImageView);
    }

    private void initListener(View... views)
    {
        for (View view : views)
        {
            view.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.button13:
                saveImgToPhone();
//                testFlatMap();
                break;
            default:
                break;
        }
    }

    private void testFlatMap()
    {
        Observable.create(new ObservableOnSubscribe<Integer>()
        {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception
            {
                for (int i = 0; i < 3; i++)
                {
                    Log.d(TAG, "ObservableEmitter: " + i + "thread:" + Thread.currentThread());
                    e.onNext(i);
                }
                e.onComplete();
            }
        }).flatMap(new Function<Integer, ObservableSource<String>>()
        {
            @Override
            public ObservableSource<String> apply(final Integer integer) throws Exception
            {
                return Observable.create(new ObservableOnSubscribe<String>()
                {
                    @Override
                    public void subscribe(ObservableEmitter<String> e) throws Exception
                    {
                        Log.d(TAG, "subscribe: " + integer + "thread:" + Thread.currentThread());
                        if (integer == 1)
                        {
                            Thread.sleep(1000);
                        } else
                        {
                            Thread.sleep(2000);
                        }
                        e.onNext(integer + "haha");
                    }
                }).subscribeOn(Schedulers.io());//指定上面subscribe子线程
            }
        }).subscribeOn(Schedulers.io())//指定第一个subscribe子线程
                .observeOn(AndroidSchedulers.mainThread())//指定下面消费线程ui线程
                .subscribe(new Consumer<String>()
                {
                    @Override
                    public void accept(String s) throws Exception
                    {
                        Log.d(TAG, "accept: " + s + "thread:" + Thread.currentThread());
                    }
                });
    }

    private String process = "flatMap转换流程:";

    //保存图片到手机
    private void saveImgToPhone()
    {
        mButton13.setText("保存图片中...");
        mButton13.setEnabled(false);
        Observable.create(new ObservableOnSubscribe<Bitmap>()
        {
            @Override
            public void subscribe(ObservableEmitter<Bitmap> e) throws Exception
            {
                Bitmap bitmap = Glide.with(mContext).asBitmap().load(imgUrl).submit().get();
                process = process + "\n生产bitmap";
                if (bitmap == null)
                {
                    e.onError(new Exception("无法下载图片"));
                }
                e.onNext(bitmap);
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io()).flatMap(new Function<Bitmap, ObservableSource<Uri>>()
        {

            @Override
            public ObservableSource<Uri> apply(Bitmap bitmap) throws Exception
            {
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath(),
                        "http_exception");
                if (!file.exists())
                {
                    file.mkdir();
                }
                String fileName = "meizi.jpg";
                imgFile = new File(file, fileName);

                FileOutputStream fileOutputStream = new FileOutputStream(imgFile);
                assert bitmap != null;
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
                Uri uri = Uri.fromFile(imgFile);
                // 通知图库更新
                Intent scannerIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
                mContext.sendBroadcast(scannerIntent);
                process = process + "\n根据bitmap生产uri";
                return Observable.just(uri);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Uri>()
                {
                    @Override
                    public void accept(Uri uri) throws Exception
                    {
                        String msg = String.format("图片已保存至 %s 文件夹", imgFile.getAbsoluteFile());
                        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                        mButton13.setEnabled(true);
                        mButton13.setText("保存图片");
                        textView.append(process + "\n消费uri");
                    }
                }, new Consumer<Throwable>()
                {
                    @Override
                    public void accept(Throwable throwable) throws Exception
                    {
                        Toast.makeText(mContext, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        mButton13.setEnabled(true);
                        mButton13.setText("保存图片");
                    }
                });
    }
}
