package com.example.savoryrestaurant;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class GuestNotificationActivity extends AppCompatActivity {

    private static final String PREFS = "NotifPrefs";
    private static final String KEY_GUEST_NOTIF = "guest_notifications";

    private Button btnOn;
    private Button btnOff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guest_notification);

        btnOn = findViewById(R.id.buttnOn1);
        btnOff = findViewById(R.id.btnOff1);

        // Load saved state
        boolean enabled = AccountNotifPrefs.isGuestEnabled(this);
        updateUi(enabled);

        btnOn.setOnClickListener(v -> {
            AccountNotifPrefs.setGuestEnabled(this, true);
            updateUi(true);
        });

        btnOff.setOnClickListener(v -> {
            AccountNotifPrefs.setGuestEnabled(this, false);
            updateUi(false);
        });

    }

    private SharedPreferences getPrefs() {
        return getSharedPreferences(PREFS, MODE_PRIVATE);
    }

    private void saveGuestNotif(boolean enabled) {
        getPrefs().edit().putBoolean(KEY_GUEST_NOTIF, enabled).apply();
    }

    private void updateUi(boolean enabled) {
        // Selected look: ON pressed when enabled, OFF pressed when disabled
        btnOn.setSelected(enabled);
        btnOff.setSelected(!enabled);

        // Optional: also disable the active button so it's obvious
        btnOn.setEnabled(!enabled);
        btnOff.setEnabled(enabled);
    }
}
