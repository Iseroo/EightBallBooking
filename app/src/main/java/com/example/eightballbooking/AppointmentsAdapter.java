package com.example.eightballbooking;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.function.Function;

public class AppointmentsAdapter extends RecyclerView.Adapter<AppointmentsAdapter.ViewHolder> {
    private List<Appointment> appointments;
    private Context context;

    private Profile profile;


    public AppointmentsAdapter(List<Appointment> appointments, Context context, Profile profile) {
        this.appointments = appointments;
        this.context = context;
        this.profile = profile;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.appointment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Appointment appointment = appointments.get(position);
        holder.tvDate.setText(new SimpleDateFormat("dd-MM-yyyy HH:mm").format(appointment.getDate().toDate()));
        holder.btnDelete.setOnClickListener(view -> deleteAppointment(appointment.getId()));
    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate;
        ImageButton btnDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }

    private void deleteAppointment(String appointmentId) {
        FirebaseFirestore.getInstance().collection("Appointments").document(appointmentId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    // Update UI after deletion
                    notifyItemRemoved(appointments.indexOf(appointments.stream().filter(a -> a.getId().equals(appointmentId)).findFirst().get()));
                    profile.refreshAppointments();
                })
                .addOnFailureListener(e -> Toast.makeText(context, "Error deleting appointment", Toast.LENGTH_SHORT).show());
    }
}
