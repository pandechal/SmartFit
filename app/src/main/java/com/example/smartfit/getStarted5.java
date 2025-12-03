package com.example.smartfit;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class getStarted5 extends AppCompatActivity {

    ImageView backButton;
    RadioButton homeWorkoutRadio, gymWorkoutRadio;
    EditText foodAllergiesInput, healthConditionsInput;
    Button completeSetupBtn;
    TextView textViewGoals;

    String gender, height, weight, age, activityLevel, goals;

    String selectedWorkout = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started5);

        backButton = findViewById(R.id.imageView12);
        foodAllergiesInput = findViewById(R.id.editTextFoodAllergies);
        healthConditionsInput = findViewById(R.id.editTextHealthConditions);
        completeSetupBtn = findViewById(R.id.button7);

        textViewGoals = findViewById(R.id.textView17); // using existing placeholder

        gender = getIntent().getStringExtra("gender");
        height = getIntent().getStringExtra("height");
        weight = getIntent().getStringExtra("weight");
        age = getIntent().getStringExtra("age");
        activityLevel = getIntent().getStringExtra("activity_level");
        goals = getIntent().getStringExtra("goal"); // comma-separated

        textViewGoals.setText("Selected goals: " + goals);

        backButton.setOnClickListener(v -> finish());

        homeWorkoutRadio.setOnClickListener(v -> {
            selectedWorkout = "Home Workouts";
            gymWorkoutRadio.setChecked(false);
        });

        gymWorkoutRadio.setOnClickListener(v -> {
            selectedWorkout = "Gym Workout";
            homeWorkoutRadio.setChecked(false);
        });

        completeSetupBtn.setOnClickListener(v -> submitData());
    }

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
