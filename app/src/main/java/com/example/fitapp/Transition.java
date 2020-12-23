package com.example.fitapp;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class Transition extends AppCompatActivity {
    View background;
    LinearLayout splashtext, practices;
    Button btn_short, btn_medium, btn_long;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_timer_transition);


        mediaPlayer = MediaPlayer.create(getBaseContext(), Settings.System.DEFAULT_ALARM_ALERT_URI);

            mediaPlayer.start();

        background = (View) findViewById(R.id.v_transition_bgapp);
        splashtext = (LinearLayout) findViewById(R.id.ll_transition_textsplash);
        practices = (LinearLayout) findViewById(R.id.ll_transition_practices);
        btn_short = (Button) findViewById(R.id.b_transition_short);
        btn_medium = (Button) findViewById(R.id.b_transition_medium);
        btn_long = (Button) findViewById(R.id.b_transition_long);

        background.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent = new Intent(getBaseContext(), MyService.class);
                stopService(intent);
                mediaPlayer.stop();
                splashtext.setVisibility(View.INVISIBLE);
                practices.setVisibility(View.VISIBLE);
                return false;
            }
        });

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int practiceMode=0;
                switch (v.getId()) {
                    case R.id.b_transition_short:
                        practiceMode = 5;
                        break;
                    case R.id.b_transition_medium:
                        practiceMode = 10;
                        break;
                    case R.id.b_transition_long:
                        practiceMode = 15;
                        break;
                    default:
                        break;
                }
                TimerExercise myFragment = new TimerExercise();
                Bundle bundle = new Bundle();
                bundle.putInt("practice mode", practiceMode);
                myFragment.setArguments(bundle);
                // получаем экземпляр FragmentTransaction
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager
                        .beginTransaction();
                fragmentTransaction.add(R.id.fragment_container, myFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        };
        btn_medium.setOnClickListener(onClickListener);
        btn_short.setOnClickListener(onClickListener);
        btn_long.setOnClickListener(onClickListener);
    }
}
