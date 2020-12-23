package com.example.fitapp.Model;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.fitapp.R;

import java.util.Calendar;

public class WaterBottleFragment extends Fragment {
    LinearLayout waterBottle, help;
    TextView iconName;
    TextView plus, minus;

    SharedPreferences sPref;
    int counter;
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_water_bottle, container, false);

        // Обновление счетчика воды раз в сутки
        Calendar calendar = Calendar.getInstance();
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        sPref = this.getActivity().getSharedPreferences("startApp", Context.MODE_PRIVATE);
        int lastDay = sPref.getInt("day", 0);
        if(lastDay != currentDay){
            SharedPreferences.Editor ed = sPref.edit();
            ed.putInt("day", currentDay);
            ed.commit();
            counter = 0;
        } else {
            counter = (int) loadText();
        }

        waterBottle = (LinearLayout) view.findViewById(R.id.ll_water_bottle);
        iconName = (TextView) view.findViewById(R.id.tv_icon_water);
        plus = (TextView) view.findViewById(R.id.tv_plus_icon_water);
        minus = (TextView) view.findViewById(R.id.tv_minus_icon_water);
        help = (LinearLayout) view.findViewById(R.id.ll_help);

        // Обработчик счетчика воды
        iconName.setText(""+counter);
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter++;
                iconName.setText(""+counter);
                saveText();
            }
        });
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (counter>0) {
                    counter--;
                    iconName.setText("" + counter);
                    saveText();
                }
            }
        });
        //
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // получаем экземпляр FragmentTransaction
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager
                        .beginTransaction();

                // добавляем фрагмент
                HelpFragment myFragment = new HelpFragment();
                fragmentTransaction.add(R.id.fragment_container, myFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        return view;
    }

    // Сохранение текущего количества воды
    private void saveText() {
        sPref = this.getActivity().getSharedPreferences("water_counter", Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putInt("num_of_glasses", counter);
        ed.commit();
    }

    // Загрузка текущего количества воды
    private int loadText() {
        sPref = this.getActivity().getSharedPreferences("water_counter", Context.MODE_PRIVATE);
        int savedCounter = sPref.getInt("num_of_glasses", 2);
        return savedCounter;
    }
}
