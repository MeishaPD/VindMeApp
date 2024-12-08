package com.example.vindme.activity.profile;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vindme.R;
import com.example.vindme.activity.cart.CartActivity;
import com.example.vindme.activity.wishlist.Wishlist;
import com.example.vindme.activity.wishlist.WishlistActivity;

import java.util.List;

public class ProfileItemAdapter extends RecyclerView.Adapter<ProfileItemAdapter.ViewHolder> {

    private final List<ProfileItem> items;

    public ProfileItemAdapter(List<ProfileItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_profile_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProfileItem item = items.get(position);
        holder.icon.setImageResource(item.getIconResId());
        holder.title.setText(item.getTitle());
        holder.cardView.setOnClickListener(v -> {
            if (item.getTitle().equalsIgnoreCase("Cek Keranjang")) {
                Intent intent = new Intent(holder.itemView.getContext(), CartActivity.class);
                holder.itemView.getContext().startActivity(intent);
            } else if (item.getTitle().equalsIgnoreCase("Wishlist")) {
                Intent intent = new Intent(holder.itemView.getContext(), WishlistActivity.class);
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView icon;
        TextView title;

        ViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_view);
            icon = itemView.findViewById(R.id.icon);
            title = itemView.findViewById(R.id.title);
        }
    }
}


