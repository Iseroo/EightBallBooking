package com.example.eightballbooking;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;


public class Settings extends Fragment {
    private TextView tvUserName;
    private TextView tvUserEmail;
    private Button btnLogout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        tvUserName = view.findViewById(R.id.tvUserName);
        tvUserEmail = view.findViewById(R.id.tvUserEmail);
        btnLogout = view.findViewById(R.id.btnLogout);

        // Setting user details
//        User currentUser = UserManager.getInstance().getCurrentUser();
        tvUserName.setText(User.name    );
        tvUserEmail.setText(User.email);

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
}