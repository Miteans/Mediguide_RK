package com.example.mediguide;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;

public class EditProfile extends AppCompatActivity {
    ImageView cancel;
    TextInputEditText fullname,email,phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);


        //Close Button
        cancel = (ImageView) findViewById(R.id.cancel_button1);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditProfile.this, ProfileActivity.class);
                startActivity(intent);
                finish();
            }
        });



        Intent intent = getIntent();
        String name= intent.getStringExtra("NAME");
        String mail= intent.getStringExtra("EMAIL");
        String phnum= intent.getStringExtra("PHONE");

        TextInputEditText fullname= findViewById(R.id.fname);
        TextInputEditText email = findViewById(R.id.email);
        TextInputEditText phone = findViewById(R.id.phone);

        fullname.setText(name);
        email.setText(mail);
        phone.setText(phnum);

    }

}
