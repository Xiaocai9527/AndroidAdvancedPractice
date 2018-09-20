package com.xiaokun.httpexceptiondemo.ui;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.xiaokun.httpexceptiondemo.R;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by 肖坤 on 2018/9/21.
 * <p>
 * 解决MediaPlayer频繁切换歌曲时,由于reset方法耗时太长而造成的ANR。
 * 参考：https://blog.csdn.net/I_do_can/article/details/72626069
 *
 * @author 肖坤
 * @date 2018/9/21
 */

public class MusicTestActivity extends AppCompatActivity {

    private static final String TAG = "MusicTestActivity";
    int position = 0;
    private List<String> mSongUrl;

    private final static int MEDIA_PLAYER_NUM = 4;
    private ExecutorService mExecutorService = Executors.newScheduledThreadPool(MEDIA_PLAYER_NUM);
    private Queue<MediaPlayer> mMediaPlayerQueue = new ArrayDeque<>();
    private Queue<MediaPlayer> mRecycleQueue = new ArrayDeque<>();
    private MediaPlayer mMediaPlayer;
    private final Object mAvailableLocker = new Object();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_test);

        mSongUrl = new ArrayList<>();
        mSongUrl.add("http://dl.stream.qqmusic.qq.com/M500003OUlho2HcRHC.mp3?vkey=B6DBD17EFB4671CC5BBE5C58222A62DF40044A5BA61D0E5DC6CFE07A557FD2E4F2EF6CCBB0F0CDA9CCDCF40987604B4012ADF4ECA9F94E79&guid=1392567591&fromtag=1");
        mSongUrl.add("http://dl.stream.qqmusic.qq.com/M5000042QMDR1VzSsx.mp3?vkey=B6DBD17EFB4671CC5BBE5C58222A62DF40044A5BA61D0E5DC6CFE07A557FD2E4F2EF6CCBB0F0CDA9CCDCF40987604B4012ADF4ECA9F94E79&guid=1392567591&fromtag=1");
        mSongUrl.add("http://dl.stream.qqmusic.qq.com/M500002qU5aY3Qu24y.mp3?vkey=B6DBD17EFB4671CC5BBE5C58222A62DF40044A5BA61D0E5DC6CFE07A557FD2E4F2EF6CCBB0F0CDA9CCDCF40987604B4012ADF4ECA9F94E79&guid=1392567591&fromtag=1");
        mSongUrl.add("http://dl.stream.qqmusic.qq.com/M500003aAYrm3GE0Ac.mp3?vkey=B6DBD17EFB4671CC5BBE5C58222A62DF40044A5BA61D0E5DC6CFE07A557FD2E4F2EF6CCBB0F0CDA9CCDCF40987604B4012ADF4ECA9F94E79&guid=1392567591&fromtag=1");

        findViewById(R.id.next_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        playMusic2();
                    }
                }).start();
            }
        });
    }

    private void playMusic() {
        try {
            if (mMediaPlayer.isPlaying()) {
                Log.e(TAG, "playMusic(" + TAG + ".java:" + Thread.currentThread().getStackTrace()[2].getLineNumber() + ")" + System.currentTimeMillis());
                mMediaPlayer.stop();
            }
            Log.e(TAG, "playMusic(" + TAG + ".java:" + Thread.currentThread().getStackTrace()[2].getLineNumber() + ")" + System.currentTimeMillis());
            mMediaPlayer.reset();

            Log.e(TAG, "playMusic(" + TAG + ".java:" + Thread.currentThread().getStackTrace()[2].getLineNumber() + ")" + System.currentTimeMillis());
            mMediaPlayer.setDataSource(mSongUrl.get(position));
            Log.e(TAG, "playMusic(" + TAG + ".java:" + Thread.currentThread().getStackTrace()[2].getLineNumber() + ")" + System.currentTimeMillis());
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                }
            });
            mMediaPlayer.prepareAsync();
//            mMediaPlayer.prepare();
            Log.e(TAG, "playMusic(" + TAG + ".java:" + Thread.currentThread().getStackTrace()[2].getLineNumber() + ")" + System.currentTimeMillis());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (position < (mSongUrl.size() - 1)) {
            position++;
        } else {
            position = 0;
        }
    }

    private void playMusic2() {
        findAvailableMediaPlayer();
        try {
            mMediaPlayer.setDataSource(mSongUrl.get(position));
        } catch (IOException e) {
            e.printStackTrace();
        }
        mMediaPlayer.prepareAsync();
    }

    /**
     * create a new player and release current
     * mMediaPlayer.reset(); cost too much time
     * release() 在音频未加载完时，也是耗时操作
     * 但是用户快速点击切换播放音乐，有问题
     * <p>
     * 没有到限制播放器数量，直接新建
     * 存在 可用队列 为空 的情况，即所有的 player 都在 reset ，此时直接新建
     */
    private void findAvailableMediaPlayer() {
        try {
            if (currentPlayerNumLegal() || mMediaPlayerQueue.isEmpty()) {
                MediaPlayer mediaPlayer = new MediaPlayer();
//                mediaPlayer.setLooping(true);
                mediaPlayer.setOnPreparedListener(mPreparedListener);
                mediaPlayer.setOnCompletionListener(mCompletionListener);
                queueAvailableMediaPlayer(mediaPlayer);
            }
            if (mMediaPlayer != null) {
                // 加入回收队列，待处理 释放
                synchronized (mAvailableLocker) {
                    mRecycleQueue.add(mMediaPlayer);
                }
            }
            // 此处 mMediaPlayer not null
            synchronized (mAvailableLocker) {
                mMediaPlayer = mMediaPlayerQueue.poll();
            }

            if (mRecycleQueue.size() > 0) {
                mExecutorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        MediaPlayer player;
                        synchronized (mAvailableLocker) {
                            player = mRecycleQueue.poll();
                        }
                        player.reset();
                        queueAvailableMediaPlayer(player);
                    }
                });
            }

        } catch (Exception e) {
        }
    }

    private boolean currentPlayerNumLegal() {
        synchronized (mAvailableLocker) {
            return MEDIA_PLAYER_NUM > mMediaPlayerQueue.size() + mRecycleQueue.size();
        }
    }

    private void queueAvailableMediaPlayer(MediaPlayer player) {
        synchronized (mAvailableLocker) {
            mMediaPlayerQueue.add(player);
        }
    }

    private MediaPlayer.OnPreparedListener mPreparedListener =
            new MediaPlayer.OnPreparedListener() {

                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                }
            };

    private MediaPlayer.OnCompletionListener mCompletionListener =
            new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                }
            };

}
