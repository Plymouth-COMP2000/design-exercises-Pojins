package com.example.savoryrestaurant;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class GuestProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guest_profile);

        // Notification button
        Button notificationButton = findViewById(R.id.notificationButton);
        notificationButton.setOnClickListener(v -> {
            startActivity(new Intent(
                    GuestProfileActivity.this,
                    GuestNotificationActivity.class
            ));
        });

        // Logout icon
        ImageView signOutIcon = findViewById(R.id.signOutIcon);
        signOutIcon.setOnClickListener(v -> {

            // Clear saved login/session data
            SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            prefs.edit().clear().apply();

            // Go back to login screen and clear back stack
            Intent intent = new Intent(
                    GuestProfileActivity.this,
                    MainActivity.class
            );
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }
}
