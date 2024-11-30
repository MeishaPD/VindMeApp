package com.example.vindme.activity.home;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {
  @GET("api.php")
  Call<ResponseBody> getAlbum();
}