package com.example.savoryrestaurant;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cursoradapter.widget.CursorAdapter;

public class ReservationAdapter extends CursorAdapter {

    public ReservationAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context)
                .inflate(R.layout.reservation_item_row, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView nameText = view.findViewById(R.id.reservationNameText);
        TextView detailsText = view.findViewById(R.id.reservationDetailsText);

        String name = cursor.getString(
                cursor.getColumnIndexOrThrow("name")
        );

        String dateTime = cursor.getString(
                cursor.getColumnIndexOrThrow("date_time")
        );

        int guests = cursor.getInt(
                cursor.getColumnIndexOrThrow("party_size")
        );

        String date = "";
        String time = "";

        if (dateTime != null && dateTime.contains(" ")) {
            String[] parts = dateTime.split(" ");
            date = parts[0];
            time = parts[1];
        }

        nameText.setText(name);
        detailsText.setText(date + " | " + time + " | Guests: " + guests);
    }

}
