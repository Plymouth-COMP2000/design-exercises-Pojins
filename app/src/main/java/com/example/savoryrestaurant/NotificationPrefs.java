package com.example.savoryrestaurant;

import android.content.Context;
import android.content.SharedPreferences;

public class NotificationPrefs {

    private static final String PREFS = "NotifPrefs";
    public static final String KEY_GUEST_NOTIF = "guest_notifications";
    public static final String KEY_STAFF_NOTIF = "staff_notifications";

    private static SharedPreferences prefs(Context context) {
        return context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    public static boolean guestEnabled(Context context) {
        return prefs(context).getBoolean(KEY_GUEST_NOTIF, true); // default ON
    }

    public static boolean staffEnabled(Context context) {
        return prefs(context).getBoolean(KEY_STAFF_NOTIF, true); // default ON
    }

    public static void setGuestEnabled(Context context, boolean enabled) {
        prefs(context).edit().putBoolean(KEY_GUEST_NOTIF, enabled).apply();
    }

    public static void setStaffEnabled(Context context, boolean enabled) {
        prefs(context).edit().putBoolean(KEY_STAFF_NOTIF, enabled).apply();
    }
}
