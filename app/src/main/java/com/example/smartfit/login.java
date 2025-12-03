package com.example.smartfit;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class login extends AppCompatActivity {

    private EditText email, password;
    private Button loginBtn;
    private TextView createAccount, forgotPass;
    private LinearLayout googleButton;

    private FirebaseAuth auth;
    private GoogleSignInClient mGoogleSignInClient;
    private final int RC_SIGN_IN = 1000;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Firebase Auth
        auth = FirebaseAuth.getInstance();

        // Views
        forgotPass = findViewById(R.id.forgot);
        email = findViewById(R.id.editTextText2);
        password = findViewById(R.id.editTextTextPassword);
        loginBtn = findViewById(R.id.button);
        createAccount = findViewById(R.id.create);
        googleButton = findViewById(R.id.linearLayout);

        // Email/Password Login
        loginBtn.setOnClickListener(v -> loginUser());

        // Go to Create Account screen
        createAccount.setOnClickListener(v ->
                startActivity(new Intent(login.this, createAccount.class)));
        forgotPass.setOnClickListener(v ->
                startActivity(new Intent(login.this, forgotPassword.class)));

        // Configure Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)) // Web client ID from Firebase
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Google Sign-In Button
        googleButton.setOnClickListener(v -> signInWithGoogle());
    }

    // Email/Password login
    private void loginUser() {
        String userEmail = email.getText().toString().trim();
        String userPass = password.getText().toString().trim();

        if (userEmail.isEmpty() || userPass.isEmpty()) {
            Toast.makeText(this, "Enter email and password!", Toast.LENGTH_SHORT).show();
            return;
        }

        auth.signInWithEmailAndPassword(userEmail, userPass)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        FirebaseUser user = auth.getCurrentUser();
                        if (user == null) {
                            Toast.makeText(this, "Login error: user is null", Toast.LENGTH_LONG).show();
                            return;
                        }

                        String uid = user.getUid();

                        // 🔥 Check Firestore for firstTimeLogin (same logic as Google Sign-In)
                        db.collection("users").document(uid).get()
                                .addOnSuccessListener(documentSnapshot -> {
                                    if (documentSnapshot.exists()) {

                                        Boolean firstTime = documentSnapshot.getBoolean("firstTimeLogin");
                                        if (firstTime == null) firstTime = false;

                                        Intent intent;

                                        if (firstTime) {
                                            // First time login → go to onboarding
                                            intent = new Intent(login.this, getStarted1.class);
                                        } else {
                                            // Returning user → go to home
                                            intent = new Intent(login.this, MainActivity.class);
                                        }

                                        Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show();
                                        startActivity(intent);
                                        finish();

                                    }
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(this, "Error fetching user data: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                });

                    } else {
                        Toast.makeText(this,
                                "Login Failed: " + task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }


    // Google Sign-In
    private void signInWithGoogle() {
        // Force account picker every time
        mGoogleSignInClient.signOut().addOnCompleteListener(task -> {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Toast.makeText(this, "Google Sign-In Failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        String uid = user.getUid();

                        // Reference to user's Firestore document
                        db.collection("users").document(uid).get()
                                .addOnSuccessListener(documentSnapshot -> {
                                    if (documentSnapshot.exists()) {
                                        // User exists, check firstTimeLogin
                                        Boolean firstTime = documentSnapshot.getBoolean("firstTimeLogin");
                                        if (firstTime == null) firstTime = false;

                                        Intent intent;
                                        if (firstTime) {
                                            // User's first login → placeholder activity for onboarding
                                            intent = new Intent(login.this, getStarted1.class);
                                        } else {
                                            // Returning user → go to home
                                            intent = new Intent(login.this, MainActivity.class);
                                        }

                                        Toast.makeText(login.this, "Google Sign-In Successful!", Toast.LENGTH_SHORT).show();
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        // New user, create Firestore document with firstTimeLogin = true
                                        Map<String, Object> userInfo = new HashMap<>();
                                        userInfo.put("name", user.getDisplayName());
                                        userInfo.put("email", user.getEmail());
                                        userInfo.put("uid", uid);
                                        userInfo.put("photoUrl", user.getPhotoUrl() != null ? user.getPhotoUrl().toString() : "");
                                        userInfo.put("firstTimeLogin", true);

                                        db.collection("users").document(uid)
                                                .set(userInfo)
                                                .addOnSuccessListener(aVoid -> {
                                                    // Send new user to onboarding
                                                    Intent intent = new Intent(login.this, getStarted1.class);
                                                    Toast.makeText(login.this, "Google Sign-In Successful!", Toast.LENGTH_SHORT).show();
                                                    startActivity(intent);
                                                    finish();
                                                })
                                                .addOnFailureListener(e -> {
                                                    Toast.makeText(login.this, "Failed to save user: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                                });
                                    }
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(login.this, "Error fetching user data: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                });

                    } else {
                        Toast.makeText(this, "Firebase Auth Failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }



}
