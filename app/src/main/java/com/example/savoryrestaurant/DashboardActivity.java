package com.example.savoryrestaurant;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);

        // 🔐 Get STAFF session data
        SharedPreferences prefs =
                getSharedPreferences("UserPrefs", MODE_PRIVATE);

        String username = prefs.getString("username", "STAFF");

        // 👋 WELCOME, USERNAME (ALL CAPS)
        TextView welcomeText = findViewById(R.id.welcomeText);
        if (welcomeText != null) {
            welcomeText.setText(("WELCOME, " + username).toUpperCase());
        }

        // ☰ Menu icon → Staff Profile
        ImageView menuIcon = findViewById(R.id.menuIcon);
        if (menuIcon != null) {
            menuIcon.setOnClickListener(v -> {
                startActivity(new Intent(
                        DashboardActivity.this,
                        ProfileActivity.class
                ));
            });
        }

        // 🍽 Manage Menu
        Button manageMenuButton = findViewById(R.id.manageMenuButton);
        manageMenuButton.setOnClickListener(v -> {
            startActivity(new Intent(
                    DashboardActivity.this,
                    MenuActivity.class
            ));
        });

        // 📅 Manage Reservations
        Button manageReservationButton = findViewById(R.id.manageReservationButton);
        manageReservationButton.setOnClickListener(v -> {
            startActivity(new Intent(
                    DashboardActivity.this,
                    ReservationActivity.class
            ));
        });
    }
}
