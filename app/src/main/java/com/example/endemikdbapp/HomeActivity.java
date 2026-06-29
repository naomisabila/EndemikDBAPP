package com.example.endemikdbapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.example.endemikdbapp.HewanFragment;
import com.example.endemikdbapp.TumbuhanFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    private ThemeManager themeManager;
    private TextView tabHewan, tabTumbuhan;
    private TextView tvTotalSpecies, tvCategoryLabel, tvKoleksiTitle, tvItemCount;
    private ImageView btnDarkMode;
    private LinearLayout layoutRegionChips;
    private String currentRegion = "Semua";
    private boolean isHewanActive = true;

    private final String[] regions = {"Semua", "Nusa Tenggara Timur", "Sulawesi", "Papua", "Kalimantan", "Sumatera"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        themeManager = new ThemeManager(this);
        themeManager.applyLocale(this);
        themeManager.applyTheme();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initViews();
        setupBottomNav();
        setupTabs();
        setupDarkModeToggle();

        // Default
        selectTab(true);
    }

    private void initViews() {
        tabHewan = findViewById(R.id.tabHewan);
        tabTumbuhan = findViewById(R.id.tabTumbuhan);
        tvTotalSpecies = findViewById(R.id.tvTotalSpecies);
        tvCategoryLabel = findViewById(R.id.tvCategoryLabel);
        tvKoleksiTitle = findViewById(R.id.tvKoleksiTitle);
        tvItemCount = findViewById(R.id.tvItemCount);
        btnDarkMode = findViewById(R.id.btnDarkMode);
        layoutRegionChips = findViewById(R.id.layoutRegionChips);

        findViewById(R.id.btnSearch).setOnClickListener(v -> startActivity(new Intent(this, SearchActivity.class)));
        findViewById(R.id.btnFavorite).setOnClickListener(v -> startActivity(new Intent(this, FavoriteActivity.class)));

        setupRegionChips();
    }

    private void setupRegionChips() {
        layoutRegionChips.removeAllViews();
        for (String region : regions) {
            View chipView = LayoutInflater.from(this).inflate(R.layout.item_region_chip, layoutRegionChips, false);
            TextView tvChip = chipView.findViewById(R.id.tvChipName);
            tvChip.setText(region);

            updateChipStyle(tvChip, region.equals(currentRegion));

            chipView.setOnClickListener(v -> {
                currentRegion = region;
                setupRegionChips(); // Refresh styles
                filterCurrentFragment();
            });

            layoutRegionChips.addView(chipView);
        }
    }

    private void updateChipStyle(TextView tvChip, boolean isSelected) {
        if (isSelected) {
            tvChip.setBackgroundResource(R.drawable.bg_chip_selected);
            tvChip.setTextColor(ContextCompat.getColor(this, R.color.white));
        } else {
            tvChip.setBackgroundResource(R.drawable.bg_chip_unselected);
            tvChip.setTextColor(ContextCompat.getColor(this, R.color.gray_text));
        }
    }

    private void filterCurrentFragment() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
        if (fragment instanceof HewanFragment) {
            ((HewanFragment) fragment).filterByRegion(currentRegion);
        } else if (fragment instanceof TumbuhanFragment) {
            ((TumbuhanFragment) fragment).filterByRegion(currentRegion);
        }
    }

    private void setupBottomNav() {
        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setSelectedItemId(R.id.nav_home);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                // Already here
            } else if (id == R.id.nav_search) {
                startActivity(new Intent(this, SearchActivity.class));
            } else if (id == R.id.nav_favorite) {
                startActivity(new Intent(this, FavoriteActivity.class));
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
            } else if (id == R.id.nav_settings) {
                startActivity(new Intent(this, SettingsActivity.class));
            }
            return true;
        });
    }

    private void setupTabs() {
        tabHewan.setOnClickListener(v -> selectTab(true));
        tabTumbuhan.setOnClickListener(v -> selectTab(false));
    }

    private void selectTab(boolean isHewan) {
        if (isHewan) {
            tabHewan.setBackgroundResource(R.drawable.bg_tab_item_selected);
            tabHewan.setTextColor(ContextCompat.getColor(this, R.color.accent));
            tabTumbuhan.setBackground(null);
            tabTumbuhan.setTextColor(ContextCompat.getColor(this, R.color.gray_text));
            
            tvKoleksiTitle.setText("Koleksi Hewan");
            tvCategoryLabel.setText("Kategori Fauna endemik Nusantara");
            replaceFragment(new HewanFragment());
        } else {
            tabTumbuhan.setBackgroundResource(R.drawable.bg_tab_item_selected);
            tabTumbuhan.setTextColor(ContextCompat.getColor(this, R.color.accent));
            tabHewan.setBackground(null);
            tabHewan.setTextColor(ContextCompat.getColor(this, R.color.gray_text));

            tvKoleksiTitle.setText("Koleksi Tumbuhan");
            tvCategoryLabel.setText("Kategori Flora endemik Nusantara");
            replaceFragment(new TumbuhanFragment());
        }
    }

    private void setupDarkModeToggle() {
        updateDarkModeIcon();
        btnDarkMode.setOnClickListener(v -> {
            if (themeManager.isDarkMode()) {
                themeManager.setTheme(ThemeManager.THEME_LIGHT);
            } else {
                themeManager.setTheme(ThemeManager.THEME_DARK);
            }
            themeManager.applyTheme();
            updateDarkModeIcon();
            // Activity will be recreated, but we update icon just in case
        });
    }

    private void updateDarkModeIcon() {
        if (themeManager.isDarkMode()) {
            btnDarkMode.setImageResource(android.R.drawable.ic_menu_day);
        } else {
            btnDarkMode.setImageResource(android.R.drawable.ic_menu_today); // Should be a moon icon if available
        }
    }

    public void updateStats(int count) {
        tvTotalSpecies.setText(count + " spesies");
        tvItemCount.setText(count + " item");
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }
}