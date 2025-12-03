package com.example.smartfit;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class getStarted5 extends AppCompatActivity {

    ImageView backButton;
    EditText foodAllergiesInput, healthConditionsInput;
    Button completeSetupBtn;
    TextView textViewGoals;
    ImageView homeCheck, gymCheck;

    LinearLayout homeWorkoutLayout, gymWorkoutLayout;
    TextView homeTitle, gymTitle;

    String gender, height, weight, age, activityLevel, goals;
    String selectedWorkout = "";

    boolean homeSelected = false;
    boolean gymSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started5);

        // Inputs and buttons
        backButton = findViewById(R.id.imageView12);
        foodAllergiesInput = findViewById(R.id.editTextFoodAllergies);
        healthConditionsInput = findViewById(R.id.editTextHealthConditions);
        completeSetupBtn = findViewById(R.id.button7);
        textViewGoals = findViewById(R.id.textView17); // existing placeholder

        // Workout type containers
        homeWorkoutLayout = findViewById(R.id.homeWorkoutButton);
        gymWorkoutLayout = findViewById(R.id.gymWorkoutButton);
        homeTitle = findViewById(R.id.homeTitle);
        gymTitle = findViewById(R.id.gymTitle);

        homeCheck = findViewById(R.id.HomeWorkoutsRadioButton);
        gymCheck = findViewById(R.id.GymWorkoutsRadioButton);
        homeCheck.setVisibility(View.GONE);
        gymCheck.setVisibility(View.GONE);

        // Get values from previous activity
        gender = getIntent().getStringExtra("gender");
        height = getIntent().getStringExtra("height");
        weight = getIntent().getStringExtra("weight");
        age = getIntent().getStringExtra("age");
        activityLevel = getIntent().getStringExtra("activity_level");
        goals = getIntent().getStringExtra("goal"); // comma-separated


        // Back button
        backButton.setOnClickListener(v -> finish());

        // Workout selection listeners
        homeWorkoutLayout.setOnClickListener(v -> selectWorkout("Home Workouts"));
        gymWorkoutLayout.setOnClickListener(v -> selectWorkout("Gym Workout"));

        // Complete setup button
        completeSetupBtn.setOnClickListener(v -> submitData());
    }

    // Workout selection logic
    private void selectWorkout(String workout) {
        homeSelected = workout.equals("Home Workouts");
        gymSelected = workout.equals("Gym Workout");

        updateWorkoutUI();

        selectedWorkout = workout;
    }

    private void updateWorkoutUI() {
        // Home Workout
        if (homeSelected) {
            homeWorkoutLayout.setBackgroundResource(R.drawable.rounded_small_rectangle); // Outline when selected
            homeTitle.setTextColor(Color.parseColor("#FF8C00")); // Orange text
            homeCheck.setColorFilter(Color.parseColor("#FF8C00")); // Orange check
            homeCheck.setVisibility(View.VISIBLE); // Show check
        } else {
            homeWorkoutLayout.setBackground(null); // Remove outline
            homeTitle.setTextColor(Color.parseColor("#5261a1")); // Default text
            homeCheck.setVisibility(View.GONE); // Hide check
            homeCheck.setColorFilter(null); // Remove any tint
        }

        // Gym Workout
        if (gymSelected) {
            gymWorkoutLayout.setBackgroundResource(R.drawable.rounded_small_rectangle); // Outline when selected
            gymTitle.setTextColor(Color.parseColor("#FF8C00")); // Orange text
            gymCheck.setColorFilter(Color.parseColor("#FF8C00")); // Orange check
            gymCheck.setVisibility(View.VISIBLE); // Show check
        } else {
            gymWorkoutLayout.setBackground(null); // Remove outline
            gymTitle.setTextColor(Color.parseColor("#5261a1")); // Default text
            gymCheck.setVisibility(View.GONE); // Hide check
            gymCheck.setColorFilter(null); // Remove tint
        }
    }



    // Submit all collected data
    private void submitData() {
        String foodAllergies = foodAllergiesInput.getText().toString().trim();
        String healthConditions = healthConditionsInput.getText().toString().trim();

        if (selectedWorkout.isEmpty()) {
            Toast.makeText(this, "Please select a workout type", Toast.LENGTH_SHORT).show();
            return;
        }

        String payload = "{\n" +
                "  \"gender\": \"" + gender + "\",\n" +
                "  \"height\": \"" + height + "\",\n" +
                "  \"weight\": \"" + weight + "\",\n" +
                "  \"age\": \"" + age + "\",\n" +
                "  \"activity_level\": \"" + activityLevel + "\",\n" +
                "  \"goals\": \"" + goals + "\",\n" +
                "  \"workout_type\": \"" + selectedWorkout + "\",\n" +
                "  \"food_allergies\": \"" + foodAllergies + "\",\n" +
                "  \"health_conditions\": \"" + healthConditions + "\"\n" +
                "}";

        System.out.println("Payload to backend: \n" + payload);
        Toast.makeText(this, "Data ready to be sent to backend", Toast.LENGTH_SHORT).show();
    }
}
