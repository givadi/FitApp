package com.example.fitapp;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitapp.Adapter.MyRecycleAdapter;
import com.example.fitapp.Model.Exercise;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListOfExercises extends Fragment {
    TextView tv;
    RecyclerView recyclerView;
    Button back;

    private MyRecycleAdapter recyclerAdapter;
    private  DatabaseReference databaseReference;
    private Context mContext;

    private ArrayList<Exercise> exerciseArrayList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_list_of_exercises, container, false);
        tv = (TextView) view.findViewById(R.id.tv_exercises_header);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_list_exercises);
        back = (Button) view.findViewById(R.id.btn_exercises);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        // Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference();

        FirebaseRecyclerOptions<Exercise> options = new FirebaseRecyclerOptions.Builder<Exercise>().setQuery(databaseReference.child("exercices"), Exercise.class).build();
        recyclerAdapter = new MyRecycleAdapter(options);
        recyclerView.setAdapter(recyclerAdapter);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                if (fm.getBackStackEntryCount() > 0)
                    fm.popBackStack();
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        recyclerAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        recyclerAdapter.stopListening();
    }

}
