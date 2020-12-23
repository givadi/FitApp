package com.example.fitapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.example.fitapp.Model.Exercise;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class TimerExercise extends Fragment {
    List<Exercise> exercisesList = new ArrayList<>();
    List<Exercise> exercisesPractice = new ArrayList<>();
    
    private static final long START_TIME_IN_MILLIS = 60000;
    private boolean timerRunning = false;
    CountDownTimer countDownTimer;
    private long timeLeft = START_TIME_IN_MILLIS;
    int exerciseCounter = 0;

    View gradient;
    TextView pose_name, description;
    ConstraintLayout layout;
    Button btnExTimer, btnExStop;
    ImageView pose_image;
    Animation first,second;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_timer_exercise, container, false);
        Bundle bundle = this.getArguments();
        practiceMode = bundle.getInt("practice mode", 0);
        // Загрузка данных из Firebase в список exercisePractice
        init();

        first = AnimationUtils.loadAnimation(getContext(), R.anim.anim_exercise);
        second = AnimationUtils.loadAnimation(getContext(),R.anim.anim_exercise_background);

        //initPracticeMode(practiceMode);
        pose_image = (ImageView) view.findViewById(R.id.iv_pose_exercise);
        btnExTimer = (Button) view.findViewById(R.id.b_exercise_left);
        btnExStop = (Button) view.findViewById(R.id.b_exercise_right);
        pose_name = (TextView) view.findViewById(R.id.tv_exercise_pose_name);
        description = (TextView) view.findViewById(R.id.tv_exercise_description);
        layout = (ConstraintLayout) view.findViewById(R.id.cl_exercise) ;
        gradient = (View) view.findViewById(R.id.v_timer_exercise__gradient_background);
        description.setMovementMethod(new ScrollingMovementMethod());

        btnExTimer.setEnabled(false);
        btnExStop.setEnabled(false);

        gradient.startAnimation(second);


        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                // Установка данных из списка на виджеты
                initPracticeMode(practiceMode);
                Glide.with(pose_image.getContext()).load(exercisesPractice.get(exerciseCounter).getImage()).into(pose_image);
                pose_name.setText(exercisesPractice.get(exerciseCounter).getName());
                description.setText(exercisesPractice.get(exerciseCounter).getDescription());
                layout.startAnimation(first);
                btnExTimer.setEnabled(true);
                btnExStop.setEnabled(true);
            }
        }, 1700);


        btnExTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timerRunning ){
                    pauseTimer();
                }
                else {
                    startTimer();
                }

            }
        });

        btnExStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timerRunning) {
                    timerRunning = false;
                    stopTimer();
                }
                Intent intent = new Intent(getContext(), TimerWork.class);
                startActivity(intent);
            }
        });
        return view;
    }

    int practiceMode;

    private void startTimer(){
        countDownTimer = new CountDownTimer(timeLeft,1000){

            @Override
            public void onTick(long millisUntilFinished) {
                timeLeft = millisUntilFinished;
                btnExTimer.setText(String.format(Locale.getDefault(), "%02d:%02d",millisUntilFinished/1000/60,millisUntilFinished/1000%60));
            }

            @Override
            public void onFinish() {
                if (exerciseCounter < practiceMode-1) {

                    exerciseCounter+=1;
                    Glide.with(pose_image.getContext()).load(exercisesPractice.get(exerciseCounter).getImage()).into(pose_image);
                    description.setText(exercisesPractice.get(exerciseCounter).getDescription()+exercisesPractice.size());
                    //pose_image.setImageResource(exercisesList.get(exerciseCounter).getIcon_id());
                    pose_name.setText(exercisesPractice.get(exerciseCounter).getName());
                } else {
                    Intent intent = new Intent(getContext(), TimerWork.class);
                    startActivity(intent);
                }
                timerRunning = false;
                stopTimer();
            }
        }.start();
        timerRunning = true;
    }

    private void pauseTimer(){
        countDownTimer.cancel();
        timerRunning = false;
        btnExTimer.setText(getContext().getResources().getString(R.string.button_resume));

    }
    private void stopTimer(){
        timeLeft = START_TIME_IN_MILLIS;
        countDownTimer.cancel();
        updateCountDownTimerText();
        btnExTimer.setText(getContext().getResources().getString(R.string.button_start));

    }

    private void updateCountDownTimerText(){
        int minutes = (int) (timeLeft /1000)/60;
        int seconds = (int) (timeLeft /1000)%60;
    }
    private void init() {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = database.child("exercices");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot singleSnapshot : snapshot.getChildren()){
                    exercisesList.add(singleSnapshot.getValue(Exercise.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void initPracticeMode(int mode){
        exercisesPractice.clear();
        switch (mode){
            case 5:
                exercisesPractice.add(exercisesList.get(14));
                exercisesPractice.add(exercisesList.get(7));
                exercisesPractice.add(exercisesList.get(12));
                exercisesPractice.add(exercisesList.get(3));
                exercisesPractice.add(exercisesList.get(5));
                break;
            case 10:
                exercisesPractice.add(exercisesList.get(14));
                exercisesPractice.add(exercisesList.get(13));
                exercisesPractice.add(exercisesList.get(4));
                exercisesPractice.add(exercisesList.get(12));
                exercisesPractice.add(exercisesList.get(11));
                exercisesPractice.add(exercisesList.get(0));
                exercisesPractice.add(exercisesList.get(9));
                exercisesPractice.add(exercisesList.get(6));
                exercisesPractice.add(exercisesList.get(5));
                exercisesPractice.add(exercisesList.get(14));
                break;
            case 15:
                exercisesPractice.add(exercisesList.get(14));
                exercisesPractice.add(exercisesList.get(13));
                exercisesPractice.add(exercisesList.get(4));
                exercisesPractice.add(exercisesList.get(7));
                exercisesPractice.add(exercisesList.get(12));
                exercisesPractice.add(exercisesList.get(11));
                exercisesPractice.add(exercisesList.get(2));
                exercisesPractice.add(exercisesList.get(1));
                exercisesPractice.add(exercisesList.get(10));
                exercisesPractice.add(exercisesList.get(9));
                exercisesPractice.add(exercisesList.get(3));
                exercisesPractice.add(exercisesList.get(1));
                exercisesPractice.add(exercisesList.get(8));
                exercisesPractice.add(exercisesList.get(7));
                break;
            default:
                break;
        }
    }

}
