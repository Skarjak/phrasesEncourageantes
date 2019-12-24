package com.example.encourage;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static android.media.MediaPlayer.SEEK_CLOSEST;
import static java.lang.Math.*;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private TextView musicCredit;
    private Button musicToggle;
    private String[] sentences;
    private ObjectAnimator fadeIn;
    private ObjectAnimator fadeOut;
    private int fadeInTime;
    private int fadeOutTime;
    private MediaPlayer mediaPlayer;
    private int musicPosition = 0;
    private static final String TAG = "Main activity";
    static final String STATE_MUSIC = "musicPosition";
    static final String STATE_SILENCED = "musicSilenced";
    boolean silenced = false;
    private int lastSeed = 0;
    private int currentSeed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Resources res = getResources();
        sentences = res.getStringArray(R.array.sentences);
        textView = findViewById(R.id.textView);
        musicCredit = findViewById(R.id.musicCredit);
        musicToggle = findViewById(R.id.musicToggle);
        textView.setText(res.getString(R.string.first));
        fadeInTime = 800;
        fadeOutTime = 600;
        if(savedInstanceState != null) {
            musicPosition = savedInstanceState.getInt(STATE_MUSIC);
            silenced = savedInstanceState.getBoolean(STATE_SILENCED);
        }
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.starling);
        mediaPlayer.setLooping(true);
        fadeOut = ObjectAnimator.ofFloat(musicCredit,"alpha",0f);
        fadeOut.setDuration(4000);
        fadeOut.start();
        //next line only supported in api version 26
        mediaPlayer.seekTo(musicPosition, SEEK_CLOSEST);
        if (!silenced) {
            musicToggle.setForeground(getDrawable(R.drawable.music_on));
            mediaPlayer.start();
        }else{
            musicToggle.setForeground(getDrawable(R.drawable.music_off));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        savedInstanceState.putInt(STATE_MUSIC, musicPosition);
        savedInstanceState.putBoolean(STATE_SILENCED, silenced);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onPause(){
        super.onPause();
        //mediaPlayer.pause();
        //musicPosition = mediaPlayer.getCurrentPosition();
        musicPosition = mediaPlayer.getCurrentPosition();
        mediaPlayer.pause();
        //mediaPlayer.release();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
    }

    @Override
    protected void onStart(){
        super.onStart();
        mediaPlayer.seekTo(musicPosition, SEEK_CLOSEST);
        if (!silenced) {
            mediaPlayer.start();
        }
    }

  //  @Override
  //  protected void onStop(){
  //      super.onStop();
  //      mediaPlayer.stop();
  //      mediaPlayer.release();
  //  }

    //called when screen is tapped
    public void tapSentence(View view) {
        fetchSentence();
    }

    //changes displayed text
    private void fetchSentence() {
        currentSeed = (int)floor(random()*sentences.length);
        while(currentSeed == lastSeed) {
            currentSeed = (int)floor(random()*sentences.length);
        }
        fadeIn = ObjectAnimator.ofFloat(textView,"alpha", 1f);
        fadeIn.setDuration(fadeInTime);
        fadeOut = ObjectAnimator.ofFloat(textView,"alpha", 0f);
        fadeOut.setDuration(fadeOutTime);
        fadeOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                textView.setText(sentences[currentSeed]);
                lastSeed = currentSeed;
                fadeIn.start();
            }
        });
        fadeOut.start();
    }

    //toggles background music
    public void tapMusic(View view){
        if (mediaPlayer.isPlaying()){
            musicToggle.setForeground(getDrawable(R.drawable.music_off));
            mediaPlayer.pause();
            silenced = true;
        }else{
            musicToggle.setForeground(getDrawable(R.drawable.music_on));
            mediaPlayer.start();
            silenced = false;
        }
    }
}

