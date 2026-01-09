package com.example.savoryrestaurant;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class GuestMenuActivity extends AppCompatActivity {

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guest_menu);

        // Menu icon → Guest Profile
        ImageView menuIcon = findViewById(R.id.guestmenuIcon);
        if (menuIcon != null) {
            menuIcon.setOnClickListener(v -> {
                startActivity(new Intent(
                        GuestMenuActivity.this,
                        GuestProfileActivity.class
                ));
            });
        }

        // ListView
        ListView menuListView = findViewById(R.id.menuListView);


        MenuDatabaseHelper db = new MenuDatabaseHelper(this);
        Cursor cursor = db.getAllMenuItems();

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "Menu coming soon", Toast.LENGTH_SHORT).show();
        }

        MenuAdapter adapter = new MenuAdapter(this, cursor);
        menuListView.setAdapter(adapter);

    }
}
