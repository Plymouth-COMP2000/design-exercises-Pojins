package com.example.savoryrestaurant;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class GuestReservationActivity extends AppCompatActivity {

    private ReservationDatabaseHelper db;
    private Cursor cursor;

    private long selectedReservationId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new ReservationDatabaseHelper(this);
        renderScreen();
    }

    @Override
    protected void onResume() {
        super.onResume();
        renderScreen();
    }

    private void renderScreen() {
        if (cursor != null && !cursor.isClosed()) cursor.close();
        cursor = db.getAllReservations();

        boolean hasReservations = (cursor != null && cursor.getCount() > 0);
        setContentView(hasReservations ? R.layout.guest_reservation2 : R.layout.guest_reservation);

        // Menu icon
        ImageView menuIcon = findViewById(R.id.guestmenuIcon);
        if (menuIcon != null) {
            menuIcon.setOnClickListener(v ->
                    startActivity(new Intent(this, GuestProfileActivity.class))
            );
        }

        // ADD button (exists on both layouts)
        Button addButton = findViewById(R.id.addReservationButton);
        if (addButton != null) {
            addButton.setOnClickListener(v ->
                    startActivity(new Intent(this, GuestAddReservationActivity.class))
            );
        }

        if (!hasReservations) return;

        // List
        ListView listView = findViewById(R.id.reservationsListView);
        ReservationAdapter adapter = new ReservationAdapter(this, cursor);
        listView.setAdapter(adapter);

        // Use our row background selector, not the default ListView highlight
        listView.setSelector(android.R.color.transparent);

        // Allow selecting one reservation
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        selectedReservationId = -1;

        listView.setOnItemClickListener((parent, view, position, id) -> {
            // "id" is the row _id from cursor (maps to your real id)
            selectedReservationId = id;
            listView.setItemChecked(position, true);

            // Trigger state_activated for your selector background
            view.setActivated(true);
        });

        // EDIT button
        Button editButton = findViewById(R.id.editReservationButton);
        if (editButton != null) {
            editButton.setOnClickListener(v -> {
                if (selectedReservationId == -1) {
                    Toast.makeText(this, "Tap a reservation to select it first", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(this, GuestAddReservationActivity.class);
                intent.putExtra("reservation_id", selectedReservationId);
                startActivity(intent);
            });
        }

        // CANCEL button
        Button cancelButton = findViewById(R.id.cancelReservationButton);
        if (cancelButton != null) {
            cancelButton.setOnClickListener(v -> {
                if (selectedReservationId == -1) {
                    Toast.makeText(this, "Tap a reservation to select it first", Toast.LENGTH_SHORT).show();
                    return;
                }

                new AlertDialog.Builder(this)
                        .setTitle("Cancel reservation?")
                        .setMessage("Are you sure you want to cancel this reservation?")
                        .setPositiveButton("Yes", (dialog, which) -> {

                            int deleted = db.deleteReservationById(selectedReservationId);

                            if (deleted > 0) {
                                Toast.makeText(this, "Reservation cancelled", Toast.LENGTH_SHORT).show();
                                selectedReservationId = -1;
                                renderScreen(); // refresh list / swap to empty if none left
                            } else {
                                Toast.makeText(this, "Could not cancel reservation", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                        .show();
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cursor != null && !cursor.isClosed()) cursor.close();
    }
}
