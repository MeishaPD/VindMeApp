package com.example.vindme.activity.wishlist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vindme.R;

public class AddWishlistActivity extends AppCompatActivity {

  String artis = "Taylor Swift";
  String deskripsi = "The Tortured Poets Department Vinyl + Bonus Track “The Manuscript”";
  String harga = "699.000";

  TextView tvArtist, tvDescription, tvPrice;
  Button  btWIshlist, btBuy;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    EdgeToEdge.enable(this);
    setContentView(R.layout.activity_add_wishlist);

    tvArtist = findViewById(R.id.tvArtist);
    tvDescription = findViewById(R.id.tvDescription);
    tvPrice = findViewById(R.id.tvPrice);
    btWIshlist = findViewById(R.id.btWishlist);
    btBuy = findViewById(R.id.btBuy);


    tvArtist.setText(artis);
    tvDescription.setText(deskripsi);
    tvPrice.setText("Rp. " + harga);

    btWIshlist.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(getApplicationContext(), WishlistActivity.class);

        intent.putExtra("artis", artis);
        intent.putExtra("deskripsi", deskripsi);
        intent.putExtra("harga", harga);

        startActivity(intent);
      }
    });

    String pesan = getIntent().getStringExtra("pesan");
    if (pesan == null){
      btBuy.setText("Buy Now");
    } else {
      btBuy.setText(pesan);
    }
  }
}