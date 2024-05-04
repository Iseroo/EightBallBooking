package com.example.eightballbooking;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;


public class Profile extends Fragment {

    public Profile() {
        // Required empty public constructor
    }


    private RecyclerView rvAppointments;
    private AppointmentsAdapter adapter;
    private List<Appointment> appointments;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        rvAppointments = view.findViewById(R.id.rvAppointments);
        appointments = new ArrayList<>();
        adapter = new AppointmentsAdapter(appointments,
                getContext(),
                this);
        rvAppointments.setLayoutManager(new LinearLayoutManager(getContext()));
        rvAppointments.setAdapter(adapter);
        loadAppointments();
        return view;
    }

    private void loadAppointments() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Appointments")
                .whereEqualTo("user_email", User.email) // Use the email to filter appointments
                .orderBy("date", Query.Direction.DESCENDING) // Ordering by date descending
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        appointments.clear(); // Clear existing data
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Appointment appointment = document.toObject(Appointment.class);
                            appointment.setId(document.getId()); // Make sure to set the document ID as well
                            appointments.add(appointment);
                        }
                        adapter.notifyDataSetChanged();
                        System.out.println("Appointments loaded: " + appointments.size());
                    } else {
                        Toast.makeText(getContext(), "Error getting appointments: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void refreshAppointments() {
        loadAppointments();
    }


}

