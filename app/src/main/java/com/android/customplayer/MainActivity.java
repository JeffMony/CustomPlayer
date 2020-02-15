package com.android.customplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.customplayer.model.CustomTimeInfo;
import com.android.customplayer.opengl.CustomGLSurfaceView;
import com.android.customplayer.utils.LogUtils;
import com.android.customplayer.utils.TimeUtils;

import java.io.File;

public class MainActivity extends AppCompatActivity {

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

        initPlayer();

    }

    private void initPlayer() {
        mPlayer = new CustomPlayer();
        mPlayer.setGLSurfaceView(mVideoView);
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
                message.what = 1;
                message.obj = timeInfoBean;
                handler.sendMessage(message);

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

    public void start(View view) {
        mPlayer.setDataSource("/sdcard/tencent/MicroMsg/WeiXin/1579791536418.mp4");
        mPlayer.prepare();
    }

    public void pause(View view) {
        mPlayer.pause();
    }

    public void resume(View view) {
        mPlayer.resume();
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1)
            {
                CustomTimeInfo timeInfo = (CustomTimeInfo) msg.obj;
                mTimeView.setText(TimeUtils.secdsToDateFormat(timeInfo.getTotalTime(), timeInfo.getTotalTime())
                        + "/" + TimeUtils.secdsToDateFormat(timeInfo.getCurrentTime(), timeInfo.getTotalTime()));


                if(!isSeeking && timeInfo.getTotalTime() > 0)
                {
                    mProgressView.setProgress(timeInfo.getCurrentTime() * 100 / timeInfo.getTotalTime());
                }
            }
        }
    };

    public void stop(View view) {
        mPlayer.stop();
    }


    public void next(View view) {
        mPlayer.playNext(Environment.getExternalStorageDirectory().getAbsolutePath() + "/黄f鸿之南北y雄.1080p.mp4");
    }

}
