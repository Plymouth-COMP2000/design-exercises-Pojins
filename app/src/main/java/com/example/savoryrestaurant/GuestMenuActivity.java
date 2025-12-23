package com.example.savoryrestaurant;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.savoryrestaurant.data.MenuRepository;

public class GuestMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guest_menu);

        // Menu icon click handling
        ImageView menuIcon = findViewById(R.id.menuIcon);

        if (menuIcon != null) {
            menuIcon.setOnClickListener(v -> {
                Intent intent = new Intent(
                        GuestMenuActivity.this,
                        ProfileActivity.class
                );
                startActivity(intent);
            });
        }

        MenuRepository repo = MenuRepository.getInstance();

        // Later: plug this into RecyclerView
    }
}
