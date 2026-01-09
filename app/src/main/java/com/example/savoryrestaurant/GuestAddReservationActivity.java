package com.example.savoryrestaurant;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Locale;

public class GuestAddReservationActivity extends AppCompatActivity {

    private EditText nameInput, dateInput, timeInput, guestsInput;
    private Button saveButton;

    private ReservationDatabaseHelper db;

    private long editingId = -1;

    // store the "real" selected values for saving as YYYY-MM-DD HH:MM
    private int selYear = -1, selMonth = -1, selDay = -1, selHour = -1, selMinute = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guest_add_reservation);

        db = new ReservationDatabaseHelper(this);

        ImageView menuIcon = findViewById(R.id.guestmenuIcon);
        if (menuIcon != null) {
            menuIcon.setOnClickListener(v ->
                    startActivity(new Intent(this, GuestProfileActivity.class))
            );
        }

        nameInput = findViewById(R.id.nameInput);
        dateInput = findViewById(R.id.dateInput);
        timeInput = findViewById(R.id.timeInput);
        guestsInput = findViewById(R.id.guestsInput);
        saveButton = findViewById(R.id.saveChangesButton);

        dateInput.setOnClickListener(v -> showDatePicker());
        timeInput.setOnClickListener(v -> showTimePicker());

        // Are we editing?
        editingId = getIntent().getLongExtra("reservation_id", -1);
        if (editingId != -1) {
            loadReservationIntoForm(editingId);
        }

        saveButton.setOnClickListener(v -> saveOrUpdate());
    }

    private void loadReservationIntoForm(long id) {
        Cursor c = null;
        try {
            c = db.getReservationById(id);
            if (c != null && c.moveToFirst()) {
                String name = c.getString(c.getColumnIndexOrThrow("name"));
                String dateTime = c.getString(c.getColumnIndexOrThrow("date_time"));
                int guests = c.getInt(c.getColumnIndexOrThrow("party_size"));

                nameInput.setText(name);
                guestsInput.setText(String.valueOf(guests));

                // date_time format assumed "YYYY-MM-DD HH:MM"
                if (dateTime != null && dateTime.contains(" ")) {
                    String[] parts = dateTime.split(" ");
                    if (parts.length >= 2) {
                        dateInput.setText(parts[0]); // YYYY-MM-DD
                        timeInput.setText(parts[1]); // HH:MM

                        String[] d = parts[0].split("-");
                        String[] t = parts[1].split(":");
                        if (d.length == 3) {
                            selYear = Integer.parseInt(d[0]);
                            selMonth = Integer.parseInt(d[1]) - 1;
                            selDay = Integer.parseInt(d[2]);
                        }
                        if (t.length >= 2) {
                            selHour = Integer.parseInt(t[0]);
                            selMinute = Integer.parseInt(t[1]);
                        }
                    }
                }
            }
        } catch (Exception ignored) {
            Toast.makeText(this, "Could not load reservation", Toast.LENGTH_SHORT).show();
        } finally {
            if (c != null) c.close();
        }
    }

    private void showDatePicker() {
        Calendar cal = Calendar.getInstance();
        int year = (selYear != -1) ? selYear : cal.get(Calendar.YEAR);
        int month = (selMonth != -1) ? selMonth : cal.get(Calendar.MONTH);
        int day = (selDay != -1) ? selDay : cal.get(Calendar.DAY_OF_MONTH);

        new DatePickerDialog(this, (view, y, m, d) -> {
            selYear = y;
            selMonth = m;
            selDay = d;

            String date = String.format(Locale.UK, "%04d-%02d-%02d", y, m + 1, d);
            dateInput.setText(date);
        }, year, month, day).show();
    }

    private void showTimePicker() {
        Calendar cal = Calendar.getInstance();
        int hour = (selHour != -1) ? selHour : cal.get(Calendar.HOUR_OF_DAY);
        int min = (selMinute != -1) ? selMinute : cal.get(Calendar.MINUTE);

        new TimePickerDialog(this, (view, h, m) -> {
            selHour = h;
            selMinute = m;

            String time = String.format(Locale.UK, "%02d:%02d", h, m);
            timeInput.setText(time);
        }, hour, min, true).show();
    }

    private void saveOrUpdate() {
        String name = nameInput.getText().toString().trim();
        String date = dateInput.getText().toString().trim();
        String time = timeInput.getText().toString().trim();
        String guestsText = guestsInput.getText().toString().trim();

        if (name.isEmpty() || date.isEmpty() || time.isEmpty() || guestsText.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int guests;
        try {
            guests = Integer.parseInt(guestsText);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid number of guests", Toast.LENGTH_SHORT).show();
            return;
        }

        if (guests <= 0) {
            Toast.makeText(this, "Guests must be at least 1", Toast.LENGTH_SHORT).show();
            return;
        }

        String dateTime = date + " " + time;

        // ✅ session check once, reuse
        SharedPreferences userPrefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        boolean loggedIn = userPrefs.getBoolean("loggedIn", false);
        String ownerUsername = userPrefs.getString("username", "guest");

        if (editingId == -1) {

            long id = db.addReservation(ownerUsername, name, "", guests, dateTime); // ✅ updated signature
            if (id == -1) {
                Toast.makeText(this, "Failed to save reservation", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(this, "Reservation saved", Toast.LENGTH_SHORT).show();

            // ✅ Guest notification: only if logged in + toggle enabled
            if (loggedIn && AccountNotifPrefs.isGuestEnabled(this)) {
                NotificationUtils.show(this, 2001, "Reservation saved", "Your reservation has been added.");
            }

            // ✅ Staff notification: only if logged in + toggle enabled
            // (Note: on a real multi-device system this would be server-side push;
            // in this coursework it shows locally on the same device.)
            if (loggedIn && AccountNotifPrefs.isStaffEnabled(this)) {
                NotificationUtils.show(this, 3002, "New reservation", name + " made a reservation.");
            }

        } else {
            int rows = db.updateReservation(editingId, name, dateTime, guests);
            if (rows <= 0) {
                Toast.makeText(this, "Failed to update reservation", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(this, "Reservation updated", Toast.LENGTH_SHORT).show();

            // ✅ Guest notification: only if logged in + toggle enabled
            if (loggedIn && AccountNotifPrefs.isGuestEnabled(this)) {
                NotificationUtils.show(this, 2002, "Reservation updated", "Your reservation has been updated.");
            }
        }

        finish();
    }
}
