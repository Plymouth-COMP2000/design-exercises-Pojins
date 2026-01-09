package com.example.savoryrestaurant;

import android.content.Context;
import android.content.SharedPreferences;

public class AccountNotifPrefs {

    private static final String PREFS = "NotifPrefs";

    private static String currentUserKey(Context context) {
        SharedPreferences userPrefs = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);

        // Prefer email if present (guests), else username
        String email = userPrefs.getString("email", "");
        if (email != null && !email.trim().isEmpty()) return email.trim().toLowerCase();

        String username = userPrefs.getString("username", "");
        return (username == null ? "" : username.trim().toLowerCase());
    }

    private static SharedPreferences prefs(Context context) {
        return context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    // --- Guest toggle (per user) ---
    public static boolean isGuestEnabled(Context context) {
        String userKey = currentUserKey(context);
        return prefs(context).getBoolean("guest_notifications_" + userKey, true);
    }

    public static void setGuestEnabled(Context context, boolean enabled) {
        String userKey = currentUserKey(context);
        prefs(context).edit().putBoolean("guest_notifications_" + userKey, enabled).apply();
    }

    // --- Staff toggle (per user) ---
    public static boolean isStaffEnabled(Context context) {
        String userKey = currentUserKey(context);
        return prefs(context).getBoolean("staff_notifications_" + userKey, true);
    }

    public static void setStaffEnabled(Context context, boolean enabled) {
        String userKey = currentUserKey(context);
        prefs(context).edit().putBoolean("staff_notifications_" + userKey, enabled).apply();
    }
}
