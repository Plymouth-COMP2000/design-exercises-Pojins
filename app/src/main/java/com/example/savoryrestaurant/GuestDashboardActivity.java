package com.example.savoryrestaurant;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView; // ✅ ADD THIS

import androidx.appcompat.app.AppCompatActivity;

public class GuestDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guest_home);

        // ✅ WELCOME USERNAME
        TextView welcomeText = findViewById(R.id.welcomeText);

        SharedPreferences prefs =
                getSharedPreferences("UserPrefs", MODE_PRIVATE);

        String username = prefs.getString("username", "Guest");

        if (welcomeText != null) {
            welcomeText.setText(("WELCOME, " + username).toUpperCase());
        }

        // Menu icon → Guest Profile
        ImageView menuIcon = findViewById(R.id.guestmenuIcon);
        if (menuIcon != null) {
            menuIcon.setOnClickListener(v -> {
                startActivity(new Intent(
                        GuestDashboardActivity.this,
                        GuestProfileActivity.class
                ));
            });
        }

        Button guestMenuButton = findViewById(R.id.GuestMenuButton);
        Button guestReservationButton = findViewById(R.id.GuestReservationButton);

        if (guestMenuButton != null) {
            guestMenuButton.setOnClickListener(v -> {
                startActivity(new Intent(this, GuestMenuActivity.class));
            });
        }

        if (guestReservationButton != null) {
            guestReservationButton.setOnClickListener(v -> {
                startActivity(new Intent(this, GuestReservationActivity.class));
            });
        }
    }
}
