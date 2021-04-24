package com.example.mediguide;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private Button carousel_button;
    /*Intent mServiceIntent;
    private DatabaseConnectBg mSensorService;*/

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser mFirebaseUser=mFirebaseAuth.getCurrentUser();
        System.out.println(mFirebaseAuth.getCurrentUser());

        if(mFirebaseUser != null){
            /*mSensorService = new DatabaseConnectBg(this);
            mServiceIntent = new Intent(this, mSensorService.getClass());
            if (!isMyServiceRunning(mSensorService.getClass())) {
                startService(mServiceIntent);
            }*/
            startActivity(new Intent(MainActivity.this,HomeActivity.class));
            finish();
        }
        else{
            openCarouselActivity();
            finish();
        }
    }

    public void openCarouselActivity(){
        Intent intent = new Intent(this, CarouselActivity.class);
        startActivity(intent);
    }

    /*@RequiresApi(api = Build.VERSION_CODES.M)
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }*/


}