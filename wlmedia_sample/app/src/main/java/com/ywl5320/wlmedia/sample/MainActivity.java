package com.ywl5320.wlmedia.sample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void play_audio(View view) {

        Intent intent = new Intent(this, PlayAudioActivity.class);
        startActivity(intent);

    }

    public void play_video(View view) {
        Intent intent = new Intent(this, PlayVideoActivity.class);
        startActivity(intent);
    }

}
