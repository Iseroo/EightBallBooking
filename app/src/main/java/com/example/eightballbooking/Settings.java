package com.example.eightballbooking;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class Settings extends Fragment {
    private TextView tvUserEmail;

    private EditText etUserName;
    private Button btnSaveName;
    private Button btnLogout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        tvUserEmail = view.findViewById(R.id.tvUserEmail);
        btnLogout = view.findViewById(R.id.btnLogout);
        etUserName = view.findViewById(R.id.etUserName);
        btnSaveName = view.findViewById(R.id.btnSaveName);

        // Setting user details
//        User currentUser = UserManager.getInstance().getCurrentUser();
        etUserName.setText(User.name);
        tvUserEmail.setText(User.email);
        btnSaveName.setOnClickListener(v -> updateUserName());
        btnLogout.setOnClickListener(v -> logout());
        return view;
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        // Navigate back to login screen
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clears the activity stack
        startActivity(intent);
    }

    private void updateUserName() {
        String newName = etUserName.getText().toString();
        if (!newName.isEmpty()) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            if (user != null) {
                Query query = db.collection("User").whereEqualTo("email", user.getEmail());
                query.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            String docId = document.getId();
                            db.collection("User").document(docId)
                                    .update("name", newName)
                                    .addOnSuccessListener(aVoid -> Toast.makeText(getContext(), "Name updated successfully", Toast.LENGTH_SHORT).show())
                                    .addOnFailureListener(e -> Toast.makeText(getContext(), "Error updating name", Toast.LENGTH_SHORT).show());
                        }
                    } else {
                        Toast.makeText(getContext(), "Error finding user", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } else {
            Toast.makeText(getContext(), "Name cannot be empty", Toast.LENGTH_SHORT).show();
        }
    }
}