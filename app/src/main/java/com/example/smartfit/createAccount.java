package com.example.smartfit;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class createAccount extends AppCompatActivity {

    EditText firstName, lastName, email, phone, password, confirmPassword;
    Button signUp;

    FirebaseAuth auth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_account);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        firstName = findViewById(R.id.editTextText);
        lastName = findViewById(R.id.editTextText3);
        email = findViewById(R.id.editTextText4);
        phone = findViewById(R.id.editTextText5);
        password = findViewById(R.id.editTextTextPassword2);
        confirmPassword = findViewById(R.id.editTextTextPassword3);
        signUp = findViewById(R.id.button2);

        signUp.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        String fName = firstName.getText().toString().trim();
        String lName = lastName.getText().toString().trim();
        String userEmail = email.getText().toString().trim();
        String userPhone = phone.getText().toString().trim();
        String userPass = password.getText().toString().trim();
        String userConfirm = confirmPassword.getText().toString().trim();

        if(fName.isEmpty() || lName.isEmpty() || userEmail.isEmpty() || userPhone.isEmpty() || userPass.isEmpty() || userConfirm.isEmpty()){
            Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!userPass.equals(userConfirm)){
            Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
            return;
        }

        auth.createUserWithEmailAndPassword(userEmail, userPass)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        FirebaseUser user = auth.getCurrentUser();
                        if(user != null){
                            saveUserDetailsToFirestore(user, fName, lName, userPhone, userEmail);
                        }
                        Toast.makeText(this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(this, login.class));
                        finish();
                    } else {
                        Toast.makeText(this, "Failed: "+task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void saveUserDetailsToFirestore(FirebaseUser user, String firstName, String lastName, String phone, String email){
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("firstName", firstName);
        userInfo.put("lastName", lastName);
        userInfo.put("email", email);
        userInfo.put("phone", phone);
        userInfo.put("createdAt", FieldValue.serverTimestamp());
        userInfo.put("googleSignIn", false);
        userInfo.put("firstTimeLogin", true);

        db.collection("users")
                .document(user.getUid())
                .set(userInfo)
                .addOnSuccessListener(aVoid -> {
                    Log.d("FirestoreTest", "User saved successfully!");
                })
                .addOnFailureListener(e -> {
                    Log.e("FirestoreTest", "Failed to save user: " + e.getMessage());
                });
    }
}
