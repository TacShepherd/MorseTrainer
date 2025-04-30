package com.example.morsetrainer;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    private ImageView telegraphKey;
    private long lastDown, lastDuration;
    private TextView tapOutput, tapMessage, listenText;
    private EditText listenEditText;
    private String tapOutputString;
    private int questionId;
    private Handler handler = new Handler();
    private Runnable checkAnswerRunnable;
    private String[] tapQuestion = {"a._", "b_..", "c_._.", "d_..", "e.",
            "f.._.", "g__.", "h....", "i..", "j.___", "k_._", "l._..",
            "m__", "n_.", "o___", "p.__.", "q__._", "r._.", "s...", "t_",
            "u.._", "v..._", "w.__", "x_.._", "y_.__", "z__..", "1.____",
            "2..___", "3...__", "4...._", "5.....", "6_....", "7__...",
            "8___..", "9____.", "0_____"};
    MediaPlayer.OnCompletionListener completionListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.practice_menu);
    }

    public void listenButtonOnClick(View v){
        setContentView(R.layout.listen);
        listenText = findViewById(R.id.listenText);

        completionListener = new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                releaseMediaPlayer();
            }
        };


    }
    @SuppressLint("ClickableViewAccessibility")
    public void tapButtonOnClick(View v){
        setContentView(R.layout.tap);
        telegraphKey = findViewById(R.id.telegraphKey);
        tapOutput = findViewById(R.id.tapOutput);
        tapOutputString = "";
        tapMessage = findViewById(R.id.tapMessage);
        questionId = (int)(Math.random()*36);

        //set question
        tapMessage.setText(tapQuestion[questionId].substring(0, 1));

        telegraphKey.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    telegraphKey.setImageResource(R.drawable.pressed_telegraph_key);
                    lastDown = System.currentTimeMillis();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    telegraphKey.setImageResource(R.drawable.depressed_telegraph_key);
                    lastDuration = System.currentTimeMillis() - lastDown;

                    // Determine press length
                    if (lastDuration > 150) {
                        tapOutputString += "_";  // long press
                    } else {
                        tapOutputString += ".";  // short press
                    }

                    tapOutput.setText(tapOutputString);

                    //check for inactive button
                    handler.removeCallbacks(checkAnswerRunnable);
                    checkAnswerRunnable = new Runnable() {
                        @Override
                        public void run() {
                            if (tapQuestion[questionId].substring(1).equals(tapOutputString)) {
                                tapOutput.setText("Correct!");
                                questionId = (int)(Math.random()*36);
                                tapMessage.setText(tapQuestion[questionId].substring(0, 1));
                            } else {
                                tapOutput.setText("Incorrect: " + tapOutputString);
                            }
                            tapOutputString = "";
                        }
                    };
                    //2 seconds of inactivity before answer is checked
                    handler.postDelayed(checkAnswerRunnable, 2000);
                }
                return true;
            }
        });

    }
    public void backButtonOnClick(View v){
        setContentView(R.layout.practice_menu);

    }

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

    public void playButtonOnClick(View v){
        mediaPlayer = MediaPlayer.create(this, R.raw.a);
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(completionListener);
    }

    public void checkButtonOnClick(View v){
        if(listenEditText.getText().equals("a")){
            listenText.setText("Correct!");
        } else{
            listenText.setText("Incorrect :(");
        }
    }
}