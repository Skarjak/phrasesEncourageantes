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

import static java.lang.Math.*;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private Button musicToggle;
    private String[] sentences;
    private ObjectAnimator fadeIn;
    private ObjectAnimator fadeOut;
    private int fadeInTime;
    private int fadeOutTime;
    private MediaPlayer mediaPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Resources res = getResources();
        sentences = res.getStringArray(R.array.sentences);
        textView = findViewById(R.id.textView);
        musicToggle = findViewById(R.id.musicToggle);
        textView.setText(res.getString(R.string.first));
        fadeInTime = 800;
        fadeOutTime = 600;
        //pas oublier d'attribuer la musique a Podington Bear -Starling, soundofpicture.com
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.starling);
        mediaPlayer.start();
    }

    //called when screen is tapped
    public void tapSentence(View view) {
        fetchSentence();
    }

    //changes displayed text
    private void fetchSentence() {
        fadeIn = ObjectAnimator.ofFloat(textView,"alpha", 1f);
        fadeIn.setDuration(fadeInTime);
        fadeOut = ObjectAnimator.ofFloat(textView,"alpha", 0f);
        fadeOut.setDuration(fadeOutTime);
        fadeOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                textView.setText(sentences[(int)floor(random()*sentences.length)]);
                fadeIn.start();
            }
        });
        fadeOut.start();
    }

    //test
    public void tapMusic(View view){
        musicToggle.setText("lawl");
    }
}

