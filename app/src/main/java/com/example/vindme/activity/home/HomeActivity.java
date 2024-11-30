package com.example.vindme.activity.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.vindme.activity.cart.CartActivity;
import com.example.vindme.activity.profile.ProfileActivity;
import com.example.vindme.R;
import com.example.vindme.activity.search.SearchActivity;
import com.example.vindme.activity.wishlist.WishlistActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeActivity extends AppCompatActivity {

//  RecyclerView recyclerView;
//  HomeAdapter homeAdapter;
//  List<Album> albumList;
  TextView tvActivity;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    EdgeToEdge.enable(this);
    setContentView(R.layout.activity_home);

//    recyclerView = findViewById(R.id.rvHome);
//    albumList = new ArrayList<>();
//    homeAdapter = new HomeAdapter(this, albumList);
//    recyclerView.setAdapter(homeAdapter);
//    recyclerView.setLayoutManager(new GridLayoutManager(this,2));
//
//    fetchAlbums();

    tvActivity = findViewById(R.id.tvActivity);
    tvActivity.setText("Home");

    loadFragment(new HomeFragment());

    BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
    bottomNav.setSelectedItemId(R.id.home);
    bottomNav.setOnItemSelectedListener(item -> {
      if (item.getItemId() == R.id.home){
        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        finish();
      } else if(item.getItemId() == R.id.search){
        startActivity(new Intent(getApplicationContext(), SearchActivity.class));
        finish();
      } else if (item.getItemId() == R.id.cart) {
        startActivity(new Intent(getApplicationContext(), CartActivity.class));
        finish();
      } else if (item.getItemId() == R.id.wishlist) {
        startActivity(new Intent(getApplicationContext(), WishlistActivity.class));
        finish();
      } else if (item.getItemId() == R.id.profile) {
        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
        finish();
      }
      return false;
    });
  }

  private void loadFragment(Fragment fragment) {
    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    transaction.replace(R.id.fragment_container, fragment);
    transaction.addToBackStack(null);
    transaction.commit();
  }

//  private void fetchAlbums() {
//    Thread thread = new Thread(new Runnable() {
//      @Override
//      public void run() {
//        Retrofit retrofit = new Retrofit.Builder()
//            .baseUrl("http://10.0.2.2/ApiVindMe/")
//            .build();
//
//        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
//        Call<ResponseBody> call = apiInterface.getAlbum();
//
//        call.enqueue(new Callback<ResponseBody>() {
//          @Override
//          public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//            if (response.isSuccessful() && response.body() != null) {
//              try {
//                String json = response.body().string();
//
//                Gson gson = new Gson();
//                Type albumListType = new TypeToken<List<Album>>(){}.getType();
//                List<Album> albums = gson.fromJson(json, albumListType);
//
//                runOnUiThread(new Runnable() {
//                  @Override
//                  public void run() {
//                    albumList.clear();
//                    albumList.addAll(albums);
//                    homeAdapter.notifyDataSetChanged();
//                  }
//                });
//
//              } catch (IOException e) {
//                e.printStackTrace();
//                Toast.makeText(HomeActivity.this, "Error parsing data", Toast.LENGTH_SHORT).show();
//              }
//            } else {
//              Toast.makeText(HomeActivity.this, "Gagal mengambil data", Toast.LENGTH_SHORT).show();
//            }
//          }
//
//          @Override
//          public void onFailure(Call<ResponseBody> call, Throwable t) {
//            Toast.makeText(HomeActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//          }
//        });
//      }
//    });
//
//    thread.start();
//  }

}


class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolder> {

  Context context;
  List<Album> albumList;

  public HomeAdapter(Context context, List<Album> albumList) {
    this.context = context;
    this.albumList = albumList;
  }

  public class HomeViewHolder extends RecyclerView.ViewHolder {

    ImageView ivCover;
    TextView tvArtist, tvTitle, tvPrice;


    public HomeViewHolder(@NonNull View itemView) {
      super(itemView);
      ivCover = itemView.findViewById(R.id.ivCover);
      tvArtist = itemView.findViewById(R.id.tvArtist);
      tvTitle = itemView.findViewById(R.id.tvTitle);
      tvPrice = itemView.findViewById(R.id.tvPrice);
    }
  }

  @NonNull
  @Override
  public HomeAdapter.HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(context).inflate(R.layout.row_home, parent, false);
    return new HomeViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull HomeAdapter.HomeViewHolder holder, int position) {
    Album album = albumList.get(position);
    Glide.with(context).load(album.getCover()).into(holder.ivCover);
    holder.tvArtist.setText(album.getArtist());
    holder.tvTitle.setText(album.getTitle());
    holder.tvPrice.setText(album.getPrice());

    holder.ivCover.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

//        Intent intent = new Intent(context, DetailPembelianActivity.class);
//        intent.putExtra("cover", album.getCover());
//        intent.putExtra("title", album.getTitle());
//        intent.putExtra("artist", album.getArtist());
//        intent.putExtra("detail",album.getDetailAlbum());
//        intent.putExtra("price", album.getPrice());
//        intent.putExtra("pesan", album.getTitle() + " Detail Product");
//        context.startActivity(intent);

        DetailPembelianFragment detailFragment = new DetailPembelianFragment();

        Bundle bundle = new Bundle();
        bundle.putInt("albumId", album.getAlbumId());
        bundle.putString("cover", album.getCover());
        bundle.putString("title", album.getTitle());
        bundle.putString("artist", album.getArtist());
        bundle.putString("detail", album.getDetailAlbum());
        bundle.putString("price", album.getPrice());
        bundle.putString("pesan", album.getTitle() + " Detail Product");

        detailFragment.setArguments(bundle);

        if (context instanceof AppCompatActivity) {
          AppCompatActivity activity = (AppCompatActivity) context;
          activity.getSupportFragmentManager().beginTransaction()
              .replace(R.id.fragment_container, detailFragment)
              .addToBackStack(null)
              .commit();
        }
      }
    });
  }

  @Override
  public int getItemCount() {
    return albumList.size();
  }
}