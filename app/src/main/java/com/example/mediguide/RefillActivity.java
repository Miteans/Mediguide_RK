package com.example.mediguide;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mediguide.adapters.DeviceConnectAdapter;
import com.example.mediguide.adapters.RefillAdapter;
import com.example.mediguide.data.MedicineInformation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RefillActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<String> arrayList = new ArrayList<>();
    DatabaseReference reference;
    ArrayList<MedicineInformation> retrieveMedDetails;
    RefillAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.refill_activity);

        reference = FirebaseDatabase.getInstance().getReference().child("MedicineInformation");
        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
        recyclerView = findViewById(R.id.recyclerView);

        reference.orderByChild("userId").equalTo(mFirebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i = 0;
                if (dataSnapshot.exists()) {
                    retrieveMedDetails = new ArrayList<MedicineInformation>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        int duration = Integer.parseInt(snapshot.child("duration").getValue().toString());
                        String startDate = snapshot.child("setStartDate").getValue().toString();
                        System.out.println("Hello------------------------------------------------");

                        if(checkActiveMed(duration, startDate)){
                            getData(snapshot);

                            System.out.println("Hello11111111111111------------------------------------------------");
                        }


                        setUpMedicineCards();
                    }
                }

                //No medicine detail is found
                else{
                    //  recyclerView.setVisibility(View.GONE);
                    //noData.setVisibility(View.VISIBLE);
                    //nDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    private boolean checkActiveMed(int duration, String startDateString){
        Date currentDate = new Date();
        Date medDate = null;
        Date endDate = null;
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Calendar c = Calendar.getInstance();
        try{
            medDate = format.parse(startDateString);
            c.setTime(medDate);
        }
        catch (Exception e){
        }

        c.add(Calendar.DAY_OF_MONTH, duration);

        try{
            endDate = format.parse(format.format(c.getTime()));
        }
        catch (Exception e){}

        return (medDate.compareTo(currentDate) * currentDate.compareTo(endDate) >= 0);

    }
    private void setUpMedicineCards(){
        //Assign variable

        recyclerView.setFocusable(false);
        adapter = new RefillAdapter(this);
        recyclerView.setAdapter(adapter);
        adapter.setDataToRefillAdapter(retrieveMedDetails);
        recyclerView.setVisibility(View.VISIBLE);
        /*noData.setVisibility(View.GONE);
        nDialog.dismiss();*/
    }

    private void getData(DataSnapshot snapshot){
        MedicineInformation dummy = new MedicineInformation();
        dummy.setMedicineName(snapshot.child("medicineName").getValue().toString());
        System.out.println(snapshot.child("medicineName").getValue().toString());
        dummy.setRefillCount(Integer.parseInt(snapshot.child("refillCount").getValue().toString()));
//        dummy.setEverydayMed((boolean) snapshot.child("everydayMed").getValue(Boolean.class));
//        if (!dummy.getEverydayMed()) {
//            dummy.setNoMedIntake(Integer.parseInt(snapshot.child("noMedIntake").getValue().toString()));
//        }
//
//
//        GenericTypeIndicator<List<String>> genericTypeIndicator = new GenericTypeIndicator<List<String>>() {
//        };


        retrieveMedDetails.add(dummy);
    }
}