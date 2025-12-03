package com.example.smartfit;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);

        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                // Already in home
                return true;
            } else if (itemId == R.id.nav_workout) {
                // Go to Workout activity
                startActivity(new Intent(this, workout.class));
                return true;
            } else if (itemId == R.id.nav_nutrition) {
                startActivity(new Intent(this, nutrition.class));
                return true;
            } else if (itemId == R.id.nav_progress) {
                startActivity(new Intent(this, progress_weight.class));
                return true;
            } else if (itemId == R.id.nav_profile) {
                startActivity(new Intent(this, profile.class));
                return true;
            }

            return false;
        });
    }

    @Override
    public void onBackPressed() {
        // Go back to login screen
        Intent intent = new Intent(home.this, login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish(); // Finish current activity
    }
}
