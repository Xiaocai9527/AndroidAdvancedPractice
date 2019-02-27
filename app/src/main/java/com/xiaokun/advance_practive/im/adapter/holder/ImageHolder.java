package com.xiaokun.advance_practive.im.adapter.holder;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.xiaokun.advance_practive.R;
import com.xiaokun.advance_practive.artimgloader.ImageResizer;
import com.xiaokun.advance_practive.im.database.bean.PdMessage;
import com.xiaokun.advance_practive.im.database.bean.msgBody.PdImgMsgBody;
import com.xiaokun.advance_practive.im.entity.Message;

import java.util.Map;

/**
 * <pre>
 *      作者  ：肖坤
 *      时间  ：2019/02/27
 *      描述  ：
 *      版本  ：1.0
 * </pre>
 */
public class ImageHolder extends BaseMsgHolder {

    private static final String TAG = "ImageHolder";

    public ImageHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bind(Message message) {
        super.bind(message);
        ImageView imageView = (ImageView) getView(R.id.iv_img);
        Glide.with(mContext).load(message.avatarUrl).apply(RequestOptions.bitmapTransform(new CircleCrop())
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher))
                .into((ImageView) getView(R.id.iv_avatar));

        Glide.with(mContext).asBitmap().load(((PdImgMsgBody) message.pdMsgBody).remoteUrl)
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(20))
                        .placeholder(R.mipmap.ic_launcher)
                        .error(R.mipmap.ic_launcher))
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        int width = resource.getWidth();
                        int height = resource.getHeight();
                        Log.e(TAG, "onResourceReady(" + TAG + ".java:" + Thread.currentThread().getStackTrace()[2].getLineNumber() + ")" +
                                "width:" + width + ";height:" + height
                        );

                        int i = calculateSize(width, height, imageView.getMinimumWidth(), imageView.getMinimumHeight());
                        Bitmap resizedBitmap = getResizedBitmap(resource, width / i, height / i);
                        imageView.setImageBitmap(resizedBitmap);

                        imageView.post(new Runnable() {
                            @Override
                            public void run() {
                                Log.e(TAG, "onResourceReady(" + TAG + ".java:" + Thread.currentThread().getStackTrace()[2].getLineNumber() + ")" +
                                        imageView.getWidth() + ";" + imageView.getHeight()
                                );
                                ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
                            }
                        });

                    }
                });

//        Glide.with(mContext).load(((PdImgMsgBody) message.pdMsgBody).remoteUrl).apply(RequestOptions.bitmapTransform(new RoundedCorners(4))
//                .placeholder(R.mipmap.ic_launcher)
//                .error(R.mipmap.ic_launcher))
//                .into((ImageView) getView(R.id.iv_img));

        setVisible(R.id.pb_status, message.msgStatus == PdMessage.PDMessageStatus.DELIVERING);
        if (message.msgStatus == PdMessage.PDMessageStatus.FAIL) {
            setVisible(R.id.iv_send_failed, true);
        }
    }

    private int calculateSize(int originWidth, int originHeight, int minWidth, int minHeight) {
        int inSampleSize = 1;
        if (originWidth > minWidth || originHeight > minHeight) {
            int halfWidth = originWidth / 2;
            int halfHeight = originHeight / 2;

            //保证压缩后的宽和高,比展示的宽和高都要大
            while ((halfWidth / inSampleSize) > minWidth && (halfHeight / inSampleSize) > minHeight) {
                inSampleSize *= 2;
            }
        }

        Log.d(TAG, "sampleSize:" + inSampleSize);
        return inSampleSize;

    }


    public static int computeSizeByLuban(int srcWidth, int srcHeight) {
        int sampleSize;
        srcWidth = srcWidth % 2 == 1 ? srcWidth + 1 : srcWidth;
        srcHeight = srcHeight % 2 == 1 ? srcHeight + 1 : srcHeight;
        srcWidth = srcWidth > srcHeight ? srcHeight : srcWidth;
        srcHeight = srcWidth > srcHeight ? srcWidth : srcHeight;
        double scale = ((double) srcWidth / srcHeight);
        if (scale <= 1 && scale > 0.5625) {
            if (srcHeight < 1664) {
                sampleSize = 1;
            } else if (srcHeight >= 1664 && srcHeight < 4990) {
                sampleSize = 2;
            } else if (srcHeight >= 4990 && srcHeight < 10240) {
                sampleSize = 4;
            } else {
                sampleSize = srcHeight / 1280 == 0 ? 1 : srcHeight / 1280;
            }
        } else if (scale <= 0.5625 && scale > 0.5) {
            sampleSize = srcHeight / 1280 == 0 ? 1 : srcHeight / 1280;
        } else {
            sampleSize = (int) Math.ceil(srcHeight / (1280.0 / scale));
        }
        return sampleSize;
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        //bm.recycle();
        return resizedBitmap;
    }

}

