package com.example.vindme.activity.cart;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.vindme.activity.home.Album;
import com.example.vindme.activity.home.AlbumDao;
import com.example.vindme.activity.wishlist.WishlistDao;

@Database(entities = {Album.class, Cart.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

  private static AppDatabase instance;

  public abstract AlbumDao albumDao();
  public abstract CartDao cartDao();

  public static synchronized AppDatabase getInstance(Context context) {
    if (instance == null) {
      instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "app-database")
          .allowMainThreadQueries()
          .build();
    }
    return instance;
  }
}