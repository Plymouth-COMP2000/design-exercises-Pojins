package com.example.savoryrestaurant;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class GuestDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guest_home);

        // Menu icon click handling
        ImageView menuIcon = findViewById(R.id.menuIcon);
        if (menuIcon != null) {
            menuIcon.setOnClickListener(v -> {
                Intent intent = new Intent(
                        GuestDashboardActivity.this,
                        ProfileActivity.class
                );
                startActivity(intent);
            });
        }

        Button guestMenuButton = findViewById(R.id.GuestMenuButton);
        Button guestReservationButton = findViewById(R.id.GuestReservationButton);

        guestMenuButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, GuestMenuActivity.class);
            startActivity(intent);
        });

        guestReservationButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, GuestReservationActivity.class);
            startActivity(intent);
        });
    }
}
