package com.example.vindme.activity.wishlist;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.vindme.R;
import com.example.vindme.activity.cart.CartFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.WishlistViewHolder> {

  private final Context context;
  private final List<Wishlist> wishlistList;
  private WishlistAdapter.OnDeleteClickListener deleteClickListener;

  public interface OnDeleteClickListener {
    void onDeleteClick(Wishlist wishlist, int position);
  }

  public void setOnDeleteClickListener(WishlistAdapter.OnDeleteClickListener listener) {
    this.deleteClickListener = listener;
  }

  public WishlistAdapter(Context context, List<Wishlist> wishlistList) {
    this.context = context;
    this.wishlistList = wishlistList;
  }

  public static class WishlistViewHolder extends RecyclerView.ViewHolder {
    ImageView ivCover;
    TextView tvArtist, tvDescription, tvPrice;
    Button btDelete, btBuy;

    public WishlistViewHolder(@NonNull View itemView) {
      super(itemView);
      ivCover = itemView.findViewById(R.id.ivCover);
      tvArtist = itemView.findViewById(R.id.tvArtist);
      tvDescription = itemView.findViewById(R.id.tvDescription);
      tvPrice = itemView.findViewById(R.id.tvPrice);
      btDelete = itemView.findViewById(R.id.btDelete);
      btBuy = itemView.findViewById(R.id.btBuy);
    }
  }

  @NonNull
  @Override
  public WishlistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(context).inflate(R.layout.row_view_wishlist, parent, false);
    return new WishlistViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull WishlistViewHolder holder, int position) {
    if (position < 0 || position >= wishlistList.size()) {
      Log.e("CartAdapter", "Invalid position detected: " + position);
      return;
    }

    Wishlist wishlist = wishlistList.get(position);

    Glide.with(context).load(wishlist.getCover()).into(holder.ivCover);
    holder.tvDescription.setText(wishlist.getTitle());
    holder.tvArtist.setText(wishlist.getArtist());
    holder.tvPrice.setText(wishlist.getPrice());

    holder.btDelete.setOnClickListener(v -> {
      if (deleteClickListener != null) {
        deleteClickListener.onDeleteClick(wishlist, holder.getAdapterPosition());
      }
    });

    holder.btBuy.setOnClickListener(v -> {
      if (context instanceof FragmentActivity) {
        FragmentActivity activity = (FragmentActivity) context;

        // Siapkan data wishlist untuk diteruskan
        Bundle bundle = new Bundle();
        bundle.putSerializable("selectedWishlist", wishlist);

        // Pindahkan ke CartFragment
        CartFragment cartFragment = new CartFragment();
        cartFragment.setArguments(bundle);

        // Ganti fragment
        activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, cartFragment) // Ganti dengan ID container fragment Anda
                .addToBackStack(null)
                .commit();
      }
    });
  }

  @Override
  public int getItemCount() {
    return wishlistList.size();
  }
}
