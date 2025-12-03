package com.example.smartfit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the fragment_home.xml layout
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Setup click listeners
        view.findViewById(R.id.allOverProgressContainer).setOnClickListener(v -> {
            // Navigate to Progress
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).navigateToProgress();
            }
        });

        view.findViewById(R.id.nextWorkoutContainer).setOnClickListener(v -> {
            // Navigate to Workout
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).navigateToWorkout();
            }
        });

        view.findViewById(R.id.logMealContainer).setOnClickListener(v -> {
            // Navigate to Nutrition
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).navigateToNutrition();
            }
        });

        view.findViewById(R.id.coreStrengthContainer).setOnClickListener(v -> {
            // Navigate to Workout
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).navigateToWorkout();
            }
        });
    }
}