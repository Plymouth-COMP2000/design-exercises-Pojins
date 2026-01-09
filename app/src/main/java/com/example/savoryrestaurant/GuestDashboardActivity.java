package com.example.savoryrestaurant;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class GuestDashboardActivity extends AppCompatActivity {

    private static final int REQ_NOTIF = 2001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guest_home);

        // ✅ Request notification permission (Android 13+)
        requestNotifPermissionIfNeeded();

        // 🔒 SAFE SESSION GUARD (LOGGED IN ONLY)
        SharedPreferences prefs =
                getSharedPreferences("UserPrefs", MODE_PRIVATE);

        boolean loggedIn = prefs.getBoolean("loggedIn", false);

        if (!loggedIn) {
            startActivity(new Intent(this, UserSelectionActivity.class));
            finish();
            return;
        }

        // 👋 WELCOME USERNAME (ALL CAPS)
        TextView welcomeText = findViewById(R.id.welcomeText);
        String username = prefs.getString("username", "Guest");

        if (welcomeText != null) {
            welcomeText.setText(("WELCOME, " + username).toUpperCase());
        }

        // ☰ Menu icon → Guest Profile
        ImageView menuIcon = findViewById(R.id.guestmenuIcon);
        if (menuIcon != null) {
            menuIcon.setOnClickListener(v -> {
                startActivity(new Intent(
                        GuestDashboardActivity.this,
                        GuestProfileActivity.class
                ));
            });
        }

        // 🍽 Guest Menu
        Button guestMenuButton = findViewById(R.id.GuestMenuButton);
        if (guestMenuButton != null) {
            guestMenuButton.setOnClickListener(v -> {
                startActivity(new Intent(this, GuestMenuActivity.class));
            });
        }

        // 📅 Guest Reservations
        Button guestReservationButton = findViewById(R.id.GuestReservationButton);
        if (guestReservationButton != null) {
            guestReservationButton.setOnClickListener(v -> {
                startActivity(new Intent(this, GuestReservationActivity.class));
            });
        }
    }

    private void requestNotifPermissionIfNeeded() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS)
                    != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                        new String[]{android.Manifest.permission.POST_NOTIFICATIONS},
                        REQ_NOTIF
                );
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQ_NOTIF) {
            boolean granted = grantResults.length > 0
                    && grantResults[0] == android.content.pm.PackageManager.PERMISSION_GRANTED;

            Toast.makeText(this,
                    granted ? "Notifications enabled" : "Notifications disabled",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }
}
