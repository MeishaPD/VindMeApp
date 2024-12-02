package com.example.vindme.activity.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.vindme.R;
import com.example.vindme.activity.cart.AppDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

  private RecyclerView recyclerView;
  private HomeAdapter homeAdapter;
  private List<Album> albumList;
  private FirebaseAuth firebaseAuth;
  private DatabaseReference databaseReference;

  // TODO: Rename parameter arguments, choose names that match
  // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
  private static final String ARG_PARAM1 = "param1";
  private static final String ARG_PARAM2 = "param2";

  // TODO: Rename and change types of parameters
  private String mParam1;
  private String mParam2;

  public HomeFragment() {
    // Required empty public constructor
  }

  /**
   * Use this factory method to create a new instance of
   * this fragment using the provided parameters.
   *
   * @param param1 Parameter 1.
   * @param param2 Parameter 2.
   * @return A new instance of fragment HomeFragment.
   */
  // TODO: Rename and change types and number of parameters
  public static HomeFragment newInstance(String param1, String param2) {
    HomeFragment fragment = new HomeFragment();
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

    View view = inflater.inflate(R.layout.fragment_home, container, false);

    recyclerView = view.findViewById(R.id.rvHome);
    albumList = new ArrayList<>();
    homeAdapter = new HomeAdapter(getContext(), albumList);
    recyclerView.setAdapter(homeAdapter);
    recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

    firebaseAuth = FirebaseAuth.getInstance();
    databaseReference = FirebaseDatabase.getInstance("https://vindme-d1523-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("albums");

    String email = "user" + System.currentTimeMillis() + "@example.com";
    String password = "password123";

    if (firebaseAuth.getCurrentUser() == null) {
      firebaseAuth.createUserWithEmailAndPassword(email, password)
          .addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
              Log.d("FirebaseAuth", "User created successfully");
            } else {
              Log.e("FirebaseAuth", "User creation failed", task.getException());
              Toast.makeText(getContext(), "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
          });
    }

    databaseReference.get().addOnCompleteListener(task -> {
      if (task.isSuccessful() && task.getResult() != null && task.getResult().hasChildren()) {
        albumList.clear();
        for (DataSnapshot snapshot : task.getResult().getChildren()) {
          Album album = snapshot.getValue(Album.class);
          albumList.add(album);
        }
        homeAdapter.notifyDataSetChanged();
      } else {
        fetchAlbums();
      }
    });

    return view;
  }

  private void fetchAlbums() {
    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl("http://192.168.1.37/ApiPam/")
        .build();

    ApiInterface apiInterface = retrofit.create(ApiInterface.class);
    Call<ResponseBody> call = apiInterface.getAlbum();

    call.enqueue(new Callback<ResponseBody>() {
      @Override
      public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if (response.isSuccessful() && response.body() != null) {
          try {
            String json = response.body().string();
            Gson gson = new Gson();
            Type albumListType = new TypeToken<List<Album>>(){}.getType();
            List<Album> albums = gson.fromJson(json, albumListType);

//            saveAlbumsToDatabase(albums);
            saveAlbumsToFirebase(albums);

            albumList.clear();
            albumList.addAll(albums);
            homeAdapter.notifyDataSetChanged();

          } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Error " + e.getMessage(), Toast.LENGTH_SHORT).show();
          }
        }
        else {
          Toast.makeText(getContext(), "Gagal mengambil data", Toast.LENGTH_SHORT).show();
        }
      }

      @Override
      public void onFailure(Call<ResponseBody> call, Throwable t) {
        Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
      }
    });
  }

  private void saveAlbumsToFirebase(List<Album> albums) {
    for (Album album : albums) {
      String albumId = String.valueOf(album.getAlbumId());
      databaseReference.child(albumId).setValue(album)
          .addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
              Toast.makeText(getContext(), "Album saved successfully", Toast.LENGTH_SHORT).show();
              Log.d("Firebase", "Album saved successfully: " + album.getTitle());
            }
            else {
              Toast.makeText(getContext(), "Error saving album", Toast.LENGTH_SHORT).show();
              Log.e("Firebase", "Error saving album", task.getException());
            }
          });
    }
  }
}