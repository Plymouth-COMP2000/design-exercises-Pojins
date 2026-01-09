package com.example.savoryrestaurant;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class ReservationActivity extends AppCompatActivity {

    private ReservationDatabaseHelper db;
    private Cursor cursor;

    private long selectedReservationId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reservations);

        db = new ReservationDatabaseHelper(this);

        // Menu icon -> Staff profile
        ImageView menuIcon = findViewById(R.id.menuIcon);
        if (menuIcon != null) {
            menuIcon.setOnClickListener(v ->
                    startActivity(new Intent(ReservationActivity.this, ProfileActivity.class))
            );
        }

        ListView listView = findViewById(R.id.reservationsListView);
        Button cancelButton = findViewById(R.id.cancelButton);

        // Allow selecting one reservation
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        selectedReservationId = -1;

        listView.setOnItemClickListener((parent, view, position, id) -> {
            selectedReservationId = id;
            listView.setItemChecked(position, true);
        });

        cancelButton.setOnClickListener(v -> {
            if (selectedReservationId == -1) {
                Toast.makeText(this, "Tap a reservation to select it first", Toast.LENGTH_SHORT).show();
                return;
            }

            new AlertDialog.Builder(this)
                    .setTitle("Cancel reservation?")
                    .setMessage("Are you sure you want to cancel this reservation?")
                    .setPositiveButton("Yes", (dialog, which) -> {

                        // ✅ Read reservation details BEFORE delete (owner + guest name)
                        String ownerUsername = null;
                        String guestName = "Guest";

                        Cursor c = null;
                        try {
                            c = db.getReservationById(selectedReservationId);
                            if (c != null && c.moveToFirst()) {
                                ownerUsername = c.getString(c.getColumnIndexOrThrow("owner_username"));
                                guestName = c.getString(c.getColumnIndexOrThrow("name"));
                            }
                        } catch (Exception ignored) {
                        } finally {
                            if (c != null) c.close();
                        }

                        int deleted = db.deleteReservationById(selectedReservationId);

                        if (deleted > 0) {
                            Toast.makeText(this, "Reservation cancelled", Toast.LENGTH_SHORT).show();

                            // ✅ Logged-in state (who is active right now?)
                            SharedPreferences userPrefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                            boolean loggedIn = userPrefs.getBoolean("loggedIn", false);
                            String activeRole = userPrefs.getString("role", "");
                            String activeUsername = userPrefs.getString("username", "");
                            if (activeUsername == null) activeUsername = "";
                            activeUsername = activeUsername.trim().toLowerCase();

                            // ✅ STAFF notification: only if logged in as staff + toggle enabled
                            if (loggedIn && "staff".equalsIgnoreCase(activeRole) && AccountNotifPrefs.isStaffEnabled(this)) {
                                NotificationUtils.show(
                                        this,
                                        3001,
                                        "Reservation cancelled",
                                        "A reservation was cancelled by staff."
                                );
                            }

                            // ✅ GUEST notification: only if the *guest account is currently logged in*
                            // AND it's the same user who owns the reservation
                            if (ownerUsername != null) {
                                String ownerKey = ownerUsername.trim().toLowerCase();

                                boolean guestIsActiveOwner =
                                        loggedIn &&
                                                "guest".equalsIgnoreCase(activeRole) &&
                                                activeUsername.equals(ownerKey);

                                if (guestIsActiveOwner && AccountNotifPrefs.isGuestEnabled(this)) {
                                    NotificationUtils.show(
                                            this,
                                            2003,
                                            "Reservation update",
                                            guestName + "'s reservation was cancelled."
                                    );
                                }
                            }

                            selectedReservationId = -1;
                            loadReservations(); // refresh list
                        } else {
                            Toast.makeText(this, "Could not cancel reservation", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                    .show();
        });

        loadReservations();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadReservations();
    }

    private void loadReservations() {
        if (cursor != null && !cursor.isClosed()) cursor.close();

        cursor = db.getAllReservations();

        ListView listView = findViewById(R.id.reservationsListView);
        if (listView != null) {
            ReservationAdapter adapter = new ReservationAdapter(this, cursor);
            listView.setAdapter(adapter);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cursor != null && !cursor.isClosed()) cursor.close();
    }
}
