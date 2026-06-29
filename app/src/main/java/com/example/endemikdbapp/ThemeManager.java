package com.example.endemikdbapp;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatDelegate;

public class ThemeManager {

    private static final String PREF_NAME = "ThemePref";
    private static final String KEY_THEME = "theme";
    private static final String KEY_REGION = "region";
    private static final String KEY_LANG = "lang";

    // Theme constants
    public static final String THEME_LIGHT = "light";
    public static final String THEME_DARK = "dark";
    public static final String THEME_AUTO = "auto";

    // Region constants
    public static final String REGION_DEFAULT = "Indonesia";

    private SharedPreferences pref;

    public ThemeManager(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    // === THEME ===
    public void setTheme(String theme) {
        pref.edit().putString(KEY_THEME, theme).apply();
    }

    public String getTheme() {
        return pref.getString(KEY_THEME, THEME_LIGHT);
    }

    public boolean isDarkMode() {
        return THEME_DARK.equals(getTheme());
    }

    public void applyTheme() {
        String theme = getTheme();
        if (THEME_DARK.equals(theme)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else if (THEME_LIGHT.equals(theme)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        }
    }

    // === REGION ===
    public void setRegion(String region) {
        pref.edit().putString(KEY_REGION, region).apply();
    }

    public String getRegion() {
        return pref.getString(KEY_REGION, REGION_DEFAULT);
    }

    // === LOCALE / LANGUAGE ===
    public void setLocale(Context context, String langCode) {
        pref.edit().putString(KEY_LANG, langCode).apply();
        updateResources(context, langCode);
    }

    public String getLang() {
        return pref.getString(KEY_LANG, "in"); // Default Indonesia ("in" or "id")
    }

    public void applyLocale(Context context) {
        updateResources(context, getLang());
    }

    private void updateResources(Context context, String langCode) {
        java.util.Locale locale = new java.util.Locale(langCode);
        java.util.Locale.setDefault(locale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.setLocale(locale);
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
        
        // For newer versions, we might need to update the application context too
        context.getApplicationContext().getResources().updateConfiguration(config, context.getApplicationContext().getResources().getDisplayMetrics());
    }
}