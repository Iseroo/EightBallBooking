package com.example.eightballbooking;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class TableAdapter extends RecyclerView.Adapter<TableAdapter.TableViewHolder> {

    private Integer[] tableIds = {1, 2, 3};
    private FragmentManager fragmentManager;


    public TableAdapter(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public TableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.table_item, parent, false);
        return new TableViewHolder(view, fragmentManager);
    }



    @Override
    public void onBindViewHolder(@NonNull TableViewHolder holder, int position) {
        holder.bind(tableIds[position]);
    }

    @Override
    public int getItemCount() {
        return tableIds.length;
    }

    static class TableViewHolder extends RecyclerView.ViewHolder {
        TextView tableIdText;
        int tableId;
        FragmentManager fragmentManager;

        TableViewHolder(View itemView, FragmentManager fragmentManager) {
            super(itemView);
            tableIdText = itemView.findViewById(R.id.tableId_text);
            this.fragmentManager = fragmentManager;
            itemView.setOnClickListener(v -> selectDateTime());
        }

        void bind(int tableId) {
            this.tableId = tableId;
            tableIdText.setText("Table " + tableId);
        }

        private void selectDateTime() {
            DateTimePickerFragment dateTimePicker = new DateTimePickerFragment(dateTime -> {
                checkAndBook(dateTime);
            });
            dateTimePicker.show(fragmentManager, "dateTimePicker");
        }



        private void checkAndBook(Calendar selectedTime) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();


            selectedTime.set(Calendar.SECOND, 0);
            selectedTime.set(Calendar.MILLISECOND, 0);

            Timestamp selectedTimestamp = new Timestamp(selectedTime.getTime());

            System.out.println("Selected time: " + new SimpleDateFormat("dd-MM-yyyy HH:mm").format(selectedTime.getTime()));

            Query query = db.collection("Appointments")
                    .whereEqualTo("tableId", tableId)
                    .whereEqualTo("date", selectedTimestamp);

            query.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    boolean alreadyBooked = false;
                    for (DocumentSnapshot document : task.getResult()) {
                        Timestamp bookedTime = document.getTimestamp("date");
                        if (bookedTime != null && bookedTime.equals(selectedTimestamp)) {
                            alreadyBooked = true;
                            break;
                        }
                    }
                    if (alreadyBooked) {
                        Toast.makeText(itemView.getContext(), "This table is already booked for the selected time.", Toast.LENGTH_SHORT).show();
                    } else {
                        createAppointment(selectedTime);
                    }
                } else {
                    Toast.makeText(itemView.getContext(), "Error checking for existing bookings: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }



        private void createAppointment(Calendar time) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            Map<String, Object> appointment = new HashMap<>();
            appointment.put("tableId", tableId);
            appointment.put("date", new Timestamp(time.getTime()));  // Dátum és idő Timestamp-ként
            appointment.put("user_email",User.email);
            appointment.put("user_name", User.name);
            appointment.put("user_phone", User.phone);

            db.collection("Appointments").add(appointment)
                    .addOnSuccessListener(documentReference ->
                    {
                        scheduleNotification(time, documentReference.getId().hashCode());
                        Toast.makeText(itemView.getContext(), "Booked successfully.", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> Toast.makeText(itemView.getContext(), "Failed to book.", Toast.LENGTH_SHORT).show());
        }

        private void scheduleNotification(Calendar time, int notificationId) {
            long notificationTime = time.getTimeInMillis();

            Intent intent = new Intent(itemView.getContext(), NotificationReceiver.class);
            intent.putExtra(NotificationReceiver.NOTIFICATION_ID, notificationId);
            intent.putExtra(NotificationReceiver.NOTIFICATION_TITLE, "Appointment Reminder");
            intent.putExtra(NotificationReceiver.NOTIFICATION_BODY, "You have an appointment at Table " + tableId);

            PendingIntent alarmIntent = PendingIntent.getBroadcast(itemView.getContext(), notificationId, intent, PendingIntent.FLAG_IMMUTABLE);

            AlarmManager alarmManager = (AlarmManager) itemView.getContext().getSystemService(Context.ALARM_SERVICE);
            if (alarmManager != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, notificationTime, alarmIntent);
                } else {
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, notificationTime, alarmIntent);
                }
            }
        }

    }



}

