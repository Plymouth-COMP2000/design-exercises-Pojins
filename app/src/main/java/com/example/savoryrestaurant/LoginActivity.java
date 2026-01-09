package com.example.savoryrestaurant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);

        EditText emailInput = findViewById(R.id.emailInput);
        EditText passwordInput = findViewById(R.id.passwordInput);
        Button signupButton = findViewById(R.id.signupButton);

        signupButton.setOnClickListener(v -> {

            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // ✅ (Temporary validation — replace with API later if needed)
            if (!email.equals("staff@savory") || !password.equals("admin")) {
                Toast.makeText(this, "Invalid staff credentials", Toast.LENGTH_SHORT).show();
                return;
            }

            // 🔐 SAVE STAFF SESSION
            SharedPreferences prefs =
                    getSharedPreferences("UserPrefs", MODE_PRIVATE);

            prefs.edit()
                    .putBoolean("loggedIn", true)
                    .putString("role", "staff")   // 🔴 CRITICAL
                    .putString("username", email)
                    .apply();

            // 🚀 Go to staff dashboard
            Intent intent = new Intent(
                    LoginActivity.this,
                    DashboardActivity.class
            );
            startActivity(intent);
            finish();
        });
    }
}
