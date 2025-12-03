package com.example.smartfit;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

public class getStarted2 extends AppCompatActivity {

    private ConstraintLayout maleContainer, femaleContainer;
    private EditText heightEdit, weightEdit, ageEdit;
    private TextView maleText, femaleText;
    private Button continueBtn;
    private ImageView maleIcon, femaleIcon, BTback;

    private String selectedGender = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started2);

        maleContainer = findViewById(R.id.maleContainer);
        femaleContainer = findViewById(R.id.femaleContainer);
        maleIcon = findViewById(R.id.maleIcon);
        femaleIcon = findViewById(R.id.femaleIcon);
        maleText = findViewById(R.id.maleText);
        femaleText = findViewById(R.id.femaleText);
        heightEdit = findViewById(R.id.editTextText2);
        weightEdit = findViewById(R.id.editTextText7);
        ageEdit = findViewById(R.id.editTextText9);
        continueBtn = findViewById(R.id.button7);
        BTback = findViewById(R.id.imageView12);

        BTback.setOnClickListener(v -> finish());

        maleContainer.setOnClickListener(v -> selectGender("Male"));
        femaleContainer.setOnClickListener(v -> selectGender("Female"));

        continueBtn.setOnClickListener(v -> validateAndSave());
    }

    private void selectGender(String gender) {
        selectedGender = gender;

        if (gender.equals("Male")) {
            maleContainer.setBackgroundResource(R.drawable.gender_selected_bg);
            maleIcon.setColorFilter(Color.WHITE);
            maleText.setTextColor(Color.WHITE);

            femaleContainer.setBackgroundResource(R.drawable.gender_unselected_bg);
            femaleIcon.setColorFilter(ContextCompat.getColor(this, R.color.orange));
            femaleText.setTextColor(Color.BLACK);

        } else {
            femaleContainer.setBackgroundResource(R.drawable.gender_selected_bg);
            femaleIcon.setColorFilter(Color.WHITE);
            femaleText.setTextColor(Color.WHITE);

            maleContainer.setBackgroundResource(R.drawable.gender_unselected_bg);
            maleIcon.setColorFilter(ContextCompat.getColor(this, R.color.orange));
            maleText.setTextColor(Color.BLACK);
        }
    }

    private void validateAndSave() {
        String height = heightEdit.getText().toString().trim();
        String weight = weightEdit.getText().toString().trim();
        String age = ageEdit.getText().toString().trim();

        if (selectedGender.isEmpty()) {
            Toast.makeText(this, "Please select your gender", Toast.LENGTH_SHORT).show();
            return;
        }

        if (height.isEmpty() || weight.isEmpty() || age.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(getStarted2.this, getStarted3.class);
        intent.putExtra("gender", selectedGender);
        intent.putExtra("height", height);
        intent.putExtra("weight", weight);
        intent.putExtra("age", age);

        startActivity(intent);
    }

}
