package com.example.smartfit;

import android.content.Intent;
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

import com.example.smartfit.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class getStarted5 extends AppCompatActivity {

    ImageView backButton, homeCheck, gymCheck;
    EditText foodAllergiesInput, healthConditionsInput;
    Button completeSetupBtn;
    LinearLayout homeWorkoutLayout, gymWorkoutLayout;
    TextView homeTitle, gymTitle;

    String gender, height, weight, age, activityLevel, goals;
    String selectedWorkout = "";
    boolean homeSelected = false, gymSelected = false;

    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started5);

        // Initialize Firebase
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        // --- find views ---
        backButton = findViewById(R.id.imageView12);
        foodAllergiesInput = findViewById(R.id.editTextFoodAllergies);
        healthConditionsInput = findViewById(R.id.editTextHealthConditions);
        completeSetupBtn = findViewById(R.id.button7);

        homeWorkoutLayout = findViewById(R.id.homeWorkoutButton);
        gymWorkoutLayout = findViewById(R.id.gymWorkoutButton);
        homeTitle = findViewById(R.id.homeTitle);
        gymTitle = findViewById(R.id.gymTitle);

        homeCheck = findViewById(R.id.HomeWorkoutsRadioButton);
        gymCheck = findViewById(R.id.GymWorkoutsRadioButton);
        homeCheck.setVisibility(View.GONE);
        gymCheck.setVisibility(View.GONE);

        // Get intent values
        gender = getIntent().getStringExtra("gender");
        height = getIntent().getStringExtra("height");
        weight = getIntent().getStringExtra("weight");
        age = getIntent().getStringExtra("age");
        activityLevel = getIntent().getStringExtra("activity_level");
        goals = getIntent().getStringExtra("goal");

        backButton.setOnClickListener(v -> finish());

        homeWorkoutLayout.setOnClickListener(v -> selectWorkout("Home Workouts"));
        gymWorkoutLayout.setOnClickListener(v -> selectWorkout("Gym Workout"));

        completeSetupBtn.setOnClickListener(v -> submitDataToFirestore());

        // Sign in anonymously if no user
        if (auth.getCurrentUser() == null) {
            auth.signInAnonymously()
                    .addOnSuccessListener(authResult -> Toast.makeText(this, "Signed in anonymously", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(this, "Auth failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }

    private void submitDataToFirestore() {
        String foodAllergies = foodAllergiesInput.getText().toString().trim();
        String healthConditions = healthConditionsInput.getText().toString().trim();

        if (selectedWorkout.isEmpty()) {
            Toast.makeText(this, "Please select a workout type", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
            return;
        }

        String uid = currentUser.getUid();

        Map<String, Object> userData = new HashMap<>();
        userData.put("gender", gender);
        userData.put("height", height);
        userData.put("weight", weight);
        userData.put("age", age);
        userData.put("activity_level", activityLevel);
        userData.put("goals", goals);
        userData.put("workout_type", selectedWorkout);
        userData.put("food_allergies", foodAllergies);
        userData.put("health_conditions", healthConditions);
        userData.put("firstTimeLogin", false);

        db.collection("users").document(uid)
                .set(userData, SetOptions.merge())
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Registered successfully!", Toast.LENGTH_SHORT).show();
                    // Go to home activity
                    startActivity(new Intent(getStarted5.this, home.class));
                    finish(); // Optional: finish this activity so user cannot go back
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Registration Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    // --- Workout selection logic ---
    private void selectWorkout(String workout) {
        homeSelected = workout.equals("Home Workouts");
        gymSelected = workout.equals("Gym Workout");
        updateWorkoutUI();
        selectedWorkout = workout;
    }

    private void updateWorkoutUI() {
        if (homeSelected) {
            homeWorkoutLayout.setBackgroundResource(R.drawable.rounded_small_rectangle);
            homeTitle.setTextColor(Color.parseColor("#FF8C00"));
            homeCheck.setColorFilter(Color.parseColor("#FF8C00"));
            homeCheck.setVisibility(View.VISIBLE);
        } else {
            homeWorkoutLayout.setBackground(null);
            homeTitle.setTextColor(Color.parseColor("#5261a1"));
            homeCheck.setVisibility(View.GONE);
            homeCheck.setColorFilter(null);
        }

        if (gymSelected) {
            gymWorkoutLayout.setBackgroundResource(R.drawable.rounded_small_rectangle);
            gymTitle.setTextColor(Color.parseColor("#FF8C00"));
            gymCheck.setColorFilter(Color.parseColor("#FF8C00"));
            gymCheck.setVisibility(View.VISIBLE);
        } else {
            gymWorkoutLayout.setBackground(null);
            gymTitle.setTextColor(Color.parseColor("#5261a1"));
            gymCheck.setVisibility(View.GONE);
            gymCheck.setColorFilter(null);
        }
    }
}
