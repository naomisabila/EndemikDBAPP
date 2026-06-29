package com.example.endemikdbapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.endemikdbapp.data.adapter.EndemikAdapter;
import com.example.endemikdbapp.data.model.Endemik;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.List;

public class FavoriteActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LinearLayout layoutEmpty;
    private EndemikAdapter adapter;
    private FavoriteManager favoriteManager;
    private ThemeManager themeManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        themeManager = new ThemeManager(this);
        themeManager.applyLocale(this);
        themeManager.applyTheme();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        recyclerView = findViewById(R.id.recyclerView);
        layoutEmpty = findViewById(R.id.layoutEmpty);
        ImageView btnBack = findViewById(R.id.btnBack);
        Button btnExplore = findViewById(R.id.btnExplore);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        favoriteManager = new FavoriteManager(this);

        setupBottomNav();

        btnBack.setOnClickListener(v -> finish());
        btnExplore.setOnClickListener(v -> {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        });
    }

    private void setupBottomNav() {
        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setSelectedItemId(R.id.nav_favorite);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                startActivity(new Intent(this, HomeActivity.class));
                finish();
            } else if (id == R.id.nav_search) {
                startActivity(new Intent(this, SearchActivity.class));
                finish();
            } else if (id == R.id.nav_favorite) {
                // Already here
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
                finish();
            } else if (id == R.id.nav_settings) {
                startActivity(new Intent(this, SettingsActivity.class));
                finish();
            }
            return true;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadFavorites();
    }

    private void loadFavorites() {
        List<Endemik> favorites = favoriteManager.getFavorites();

        if (favorites.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            layoutEmpty.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            layoutEmpty.setVisibility(View.GONE);

            adapter = new EndemikAdapter(favorites, item -> {
                Intent intent = new Intent(this, DetailActivity.class);
                intent.putExtra("ENDEMIK", item);
                startActivity(intent);
            });
            recyclerView.setAdapter(adapter);
        }
    }
}