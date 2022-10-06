package com.c1ph3r.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.android.material.button.MaterialButtonToggleGroup;

public class MainActivity extends AppCompatActivity {
    MediaPlayer mediaPlayer;
    Button button;
    MaterialButtonToggleGroup playPause;
    TextView textView;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        playPause = findViewById(R.id.playPause);
        button = findViewById(R.id.PlayPauseBtn);
        textView = findViewById(R.id.textView);
        View gradientTheme = findViewById(R.id.gradientTheme);
        String[] songName = {"song_one","song_two","song_three"};
        mediaPlayer = MediaPlayer.create(MainActivity.this,getResources().getIdentifier(songName[2],"raw",getPackageName()));
        AnimationDrawable animate = (AnimationDrawable) gradientTheme.getBackground();
        animate.setExitFadeDuration(4000);
        animate.setEnterFadeDuration(2000);

        playPause.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                if(isChecked){
                    if(button.getId()== (checkedId)){
                        animate.start();
                        mediaPlayer.start();
                        System.out.println("true");
                    }

                }else {
                   mediaPlayer.pause();
                   animate.stop();
                    System.out.println("false");
                }
            }
        });
    }

}