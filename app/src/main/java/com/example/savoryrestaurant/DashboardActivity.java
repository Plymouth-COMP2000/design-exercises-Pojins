package com.example.savoryrestaurant;

import android.content.Intent;
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

        // Menu icon click handling
        ImageView menuIcon = findViewById(R.id.menuIcon);
        if (menuIcon != null) {
            menuIcon.setOnClickListener(v -> {
                Intent intent = new Intent(
                        DashboardActivity.this,
                        ProfileActivity.class
                );
                startActivity(intent);
            });
        }

        TextView welcomeText = findViewById(R.id.welcomeText);

        // Get name from login screen (future use)
        String userName = getIntent().getStringExtra("USER_NAME");

        // Default if no name is received yet
        if (userName == null || userName.trim().isEmpty()) {
            userName = "Guest";
        }

        // Display the placeholder text
        welcomeText.setText("WELCOME, " + userName);

        Button manageMenuButton = findViewById(R.id.manageMenuButton);
        manageMenuButton.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, MenuActivity.class);
            startActivity(intent);
        });

        Button manageReservationButton = findViewById(R.id.manageReservationButton);
        manageReservationButton.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, ReservationActivity.class);
            startActivity(intent);
        });
    }
}
