package com.example.smartfit;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class WorkoutAdapter extends RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder> {

    private List<WorkoutFragment.Workout> workoutList;

    public WorkoutAdapter(List<WorkoutFragment.Workout> workoutList) {
        this.workoutList = workoutList;
    }

    // Method to update the list and refresh the RecyclerView
    public void updateWorkouts(List<WorkoutFragment.Workout> newWorkouts) {
        this.workoutList = newWorkouts;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public WorkoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // You MUST create a layout file named 'item_workout_card.xml' for the card design
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_workout_card, parent, false);
        return new WorkoutViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutViewHolder holder, int position) {
        WorkoutFragment.Workout workout = workoutList.get(position);
        holder.categoryTextView.setText(workout.category);
        holder.titleTextView.setText(workout.title);
        holder.descriptionTextView.setText(workout.description);
        holder.metricsTextView.setText(workout.metrics);

        holder.startButton.setOnClickListener(v -> {
            // Handle Start button click
            Toast.makeText(v.getContext(), "Starting: " + workout.title, Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return workoutList.size();
    }

    public static class WorkoutViewHolder extends RecyclerView.ViewHolder {
        public TextView categoryTextView;
        public TextView titleTextView;
        public TextView descriptionTextView;
        public TextView metricsTextView;
        public Button startButton;

        public WorkoutViewHolder(View itemView) {
            super(itemView);
            // Link views from the item_workout_card.xml layout
            categoryTextView = itemView.findViewById(R.id.workoutCategory);
            titleTextView = itemView.findViewById(R.id.workoutTitle);
            descriptionTextView = itemView.findViewById(R.id.workoutDescription);
            metricsTextView = itemView.findViewById(R.id.workoutMetrics);
            startButton = itemView.findViewById(R.id.startButton);
        }
    }
}