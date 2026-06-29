package com.example.endemikdbapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.endemikdbapp.data.adapter.EndemikAdapter;
import com.example.endemikdbapp.data.model.Endemik;
import com.example.endemikdbapp.data.retrofit.RetrofitClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText etSearch;
    private TextView tvSearchCount;
    private EndemikAdapter adapter;
    private List<Endemik> allData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeManager themeManager = new ThemeManager(this);
        themeManager.applyLocale(this);
        themeManager.applyTheme();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        recyclerView = findViewById(R.id.recyclerView);
        etSearch = findViewById(R.id.etSearch);
        tvSearchCount = findViewById(R.id.tvSearchCount);
        ImageView btnBack = findViewById(R.id.btnBack);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        setupBottomNav();
        fetchData();

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterData(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        btnBack.setOnClickListener(v -> finish());
    }

    private void setupBottomNav() {
        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setSelectedItemId(R.id.nav_search);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                startActivity(new Intent(this, HomeActivity.class));
                finish();
            } else if (id == R.id.nav_search) {
                // Already here
            } else if (id == R.id.nav_favorite) {
                startActivity(new Intent(this, FavoriteActivity.class));
                finish();
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

    private void fetchData() {
        RetrofitClient.getInstance().getEndemikList().enqueue(new Callback<List<Endemik>>() {
            @Override
            public void onResponse(@NonNull Call<List<Endemik>> call, @NonNull Response<List<Endemik>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    allData = new ArrayList<>(response.body());
                    updateDisplay(allData);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Endemik>> call, @NonNull Throwable t) {}
        });
    }

    private void updateDisplay(List<Endemik> list) {
        adapter = new EndemikAdapter(list, item -> {
            Intent intent = new Intent(SearchActivity.this, DetailActivity.class);
            intent.putExtra("ENDEMIK", item);
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);
        tvSearchCount.setText(list.size() + " spesies tersedia");
    }

    private void filterData(String query) {
        List<Endemik> filtered = new ArrayList<>();
        for (Endemik item : allData) {
            if (item.getNama().toLowerCase().contains(query.toLowerCase()) ||
                (item.getNamaLatin() != null && item.getNamaLatin().toLowerCase().contains(query.toLowerCase()))) {
                filtered.add(item);
            }
        }
        if (adapter != null) {
            adapter.updateData(filtered);
            tvSearchCount.setText(filtered.size() + " spesies tersedia");
        }
    }
}