// Importing Required packages
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


// Main Activity class.
public class MainActivity extends AppCompatActivity {
    //Declaring variables used inside the class.
    MediaPlayer mediaPlayer;
    MaterialButton playPauseBtn;
    int songNo = 0;
    AnimationDrawable animate;
    SeekBar seekBar;
    MaterialButtonToggleGroup playPause;
    TextView textView, userTime, trackTime;
    String[] songName = {"song_one","song_two","song_three"};
    Thread updateSeekBar;
    ImageButton previous, next;
    //On create method.
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Initializing the variables declared outside the class.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        playPause = findViewById(R.id.playPause);
        playPauseBtn = findViewById(R.id.PlayPauseBtn);
        textView = findViewById(R.id.SongName);
        userTime = findViewById(R.id.userTime);
        trackTime = findViewById(R.id.trackTime);
        next = findViewById(R.id.nextSong);
        previous = findViewById(R.id.previousSong);
        View gradientTheme = findViewById(R.id.gradientTheme);
        textView.setText(songName[songNo]);
        mediaPlayer = MediaPlayer.create(MainActivity.this,getResources().getIdentifier(songName[songNo],"raw",getPackageName()));
        animate = (AnimationDrawable) gradientTheme.getBackground();
        animate.setExitFadeDuration(4000);
        animate.setEnterFadeDuration(2000);
        trackTime.setText(updateTime(mediaPlayer.getDuration()));

        // Creating a Thread for seekbar to be updated.
        updateSeekBar = new Thread(){
            @Override
            public void run() {
                int totalDuration = mediaPlayer.getDuration();
                int currentPosition = 0;
                while(currentPosition<totalDuration){
                    currentPosition = mediaPlayer.getCurrentPosition();
                    seekBar.setProgress(currentPosition);
                }
                super.run();
            }
        };

        //Calling Method for updating the seekbar data to the media player.
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
                userTime.setText(updateTime(mediaPlayer.getCurrentPosition()));
            }
        });
        seekBar.setMax(mediaPlayer.getDuration());
        //calling Thread
        updateSeekBar.start();
        //Overriding the Runnable thread using Handler.
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                userTime.setText(updateTime(mediaPlayer.getCurrentPosition()));
                handler.postDelayed(this,500);
            }
        }, 500);
        // Play pause Toggle button act as a Floating button.
        playPause.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
            @Override
            //Method to play or pause the Song.
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                if(isChecked) {
                    animate.start();
                    mediaPlayer.start();
                    trackTime.setText(updateTime(mediaPlayer.getDuration()));
                    System.out.println("true");
                    playPauseBtn.setIconResource(R.drawable.pause);
                    seekBar.setMax(mediaPlayer.getDuration());
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            next.performClick();
                        }
                    });
                }
                else {
                   mediaPlayer.pause();
                   animate.stop();
                    playPauseBtn.setIconResource(R.drawable.play_ic);
                    System.out.println("false");
                }
            }
        });
        //Calling the button method to change the track.
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    if(songNo <songName.length-1){
                        songNo+=1;
                    }
                    else if(songNo==songName.length-1)
                        songNo = 0;
                    setSong();


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

    //This method is used to update the media player with new song.
    @SuppressLint("NewApi")
    public void setSong(){
        if(mediaPlayer.isPlaying()){
            seekBar.setProgress(0);
            mediaPlayer.stop();
        }
        textView.setText(songName[songNo]);
        mediaPlayer = MediaPlayer.create(MainActivity.this,getResources().getIdentifier(songName[songNo],"raw",getPackageName()));
        playPauseBtn.setChecked(true);
        mediaPlayer.start();
        seekBar.setProgress(0);
        trackTime.setText(updateTime(mediaPlayer.getDuration()));
        seekBar.setMax(mediaPlayer.getDuration());
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                next.performClick();
            }
        });
    }
    //This Method is used to change the milli seconds to hr and min format.
    public String updateTime(int duration){
        String time = "";
        int min = (duration/1000/60);
        int sec = (duration/1000%60);
        time = min + ":" + ((sec<10)? "0" + sec:sec);
        return time;
    }


}