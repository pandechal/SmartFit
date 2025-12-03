package com.example.smartfit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;
    // Track the currently selected index to determine scroll direction if needed
    private int currentTabIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bottomNav = findViewById(R.id.bottomNavigationView);

        // Load HomeFragment by default
        if (savedInstanceState == null) {
            loadFragment(new HomeFragment(), 0);
        }

        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            Fragment selectedFragment = null;
            int newIndex = 0;

            if (itemId == R.id.nav_home) {
                selectedFragment = new HomeFragment();
                newIndex = 0;
            } else if (itemId == R.id.nav_workout) {
                selectedFragment = new WorkoutFragment();
                newIndex = 1;
            } else if (itemId == R.id.nav_nutrition) {
                selectedFragment = new NutritionFragment();
                newIndex = 2;
            } else if (itemId == R.id.nav_progress) {
                selectedFragment = new ProgressFragment();
                newIndex = 3;
            } else if (itemId == R.id.nav_profile) {
                selectedFragment = new ProfileFragment();
                newIndex = 4;
            }

            if (selectedFragment != null) {
                // 1. Animate the Icon
                animateIcon(newIndex);

                // 2. Load the Fragment
                loadFragment(selectedFragment, newIndex);
                return true;
            }

            return false;
        });
    }

    /**
     * Loads the fragment with a transition animation.
     */
    private void loadFragment(Fragment fragment, int newIndex) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();

        currentTabIndex = newIndex;
    }

    /**
     * Finds the specific icon View within the BottomNavigationView and applies a bounce animation.
     */
    private void animateIcon(int position) {
        try {
            // The BottomNavigationView contains a BottomNavigationMenuView
            ViewGroup menuView = (ViewGroup) bottomNav.getChildAt(0);

            // Get the specific item view at the selected index
            View menuItemView = menuView.getChildAt(position);

            // Find the icon ImageView within that item (usually index 0 or 1 depending on label)
            // A safer way is to animate the whole menuItemView or search for the icon specifically.
            // Here we animate the whole item for a nice tactile effect.

            menuItemView.animate()
                    .scaleX(1.15f) // Scale up to 115%
                    .scaleY(1.15f)
                    .setDuration(150)
                    .setInterpolator(new AccelerateDecelerateInterpolator())
                    .withEndAction(() -> {
                        // Scale back down to original size
                        menuItemView.animate()
                                .scaleX(1.0f)
                                .scaleY(1.0f)
                                .setDuration(150)
                                .setInterpolator(new AccelerateDecelerateInterpolator())
                                .start();
                    })
                    .start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Navigation methods for fragments to use
    public void navigateToWorkout() {
        bottomNav.setSelectedItemId(R.id.nav_workout);
    }

    public void navigateToNutrition() {
        bottomNav.setSelectedItemId(R.id.nav_nutrition);
    }

    public void navigateToProgress() {
        bottomNav.setSelectedItemId(R.id.nav_progress);
    }

    public void navigateToProfile() {
        bottomNav.setSelectedItemId(R.id.nav_profile);
    }

    @Override
    public void onBackPressed() {
        // Go back to login screen
        Intent intent = new Intent(MainActivity.this, login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}