package com.example.endemikdbapp;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class SettingsActivity extends AppCompatActivity {

    private ThemeManager themeManager;
    private SwitchMaterial switchDarkMode;
    private Spinner spinnerRegion;
    private Button btnSimpan;
    private ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        themeManager = new ThemeManager(this);
        themeManager.applyLocale(this);
        themeManager.applyTheme();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        btnBack = findViewById(R.id.btnBack);
        switchDarkMode = findViewById(R.id.switchDarkMode);
        spinnerRegion = findViewById(R.id.spinnerRegion);
        btnSimpan = findViewById(R.id.btnSimpan);

        // Setup spinner region
        String[] regions = {"Indonesia", "Malaysia", "Thailand", "Filipina", "Singapura"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, regions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRegion.setAdapter(adapter);

        // Load current settings
        loadSettings();

        btnBack.setOnClickListener(v -> finish());

        // Simpan button
        btnSimpan.setOnClickListener(v -> {
            saveSettings();
            Toast.makeText(this, "Pengaturan disimpan!", Toast.LENGTH_SHORT).show();
            finish();
        });

        setupBottomNav();
    }

    private void setupBottomNav() {
        com.google.android.material.bottomnavigation.BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setSelectedItemId(R.id.nav_settings);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                startActivity(new android.content.Intent(this, HomeActivity.class));
                finish();
            } else if (id == R.id.nav_search) {
                startActivity(new android.content.Intent(this, SearchActivity.class));
                finish();
            } else if (id == R.id.nav_favorite) {
                startActivity(new android.content.Intent(this, FavoriteActivity.class));
                finish();
            } else if (id == R.id.nav_profile) {
                startActivity(new android.content.Intent(this, ProfileActivity.class));
                finish();
            } else if (id == R.id.nav_settings) {
                // Already here
            }
            return true;
        });
    }

    private void loadSettings() {
        // Load theme
        switchDarkMode.setChecked(themeManager.isDarkMode());

        // Load region
        String currentRegion = themeManager.getRegion();
        for (int i = 0; i < spinnerRegion.getCount(); i++) {
            if (spinnerRegion.getItemAtPosition(i).toString().equals(currentRegion)) {
                spinnerRegion.setSelection(i);
                break;
            }
        }
    }

    private void saveSettings() {
        // 1. Save & Apply Theme
        String theme = switchDarkMode.isChecked() ? ThemeManager.THEME_DARK : ThemeManager.THEME_LIGHT;
        themeManager.setTheme(theme);
        themeManager.applyTheme();

        // 2. Save Region & Map to Language
        String region = spinnerRegion.getSelectedItem().toString();
        themeManager.setRegion(region);

        String langCode = "in"; // Default Indonesia
        switch (region) {
            case "Malaysia": langCode = "ms"; break;
            case "Thailand": langCode = "th"; break;
            case "Filipina": langCode = "tl"; break;
            case "Singapura": langCode = "en"; break;
            default: langCode = "in"; break;
        }
        
        themeManager.setLocale(this, langCode);
    }
}