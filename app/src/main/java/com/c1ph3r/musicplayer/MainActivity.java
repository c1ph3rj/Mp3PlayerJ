package com.c1ph3r.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;

public class MainActivity extends AppCompatActivity {
    Runnable runnable;
    Handler handler;
    MediaPlayer mediaPlayer;
    MaterialButton playPauseBtn;
    int songNo = 0;
    AnimationDrawable animate;
    SeekBar seekBar;
    MaterialButtonToggleGroup playPause;
    TextView textView;
    String[] songName = {"song_one","song_two","song_three"};
    ImageButton previous, next;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        seekBar = findViewById(R.id.seekBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        playPause = findViewById(R.id.playPause);
        playPauseBtn = findViewById(R.id.PlayPauseBtn);
        textView = findViewById(R.id.SongName);
        next = findViewById(R.id.nextSong);
        previous = findViewById(R.id.previousSong);
        View gradientTheme = findViewById(R.id.gradientTheme);
        textView.setText(songName[songNo]);
        mediaPlayer = MediaPlayer.create(MainActivity.this,getResources().getIdentifier(songName[songNo],"raw",getPackageName()));
        animate = (AnimationDrawable) gradientTheme.getBackground();
        animate.setExitFadeDuration(4000);
        animate.setEnterFadeDuration(2000);

        runnable = new Runnable() {
            @Override
            public void run() {
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                handler.postDelayed(this, 500);
            }
        };

        playPause.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                if(isChecked) {
                    animate.start();
                    mediaPlayer.start();
                    System.out.println("true");
                    playPauseBtn.setIconResource(R.drawable.pause);
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            next.performClick();
                        }
                    });
                    seekBar.setMax(mediaPlayer.getDuration());
                    handler.postDelayed(runnable, 0);
                }
                else {
                   mediaPlayer.pause();
                   animate.stop();
                    playPauseBtn.setIconResource(R.drawable.play_ic);
                    System.out.println("false");
                    handler.removeCallbacks(runnable);
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!mediaPlayer.isPlaying()){
                    playPauseBtn.toggle();
                }else{
                    if(songNo <songName.length-1)
                        songNo+=1;
                    else if(songNo==songName.length-1)
                        songNo = 0;
                    setSong();
                }

            }
        });
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!mediaPlayer.isPlaying()){
                    playPauseBtn.toggle();
                }else {
                    if (songNo >= 1)
                        songNo -= 1;
                    else if (songNo == 0)
                        songNo = songName.length - 1;
                    setSong();
                }
            }
        });

    }


    @SuppressLint("NewApi")
    public void setSong(){
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }
        textView.setText(songName[songNo]);
        mediaPlayer = MediaPlayer.create(MainActivity.this,getResources().getIdentifier(songName[songNo],"raw",getPackageName()));
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                next.performClick();
            }
        });
    }

}