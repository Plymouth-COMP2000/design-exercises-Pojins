package com.example.savoryrestaurant;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class GuestReservationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guest_reservation);

        // Menu icon click handling
        ImageView menuIcon = findViewById(R.id.guestmenuIcon);
        if (menuIcon != null) {
            menuIcon.setOnClickListener(v -> {
                Intent intent = new Intent(
                        GuestReservationActivity.this,
                        GuestProfileActivity.class
                );
                startActivity(intent);
            });
        }

        Button addButton = findViewById(R.id.addReservationButton);

        addButton.setOnClickListener(v -> {
            // Page 2: Add Reservation
            Intent intent = new Intent(
                    GuestReservationActivity.this,
                    GuestAddReservationActivity.class
            );
            startActivity(intent);
        });
    }
}
