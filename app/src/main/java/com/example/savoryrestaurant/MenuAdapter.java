package com.example.savoryrestaurant;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cursoradapter.widget.CursorAdapter;

import java.io.File;

public class MenuAdapter extends CursorAdapter {

    public MenuAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context)
                .inflate(R.layout.menu_item_row, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView nameText = view.findViewById(R.id.itemNameText);
        TextView priceText = view.findViewById(R.id.itemPriceText);
        ImageView itemImage = view.findViewById(R.id.itemImageView); // <-- add this in XML

        String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
        double price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));
        String image = cursor.getString(cursor.getColumnIndexOrThrow("image")); // can be null

        nameText.setText(name);
        priceText.setText("£" + String.format(java.util.Locale.UK, "%.2f", price));


        // ----- Image handling -----
        if (itemImage != null) {
            if (TextUtils.isEmpty(image)) {
                // Placeholder if no image saved
                itemImage.setImageResource(R.drawable.lamb); // create/add this drawable
            } else {
                // 1) If "image" is a file path
                File f = new File(image);
                if (f.exists()) {
                    Bitmap bm = BitmapFactory.decodeFile(image);
                    if (bm != null) {
                        itemImage.setImageBitmap(bm);
                    } else {
                        itemImage.setImageResource(R.drawable.lamb);
                    }
                } else {
                    // 2) Or if "image" is a drawable resource name (e.g. "lamb_chops")
                    int resId = context.getResources().getIdentifier(image, "drawable", context.getPackageName());
                    if (resId != 0) {
                        itemImage.setImageResource(resId);
                    } else {
                        itemImage.setImageResource(R.drawable.lamb);
                    }
                }
            }
        }
    }
}
