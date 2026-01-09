package com.example.savoryrestaurant;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class MenuActivity extends AppCompatActivity {

    private MenuDatabaseHelper db;
    private Cursor cursor;
    private MenuAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        db = new MenuDatabaseHelper(this);

        // Menu icon → Staff Profile
        ImageView menuIcon = findViewById(R.id.menuIcon);
        if (menuIcon != null) {
            menuIcon.setOnClickListener(v ->
                    startActivity(new Intent(MenuActivity.this, ProfileActivity.class))
            );
        }

        // ListView
        ListView menuListView = findViewById(R.id.menuListView);
        if (menuListView == null) {
            Toast.makeText(this, "menuListView not found in menu.xml", Toast.LENGTH_LONG).show();
            return;
        }

        cursor = db.getAllMenuItems();
        adapter = new MenuAdapter(this, cursor);
        menuListView.setAdapter(adapter);

        // ✅ Tap to edit
        menuListView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(MenuActivity.this, MenuEditActivity.class);
            intent.putExtra("menu_item_id", id); // this is the DB row id via _id
            startActivity(intent);
        });

        // ✅ Long-press to delete
        menuListView.setOnItemLongClickListener((parent, view, position, id) -> {
            long itemId = id;

            new AlertDialog.Builder(MenuActivity.this)
                    .setTitle("Delete menu item?")
                    .setMessage("Are you sure you want to delete this menu item?")
                    .setPositiveButton("Delete", (dialog, which) -> {
                        int deleted = db.deleteMenuItem(itemId);

                        if (deleted > 0) {
                            Toast.makeText(this, "Item deleted", Toast.LENGTH_SHORT).show();
                            reloadMenu();
                        } else {
                            Toast.makeText(this, "Delete failed", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                    .show();

            return true;
        });

        // + ADD NEW ITEM button
        Button addItemButton = findViewById(R.id.addItemButton);
        if (addItemButton != null) {
            addItemButton.setOnClickListener(v ->
                    startActivity(new Intent(MenuActivity.this, MenuEditActivity.class))
            );
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        reloadMenu();
    }

    private void reloadMenu() {
        Cursor newCursor = db.getAllMenuItems();
        if (adapter != null) {
            Cursor old = adapter.swapCursor(newCursor);
            if (old != null && !old.isClosed()) old.close();
        } else {
            cursor = newCursor;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cursor != null && !cursor.isClosed()) cursor.close();
    }
}
