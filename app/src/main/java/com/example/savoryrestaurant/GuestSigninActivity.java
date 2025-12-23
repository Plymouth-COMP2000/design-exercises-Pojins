package com.example.savoryrestaurant;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

public class GuestSigninActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guest_signin);

        Button signInButton = findViewById(R.id.signupButton);

        signInButton.setOnClickListener(v -> {
            Intent intent = new Intent(GuestSigninActivity.this, GuestDashboardActivity.class);
            startActivity(intent);
        });

        TextView registerText = findViewById(R.id.registerText);

        registerText.setOnClickListener(v -> {
            Intent intent = new Intent(GuestSigninActivity.this, GuestRegisterActivity.class);
            startActivity(intent);
        });

    }
}
