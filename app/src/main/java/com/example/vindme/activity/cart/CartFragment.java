package com.example.vindme.activity.cart;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vindme.R;
import com.example.vindme.activity.home.Album;
import com.example.vindme.activity.wishlist.Wishlist;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CartFragment extends Fragment {

  private RecyclerView recyclerView;
  private CartAdapter cartAdapter;
  private TextView tvTotalPrice;
  private List<Cart> cartList;
  private FirebaseAuth firebaseAuth;
  private DatabaseReference databaseReference;
  private int totalPrice;
  private String price;

  // TODO: Rename parameter arguments, choose names that match
  // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
  private static final String ARG_PARAM1 = "param1";
  private static final String ARG_PARAM2 = "param2";

  // TODO: Rename and change types of parameters
  private String mParam1;
  private String mParam2;

  public CartFragment() {
    // Required empty public constructor
  }

  /**
   * Use this factory method to create a new instance of
   * this fragment using the provided parameters.
   *
   * @param param1 Parameter 1.
   * @param param2 Parameter 2.
   * @return A new instance of fragment CartFragment.
   */
  // TODO: Rename and change types and number of parameters
  public static CartFragment newInstance(String param1, String param2) {
    CartFragment fragment = new CartFragment();
    Bundle args = new Bundle();
    args.putString(ARG_PARAM1, param1);
    args.putString(ARG_PARAM2, param2);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      mParam1 = getArguments().getString(ARG_PARAM1);
      mParam2 = getArguments().getString(ARG_PARAM2);
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_cart, container, false);

    tvTotalPrice = view.findViewById(R.id.tvTotalPrice);
    recyclerView = view.findViewById(R.id.rvCart);
    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

    firebaseAuth = FirebaseAuth.getInstance();
    databaseReference = FirebaseDatabase.getInstance("https://papbd-7cfaf-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("carts");

    cartList = new ArrayList<>();
    cartAdapter = new CartAdapter(getContext(), cartList);
    recyclerView.setAdapter(cartAdapter);

    String userId = firebaseAuth.getCurrentUser().getUid();
    if (userId == null) {
      Toast.makeText(getContext(), "Pengguna tidak terautentikasi", Toast.LENGTH_SHORT).show();
      return view;
    }
    Bundle args = getArguments();
    if (args != null) {

      Wishlist selectedWishlist = (Wishlist) args.getSerializable("selectedWishlist");
      if (selectedWishlist != null) {
        // Masukkan item ke daftar cartList lokal
        cartList = new ArrayList<>();
        Cart cartItem = new Cart();
        cartItem.setTitle(selectedWishlist.getTitle());
        cartItem.setPrice(selectedWishlist.getPrice());
        cartItem.setCover(selectedWishlist.getCover());
        cartList.add(cartItem);

        // Set data ke adapter
        cartAdapter = new CartAdapter(getContext(), cartList);
        recyclerView.setAdapter(cartAdapter);

        totalPrice = 0;
        price = cartItem.getPrice().replace("Rp", "").trim().replace(".", "");
        totalPrice += Integer.parseInt(price);

        // Hitung total harga
        calculateTotalPrice();
      }

    } else {

      databaseReference.child(userId).addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
          Log.d("CartFragment", "Data diterima dari Firebase");
          totalPrice = 0;
          cartList.clear();
          for (DataSnapshot cartSnapshot : dataSnapshot.getChildren()) {
            Cart cart = cartSnapshot.getValue(Cart.class);
            if (cart != null) {
              cart.setCartId(cartSnapshot.getKey());
              price = cart.getPrice().replace("Rp", "").trim().replace(".", "");
              cartList.add(cart);
              totalPrice += Integer.parseInt(price);
            }
          }
          cartAdapter.notifyDataSetChanged();
          calculateTotalPrice();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
          Log.w("CartFragment", "loadCartItems:onCancelled", databaseError.toException());
        }
      });

      cartAdapter.setOnDeleteClickListener((cart, position) -> {
        String cartId = cart.getCartId();
        if (cartId != null) {
          databaseReference.child(userId).child(cartId).removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
              cartAdapter.notifyDataSetChanged(); // Perbarui seluruh RecyclerView
              if (cartList.isEmpty() && isAdded()) {
                Toast.makeText(getContext(), "Keranjang kosong", Toast.LENGTH_SHORT).show();
              }
            } else {
              Log.e("CartFragment", "Gagal menghapus item: " + cartId, task.getException());
              if (isAdded()) {
                Toast.makeText(getContext(), "Gagal menghapus album", Toast.LENGTH_SHORT).show();
              }
            }
          });
        }
      });

    }
    return view;
  }

  private void calculateTotalPrice() {
    // Format harga
    DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getNumberInstance(new Locale("id", "ID"));
    decimalFormat.applyPattern("#,###");
    String formattedPrice = decimalFormat.format(totalPrice);

    tvTotalPrice.setText("Rp " + formattedPrice);
  }
}