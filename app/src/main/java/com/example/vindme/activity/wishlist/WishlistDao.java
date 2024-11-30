package com.example.vindme.activity.wishlist;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface WishlistDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insertWishlists(List<Wishlist> wishlists);

  @Query("SELECT * FROM Wishlist")
  List<Wishlist> getAllWishlist();

  @Query("DELETE FROM Wishlist WHERE idWishlist = :idWishlist")
  void deleteWishlist(String idWishlist);
}
