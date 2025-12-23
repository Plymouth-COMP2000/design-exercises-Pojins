package com.example.savoryrestaurant;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class GuestAddReservationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guest_add_reservation);

        // Menu icon click handling
        ImageView menuIcon = findViewById(R.id.menuIcon);
        if (menuIcon != null) {
            menuIcon.setOnClickListener(v -> {
                Intent intent = new Intent(
                        GuestAddReservationActivity.this,
                        ProfileActivity.class
                );
                startActivity(intent);
            });

        }
    }
}
