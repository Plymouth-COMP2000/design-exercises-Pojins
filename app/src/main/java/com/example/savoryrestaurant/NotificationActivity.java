package com.example.savoryrestaurant;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class NotificationActivity extends AppCompatActivity {

    private Button btnOn;
    private Button btnOff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification);

        btnOn = findViewById(R.id.buttnOn1);
        btnOff = findViewById(R.id.btnOff1);

        // ✅ Load saved state for THIS staff account
        boolean enabled = AccountNotifPrefs.isStaffEnabled(this);
        updateUi(enabled);

        btnOn.setOnClickListener(v -> {
            AccountNotifPrefs.setStaffEnabled(this, true);
            updateUi(true);
        });

        btnOff.setOnClickListener(v -> {
            AccountNotifPrefs.setStaffEnabled(this, false);
            updateUi(false);
        });
    }

    private void updateUi(boolean enabled) {
        btnOn.setSelected(enabled);
        btnOff.setSelected(!enabled);

        // disable active button for clarity
        btnOn.setEnabled(!enabled);
        btnOff.setEnabled(enabled);
    }
}
