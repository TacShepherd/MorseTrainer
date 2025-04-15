package com.example.morsetrainer;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    private
    ImageView telegraphKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.practice_menu);
    }

    public void listenButtonOnClick(View v){
        setContentView(R.layout.listen);

        MediaPlayer.OnCompletionListener completionListener = new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                releaseMediaPlayer();
            }
        };
    }
    public void tapButtonOnClick(View v){
        setContentView(R.layout.tap);
        telegraphKey = findViewById(R.id.telegraphKey);
        telegraphKey.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN||event.getAction()==MotionEvent.ACTION_CANCEL) {
                    telegraphKey.setImageResource(R.drawable.pressed_telegraph_key);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    telegraphKey.setImageResource(R.drawable.depressed_telegraph_key);
                }
                return true;
            }
        });
    }
    public void backButtonOnClick(View v){setContentView(R.layout.practice_menu);}

    //Clean up the media player by releasing its resources.
    private void releaseMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }

    public void playButtonOnClick(){
        mediaPlayer = MediaPlayer.create(this, R.raw.listen1);
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(completionListener);
    }
}