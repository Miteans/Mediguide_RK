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

import androidx.annotation.NonNull;

import com.example.mediguide.forms.LoginActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

    public class SettingsActivity extends Activity {
        TextView log_out,changepswrd;
        TextInputEditText old_pass,newpass1,newpass2;
        boolean isOldValid, isNew1Valid, isNew2Valid;
        AlertDialog alert;
        Boolean wantToCloseDialog = false;

        DatabaseReference reference;
        String checkpass;
        FirebaseAuth mFirebaseAuth;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.settings_activity);

            mFirebaseAuth = FirebaseAuth.getInstance();

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
                                    mFirebaseAuth.getInstance().signOut();
                                    System.out.println(mFirebaseAuth.getCurrentUser());
                                    openLoginActivity();
                                    finish();
                                }
                            })
                            .setNegativeButton("Cancel",null);

                    alert = builder.create();
                    alert.show();
                }
            });

            changepswrd = (TextView) findViewById(R.id.changepass01);
            changepswrd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    View view= LayoutInflater.from(SettingsActivity.this).inflate(R.layout.change_password_activity,null);

                    old_pass = (TextInputEditText) view.findViewById(R.id.oldpass);
                    newpass1 = (TextInputEditText) view.findViewById(R.id.newpass);
                    newpass2 = (TextInputEditText) view.findViewById(R.id.confirmpass);

                    AlertDialog.Builder builder= new AlertDialog.Builder(SettingsActivity.this);
                    builder.setCancelable(false);

                    builder.setMessage("Change password")
                            .setView(view)
                            .setPositiveButton("Reset", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    // Dismiss dialog and close activity if appropriated, do not use this (cancel) button at all.
                                    dialog.dismiss();
                                }
                            });

                    alert = builder.create();
                    alert.show();

                    alert.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            if (old_pass.getText().toString().isEmpty()) {
                                Toast.makeText(SettingsActivity.this, "Old password is required", Toast.LENGTH_LONG).show();
                                isOldValid = false;
                            } else  {
                                isOldValid = true;
                            }

                            // Check for a new valid password.
                            if (newpass1.getText().toString().isEmpty()) {
                                Toast.makeText(SettingsActivity.this, "New password is required", Toast.LENGTH_LONG).show();
                                isNew1Valid = false;
                            } else if (newpass1.getText().length() < 6) {
                                Toast.makeText(SettingsActivity.this, "Please enter a password of minimum 6 characters", Toast.LENGTH_LONG).show();
                                isNew1Valid = false;
                            } else  {
                                isNew1Valid = true;
                            }

                            // Check for a confirmation of password.
                            if (newpass2.getText().toString().isEmpty()) {
                                Toast.makeText(SettingsActivity.this, "Confirm password is required", Toast.LENGTH_LONG).show();
                                isNew2Valid = false;
                            } else if (newpass2.getText().toString().equals(newpass1.getText().toString())) {
                                isNew2Valid = true;
                            } else  {
                                Toast.makeText(SettingsActivity.this, "Please enter new password correctly", Toast.LENGTH_LONG).show();
                                isNew2Valid = false;
                            }

                            if (isOldValid && isNew1Valid && isNew2Valid) {
                                reset(old_pass.getText().toString(),newpass1.getText().toString(),newpass2.getText().toString());
                                alert.dismiss();
                            }
                        }
                    });
                }
            });

        }

        public void openLoginActivity(){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        public void reset(String old_pass,String newpass1,String newpass2){
            reference = FirebaseDatabase.getInstance().getReference().child("User");
            FirebaseUser mFirebaseUser=mFirebaseAuth.getCurrentUser();

            reference.orderByChild("userId").equalTo(mFirebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String getOldPassword;
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                            getOldPassword = snapshot.child("password").getValue().toString();

                            if(old_pass.equals(getOldPassword)){
                                reference.child(snapshot.getKey()).child("password").setValue(newpass2);
                                Toast.makeText(SettingsActivity.this, "Password Updated Successfully", Toast.LENGTH_LONG).show();

                            }
                            else{
                                Toast.makeText(SettingsActivity.this, "Old password is not correct", Toast.LENGTH_LONG).show();

                            }
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(SettingsActivity.this, "Password update is not possible. Please try again....", Toast.LENGTH_LONG).show();
                }
            });
        }

    }