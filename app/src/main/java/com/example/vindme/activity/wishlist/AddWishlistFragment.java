package com.example.vindme.activity.wishlist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.vindme.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddWishlistFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddWishlistFragment extends Fragment {

  String artis = "Taylor Swift";
  String deskripsi = "The Tortured Poets Department Vinyl + Bonus Track “The Manuscript”";
  String harga = "699.000";

  TextView tvArtist, tvDescription, tvPrice;
  Button btWishlist, btBuy;

  // TODO: Rename parameter arguments, choose names that match
  // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
  private static final String ARG_PARAM1 = "param1";
  private static final String ARG_PARAM2 = "param2";

  // TODO: Rename and change types of parameters
  private String mParam1;
  private String mParam2;

  public AddWishlistFragment() {
    // Required empty public constructor
  }

  /**
   * Use this factory method to create a new instance of
   * this fragment using the provided parameters.
   *
   * @param param1 Parameter 1.
   * @param param2 Parameter 2.
   * @return A new instance of fragment AddWishlistFragment.
   */
  // TODO: Rename and change types and number of parameters
  public static AddWishlistFragment newInstance(String param1, String param2) {
    AddWishlistFragment fragment = new AddWishlistFragment();
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

    View view = inflater.inflate(R.layout.fragment_add_wishlist, container, false);

    tvArtist = view.findViewById(R.id.tvArtist);
    tvDescription = view.findViewById(R.id.tvDescription);
    tvPrice = view.findViewById(R.id.tvPrice);
    btWishlist = view.findViewById(R.id.btWishlist);
    btBuy = view.findViewById(R.id.btBuy);

    tvArtist.setText(artis);
    tvDescription.setText(deskripsi);
    tvPrice.setText("Rp. " + harga);

    btWishlist.setOnClickListener(v -> {
      getParentFragmentManager().beginTransaction()
          .replace(R.id.fragment_container, new WishlistFragment())
          .addToBackStack(null)
          .commit();
    });

    String pesan = getArguments() != null ? getArguments().getString("pesan") : null;
    if (pesan == null) {
      btBuy.setText("Buy Now");
    } else {
      btBuy.setText(pesan);
    }

    return view;
  }
}