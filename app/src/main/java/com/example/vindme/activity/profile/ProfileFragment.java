package com.example.vindme.activity.profile;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vindme.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.List;

public class ProfileFragment extends Fragment {
    private TextView tvUsername, tvEmail;
    private FirebaseAuth auth;
    private DatabaseReference database;
    private Button btn_edit;

    private RecyclerView rvProfileItem;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance("https://vindme-d1523-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("profile");

        tvUsername = view.findViewById(R.id.tv_username);
        tvEmail = view.findViewById(R.id.tv_email);

        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            loadUserProfile(user.getUid());
        } else {
            Toast.makeText(getContext(), "Anda belum log in", Toast.LENGTH_SHORT).show();
//            createNewUser();
        }

        rvProfileItem = view.findViewById(R.id.rvProfileItem);
        rvProfileItem.setLayoutManager(new LinearLayoutManager(getContext()));

        List<ProfileItem> items = Arrays.asList(
                new ProfileItem(R.drawable.cart_icon, "Cek Keranjang"),
                new ProfileItem(R.drawable.wishlist_icon, "Wishlist"),
                new ProfileItem(R.drawable.faq_icon, "FAQ"),
                new ProfileItem(R.drawable.translate_icon, "Bahasa"),
                new ProfileItem(R.drawable.baseline_settings_24, "Pengaturan"),
                new ProfileItem(R.drawable.reset_password_icon, "Ubah Password")
        );

        ProfileItemAdapter adapter = new ProfileItemAdapter(items);
        rvProfileItem.setAdapter(adapter);

        btn_edit = view.findViewById(R.id.btn_edit);
        btn_edit.setOnClickListener(v -> {
            EditProfileFragment editProfileFragment = new EditProfileFragment();
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainerView, editProfileFragment)
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }

    private void createNewUser() {
        String email = "vindmeuser@gmail.com";
        String password = "password123";

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null) {
                            saveUserProfile(user.getUid(), "VindmeUser", email);
                        }
                    } else {
                        Log.e("ProfileFragment", "Gagal membuat pengguna baru", task.getException());
                    }
                });
    }

    private void saveUserProfile(String userId, String username, String email) {
        database.child(userId).setValue(new UserProfile(username, email))
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        tvUsername.setText(username);
                        tvEmail.setText(email);
                    } else {
                        Log.e("ProfileFragment", "Gagal menyimpan profil", task.getException());
                    }
                });
    }

    private void loadUserProfile(String userId) {
        database.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    UserProfile profile = snapshot.getValue(UserProfile.class);
                    if (profile != null) {
                        tvUsername.setText(profile.getUsername());
                        tvEmail.setText(profile.getEmail());
                    }
                } else {
                    createNewUser();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ProfileFragment", "Gagal memuat profil", error.toException());
            }
        });
    }
}