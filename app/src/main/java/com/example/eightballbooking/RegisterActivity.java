package com.example.eightballbooking;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private EditText nameEditText, emailEditText, passwordEditText, phoneEditText;
    private View loadingSpinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // Initialize UI components
        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        loadingSpinner = findViewById(R.id.loadingSpinner);
        Button registerButton = findViewById(R.id.registerButton);

        registerButton.setOnClickListener(v -> registerUser());

        ImageView logoImageView = findViewById(R.id.logoImageView);
        Animation rotationAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate_logo);
        logoImageView.startAnimation(rotationAnimation);
    }

    private void registerUser() {
        String name = nameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        loadingSpinner.setVisibility(View.VISIBLE);
        // Basic validation
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            loadingSpinner.setVisibility(View.GONE);
            return;
        }

        // Firebase Auth to create a user
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // On successful auth, save additional info in Firestore
                        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid(); // Get UID of the newly created user
                        Map<String, Object> user = new HashMap<>();
                        user.put("UUID", uid);
                        user.put("name", name);
                        user.put("email", email); // Storing email in Firestore is common, but never store passwords
                        user.put("phone", phone);
                        User user1 = new User(uid, name, email, phone);

                        FirebaseFirestore.getInstance().collection("User").document(uid)
                                .set(user)
                                .addOnSuccessListener(aVoid -> {

                                    loadingSpinner.setVisibility(View.GONE);

                                    Intent intent = new Intent(getApplicationContext(), HomePageActivity.class);
                                    startActivity(intent);
                                    finish();
                                })
                                .addOnFailureListener(e -> Toast.makeText(RegisterActivity.this, "Firestore error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                    } else {
                        // Handle failures
                        loadingSpinner.setVisibility(View.GONE);
                        Toast.makeText(RegisterActivity.this, "Auth error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
