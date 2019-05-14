package com.ywl5320.wlmedia.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ywl5320.wlmedia.WlMedia;
import com.ywl5320.wlmedia.enums.WlCodecType;
import com.ywl5320.wlmedia.enums.WlMute;
import com.ywl5320.wlmedia.enums.WlPlayModel;
import com.ywl5320.wlmedia.enums.WlSampleRate;
import com.ywl5320.wlmedia.enums.WlScaleType;
import com.ywl5320.wlmedia.listener.WlOnCompleteListener;
import com.ywl5320.wlmedia.listener.WlOnErrorListener;
import com.ywl5320.wlmedia.listener.WlOnLoadListener;
import com.ywl5320.wlmedia.listener.WlOnPauseListener;
import com.ywl5320.wlmedia.listener.WlOnPcmDataListener;
import com.ywl5320.wlmedia.listener.WlOnPreparedListener;
import com.ywl5320.wlmedia.listener.WlOnTimeInfoListener;
import com.ywl5320.wlmedia.listener.WlOnVideoViewListener;
import com.ywl5320.wlmedia.util.WlTimeUtil;
import com.ywl5320.wlmedia.widget.WlSurfaceView;

public class PlayVideoActivity extends AppCompatActivity {

    private WlMedia wlMedia;

    private SeekBar seekBar;
    private SeekBar seekBarVolume;
    private TextView tvTime;
    private TextView tvVolume;
    private WlSurfaceView wlSurfaceView;

    private double duration;
    private int seek_per = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);

        seekBar = findViewById(R.id.seek_bar);
        seekBarVolume = findViewById(R.id.seek_bar_volume);
        tvTime = findViewById(R.id.tv_time);
        tvVolume = findViewById(R.id.tv_volume);
        wlSurfaceView = findViewById(R.id.wlsurfaceview);

        wlMedia = new WlMedia();
        wlMedia.setPlayModel(WlPlayModel.PLAYMODEL_AUDIO_VIDEO);//声音视频都播放
        wlMedia.setCodecType(WlCodecType.CODEC_MEDIACODEC);//优先使用硬解码
        wlMedia.setMute(WlMute.MUTE_CENTER);//立体声
        wlMedia.setVolume(80);//80%音量
        wlMedia.setPlayPitch(1.0f);//正常速度
        wlMedia.setPlaySpeed(1.0f);//正常音调
        wlMedia.setRtspTimeOut(30);//网络流超时时间
//        wlMedia.setShowPcmData(true);//回调返回音频pcm数据
        wlMedia.setSampleRate(WlSampleRate.RATE_44100);//设置音频采样率为指定值（返回的PCM就是这个采样率）
        wlSurfaceView.setWlMedia(wlMedia);//给视频surface设置播放器

        tvVolume.setText("音量：" + wlMedia.getVolume() + "%");
        seekBarVolume.setProgress(wlMedia.getVolume());

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                seek_per = (int) (seekBar.getProgress() * duration / 100);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                wlMedia.seekStart(false);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                wlMedia.seek(seek_per);
            }
        });

        seekBarVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                wlMedia.setVolume(seekBar.getProgress());
                tvVolume.setText("音量：" + wlMedia.getVolume() + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        wlMedia.setOnPreparedListener(new WlOnPreparedListener() {
            @Override
            public void onPrepared() {
                wlMedia.setVideoScale(WlScaleType.SCALE_16_9);
                wlMedia.start();
                duration = wlMedia.getDuration();
            }
        });

        wlMedia.setOnTimeInfoListener(new WlOnTimeInfoListener() {
            @Override
            public void onTimeInfo(double time) {
                seekBar.setProgress((int) (time * 100 / duration));
                tvTime.setText(WlTimeUtil.secdsToDateFormat((int)time) + "/" + WlTimeUtil.secdsToDateFormat((int)duration));
            }
        });

        wlMedia.setOnLoadListener(new WlOnLoadListener() {
            @Override
            public void onLoad(boolean load) {
                if(load)
                {
                    Log.d("ywl5320", "加载中");
                }
                else
                {
                    Log.d("ywl5320", "播放中");
                }
            }
        });

        wlMedia.setOnErrorListener(new WlOnErrorListener() {
            @Override
            public void onError(int code, String msg) {
                Log.d("ywl5320", "code is :" + code + ", msg is :" + msg);
            }
        });

        wlMedia.setOnCompleteListener(new WlOnCompleteListener() {
            @Override
            public void onComplete() {
                Log.d("ywl5320", "播放完成");
            }
        });

        wlMedia.setOnPauseListener(new WlOnPauseListener() {
            @Override
            public void onPause(boolean pause) {
                if(pause)
                {
                    Log.d("ywl5320", "暂停中");
                }
                else
                {
                    Log.d("ywl5320", "继续播放");
                }
            }
        });

        wlMedia.setOnPcmDataListener(new WlOnPcmDataListener() {
            @Override
            public void onPcmInfo(int bit, int channel, int samplerate) {
                Log.d("ywl5320", "pcm info samplerate :" + samplerate);
            }

            @Override
            public void onPcmData(int size, byte[] data) {
                Log.d("ywl5320", "pcm data size :" + size);
            }
        });

        wlSurfaceView.setOnVideoViewListener(new WlOnVideoViewListener() {
            @Override
            public void initSuccess() {
                wlMedia.setSource("/mnt/shared/Other/testvideo/楚乔传第一集.mp4");
                wlMedia.prepared();
            }

            @Override
            public void moveSlide(double value) {
                tvTime.setText(WlTimeUtil.secdsToDateFormat((int)value) + "/" + WlTimeUtil.secdsToDateFormat((int)wlMedia.getDuration()));
            }

            @Override
            public void movdFinish(double value) {
                wlMedia.seek((int) value);
            }
        });

    }

    public void play(View view) {

        wlMedia.setSource("/mnt/shared/Other/testvideo/楚乔传第一集.mp4");
        wlMedia.prepared();

    }

    public void stop(View view) {
        wlMedia.stop();
    }

    public void pause(View view) {
        wlMedia.pause();
    }

    public void resume(View view) {
        wlMedia.resume();
    }

    public void change(View view) {
        wlMedia.setSource("/storage/sdcard1/恐龙王.1080p.HD国语中字无水印[最新电影www.66ys.tv].mp4");
        wlMedia.next();
    }

    @Override
    protected void onPause() {
        super.onPause();
        wlMedia.pause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        wlMedia.onDestroy();
    }

    public void speed_half(View view) {
        wlMedia.setPlaySpeed(0.5f);
    }

    public void speed_normal(View view) {
        wlMedia.setPlaySpeed(1.0f);
    }

    public void speed_fast(View view) {
        wlMedia.setPlaySpeed(1.5f);
    }

    public void pitch_half(View view) {
        wlMedia.setPlayPitch(0.5f);
    }

    public void pitch_normal(View view) {
        wlMedia.setPlayPitch(1.0f);
    }

    public void pitch_fast(View view) {
        wlMedia.setPlayPitch(1.5f);
    }

    public void left(View view) {
        wlMedia.setMute(WlMute.MUTE_LEFT);
    }

    public void center(View view) {
        wlMedia.setMute(WlMute.MUTE_CENTER);
    }

    public void right(View view) {
        wlMedia.setMute(WlMute.MUTE_RIGHT);
    }

    public void full(View view) {
        wlMedia.setVideoScale(WlScaleType.SCALE_FULL_SURFACE);
    }

    public void sdefault(View view) {
        wlMedia.setVideoScale(WlScaleType.SCALE_DEFAULT);
    }

    public void s_4_3(View view) {
        wlMedia.setVideoScale(WlScaleType.SCALE_4_3);
    }

    public void s_16_9(View view) {
        wlMedia.setVideoScale(WlScaleType.SCALE_16_9);
    }

    public void custom(View view) {
        wlMedia.setVideoScale(300, 200, 0);
    }
}
