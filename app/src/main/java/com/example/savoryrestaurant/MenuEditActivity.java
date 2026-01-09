package com.example.savoryrestaurant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Locale;

public class MenuEditActivity extends AppCompatActivity {

    // Spinner labels + drawable names saved in DB
    private final String[] imageLabels = {"Lamb", "Pasta", "Salmon", "Coffee"};
    private final String[] imageDrawables = {"lamb", "pasta", "salmon", "coffee"};

    private EditText nameInput, priceInput;
    private Spinner imageSpinner;
    private ImageView imagePreview;
    private Button saveButton;

    private MenuDatabaseHelper db;

    private long editingId = -1; // -1 = add mode

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_edit);

        db = new MenuDatabaseHelper(this);

        // Menu icon → Staff profile
        ImageView menuIcon = findViewById(R.id.menuIcon);
        if (menuIcon != null) {
            menuIcon.setOnClickListener(v ->
                    startActivity(new Intent(MenuEditActivity.this, ProfileActivity.class))
            );
        }

        nameInput = findViewById(R.id.nameInput);
        priceInput = findViewById(R.id.priceInput);
        saveButton = findViewById(R.id.addItemButton);

        imageSpinner = findViewById(R.id.imageSpinner);
        imagePreview = findViewById(R.id.imagePreview);

        // Spinner setup
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                imageLabels
        );
        imageSpinner.setAdapter(spinnerAdapter);

        // Preview updates when selection changes
        imageSpinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                updatePreview(position);
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
                imagePreview.setImageResource(R.drawable.placeholder_food);
            }
        });

        // ✅ Default preview on first load
        imageSpinner.post(() -> updatePreview(imageSpinner.getSelectedItemPosition()));

        // ✅ Check if we are editing
        editingId = getIntent().getLongExtra("menu_item_id", -1);
        if (editingId != -1) {
            boolean loaded = loadItem(editingId);
            if (loaded) {
                saveButton.setText("SAVE CHANGES");
            } else {
                // If item not found, fall back to add mode
                editingId = -1;
                saveButton.setText("ADD ITEM");
                Toast.makeText(this, "Item not found (maybe deleted). Adding new item.", Toast.LENGTH_SHORT).show();
            }
        } else {
            saveButton.setText("ADD ITEM");
        }

        saveButton.setOnClickListener(v -> saveOrUpdate());
    }

    private void updatePreview(int pos) {
        if (pos < 0 || pos >= imageDrawables.length) pos = 0;

        String drawableName = imageDrawables[pos];
        int resId = getResources().getIdentifier(drawableName, "drawable", getPackageName());

        if (resId != 0) imagePreview.setImageResource(resId);
        else imagePreview.setImageResource(R.drawable.placeholder_food);
    }

    /**
     * @return true if the item was found and loaded, false otherwise.
     */
    private boolean loadItem(long id) {
        Cursor c = null;
        try {
            c = db.getMenuItemById(id);
            if (c == null || !c.moveToFirst()) return false;

            String name = c.getString(c.getColumnIndexOrThrow("name"));
            double price = c.getDouble(c.getColumnIndexOrThrow("price"));
            String image = c.getString(c.getColumnIndexOrThrow("image"));

            nameInput.setText(name);

            // Only format when loading existing item (don’t annoy user input)
            priceInput.setText(String.format(Locale.UK, "%.2f", price));

            // set spinner to matching image
            int spinnerPos = 0;
            if (image != null) {
                for (int i = 0; i < imageDrawables.length; i++) {
                    if (image.equalsIgnoreCase(imageDrawables[i])) {
                        spinnerPos = i;
                        break;
                    }
                }
            }

            imageSpinner.setSelection(spinnerPos);
            updatePreview(spinnerPos);

            return true;

        } catch (Exception e) {
            return false;
        } finally {
            if (c != null) c.close();
        }
    }

    private void saveOrUpdate() {
        String name = nameInput.getText().toString().trim();
        String priceText = priceInput.getText().toString().trim();

        if (name.isEmpty() || priceText.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceText);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid price", Toast.LENGTH_SHORT).show();
            return;
        }

        int pos = imageSpinner.getSelectedItemPosition();
        if (pos < 0 || pos >= imageDrawables.length) pos = 0;
        String imageName = imageDrawables[pos];

        if (editingId == -1) {
            long newId = db.addMenuItem(name, price, imageName);
            if (newId == -1) {
                Toast.makeText(this, "Add failed", Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(this, "Menu item added", Toast.LENGTH_SHORT).show();
        } else {
            int rows = db.updateMenuItem(editingId, name, price, imageName);
            if (rows > 0) Toast.makeText(this, "Menu item updated", Toast.LENGTH_SHORT).show();
            else {
                Toast.makeText(this, "Update failed (item missing?)", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        finish();
    }
}
