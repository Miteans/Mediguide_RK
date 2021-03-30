package com.example.mediguide;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Calendar;

public class HomeActivity extends AppCompatActivity {
    MaterialToolbar mToolbar;
    DatabaseReference reference;
    ArrayList<MedicineInformation> retrieveMedDetails;
    private RecyclerView recyclerView;
    //    private MedicationAdapter medicationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        reference = FirebaseDatabase.getInstance().getReference().child("MedicineInformation");
        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser mFirebaseUser=mFirebaseAuth.getCurrentUser();

        reference.orderByChild("userId").equalTo(mFirebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i = 0;
                if(dataSnapshot.exists()){
                    retrieveMedDetails = new ArrayList<MedicineInformation>();
                    for(DataSnapshot snapshot : dataSnapshot.getChildren() ){
                        MedicineInformation dummy = new MedicineInformation();
                        dummy.setMedicineName(snapshot.child("medicineName").getValue().toString());
                        dummy.setFormOfMedicine(snapshot.child("formOfMedicine").getValue().toString());
                        dummy.setDosage(Integer.parseInt(snapshot.child("dosage").getValue().toString()));
                        dummy.setReasonForIntake(snapshot.child("reasonForIntake").getValue().toString());
                        dummy.setEverydayMed((boolean) snapshot.child("everydayMed").getValue(Boolean.class));
                        dummy.setFrequencyOfMedIntake(Integer.parseInt(snapshot.child("frequencyOfMedIntake").getValue().toString()));

                        GenericTypeIndicator<List<String>> genericTypeIndicator = new GenericTypeIndicator<List<String>>() {};
                        dummy.setIntakeTimes(snapshot.child("intakeTimes").getValue(genericTypeIndicator));

                        dummy.setImageUrl(snapshot.child("imageUrl").getValue().toString());
                        dummy.setInstruction(snapshot.child("instruction").getValue().toString());
                        dummy.setDuration(Integer.parseInt(snapshot.child("duration").getValue().toString()));
                        dummy.setRefillCount(Integer.parseInt(snapshot.child("refillCount").getValue().toString()));
                        dummy.setSetStartDate(snapshot.child("setStartDate").getValue().toString());
                        dummy.setOtherInstruction(snapshot.child("otherInstruction").getValue().toString());

                        retrieveMedDetails.add(dummy);
                        //setUpMedicineCards();
//                        printData(retrieveMedDetails);
                        Date currentTime = Calendar.getInstance().getTime();
                        System.out.println(currentTime);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });



        MaterialToolbar toolbar = (MaterialToolbar) findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("MediGuide");

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMedAddPageOneActivity();
            }
        });

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        openHomeActivity();
                        break;
                    case R.id.medication:
                        openMedicationActivity();
                        break;
                    case R.id.connect:
                        openConnectActivity();
                        break;
                    case R.id.profile:
                        openProfileActivity();
                        break;
                }
                return true;
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_top_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.appointment:
                //add the function to perform here
                openAppointmentCardActivity();
                break;
            case R.id.refill:
                //add the function to perform here
                openConnectActivity();
                return (true);
            case R.id.report:
                //add the function to perform here
                openHomeActivity();
                break;
            case R.id.settings:
                //add the function to perform here
                openSettingsActivity();
                break;
        }
        return true;
    }

    public void openHomeActivity(){
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    public void openMedicationActivity(){
        Intent intent = new Intent(this, MedicationActivity.class);
        startActivity(intent);
    }

    public void openConnectActivity(){
        Intent intent = new Intent(this, Dummy.class);
        startActivity(intent);
    }

    public void openProfileActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void openMedAddPageOneActivity(){
        Intent intent = new Intent(this, MedAddActivity.class);
        startActivity(intent);
    }

    public void openAppointmentCardActivity(){
        Intent intent = new Intent(this, AppointmentCardActivity.class);
        startActivity(intent);
    }

    public void openSettingsActivity(){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}