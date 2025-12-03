package com.example.smartfit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileFragment extends Fragment {

    private TextView tvName, tvJoined, tvAge, tvHeight, tvWeight, tvGender, tvActivityLevel, tvHealthCondition,tvfoodAllergies;
    private ImageView ivAvatar;
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    public ProfileFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Initialize views
        ivAvatar = view.findViewById(R.id.imageView5);
        tvName = view.findViewById(R.id.homeWorkoutTitle);
        tvJoined = view.findViewById(R.id.dayText);

        tvAge = view.findViewById(R.id.ageText);
        tvHeight = view.findViewById(R.id.heightText);
        tvWeight = view.findViewById(R.id.weightText);
        tvGender = view.findViewById(R.id.genderText);
        tvActivityLevel = view.findViewById(R.id.activityLevelText);
        tvHealthCondition = view.findViewById(R.id.healthConditionText);
        tvfoodAllergies = view.findViewById(R.id.foodAllergyText);


        // Initialize Firebase
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        loadUserProfile();

        return view;
    }

    private void loadUserProfile() {
        String userId = auth.getCurrentUser().getUid();
        DocumentReference docRef = db.collection("users").document(userId);

        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    String name = document.getString("name");
                    String joined = document.getString("joinedDate");
                    String birthdayStr = document.getString("birthday"); // new field
                    Long height = document.getLong("height");
                    Long weight = document.getLong("weight");
                    String gender = document.getString("gender");
                    String activity = document.getString("activity_level");
                    String health = document.getString("health_conditions");
                    String allergy = document.getString("food_allergies");

                    tvName.setText(name != null ? name : "No Name");
                    tvJoined.setText(joined != null ? "Active since " + joined : "Active since N/A");

                    // Calculate age from birthday
                    if (birthdayStr != null && !birthdayStr.isEmpty()) {
                        try {
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                            LocalDate birthDate = LocalDate.parse(birthdayStr, formatter);
                            LocalDate now = LocalDate.now();
                            int calculatedAge = Period.between(birthDate, now).getYears();
                            tvAge.setText(calculatedAge + " years old");
                        } catch (Exception e) {
                            tvAge.setText("N/A");
                        }
                    } else {
                        tvAge.setText("N/A");
                    }

                    tvHeight.setText(height != null ? height + " cm" : "N/A");
                    tvWeight.setText(weight != null ? weight + " kg" : "N/A");
                    tvGender.setText(gender != null ? gender : "N/A");
                    tvActivityLevel.setText(activity != null ? activity : "N/A");
                    tvHealthCondition.setText(health != null ? health : "N/A");
                    tvfoodAllergies.setText(allergy != null ? allergy : "N/A");

                } else {
                    tvName.setText("User not found");
                }
            } else {
                tvName.setText("Error loading data");
            }
        });
    }
}
