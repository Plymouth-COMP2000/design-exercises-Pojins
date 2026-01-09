package com.example.savoryrestaurant;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        // 🔔 Notifications
        Button notificationButton = findViewById(R.id.notificationButton);
        notificationButton.setOnClickListener(v -> {
            startActivity(new Intent(
                    ProfileActivity.this,
                    NotificationActivity.class
            ));
        });

        // 🚪 STAFF LOGOUT
        ImageView signOutIcon = findViewById(R.id.signOutIcon);

        signOutIcon.setOnClickListener(v -> {

            // Clear staff session
            SharedPreferences prefs =
                    getSharedPreferences("UserPrefs", MODE_PRIVATE);
            prefs.edit().clear().apply();

            // Go back to MAIN ACTIVITY
            Intent intent = new Intent(
                    ProfileActivity.this,
                    MainActivity.class
            );

            intent.setFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK
            );

            startActivity(intent);
            finish();
        });

    }
}
