package com.example.fitapp;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.Locale;

public class TimerWork extends AppCompatActivity {
    private static final long START_TIME_IN_MILLIS = 3600000;
    boolean timerRunning = false;
    private long timeLeft = START_TIME_IN_MILLIS;
    IntentFilter intentFilter;
    Integer integerTime;
    Integer integerTimeSet;

    Button btnStart, btnStop;
    TextView timer_set;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_timer_work);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.FOREGROUND_SERVICE}, PackageManager.PERMISSION_GRANTED);

        btnStart = (Button) findViewById(R.id.b_work_left);
        btnStop = (Button) findViewById(R.id.b_work_right);
        timer_set = (TextView) findViewById(R.id.tv_work_time_set);

        intentFilter = new IntentFilter();
        intentFilter.addAction("Counter");
        timer_set.setText(String.format(Locale.getDefault(), "%02d:%02d",START_TIME_IN_MILLIS/1000/60,START_TIME_IN_MILLIS/1000%60));

        Intent intentService = new Intent(getBaseContext(), MyService.class);
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                integerTime = intent.getIntExtra("TimeRemaining", 0);
                timeLeft = integerTime;
                timer_set.setText(String.format(Locale.getDefault(), "%02d:%02d",integerTime/1000/60,integerTime/1000%60));
                if (timeLeft == 0) {
                    Intent intentNew = new Intent(getBaseContext(), Transition.class);
                    startActivity(intentNew);
                    timerRunning = false;
                    timeLeft = START_TIME_IN_MILLIS;
                    finish();
                }
            }
        };
        registerReceiver(broadcastReceiver, intentFilter);


        btnStart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (timerRunning ){
                    stopService(intentService);
                    timerRunning = false;
                    btnStart.setText(getBaseContext().getResources().getString(R.string.button_resume));
                }
                else {
                    integerTimeSet = Integer.parseInt(String.valueOf(timeLeft));
                    intentService.putExtra("TimeValue", integerTimeSet);
                    startService(intentService);
                    btnStart.setText(getBaseContext().getResources().getString(R.string.button_pause));
                    timerRunning = true;
                }
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(intentService);
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                intent.putExtra("rejectWelcomeScreen", "yes");
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {

    }
}
