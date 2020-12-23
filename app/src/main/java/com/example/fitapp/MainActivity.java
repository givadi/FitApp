package com.example.fitapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.fitapp.Model.HelpFragment;

public class MainActivity extends AppCompatActivity {
    View bgapp;
    Animation bgainm, bganimtext, frombottom,bganimtextfrombottom;
    LinearLayout textsplash, textmain, menu, exercises, start, waterBottle;
    Fragment water;
    FragmentManager fragmentManager = getSupportFragmentManager();
    SharedPreferences sharedPreferences;
    TextView checking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        // Настройка виджетов
        bgapp = (View) findViewById(R.id.v_bgapp);
        textsplash = (LinearLayout) findViewById(R.id.ll_textsplash);
        textmain = (LinearLayout) findViewById(R.id.ll_textmain);
        menu = (LinearLayout) findViewById(R.id.ll_menu);
        exercises = (LinearLayout) findViewById(R.id.ll_exercises);
        start = (LinearLayout) findViewById(R.id.ll_start);
        water = fragmentManager.findFragmentById(R.id.frg_water_bottle);
        waterBottle = (LinearLayout) water.getView().findViewById(R.id.ll_water_bottle);
        checking = (TextView)findViewById(R.id.tv_main_header);

        sharedPreferences = getSharedPreferences("mainAlreadyStarted", MODE_PRIVATE);

        if (getIntent().getStringExtra("rejectWelcomeScreen") == null){
            // Запуск приветственного экрана при первом открытии
            bgainm = AnimationUtils.loadAnimation(this, R.anim.anim_background);
            bganimtext =AnimationUtils.loadAnimation(this,R.anim.anim_text_background);
            bganimtextfrombottom = AnimationUtils.loadAnimation(this, R.anim.anim_text_background_from_bottom);
            frombottom = AnimationUtils.loadAnimation(this, R.anim.anim_from_bottom);
            waterBottle.setEnabled(false);
            exercises.setEnabled(false);
            start.setEnabled(false);
            exercises.postDelayed(new Runnable() {
                @Override
                public void run() {
                    exercises.setEnabled(true);
                    start.setEnabled(true);
                    waterBottle.setEnabled(true);
                }
            }, 3500);
            textsplash.startAnimation(bganimtextfrombottom);
            bgapp.startAnimation(bgainm);
            textsplash.startAnimation(bganimtext);
            textmain.startAnimation(frombottom);
            menu.startAnimation(frombottom);
        } else {
            bgapp.setTranslationY(-1950);
        }

        // Обработчик кнопки Старт
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TimerWork.class);
                startActivity(intent);
            }
        });
        // Обработчик кнопки Упражнения
        exercises.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(MainActivity.this, ListOfExercises.class);
                //startActivity(intent);
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager
                        .beginTransaction();

                // добавляем фрагмент
                ListOfExercises myFragment = new ListOfExercises();
                fragmentTransaction.add(R.id.fragment_container, myFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}