package com.ywl5320.wlmedia.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ywl5320.wlmedia.WlMedia;
import com.ywl5320.wlmedia.enums.WlMute;
import com.ywl5320.wlmedia.enums.WlPlayModel;
import com.ywl5320.wlmedia.listener.WlOnCompleteListener;
import com.ywl5320.wlmedia.listener.WlOnErrorListener;
import com.ywl5320.wlmedia.listener.WlOnLoadListener;
import com.ywl5320.wlmedia.listener.WlOnPauseListener;
import com.ywl5320.wlmedia.listener.WlOnPreparedListener;
import com.ywl5320.wlmedia.listener.WlOnTimeInfoListener;
import com.ywl5320.wlmedia.util.WlTimeUtil;

public class PlayAudioActivity extends AppCompatActivity {

    private WlMedia wlMedia;

    private SeekBar seekBar;
    private SeekBar seekBarVolume;
    private TextView tvTime;
    private TextView tvVolume;

    private double duration;
    private int seek_per = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_audio);

        seekBar = findViewById(R.id.seek_bar);
        seekBarVolume = findViewById(R.id.seek_bar_volume);
        tvTime = findViewById(R.id.tv_time);
        tvVolume = findViewById(R.id.tv_volume);

        wlMedia = new WlMedia();
        wlMedia.setPlayModel(WlPlayModel.PLAYMODEL_ONLY_AUDIO);
        wlMedia.setMute(WlMute.MUTE_CENTER);
        wlMedia.setVolume(80);
        wlMedia.setPlayPitch(1.0f);
        wlMedia.setPlaySpeed(1.0f);
        wlMedia.setTimeOut(30);

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

    }

    public void play(View view) {

        wlMedia.setSource("http://mpge.5nd.com/2015/2015-11-26/69708/1.mp3");
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
        wlMedia.setSource("http://mpge.5nd.com/2015/2015-11-26/69708/1.mp3");
        wlMedia.next();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
}
