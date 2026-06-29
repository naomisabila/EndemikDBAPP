package com.example.endemikdbapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.endemikdbapp.data.adapter.EndemikAdapter;
import com.example.endemikdbapp.data.model.Endemik;
import com.example.endemikdbapp.data.retrofit.RetrofitClient;
import com.example.endemikdbapp.DetailActivity;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TumbuhanFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private EndemikAdapter adapter;
    private List<Endemik> allTumbuhanData = new ArrayList<>();
    private String currentRegion = "Semua";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        progressBar = view.findViewById(R.id.progressBar);

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        fetchData();

        return view;
    }

    private void fetchData() {
        progressBar.setVisibility(View.VISIBLE);

        RetrofitClient.getInstance().getEndemikList().enqueue(new Callback<List<Endemik>>() {
            @Override
            public void onResponse(@NonNull Call<List<Endemik>> call, @NonNull Response<List<Endemik>> response) {
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    allTumbuhanData = new ArrayList<>();
                    for (Endemik item : response.body()) {
                        if ("Tumbuhan".equals(item.getKategori())) {
                            allTumbuhanData.add(item);
                        }
                    }
                    applyFilter();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Endemik>> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    public void filterByRegion(String region) {
        this.currentRegion = region;
        applyFilter();
    }

    private void applyFilter() {
        List<Endemik> filteredList = new ArrayList<>();
        if ("Semua".equals(currentRegion)) {
            filteredList.addAll(allTumbuhanData);
        } else {
            for (Endemik item : allTumbuhanData) {
                if (item.getAsal() != null && item.getAsal().contains(currentRegion)) {
                    filteredList.add(item);
                }
            }
        }

        adapter = new EndemikAdapter(filteredList, item -> {
            Intent intent = new Intent(getContext(), DetailActivity.class);
            intent.putExtra("ENDEMIK", item);
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);

        if (getActivity() instanceof HomeActivity) {
            ((HomeActivity) getActivity()).updateStats(filteredList.size());
        }
    }
}