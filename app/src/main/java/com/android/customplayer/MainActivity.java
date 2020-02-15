package com.android.customplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.customplayer.model.CustomTimeInfo;
import com.android.customplayer.model.VideoSizeInfo;
import com.android.customplayer.opengl.CustomGLSurfaceView;
import com.android.customplayer.utils.LogUtils;
import com.android.customplayer.utils.ScreenUtils;
import com.android.customplayer.utils.TimeUtils;

public class MainActivity extends AppCompatActivity {

    private static final int MSG_UPDATE_PROGRESS = 0x1;
    private static final int MSG_VIDEO_SIZE_CHANGED = 0x2;

    private int mScreenWidth = 0;
    private int mScreenHeight = 0;
    private CustomPlayer mPlayer;
    private CustomGLSurfaceView mVideoView;
    private TextView mTimeView;
    private SeekBar mProgressView;
    private int mPosition;
    private boolean isSeeking = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mVideoView = (CustomGLSurfaceView) findViewById(R.id.video_view);
        mProgressView = (SeekBar) findViewById(R.id.progress_view);
        mTimeView = (TextView) findViewById(R.id.time_view);
        mScreenWidth = ScreenUtils.getScreenWidth(this);
        mScreenHeight = ScreenUtils.getScreenHeight(this);

        initPlayer();

    }

    private void initPlayer() {
        mPlayer = new CustomPlayer();
        mPlayer.setGLSurfaceView(mVideoView);
        mPlayer.setOnVideoSizeChangedListener(new CustomPlayer.OnVideoSizeChanged() {
            @Override
            public void onVideoSizeChanged(int width, int height, float dar) {
                Message msg = Message.obtain();
                VideoSizeInfo info = new VideoSizeInfo(width, height, dar);
                msg.obj = info;
                msg.what = MSG_VIDEO_SIZE_CHANGED;
                mHandler.sendMessage(msg);
            }
        });

        mPlayer.setOnPreparedListener(new CustomPlayer.OnPreparedListener() {

            @Override
            public void onPrepared() {
                LogUtils.d("准备好了，可以开始播放声音了");
                mPlayer.start();
            }
        });

        mPlayer.setOnLoadListener(new CustomPlayer.OnLoadListener() {

            @Override
            public void onLoad(boolean load) {
                if(load) {
                    LogUtils.d("加载中...");
                }
                else {
                    LogUtils.d("播放中...");
                }
            }
        });

        mPlayer.setOnPauseResumeListener(new CustomPlayer.OnPauseResumeListener() {
            @Override
            public void onPause(boolean pause) {
                if(pause) {
                    LogUtils.d("暂停中...");
                }
                else {
                    LogUtils.d("播放中...");
                }
            }
        });

        mPlayer.setOnTimeUpdateListener(new CustomPlayer.OnTimeUpdateListener() {

            @Override
            public void onTimeUpdate(CustomTimeInfo timeInfoBean) {
                Message message = Message.obtain();
                message.what = MSG_UPDATE_PROGRESS;
                message.obj = timeInfoBean;
                mHandler.sendMessage(message);

            }
        });

        mPlayer.setOnErrorListener(new CustomPlayer.OnErrorListener() {
            @Override
            public void onError(int code, String msg) {
                LogUtils.d("code:" + code + ", msg:" + msg);
            }
        });

        mPlayer.setOnCompleteListener(new CustomPlayer.OnCompleteListener() {
            @Override
            public void onComplete() {
                LogUtils.d("播放完成了");
            }
        });

        mProgressView.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mPosition = progress * mPlayer.getDuration() / 100;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isSeeking = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mPlayer.seek(mPosition);
                isSeeking = false;
            }
        });
    }

    private void updateVideoSurfaceView(VideoSizeInfo info) {
        int videoWidth = info.getWidth();
        int videoHeight = info.getHeight();

        int viewWidth = mScreenWidth;
        int viewHeight = (int)(viewWidth * videoHeight * 1.0f / videoWidth);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(viewWidth, viewHeight);
        params.gravity = Gravity.CENTER;
        LogUtils.d("viewWith="+viewWidth+", viewHeight="+viewHeight);
        mVideoView.setLayoutParams(params);
    }

    public void start(View view) {
        mPlayer.setDataSource("http://gv.vivo.com.cn/appstore/gamecenter/upload/video/201701/2017011314414026850.mp4");
        mPlayer.prepare();
    }

    public void pause(View view) {
        mPlayer.pause();
    }

    public void resume(View view) {
        mPlayer.resume();
    }

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == MSG_UPDATE_PROGRESS) {
                CustomTimeInfo timeInfo = (CustomTimeInfo) msg.obj;
                mTimeView.setText(TimeUtils.secdsToDateFormat(timeInfo.getTotalTime(), timeInfo.getTotalTime())
                        + "/" + TimeUtils.secdsToDateFormat(timeInfo.getCurrentTime(), timeInfo.getTotalTime()));


                if(!isSeeking && timeInfo.getTotalTime() > 0) {
                    mProgressView.setProgress(timeInfo.getCurrentTime() * 100 / timeInfo.getTotalTime());
                }
            } else if (msg.what == MSG_VIDEO_SIZE_CHANGED) {
                VideoSizeInfo info = (VideoSizeInfo) msg.obj;
                updateVideoSurfaceView(info);
            }
        }
    };

    public void stop(View view) {
        mPlayer.stop();
    }


    public void next(View view) {
        mPlayer.playNext("/sdcard/videotest/test2.mp4");
    }

}
