package com.example.vindme.activity.profile;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.vindme.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditProfileFragment extends Fragment {
    private EditText etUsername, etEmail;
    private Button btnSave;

    private FirebaseAuth auth;
    private DatabaseReference database;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance("https://vindme-d1523-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("profile");

        etUsername = view.findViewById(R.id.editTextText);
        etEmail = view.findViewById(R.id.editTextText2);
        btnSave = view.findViewById(R.id.button);

        loadUserProfile();
        btnSave.setOnClickListener(v -> saveUserProfile());

        return view;
    }

    private void loadUserProfile() {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();

            database.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String username = snapshot.child("username").getValue(String.class);
                        String email = snapshot.child("email").getValue(String.class);

                        etUsername.setText(username);
                        etEmail.setText(email);

                        Toast.makeText(getContext(), "Data profil ditemukan", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(requireContext(), "Data profil tidak ditemukan", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("EditProfileFragment", "Gagal memuat profil", error.toException());
                }
            });
        } else {
            Toast.makeText(getContext(), "Data profil tidak ditemukan", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveUserProfile() {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            String newUsername = etUsername.getText().toString().trim();
            String newEmail = etEmail.getText().toString().trim();

            if (newUsername.isEmpty() || newEmail.isEmpty()) {
                Toast.makeText(requireContext(), "Username dan email tidak boleh kosong", Toast.LENGTH_SHORT).show();
                return;
            }

            database.child(userId).child("username").setValue(newUsername);
            database.child(userId).child("email").setValue(newEmail)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(requireContext(), "Profil berhasil diperbarui", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(requireContext(), "Gagal memperbarui profil", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}