package com.example.savoryrestaurant;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        Button notificationButton = findViewById(R.id.notificationButton);

        notificationButton.setOnClickListener(v -> {
            startActivity(new Intent(
                    ProfileActivity.this,
                    NotificationActivity.class
            ));
        });
    }
}
