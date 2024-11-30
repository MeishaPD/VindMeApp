package com.example.vindme.activity.wishlist;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity(tableName = "Wishlist")
public class Wishlist implements Serializable {

  @SerializedName("idWishlist")
  @PrimaryKey
  @ColumnInfo(name = "wishlistId")
  @NonNull
  String wishlistId;

  @ColumnInfo(name = "albumId")
  private int albumId;

  @ColumnInfo(name = "cover")
  private String cover;

  @ColumnInfo(name = "title")
  private String title;

  @ColumnInfo(name = "artist")
  private String artist;

  @ColumnInfo(name = "price")
  private String price;

  public Wishlist() {
    // Default constructor for Firebase
  }

  public Wishlist(String wishlistId, int albumId, String cover, String title, String artist, String price) {
    this.wishlistId = wishlistId;
    this.albumId = albumId;
    this.cover = cover;
    this.title = title;
    this.artist = artist;
    this.price = price;
  }

  public void setWishlistId(String wishlistId) {
    this.wishlistId = wishlistId;
  }

  public String getWishlistId() {
    return wishlistId;
  }

  public void setAlbumId(int albumId) {
    this.albumId = albumId;
  }

  public int getAlbumId() {
    return albumId;
  }

  public void setCover(String cover) {
    this.cover = cover;
  }

  public String getCover() {
    return cover;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getTitle() {
    return title;
  }

  public void setArtist(String artist) {
    this.artist = artist;
  }

  public String getArtist() {
    return artist;
  }

  public void setPrice(String price) {
    this.price = price;
  }

  public String getPrice() {
    return price;
  }

}
