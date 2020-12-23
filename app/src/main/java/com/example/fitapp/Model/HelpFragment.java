package com.example.fitapp.Model;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.fitapp.R;

public class HelpFragment extends Fragment {
    Button back;
    TextView textView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_help, container, false);
        back = (Button) view.findViewById(R.id.iv_button);
        textView = (TextView) view.findViewById(R.id.tv_help_text);
        textView.setMovementMethod(new ScrollingMovementMethod());
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


}
