package com.example.mediguide;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class AppointmentActivity extends AppCompatActivity {
    MaterialToolbar mToolbar;
    DatabaseReference reference;
    private ImageView imageView;
    ArrayList<Appointment> retrieveAppointmentDetails;
    private RecyclerView recyclerView;
    private AppointmentAdapter appointmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appointment_activity);

        FloatingActionButton fab = findViewById(R.id.fab1);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAppointmentForm();
            }
        });


        reference = FirebaseDatabase.getInstance().getReference().child("Appointment");

        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser mFirebaseUser=mFirebaseAuth.getCurrentUser();

        imageView = findViewById(R.id.image_view);
        try {
            Picasso.with(this)
                    .load(R.drawable.calendar)
                    /*.placeholder(R.drawable.placeholder) //optional*/
                    .resize(350, 260)         //optional
                    /*.centerCrop()       */                 //optional
                    .into(imageView);
        }
        catch (Exception e){}


        reference.orderByChild("userId").equalTo(mFirebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i = 0;
                if (dataSnapshot.exists()) {
                    retrieveAppointmentDetails = new ArrayList<Appointment>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Appointment dummy = new Appointment();
                        dummy.setAppointment_title(snapshot.child("appointment_title").getValue().toString());
                        dummy.setDoctor_name(snapshot.child("doctor_name").getValue().toString());
                        dummy.setHospital_name(snapshot.child("hospital_name").getValue().toString());
                        dummy.setDate(snapshot.child("date").getValue().toString());
                        dummy.setTime(snapshot.child("time").getValue().toString());

                        retrieveAppointmentDetails.add(dummy);
                        setUpAppointmentCards();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Back Button(To Home)
        mToolbar = (MaterialToolbar) findViewById(R.id.topAppBar1);
        mToolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AppointmentActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }

    public void openAppointmentForm() {
        Intent intent = new Intent(this, com.example.mediguide.AppointmentForm.class);
        startActivity(intent);
    }

    private void setUpAppointmentCards() {
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setFocusable(false);
        appointmentAdapter = new AppointmentAdapter(this);
        recyclerView.setAdapter(appointmentAdapter);
        appointmentAdapter.setDataToAppointmentAdapter(retrieveAppointmentDetails);
    }
}