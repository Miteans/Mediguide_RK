package com.example.mediguide;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button carousel_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        carousel_button = (Button) findViewById(R.id.carousel_button);
        carousel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCarouselActivity();
            }
        });

    }

    public void openCarouselActivity(){
        Intent intent = new Intent(this, CarouselActivity.class);
        startActivity(intent);
    }

}