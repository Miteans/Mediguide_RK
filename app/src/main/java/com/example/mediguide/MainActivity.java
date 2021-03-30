package com.example.mediguide;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private Button carousel_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser mFirebaseUser=mFirebaseAuth.getCurrentUser();
        System.out.println(mFirebaseAuth.getCurrentUser());

        if(mFirebaseUser!=null){
            startActivity(new Intent(MainActivity.this,HomeActivity.class));
            finish();
        }
        else{
            openCarouselActivity();
        }
    }

    public void openCarouselActivity(){
        Intent intent = new Intent(this, CarouselActivity.class);
        startActivity(intent);
    }
}