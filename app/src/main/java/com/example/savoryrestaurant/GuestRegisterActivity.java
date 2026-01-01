package com.example.savoryrestaurant;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class GuestRegisterActivity extends AppCompatActivity {

    private static final String TAG = "GuestRegister";

    private static final String BASE_URL = "http://10.240.72.69/comp2000/coursework";
    private static final String STUDENT_ID = "10928038";

    private static String CREATE_STUDENT_URL() {
        return BASE_URL + "/create_student/" + STUDENT_ID;
    }

    private static String CREATE_USER_URL() {
        return BASE_URL + "/create_user/" + STUDENT_ID;
    }

    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guest_register);

        queue = Volley.newRequestQueue(this);

        EditText usernameInput = findViewById(R.id.usernameInput);
        EditText firstNameInput = findViewById(R.id.firstNameInput);
        EditText lastNameInput = findViewById(R.id.lastNameInput);
        EditText contactInput = findViewById(R.id.contactInput);
        EditText emailInput = findViewById(R.id.emailInput);
        EditText passwordInput = findViewById(R.id.passwordInput);
        Button signupButton = findViewById(R.id.signupButton);

        signupButton.setOnClickListener(v -> {

            String username = usernameInput.getText().toString().trim();
            String firstName = firstNameInput.getText().toString().trim();
            String lastName = lastNameInput.getText().toString().trim();
            String contact = contactInput.getText().toString().trim();
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (username.isEmpty() || firstName.isEmpty() || lastName.isEmpty()
                    || contact.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            JSONObject userData = new JSONObject();
            try {
                userData.put("username", username);
                userData.put("password", password);
                userData.put("firstname", firstName);
                userData.put("lastname", lastName);
                userData.put("email", email);
                userData.put("contact", contact);

                // API doc uses "student" / "staff". Use "student" for guest side. :contentReference[oaicite:2]{index=2}
                userData.put("usertype", "student");
            } catch (JSONException e) {
                Toast.makeText(this, "JSON error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                return;
            }

            // 1) Ensure DB exists, then 2) Create user
            ensureStudentDbThenRegister(userData, username);
        });
    }

    private void ensureStudentDbThenRegister(JSONObject userData, String username) {
        // create_student has no request body
        JsonObjectRequest createDbReq = new JsonObjectRequest(
                Request.Method.POST,
                CREATE_STUDENT_URL(),
                null,
                response -> {
                    // Even if it already exists, it may still respond OK. Then register.
                    sendRegisterRequest(userData, username);
                },
                error -> {
                    // If DB already exists, some APIs still return an error.
                    // We'll still attempt registration after logging the error.
                    Log.e(TAG, "create_student error: " + volleyErrorToString(error));
                    sendRegisterRequest(userData, username);
                }
        );

        createDbReq.setRetryPolicy(new DefaultRetryPolicy(
                15000, // 15s
                0,
                1.0f
        ));

        queue.add(createDbReq);
    }

    private void sendRegisterRequest(JSONObject userData, String username) {
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                CREATE_USER_URL(),
                userData,
                response -> {
                    SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                    prefs.edit()
                            .putBoolean("loggedIn", true)
                            .putString("role", "guest")
                            .putString("username", username)
                            .apply();

                    Toast.makeText(this, "Registration successful", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(GuestRegisterActivity.this, GuestDashboardActivity.class));
                    finish();
                },
                error -> {
                    Log.e(TAG, "create_user error: " + volleyErrorToString(error));
                    Toast.makeText(this, "Registration failed: " + readableVolleyError(error), Toast.LENGTH_LONG).show();
                }
        );

        request.setRetryPolicy(new DefaultRetryPolicy(
                15000, // 15s
                0,
                1.0f
        ));

        queue.add(request);
    }

    private String readableVolleyError(VolleyError error) {
        if (error.networkResponse == null) return "No response (check VPN/Wi-Fi, cleartext HTTP, server reachability)";
        return "HTTP " + error.networkResponse.statusCode;
    }

    private String volleyErrorToString(VolleyError error) {
        if (error == null) return "null";
        if (error.networkResponse == null) return error.toString();
        return error.toString() + " | status=" + error.networkResponse.statusCode;
    }
}
