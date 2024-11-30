package com.example.vindme.activity.cart;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CartDao {

  @Insert
  void insertCart(Cart cart);

  @Query("SELECT Cart.cartId, Cart.albumId, Album.cover, Album.title, Album.artist, Album.price FROM Cart " +
      "INNER JOIN Album ON Cart.albumId = Album.albumId")
  List<Cart> getAllCarts();

  @Query("DELETE FROM Cart WHERE cartId = :cartId")
  void deleteCart(int cartId);

}
