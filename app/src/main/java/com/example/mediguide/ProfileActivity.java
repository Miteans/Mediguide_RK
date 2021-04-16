package com.example.mediguide;

import android.app.usage.NetworkStats;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {
    Button edit_profile;
    MaterialToolbar mToolbar;
    TextView fullname, email, phone,profileName;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        fullname = (TextView) findViewById(R.id.PersonName2);
        email = (TextView) findViewById(R.id.email2);
        phone = (TextView) findViewById(R.id.phone2);
        profileName = (TextView) findViewById(R.id.topText3);


        reference = FirebaseDatabase.getInstance().getReference().child("User");
        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();

        reference.orderByChild("userId").equalTo(mFirebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i = 0;
                if (dataSnapshot.exists()) {
                    User retrieveProfileDetails = new User();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        retrieveProfileDetails.setName(snapshot.child("name").getValue().toString());
                        retrieveProfileDetails.setEmail(snapshot.child("email").getValue().toString());
                        retrieveProfileDetails.setPhoneNumber(Long.parseLong(snapshot.child("phoneNumber").getValue().toString()));
                        fullname.setText(retrieveProfileDetails.getName());
                        System.out.println(retrieveProfileDetails.getName());
                        email.setText(retrieveProfileDetails.getEmail());
                        phone.setText(String.valueOf(retrieveProfileDetails.getPhoneNumber()));
                        profileName.setText(retrieveProfileDetails.getName());
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

        edit_profile = (Button) findViewById(R.id.edit_profile);
        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String fname = fullname.getText().toString();
                String mail = email.getText().toString();
                String phnum = phone.getText().toString();

                Intent intent = new Intent(ProfileActivity.this, EditProfile.class);
                intent.putExtra("NAME", fname);
                intent.putExtra("EMAIL", mail);
                intent.putExtra("PHONE", phnum);

                startActivity(intent);
            }
        });

        mToolbar = (MaterialToolbar) findViewById(R.id.topAppBar1);
        mToolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

}
