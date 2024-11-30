package com.example.vindme.activity.home;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface AlbumDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insertAlbums(Album album);

  @Query("SELECT * FROM Album WHERE albumId = :albumId")
  Album getAlbumById(int albumId);

  @Query("SELECT * FROM Album")
  List<Album> getAllAlbums();

}
