package com.example.smartfit;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class getStarted4 extends AppCompatActivity {

    LinearLayout loseWeightContainer, buildMuscleContainer, gainWeightContainer, manageDietContainer;
    TextView loseWeightTitle, buildMuscleTitle, gainWeightTitle, manageDietTitle;
    ImageView BTback,check1,check2,check3,check4;
    Button continueBtn;

    String selectedGoal = "";

    String gender, height, weight, age, activityLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_get_started4);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        gender = intent.getStringExtra("gender");
        height = intent.getStringExtra("height");
        weight = intent.getStringExtra("weight");
        age = intent.getStringExtra("age");
        activityLevel = intent.getStringExtra("activity_level");

        loseWeightContainer = findViewById(R.id.loseWeightContainer);
        buildMuscleContainer = findViewById(R.id.buildMuscleContainer);
        gainWeightContainer = findViewById(R.id.gainWeightContainer);
        manageDietContainer = findViewById(R.id.manageDietContainer);

        check1 = findViewById(R.id.check1);
        check2 = findViewById(R.id.check2);
        check3 = findViewById(R.id.check3);
        check4 = findViewById(R.id.check4);

        loseWeightTitle = loseWeightContainer.findViewById(R.id.loseWeightTitle); // replace with actual IDs
        buildMuscleTitle = buildMuscleContainer.findViewById(R.id.buildMuscleTitle); // replace with actual IDs
        gainWeightTitle = gainWeightContainer.findViewById(R.id.gainWeightTitle); // replace with actual IDs
        manageDietTitle = manageDietContainer.findViewById(R.id.manageDietTitle); // replace with actual IDs

        continueBtn = findViewById(R.id.button7);
        BTback = findViewById(R.id.imageView12);

        loseWeightContainer.setOnClickListener(v -> selectGoal("Lose Weight", loseWeightContainer, loseWeightTitle));
        buildMuscleContainer.setOnClickListener(v -> selectGoal("Build Muscle", buildMuscleContainer, buildMuscleTitle));
        gainWeightContainer.setOnClickListener(v -> selectGoal("Gain Weight", gainWeightContainer, gainWeightTitle));
        manageDietContainer.setOnClickListener(v -> selectGoal("Manage Diet", manageDietContainer, manageDietTitle));

        continueBtn.setOnClickListener(v -> {
            if (selectedGoal.isEmpty()) {
                continueBtn.setText("Select one first!");
                return;
            }

            // Pass all collected values to next activity (e.g., getStarted5)
            Intent i = new Intent(getStarted4.this, getStarted5.class);
            i.putExtra("gender", gender);
            i.putExtra("height", height);
            i.putExtra("weight", weight);
            i.putExtra("age", age);
            i.putExtra("activity_level", activityLevel);
            i.putExtra("goal", selectedGoal);
            startActivity(i);
        });

        BTback.setOnClickListener(v -> finish()); // go back to previous page
    }

    private void selectGoal(String goal, LinearLayout container, TextView title) {
        // Toggle selection
        boolean currentlySelected = isGoalSelected(goal);
        setGoalSelected(goal, !currentlySelected);

        // Handle mutual exclusivity for Lose Weight and Gain Weight
        if (goal.equals("Lose Weight") && !currentlySelected && gainWeightSelected) {
            setGoalSelected("Gain Weight", false);
        } else if (goal.equals("Gain Weight") && !currentlySelected && loseWeightSelected) {
            setGoalSelected("Lose Weight", false);
        }

        // Update the comma-separated selected goals
        updateSelectedGoalsString();
    }

    private void updateSelectedGoalsString() {
        StringBuilder sb = new StringBuilder();
        if (loseWeightSelected) sb.append("Lose Weight,");
        if (buildMuscleSelected) sb.append("Build Muscle,");
        if (gainWeightSelected) sb.append("Gain Weight,");
        if (manageDietSelected) sb.append("Manage Diet,");

        if (sb.length() > 0) sb.setLength(sb.length() - 1); // remove last comma
        selectedGoal = sb.toString();
    }

    boolean loseWeightSelected = false;
    boolean buildMuscleSelected = false;
    boolean gainWeightSelected = false;
    boolean manageDietSelected = false;

    private boolean isGoalSelected(String goal) {
        switch (goal) {
            case "Lose Weight": return loseWeightSelected;
            case "Build Muscle": return buildMuscleSelected;
            case "Gain Weight": return gainWeightSelected;
            case "Manage Diet": return manageDietSelected;
        }
        return false;
    }

    private void setGoalSelected(String goal, boolean value) {
        LinearLayout container = null;
        TextView title = null;
        ImageView check = null;

        switch (goal) {
            case "Lose Weight":
                loseWeightSelected = value;
                container = loseWeightContainer;
                title = loseWeightTitle;
                check = check1;
                break;
            case "Build Muscle":
                buildMuscleSelected = value;
                container = buildMuscleContainer;
                title = buildMuscleTitle;
                check = check2;
                break;
            case "Gain Weight":
                gainWeightSelected = value;
                container = gainWeightContainer;
                title = gainWeightTitle;
                check = check3;
                break;
            case "Manage Diet":
                manageDietSelected = value;
                container = manageDietContainer;
                title = manageDietTitle;
                check = check4;
                break;
        }

        if (value) {
            container.setBackgroundResource(R.drawable.rounded_small_rectangle);
            title.setTextColor(Color.parseColor("#FF8C00")); // Orange
            check.setColorFilter(Color.parseColor("#fc7a1e")); // Orange check
        } else {
            container.setBackground(null);
            title.setTextColor(Color.parseColor("#5261a1")); // Default color
            check.setColorFilter(null); // Remove check
        }
    }
}