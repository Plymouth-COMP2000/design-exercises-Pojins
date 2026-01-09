package com.example.savoryrestaurant.session;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private static final String PREFS = "UserPrefs";
    private static final String KEY_LOGGED_IN = "loggedIn";
    private static final String KEY_ROLE = "role"; // "guest" or "staff"
    private static final String KEY_USERNAME = "username";

    public static void login(Context ctx, String username, String role) {
        SharedPreferences prefs = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        prefs.edit()
                .putBoolean(KEY_LOGGED_IN, true)
                .putString(KEY_USERNAME, username)
                .putString(KEY_ROLE, role)
                .apply();
    }

    public static boolean isLoggedIn(Context ctx) {
        SharedPreferences prefs = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        return prefs.getBoolean(KEY_LOGGED_IN, false);
    }

    public static String getRole(Context ctx) {
        SharedPreferences prefs = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        return prefs.getString(KEY_ROLE, "");
    }

    public static String getUsername(Context ctx) {
        SharedPreferences prefs = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        return prefs.getString(KEY_USERNAME, "");
    }

    public static void logout(Context ctx) {
        SharedPreferences prefs = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        prefs.edit().clear().apply();
    }
}
