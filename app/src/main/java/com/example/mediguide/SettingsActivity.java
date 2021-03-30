package com.example.mediguide;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class SettingsActivity extends Activity {
    TextView log_out,changepswrd;
    TextInputEditText old_pass,newpass1,newpass2;
    boolean isOldValid, isNew1Valid, isNew2Valid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        old_pass = (TextInputEditText) findViewById(R.id.oldpass);
        newpass1 = (TextInputEditText) findViewById(R.id.newpass1);
        newpass2 = (TextInputEditText) findViewById(R.id.newpass2);

        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();

        MaterialToolbar mToolbar = (MaterialToolbar) findViewById(R.id.topAppBarSetting);
        mToolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this,HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        log_out = (TextView) findViewById(R.id.logout);
        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                AlertDialog.Builder builder= new AlertDialog.Builder(SettingsActivity.this);
                builder.setMessage("Are you sure?")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mFirebaseAuth.signOut();
                                System.out.println(mFirebaseAuth.getCurrentUser());
                                openLoginActivity();
                                finish();
                            }
                        })
                        .setNegativeButton("Cancel",null);

                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        changepswrd = (TextView) findViewById(R.id.changepass01);
        changepswrd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                View view= LayoutInflater.from(SettingsActivity.this).inflate(R.layout.change_password_activity,null);

                AlertDialog.Builder builder= new AlertDialog.Builder(SettingsActivity.this);

                builder.setMessage("Change password")
                        .setView(view)
                        .setPositiveButton("Reset", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SetValidation();
                            }
                        })
                        .setNegativeButton("Cancel",null);

                AlertDialog alert = builder.create();
                alert.show();
            }
        });

    }

    public void openLoginActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void SetValidation() {
        // Check for a oldpassword.
        if (old_pass.getText().toString().isEmpty()) {
            old_pass.setError(getResources().getString(R.string.name_error));
            isOldValid = false;
        } else  {
            isOldValid = true;
        }

        // Check for a new valid password.
        if (newpass1.getText().toString().isEmpty()) {
            newpass1.setError(getResources().getString(R.string.password_error));
            isNew1Valid = false;
        } else if (newpass1.getText().length() < 6) {
            newpass1.setError(getResources().getString(R.string.error_invalid_password));
            isNew1Valid = false;
        } else  {
            isNew1Valid = true;
        }

        // Check for a confirmation of password.
        if (newpass2.getText().toString().isEmpty()) {
            newpass2.setError(getResources().getString(R.string.confirm_password_error));
            isNew2Valid = false;
        } else if (newpass2.getText() != newpass1.getText()) {
            newpass2.setError(getResources().getString(R.string.error_password_not_confirmed));
            isNew2Valid = false;
        } else  {
            isNew2Valid = true;
        }

        if (isOldValid && isNew1Valid && isNew2Valid) {
            Toast.makeText(getApplicationContext(), "Successfull", Toast.LENGTH_SHORT).show();
        }
    }


}