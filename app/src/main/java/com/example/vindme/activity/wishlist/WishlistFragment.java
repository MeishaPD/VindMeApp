package com.example.vindme.activity.wishlist;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vindme.R;
import com.example.vindme.activity.cart.AppDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WishlistFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WishlistFragment extends Fragment {

  private RecyclerView rvWishlist;
  private WishlistAdapter wishlistAdapter;
  private List<Wishlist> wishlistList;
  private AppDatabase appDatabase;
  private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
  private DatabaseReference reference = FirebaseDatabase.getInstance("https://vindme-d1523-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("wishlist");

  // TODO: Rename parameter arguments, choose names that match
  // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
  private static final String ARG_PARAM1 = "param1";
  private static final String ARG_PARAM2 = "param2";

  // TODO: Rename and change types of parameters
  private String mParam1;
  private String mParam2;

  public WishlistFragment() {
    // Required empty public constructor
  }

  /**
   * Use this factory method to create a new instance of
   * this fragment using the provided parameters.
   *
   * @param param1 Parameter 1.
   * @param param2 Parameter 2.
   * @return A new instance of fragment WishlistFragment.
   */
  // TODO: Rename and change types and number of parameters
  public static WishlistFragment newInstance(String param1, String param2) {
    WishlistFragment fragment = new WishlistFragment();
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

    View view = inflater.inflate(R.layout.fragment_wishlist, container, false);

    rvWishlist = view.findViewById(R.id.rvWishlist);
    wishlistList = new ArrayList<>();

    wishlistAdapter = new WishlistAdapter(requireContext(), wishlistList);
    rvWishlist.setAdapter(wishlistAdapter);
    rvWishlist.setLayoutManager(new GridLayoutManager(requireContext(), 1));

    appDatabase = AppDatabase.getInstance(requireContext());

    String userId = firebaseAuth.getCurrentUser().getUid();
    if (userId == null) {
      Toast.makeText(getContext(), "Pengguna tidak terautentikasi", Toast.LENGTH_SHORT).show();
      return view;
    }
    reference.child(userId).addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot snapshot) {
        wishlistList.clear();
        for (DataSnapshot data : snapshot.getChildren()) {
          Wishlist wishlist = data.getValue(Wishlist.class);
          if (wishlist != null) {
            wishlist.setWishlistId(data.getKey()); // Simpan ID wishlist sebagai key
            wishlistList.add(wishlist);

            Log.d("WishlistData", "Cover: " + wishlist.getCover() + ", Title: " + wishlist.getTitle() + ", Price: " + wishlist.getPrice());
          }
        }
        wishlistAdapter.notifyDataSetChanged();
      }

      @Override
      public void onCancelled(@NonNull DatabaseError error) {
        Log.e("FirebaseError", "Gagal memuat data: " + error.getMessage());
      }
    });

    wishlistAdapter.setOnDeleteClickListener((wishlist, position) -> {
      String wishlistId = wishlist.getWishlistId();
      if (wishlistId != null) {
        reference.child(userId).child(wishlistId).removeValue().addOnCompleteListener(task -> {
          if (task.isSuccessful()) {
            wishlistAdapter.notifyDataSetChanged(); // Perbarui seluruh RecyclerView
            if (wishlistList.isEmpty() && isAdded()) {
              Toast.makeText(getContext(), "Wishlist kosong", Toast.LENGTH_SHORT).show();
            }
          } else {
            Log.e("WishlistFragment", "Gagal menghapus item: " + wishlistId, task.getException());
            if (isAdded()) {
              Toast.makeText(getContext(), "Gagal menghapus album", Toast.LENGTH_SHORT).show();
            }
          }
        });
      }
    });

    return view;
  }
}