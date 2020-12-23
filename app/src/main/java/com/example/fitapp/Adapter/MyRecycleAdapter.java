package com.example.fitapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fitapp.Model.Exercise;
import com.example.fitapp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class MyRecycleAdapter extends FirebaseRecyclerAdapter<Exercise, MyRecycleAdapter.MyViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public MyRecycleAdapter(@NonNull FirebaseRecyclerOptions<Exercise> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull Exercise model) {
        holder.tv.setText(model.getName());
        Glide.with(holder.icon.getContext()).load(model.getImage()).into(holder.icon);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exercise, parent, false);
        return new MyViewHolder(view);
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tv;
        ImageView icon;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tv = (TextView) itemView.findViewById(R.id.tv_pose_name_exercise);
            icon = (ImageView) itemView.findViewById(R.id.iv_pose_icon_exercise);
        }
    }
}
