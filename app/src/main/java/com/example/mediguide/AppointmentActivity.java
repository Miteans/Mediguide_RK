package com.example.mediguide;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.mbms.StreamingServiceInfo;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.LinearLayout;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class AppointmentActivity extends AppCompatActivity {
    MaterialToolbar mToolbar;
    DatabaseReference reference;
    private ImageView imageView,imageView1;
    ArrayList<Appointment> retrieveAppointmentDetails;
    private RecyclerView recyclerView;
    private LinearLayout noData,noTodayApp;
    private AppointmentAdapter appointmentAdapter;
    private View v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appointment_activity);

        noTodayApp = findViewById(R.id.noTodayApp);
        noData = findViewById(R.id.noData);
        recyclerView = findViewById(R.id.recyclerView);

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

        imageView1 = findViewById(R.id.image_view1);
        try {
            Picasso.with(this)
                    .load(R.drawable.calendar)
                    /*.placeholder(R.drawable.placeholder) //optional*/
                    .resize(350, 260)         //optional
                    /*.centerCrop()       */                 //optional
                    .into(imageView1);
        }
        catch (Exception e){}


        reference.orderByChild("userId").equalTo(mFirebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
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

//                        //code to get appointment notification
//                        if(snapshot.child("isReminderSet").getValue(Boolean.class)){
//                            if(checkTheDate(snapshot.child("date").getValue().toString())) {
//                                //  retrieveDataFromDatabase(snapshot);
//                                System.out.println("**************************************Hello************************************");
//                                public void sendMsg(View v) {
//                                    String uniqueActionString = "com.androidbook.intents.testbc";
//                                    Intent broadcastIntent = new Intent(uniqueActionString);
//                                    broadcastIntent.putExtra("message","Hello world");
//                                    sendBroadcast(broadcastIntent);
//                                }
//                            }
//                        }
                    }
                    //No medication to take today
                    if(retrieveAppointmentDetails.size() == 0){
//                        nDialog.dismiss();
                        noTodayApp.setVisibility(View.VISIBLE);
                        noData.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);
                    }
                }
                //No medicine detail is found
                else{
//                    nDialog.dismiss();
                    noTodayApp.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                    noData.setVisibility(View.VISIBLE);
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean checkTheDate(String appDateString){
        Date currentDate = new Date();
        Date appDate = currentDate;
        Date prevDate = currentDate;
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Calendar c = Calendar.getInstance();
        try{
            appDate = format.parse(appDateString);
            c.setTime(format.parse(appDateString));
        }
        catch (Exception e){
        }

        c.add(Calendar.DAY_OF_MONTH,-1);

        try{
            prevDate = format.parse(format.format(c.getTime()));
        }
        catch (Exception e){}

        return (appDate.compareTo(currentDate) * prevDate.compareTo(currentDate) <= 0);
    }
}