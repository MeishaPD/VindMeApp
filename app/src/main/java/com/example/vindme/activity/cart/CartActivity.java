package com.example.vindme.activity.cart;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.vindme.R;
import com.example.vindme.activity.home.HomeActivity;
import com.example.vindme.activity.profile.ProfileActivity;
import com.example.vindme.activity.search.SearchActivity;
import com.example.vindme.activity.wishlist.WishlistActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class CartActivity extends AppCompatActivity {

  TextView tvActivity;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    EdgeToEdge.enable(this);
    setContentView(R.layout.activity_cart);

    tvActivity = findViewById(R.id.tvActivity);
    tvActivity.setText("Cart");

    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    transaction.replace(R.id.fragment_container, new CartFragment());
    transaction.commit();

    BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
    bottomNav.setSelectedItemId(R.id.cart);
    bottomNav.setOnItemSelectedListener(item -> {
      if (item.getItemId() == R.id.home){
        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        finish();
      }
      else if(item.getItemId() == R.id.search){
        startActivity(new Intent(getApplicationContext(), SearchActivity.class));
        finish();
      }
      else if (item.getItemId() == R.id.cart) {
        startActivity(new Intent(getApplicationContext(), CartActivity.class));
        finish();
      }
      else if (item.getItemId() == R.id.wishlist) {
        startActivity(new Intent(getApplicationContext(), WishlistActivity.class));
        finish();
      }
      else if (item.getItemId() == R.id.profile) {
        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
        finish();
      }
      return true;
    });
  }
}

class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

  private final Context context;
  private final List<Cart> cartList;
  private OnDeleteClickListener deleteClickListener;

  public interface OnDeleteClickListener {
    void onDeleteClick(Cart cart, int position);
  }

  public void setOnDeleteClickListener(OnDeleteClickListener listener) {
    this.deleteClickListener = listener;
  }

  public CartAdapter(Context context, List<Cart> cartList) {
    this.context = context;
    this.cartList = cartList;
  }

  @NonNull
  @Override
  public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(context).inflate(R.layout.row_cart, parent, false);
    return new CartViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
    if (position < 0 || position >= cartList.size()) {
      Log.e("CartAdapter", "Invalid position detected: " + position);
      return;
    }

    Cart cart = cartList.get(position);

    Glide.with(context).load(cart.getCover()).into(holder.ivCover);
    holder.tvTitle.setText(cart.getTitle());
    holder.tvArtist.setText(cart.getArtist());
    holder.tvPrice.setText(cart.getPrice());

    holder.btDelete.setOnClickListener(v -> {
      if (deleteClickListener != null) {
        deleteClickListener.onDeleteClick(cart, holder.getAdapterPosition());
      }
    });
  }

  @Override
  public int getItemCount() {
    return cartList.size();
  }

  public static class CartViewHolder extends RecyclerView.ViewHolder {
    ImageView ivCover;
    TextView tvTitle, tvArtist, tvPrice;
    Button btDelete;

    public CartViewHolder(@NonNull View itemView) {
      super(itemView);
      ivCover = itemView.findViewById(R.id.ivCover);
      tvTitle = itemView.findViewById(R.id.tvTitle);
      tvArtist = itemView.findViewById(R.id.tvArtist);
      tvPrice = itemView.findViewById(R.id.tvPrice);
      btDelete = itemView.findViewById(R.id.btDelete);
    }
  }
}