package com.example.mediguide;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
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

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    DatabaseReference reference;
    ArrayList<MedicineInformation> retrieveMedDetails;
    private RecyclerView recyclerView;
    private HomeAdapter homeAdapter;
    private ImageView imageView;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        MaterialToolbar toolbar = (MaterialToolbar) findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("MediGuide");

        /*createNotificationChannel();

        Intent intent = new Intent(HomeActivity.this, ReminderBroadcast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0,intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, 20000, pendingIntent);*/


        reference = FirebaseDatabase.getInstance().getReference().child("MedicineInformation");
        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser mFirebaseUser=mFirebaseAuth.getCurrentUser();

        imageView = findViewById(R.id.image_view);
        try {
            Picasso.with(this)
                    .load(R.drawable.calendar1)
                    /*.placeholder(R.drawable.placeholder) //optional*/
                    .resize(350, 260)         //optional
                    /*.centerCrop()       */                 //optional
                    .into(imageView);
        }
        catch (Exception e){}

        reference.orderByChild("userId").equalTo(mFirebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i = 0;
                if(dataSnapshot.exists()){
                    retrieveMedDetails = new ArrayList<MedicineInformation>();
                    for(DataSnapshot snapshot : dataSnapshot.getChildren() ){
                        //Daily Medication
                        if(snapshot.child("everydayMed").getValue(Boolean.class)){
                            if(checkTheDate(snapshot.child("setStartDate").getValue().toString(), snapshot.child("duration").getValue().toString())){
                                retrieveDataFromDatabase(snapshot);
                            }
                        }
                        //Not a daily Medication
                        else{
                            if(checkNoDate(snapshot)){
                                retrieveDataFromDatabase(snapshot);
                            }
                        }
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });



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
                openAppointmentActivity();
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
        Intent intent = new Intent(this, MedicationActivity.class);
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

    public void openAppointmentActivity(){
        Intent intent = new Intent(this, AppointmentActivity.class);
        startActivity(intent);
    }

    public void openSettingsActivity(){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    private void setUpMedicineCards() {
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setFocusable(false);

        homeAdapter = new HomeAdapter(this);
        recyclerView.setAdapter(homeAdapter);

        homeAdapter.setDataToMedicationAdapter(retrieveMedDetails);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean checkTheDate(String medDateString, String medDuration){
        Date currentDate = new Date();
        Date medDate = currentDate;
        Date endDate = currentDate;
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Calendar c = Calendar.getInstance();
        try{
            medDate = format.parse(medDateString);
            c.setTime(format.parse(medDateString));
        }
        catch (Exception e){
        }

        c.add(Calendar.DAY_OF_MONTH, Integer.parseInt(medDuration));

        try{
            endDate = format.parse(format.format(c.getTime()));
        }
        catch (Exception e){}

        return (medDate.compareTo(currentDate) * currentDate.compareTo(endDate) >= 0);
    }

    private boolean checkNoDate(DataSnapshot snapshot){
        Date currentDate = new Date();
        Date medDate = currentDate;
        Date endDate = currentDate;
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Calendar c = Calendar.getInstance();

        try {
            medDate = format.parse(snapshot.child("setStartDate").getValue().toString());
            c.setTime(medDate);
            c.add(Calendar.DAY_OF_MONTH, Integer.parseInt(snapshot.child("duration").getValue().toString()));
            endDate = format.parse(format.format(c.getTime()));

            c = Calendar.getInstance();
            c.setTime(medDate);

            while ((format.parse(format.format(c.getTime())).after(medDate) ||
                    format.parse(format.format(c.getTime())).equals(medDate))
                    && (format.parse(format.format(c.getTime())).before(endDate) ||
                    format.parse(format.format(c.getTime())).equals(endDate))){

                if(format.parse(format.format(c.getTime())).equals(currentDate)){
                    return true;
                }

                else if(format.parse(format.format(c.getTime())).after(currentDate)){
                    return false;
                }

                c.add(Calendar.DAY_OF_MONTH, Integer.parseInt(snapshot.child("noMedIntake").getValue().toString()));
            }
        }
        catch (Exception e){ }
        return false;
    }

    private void retrieveDataFromDatabase(DataSnapshot snapshot){
        MedicineInformation dummy = new MedicineInformation();
        dummy.setMedicineName(snapshot.child("medicineName").getValue().toString());
        dummy.setDosage(Integer.parseInt(snapshot.child("dosage").getValue().toString()));
        dummy.setFrequencyOfMedIntake(Integer.parseInt(snapshot.child("frequencyOfMedIntake").getValue().toString()));

        GenericTypeIndicator<List<String>> genericTypeIndicator = new GenericTypeIndicator<List<String>>() {};
        dummy.setIntakeTimes(snapshot.child("intakeTimes").getValue(genericTypeIndicator));

        dummy.setImageUrl(snapshot.child("imageUrl").getValue().toString());

        retrieveMedDetails.add(dummy);
        setUpMedicineCards();
    }

    /*@RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel(){
        CharSequence name  = "cghhfjknl";
        String description = "hchvkhnlkmkln";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel("NotifyMedIntake", name, importance);
        channel.setDescription(description);

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }*/
}