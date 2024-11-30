package com.example.vindme.activity.cart;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.example.vindme.activity.home.Album;

@Entity(
    tableName = "Cart",
    foreignKeys = @ForeignKey(
        entity = Album.class,
        parentColumns = "albumId",
        childColumns = "albumId",
        onDelete = ForeignKey.CASCADE
    )
)
public class Cart {

  @ColumnInfo(name = "cartId")
  @PrimaryKey(autoGenerate = false)
  @NonNull
  private String cartId;

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

  public Cart() {}

  public Cart(String cartId, int albumId, String cover, String title, String artist, String price) {
    this.cartId = cartId;
    this.albumId = albumId;
    this.cover = cover;
    this.title = title;
    this.artist = artist;
    this.price = price;
  }

  public String getCartId() {
    return cartId;
  }

  public void setCartId(String cartId) {
    this.cartId = cartId;
  }

  public int getAlbumId() {
    return albumId;
  }

  public void setAlbumId(int albumId) {
    this.albumId = albumId;
  }

  public String getCover() {
    return cover;
  }

  public void setCover(String cover) {
    this.cover = cover;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getArtist() {
    return artist;
  }

  public void setArtist(String artist) {
    this.artist = artist;
  }

  public String getPrice() {
    return price;
  }

  public void setPrice(String price) {
    this.price = price;
  }
}
