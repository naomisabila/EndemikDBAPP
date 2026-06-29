package com.example.endemikdbapp.data.adapter;

import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.endemikdbapp.FavoriteManager;
import com.example.endemikdbapp.R;
import com.example.endemikdbapp.data.model.Endemik;
import java.util.ArrayList;
import java.util.List;

public class EndemikAdapter extends RecyclerView.Adapter<EndemikAdapter.ViewHolder> {

    private List<Endemik> list;
    private OnItemClickListener onClick;

    public interface OnItemClickListener {
        void onItemClick(Endemik item);
    }

    public EndemikAdapter(List<Endemik> list, OnItemClickListener onClick) {
        this.list = list;
        this.onClick = onClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_endemik, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Endemik item = list.get(position);

        holder.tvNama.setText(item.getNama());
        holder.tvNamaLatin.setText(item.getNama()); // For now using same name as latin placeholder
        holder.tvLocationTag.setText(item.getKategori()); // Using kategori as location tag placeholder

        // Apply Gradient based on position or category
        int[] colors;
        if (position % 3 == 0) {
            colors = new int[]{ContextCompat.getColor(holder.itemView.getContext(), R.color.grad_red_start), 
                              ContextCompat.getColor(holder.itemView.getContext(), R.color.grad_red_end)};
        } else if (position % 3 == 1) {
            colors = new int[]{ContextCompat.getColor(holder.itemView.getContext(), R.color.grad_green_start), 
                              ContextCompat.getColor(holder.itemView.getContext(), R.color.grad_green_end)};
        } else {
            colors = new int[]{ContextCompat.getColor(holder.itemView.getContext(), R.color.grad_yellow_start), 
                              ContextCompat.getColor(holder.itemView.getContext(), R.color.grad_yellow_end)};
        }
        
        GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors);
        gd.setCornerRadius(0f);
        holder.layoutImageBg.setBackground(gd);

        Glide.with(holder.itemView.getContext())
                .load(item.getGambar())
                .into(holder.img);

        FavoriteManager fm = new FavoriteManager(holder.itemView.getContext());
        if (fm.isFavorite(item.getId())) {
            holder.btnFavItem.setImageResource(android.R.drawable.btn_star_big_on);
        } else {
            holder.btnFavItem.setImageResource(android.R.drawable.btn_star_big_off);
        }

        holder.btnFavItem.setOnClickListener(v -> {
            fm.toggleFavorite(item);
            notifyItemChanged(position);
        });

        holder.itemView.setOnClickListener(v -> {
            if (onClick != null) onClick.onItemClick(item);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void updateData(List<Endemik> newList) {
        this.list = new ArrayList<>(newList);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img, btnFavItem;
        TextView tvNama, tvNamaLatin, tvLocationTag;
        RelativeLayout layoutImageBg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imgEndemik);
            btnFavItem = itemView.findViewById(R.id.btnFavItem);
            tvNama = itemView.findViewById(R.id.tvNama);
            tvNamaLatin = itemView.findViewById(R.id.tvNamaLatin);
            tvLocationTag = itemView.findViewById(R.id.tvLocationTag);
            layoutImageBg = itemView.findViewById(R.id.layoutImageBg);
        }
    }
}