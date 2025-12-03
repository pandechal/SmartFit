package com.example.smartfit;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class getStarted3 extends AppCompatActivity {

    LinearLayout beginnerContainer, lightContainer, activeContainer, veryActiveContainer;
    RadioButton beginnerRadio, lightRadio, activeRadio, veryActiveRadio;

    ImageView BTback;
    TextView beginnerTitle, lightTitle, activeTitle, veryActiveTitle;
    Button continueBtn;

    String selectedActivity = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started3);

        Intent intent = getIntent();
        String gender = intent.getStringExtra("gender");
        String height = intent.getStringExtra("height");
        String weight = intent.getStringExtra("weight");
        String birthday = intent.getStringExtra("birthday");

        beginnerContainer = findViewById(R.id.beginnerContainer);
        lightContainer = findViewById(R.id.lightContainer);
        activeContainer = findViewById(R.id.activeContainer);
        veryActiveContainer = findViewById(R.id.veryActiveContainer);

        beginnerRadio = findViewById(R.id.beginnerRadioButton);
        lightRadio = findViewById(R.id.lightRadioButton);
        activeRadio = findViewById(R.id.activeRadioButton);
        veryActiveRadio = findViewById(R.id.veryActiveRadioButton);

        beginnerTitle = findViewById(R.id.beginnerTitle);
        lightTitle = findViewById(R.id.lightTitle);
        activeTitle = findViewById(R.id.activeTitle);
        veryActiveTitle = findViewById(R.id.veryActiveTitle);

        BTback = findViewById(R.id.imageView12);

        continueBtn = findViewById(R.id.button7);

        ColorStateList colorStateList = ContextCompat.getColorStateList(this, R.color.radio_button_color);
        beginnerRadio.setButtonTintList(colorStateList);
        lightRadio.setButtonTintList(colorStateList);
        activeRadio.setButtonTintList(colorStateList);
        veryActiveRadio.setButtonTintList(colorStateList);

        beginnerContainer.setOnClickListener(v -> selectOption("Beginner"));
        lightContainer.setOnClickListener(v -> selectOption("Light"));
        activeContainer.setOnClickListener(v -> selectOption("Active"));
        veryActiveContainer.setOnClickListener(v -> selectOption("Very Active"));

        continueBtn.setOnClickListener(v -> {
            if (selectedActivity.isEmpty()) {
                continueBtn.setText("Select one first!");
                return;
            }

            Intent i = new Intent(getStarted3.this, getStarted4.class);
            i.putExtra("gender", gender);
            i.putExtra("height", height);
            i.putExtra("weight", weight);
            i.putExtra("birthday", birthday);
            i.putExtra("activity_level", selectedActivity); // new value

            startActivity(i);
        });

        BTback.setOnClickListener(v -> finish());
    }

    private void selectOption(String type) {

        resetAll();
        selectedActivity = type;

        switch (type) {
            case "Beginner":
                beginnerContainer.setBackgroundResource(R.drawable.rounded_small_rectangle);
                beginnerTitle.setTextColor(Color.parseColor("#fc7a1e"));
                beginnerRadio.setChecked(true);
                break;

            case "Light":
                lightContainer.setBackgroundResource(R.drawable.rounded_small_rectangle);
                lightTitle.setTextColor(Color.parseColor("#fc7a1e"));
                lightRadio.setChecked(true);
                break;

            case "Active":
                activeContainer.setBackgroundResource(R.drawable.rounded_small_rectangle);
                activeTitle.setTextColor(Color.parseColor("#fc7a1e"));
                activeRadio.setChecked(true);
                break;

            case "Very Active":
                veryActiveContainer.setBackgroundResource(R.drawable.rounded_small_rectangle);
                veryActiveTitle.setTextColor(Color.parseColor("#fc7a1e"));
                veryActiveRadio.setChecked(true);
                break;
        }
    }

    private void resetAll() {
        int defaultColor = Color.parseColor("#5261a1");

        beginnerContainer.setBackground(null);
        lightContainer.setBackground(null);
        activeContainer.setBackground(null);
        veryActiveContainer.setBackground(null);

        beginnerTitle.setTextColor(defaultColor);
        lightTitle.setTextColor(defaultColor);
        activeTitle.setTextColor(defaultColor);
        veryActiveTitle.setTextColor(defaultColor);

        beginnerRadio.setChecked(false);
        lightRadio.setChecked(false);
        activeRadio.setChecked(false);
        veryActiveRadio.setChecked(false);
    }
}
