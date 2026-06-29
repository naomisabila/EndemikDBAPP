package com.example.endemikdbapp;

import android.content.Context;
import android.content.SharedPreferences;
import com.example.endemikdbapp.data.model.Endemik;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FavoriteManager {

    private static final String PREF_NAME = "FavoritesPref";
    private static final String KEY_FAVORITES = "favorites";

    private SharedPreferences pref;
    private Gson gson;

    public FavoriteManager(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    // Ambil semua data favorit
    public List<Endemik> getFavorites() {
        String json = pref.getString(KEY_FAVORITES, null);
        if (json == null) {
            return new ArrayList<>();
        }
        Type type = new TypeToken<List<Endemik>>(){}.getType();
        return gson.fromJson(json, type);
    }

    // Cek apakah item sudah difavoritkan
    public boolean isFavorite(String id) {
        List<Endemik> favorites = getFavorites();
        for (Endemik item : favorites) {
            if (item.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    // Tambah ke favorit
    public void addFavorite(Endemik endemik) {
        List<Endemik> favorites = getFavorites();
        favorites.add(endemik);
        saveFavorites(favorites);
    }

    // Hapus dari favorit
    public void removeFavorite(String id) {
        List<Endemik> favorites = getFavorites();
        for (int i = 0; i < favorites.size(); i++) {
            if (favorites.get(i).getId().equals(id)) {
                favorites.remove(i);
                break;
            }
        }
        saveFavorites(favorites);
    }

    // Toggle favorit (tambah kalo belum ada, hapus kalo sudah ada)
    public void toggleFavorite(Endemik endemik) {
        if (isFavorite(endemik.getId())) {
            removeFavorite(endemik.getId());
        } else {
            addFavorite(endemik);
        }
    }

    // Simpan ke SharedPreferences
    private void saveFavorites(List<Endemik> favorites) {
        String json = gson.toJson(favorites);
        pref.edit().putString(KEY_FAVORITES, json).apply();
    }
}