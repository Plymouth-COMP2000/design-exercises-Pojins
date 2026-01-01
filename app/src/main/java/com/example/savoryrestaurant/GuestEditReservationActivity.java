package com.example.savoryrestaurant;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class GuestEditReservationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guest_edit_reservation);

        // Menu icon click handling
        ImageView menuIcon = findViewById(R.id.guestmenuIcon);
        if (menuIcon != null) {
            menuIcon.setOnClickListener(v -> {
                Intent intent = new Intent(
                        GuestEditReservationActivity.this,
                        GuestProfileActivity.class
                );
                startActivity(intent);
            });
        }


        Button saveChanges = findViewById(R.id.saveButton);

        saveChanges.setOnClickListener(v -> {
            Toast.makeText(this, "Changes saved", Toast.LENGTH_SHORT).show();
            finish(); // go back
        });
    }
}
