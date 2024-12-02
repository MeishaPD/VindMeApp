package com.example.vindme.activity.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.vindme.R;
import com.example.vindme.activity.cart.AppDatabase;
import com.example.vindme.activity.cart.Cart;
import com.example.vindme.activity.wishlist.Wishlist;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailPembelianFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailPembelianFragment extends Fragment {

  private TextView tvArtist, tvTitle, tvPrice, tvDetail;
  private ImageView ivCover;
  private Button btCart, btWishlist;
  private int albumId;
  private String cover, title, artist, detail, price, pesan;
  private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
  private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://vindme-d1523-default-rtdb.asia-southeast1.firebasedatabase.app/");
  private DatabaseReference databaseReference;

  // TODO: Rename parameter arguments, choose names that match
  // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
  private static final String ARG_PARAM1 = "param1";
  private static final String ARG_PARAM2 = "param2";

  // TODO: Rename and change types of parameters
  private String mParam1;
  private String mParam2;

  public DetailPembelianFragment() {
    // Required empty public constructor
  }

  /**
   * Use this factory method to create a new instance of
   * this fragment using the provided parameters.
   *
   * @param param1 Parameter 1.
   * @param param2 Parameter 2.
   * @return A new instance of fragment DetailPembelianFragment.
   */
  // TODO: Rename and change types and number of parameters
  public static DetailPembelianFragment newInstance(String param1, String param2) {
    DetailPembelianFragment fragment = new DetailPembelianFragment();
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

    View view = inflater.inflate(R.layout.fragment_detail_pembelian, container, false);

    tvArtist = view.findViewById(R.id.tvArtist);
    tvTitle = view.findViewById(R.id.tvTitle);
    tvPrice = view.findViewById(R.id.tvPrice);
    ivCover = view.findViewById(R.id.ivCover);
    tvDetail = view.findViewById(R.id.tvDetail);
    btCart = view.findViewById(R.id.btCart);
    btWishlist = view.findViewById(R.id.btWishlist);

    if (getArguments() != null) {
      albumId = getArguments().getInt("albumId");
      cover = getArguments().getString("cover");
      title = getArguments().getString("title");
      artist = getArguments().getString("artist");
      detail = getArguments().getString("detail");
      price = getArguments().getString("price");
      pesan = getArguments().getString("pesan");

      Glide.with(requireContext()).load(cover).into(ivCover);
      tvArtist.setText(artist);
      tvTitle.setText(title);
      tvPrice.setText(price);
      tvDetail.setText(detail);

      Toast.makeText(getContext(), pesan, Toast.LENGTH_SHORT).show();
    }

    btWishlist.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        databaseReference = firebaseDatabase.getReference("wishlist");
        String userId = firebaseAuth.getCurrentUser().getUid();
        Log.d("userID", userId);
        String wishlistId = databaseReference.push().getKey();

        Wishlist wishlist = new Wishlist(wishlistId, albumId, cover, title, artist, price);

        if (wishlistId != null) {
          databaseReference.child(userId).child(wishlistId).setValue(wishlist)
                  .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Album ditambahkan ke wishlist", Toast.LENGTH_SHORT).show();

                    Log.d("WishlistData", "Cover: " + wishlist.getCover() + ", Title: " + wishlist.getTitle() + ", Price: " + wishlist.getPrice());

                  })
                  .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Album gagal ditambahkan ke wishlist", Toast.LENGTH_SHORT).show();
                  });
        }
      }
    });

    btCart.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        databaseReference = firebaseDatabase.getReference("carts");

        String userId = firebaseAuth.getCurrentUser().getUid();
        Log.d("userID", userId);
        String cartId = databaseReference.push().getKey();

        Cart cart = new Cart(cartId, albumId, cover, title, artist, price);

        if (cartId != null) {
          databaseReference.child(userId).child(cartId).setValue(cart)
              .addOnSuccessListener(aVoid -> {
                Toast.makeText(getContext(), "Album ditambahkan ke keranjang", Toast.LENGTH_SHORT).show();
              })
              .addOnFailureListener(e -> {
                Toast.makeText(getContext(), "Gagal menambahkan ke keranjang", Toast.LENGTH_SHORT).show();
              });
        }

      }
    });

    return view;
  }
}