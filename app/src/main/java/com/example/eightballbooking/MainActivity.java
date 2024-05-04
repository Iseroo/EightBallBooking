package com.example.eightballbooking;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button loginButton, registerButton;

    private View loadingSpinner;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);
        loadingSpinner = findViewById(R.id.loadingSpinner);

        loginButton.setOnClickListener(view -> loginUser());
        registerButton.setOnClickListener(view -> openRegisterActivity());

        ImageView logoImageView = findViewById(R.id.logoImageView);
        Animation rotationAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate_logo);
        logoImageView.startAnimation(rotationAnimation);
    }

    private void loginUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        loadingSpinner.setVisibility(View.VISIBLE);
        // Simple validation
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(MainActivity.this, "Email or password cannot be empty.", Toast.LENGTH_SHORT).show();
            loadingSpinner.setVisibility(View.GONE);
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                System.out.println(task.getResult().getUser().getUid());
                FirebaseFirestore.getInstance().collection("User").whereEqualTo("UUID", task.getResult().getUser().getUid()).get()
                        .addOnCompleteListener(task2 -> {
                            System.out.println(task2.getResult().getDocuments().toString());
                            if (task2.isSuccessful()) {
                                if (task2.getResult().getDocuments().size() == 0) {
                                    Toast.makeText(MainActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                loadingSpinner.setVisibility(View.GONE);
                                DocumentSnapshot userSnapshot = task2.getResult().getDocuments().get(0);
                                new User(userSnapshot.getString("UUID"), userSnapshot.getString("name"), userSnapshot.getString("email"), userSnapshot.getString("phone"));

                            }

                            Intent intent = new Intent(getApplicationContext(), HomePageActivity.class);
                            startActivity(intent);
                            finish();
                        });

            } else {
                loadingSpinner.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Login failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openRegisterActivity() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}