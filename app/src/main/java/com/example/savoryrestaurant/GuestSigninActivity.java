package com.example.savoryrestaurant;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class GuestSigninActivity extends AppCompatActivity {

    // API base
    private static final String BASE_URL =
            "http://10.240.72.69/comp2000/coursework";

    // YOUR student ID
    private static final String STUDENT_ID = "10928038";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guest_signin);

        EditText usernameInput = findViewById(R.id.usernameInput);
        EditText passwordInput = findViewById(R.id.passwordInput);
        Button signInButton = findViewById(R.id.signInButton);
        TextView registerText = findViewById(R.id.registerText);

        // LOGIN
        signInButton.setOnClickListener(v -> {

            String username = usernameInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Enter username and password", Toast.LENGTH_SHORT).show();
                return;
            }

            loginUser(username, password);
        });

        // GO TO REGISTER
        registerText.setOnClickListener(v -> {
            startActivity(new Intent(this, GuestRegisterActivity.class));
        });
    }

    private void loginUser(String username, String password) {

        // API endpoint to read user
        String loginUrl = BASE_URL + "/read_user/" + STUDENT_ID + "/" + username;

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                loginUrl,
                null,
                response -> {
                    try {
                        JSONObject user = response.getJSONObject("user");

                        String apiPassword = user.getString("password");
                        String userType = user.getString("usertype");

                        // Check password
                        if (!apiPassword.equals(password)) {
                            Toast.makeText(this, "Incorrect password", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Check role
                        if (!userType.equals("student")) {
                            Toast.makeText(this, "Not a guest account", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Save login session
                        SharedPreferences prefs =
                                getSharedPreferences("UserPrefs", MODE_PRIVATE);

                        prefs.edit()
                                .putBoolean("loggedIn", true)
                                .putString("role", "guest")
                                .putString("username", username)
                                .apply();

                        Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(this, GuestDashboardActivity.class));
                        finish();

                    } catch (JSONException e) {
                        Toast.makeText(this, "Invalid API response", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Toast.makeText(
                            this,
                            "User not found or network error",
                            Toast.LENGTH_LONG
                    ).show();
                }
        );

        queue.add(request);
    }
}
