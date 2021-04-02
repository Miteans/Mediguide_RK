package com.example.mediguide;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
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

import java.util.ArrayList;
import java.util.List;

public class MedicationActivity extends AppCompatActivity {
    MaterialToolbar mToolbar;
    DatabaseReference reference;
    ArrayList<MedicineInformation> retrieveMedDetails;
    private RecyclerView recyclerView;
    private MedicationAdapter medicationAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.medication_activity);

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

                        if(!dummy.getEverydayMed()) {
                            dummy.setNoMedIntake(Integer.parseInt(snapshot.child("noMedIntake").getValue().toString()));
                        }

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
                        setUpMedicineCards();
                        printData(retrieveMedDetails);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        //Back Button(To Home)
        mToolbar = (MaterialToolbar) findViewById(R.id.topAppBar);
        mToolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MedicationActivity.this,HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMedAddPageOneActivity();
            }
        });
    }

    private void openMedAddPageOneActivity(){
        Intent intent = new Intent(this, MedAddActivity.class);
        startActivity(intent);
    }

    private void printData(ArrayList<MedicineInformation> datas){
        for(MedicineInformation data: datas){
            System.out.println(data.getMedicineName());
        }
    }

    private void setUpMedicineCards(){
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setFocusable(false);

        medicationAdapter = new MedicationAdapter(this);
        recyclerView.setAdapter(medicationAdapter);

        medicationAdapter.setDataToMedicationAdapter(retrieveMedDetails);
    }
}