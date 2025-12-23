package com.example.savoryrestaurant;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class UserSelectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userselection);

        Button guestButton = findViewById(R.id.guestButton);
        Button staffButton = findViewById(R.id.staffButton);

        // GUEST → Guest Home Page (replace with your actual activity)
        guestButton.setOnClickListener(v -> {
            Intent intent = new Intent(UserSelectionActivity.this, GuestSigninActivity.class);
            startActivity(intent);
        });

        // STAFF → Staff Login Page
        staffButton.setOnClickListener(v -> {
            Intent intent = new Intent(UserSelectionActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }
}
