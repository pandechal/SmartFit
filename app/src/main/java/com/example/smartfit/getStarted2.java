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
    private TextView birthdayText;
    private String birthday = ""; // store the selected date


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
        continueBtn = findViewById(R.id.button7);
        BTback = findViewById(R.id.imageView12);
        birthdayText = findViewById(R.id.birthdayText);

        birthdayText.setOnClickListener(v -> showDatePickerDialog());


        BTback.setOnClickListener(v -> finish());

        maleContainer.setOnClickListener(v -> selectGender("Male"));
        femaleContainer.setOnClickListener(v -> selectGender("Female"));

        continueBtn.setOnClickListener(v -> validateAndSave());
    }
    private void showDatePickerDialog() {
        final java.util.Calendar calendar = java.util.Calendar.getInstance();
        int year = calendar.get(java.util.Calendar.YEAR);
        int month = calendar.get(java.util.Calendar.MONTH);
        int day = calendar.get(java.util.Calendar.DAY_OF_MONTH);

        android.app.DatePickerDialog datePickerDialog = new android.app.DatePickerDialog(
                getStarted2.this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // Month is 0-based
                    selectedMonth += 1;
                    birthday = selectedYear + "-" + (selectedMonth < 10 ? "0" + selectedMonth : selectedMonth)
                            + "-" + (selectedDay < 10 ? "0" + selectedDay : selectedDay);
                    birthdayText.setText(birthday);
                },
                year, month, day);
        datePickerDialog.show();
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

        if (birthday.isEmpty()) {
            Toast.makeText(this, "Please select your birthday", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedGender.isEmpty()) {
            Toast.makeText(this, "Please select your gender", Toast.LENGTH_SHORT).show();
            return;
        }

        if (height.isEmpty() || weight.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }


        Intent intent = new Intent(getStarted2.this, getStarted3.class);
        intent.putExtra("gender", selectedGender);
        intent.putExtra("height", height);
        intent.putExtra("weight", weight);
        intent.putExtra("birthday", birthday);

        startActivity(intent);
    }

}
