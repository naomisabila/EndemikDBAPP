package com.example.endemikdbapp;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.bumptech.glide.Glide;
import com.example.endemikdbapp.data.model.Endemik;

public class DetailActivity extends AppCompatActivity {

    private FavoriteManager favoriteManager;
    private Endemik currentEndemik;
    private Button btnFavorit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeManager themeManager = new ThemeManager(this);
        themeManager.applyLocale(this);
        themeManager.applyTheme();
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView btnBack = findViewById(R.id.btnBack);
        btnFavorit = findViewById(R.id.btnFavorit);

        favoriteManager = new FavoriteManager(this);

        currentEndemik = (Endemik) getIntent().getSerializableExtra("ENDEMIK");

        if (currentEndemik != null) {
            setupData();
            updateFavoriteIcon();
        }

        btnBack.setOnClickListener(v -> finish());
        btnFavorit.setOnClickListener(v -> {
            if (currentEndemik != null) {
                favoriteManager.toggleFavorite(currentEndemik);
                updateFavoriteIcon();
                boolean isFav = favoriteManager.isFavorite(currentEndemik.getId());
                Toast.makeText(this, isFav ? "Ditambahkan ke favorit" : "Dihapus dari favorit", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupData() {
        ((TextView) findViewById(R.id.tvJudul)).setText(currentEndemik.getNama());
        ((TextView) findViewById(R.id.tvLatin)).setText(currentEndemik.getNamaLatin());
        ((TextView) findViewById(R.id.tvDeskripsi)).setText(currentEndemik.getDeskripsi());
        ((TextView) findViewById(R.id.tvRegion)).setText(currentEndemik.getAsal());
        ((TextView) findViewById(R.id.tvStatus)).setText(currentEndemik.getStatus());
        ((TextView) findViewById(R.id.tvHabitat)).setText(currentEndemik.getSebaran());
        ((TextView) findViewById(R.id.tvTag)).setText(currentEndemik.getKategori().toUpperCase());
        
        Glide.with(this).load(currentEndemik.getGambar()).into((ImageView) findViewById(R.id.imgDetail));

        // Random header gradient for fun/style
        int[] colors = {ContextCompat.getColor(this, R.color.grad_yellow_start), ContextCompat.getColor(this, R.color.grad_yellow_end)};
        GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors);
        findViewById(R.id.layoutDetailHeader).setBackground(gd);
    }

    private void updateFavoriteIcon() {
        if (currentEndemik != null && favoriteManager.isFavorite(currentEndemik.getId())) {
            btnFavorit.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.btn_star_big_on, 0, 0, 0);
            btnFavorit.setText("Hapus dari Favorit");
        } else {
            btnFavorit.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.btn_star_big_off, 0, 0, 0);
            btnFavorit.setText("Tambah ke Favorit");
        }
    }
}