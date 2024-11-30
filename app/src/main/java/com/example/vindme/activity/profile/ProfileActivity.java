package com.example.vindme.activity.profile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.vindme.activity.cart.CartActivity;
import com.example.vindme.R;
import com.example.vindme.activity.home.HomeActivity;
import com.example.vindme.activity.search.SearchActivity;
import com.example.vindme.activity.wishlist.WishlistActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    EdgeToEdge.enable(this);
    setContentView(R.layout.activity_profile);

    BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
    bottomNav.setSelectedItemId(R.id.profile);
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

    if (savedInstanceState == null) {
      loadFragment(new ProfileFragment());
    }

  }

  private void loadFragment(Fragment fragment) {
    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    transaction.replace(R.id.fragmentContainerView, fragment);
    transaction.addToBackStack(null);
    transaction.commit();
  }

}