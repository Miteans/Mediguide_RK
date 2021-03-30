package com.example.mediguide;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.util.Patterns;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUpActivity extends Activity{
    EditText name, email, phone, password, confirm_password;
    Button register;
    boolean isNameValid, isEmailValid, isPhoneValid, isPasswordValid, isConfirmPasswordValid;
    DatabaseReference reference;
    User user;
    long maxid = 0;
    FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_activity);

        mFirebaseAuth=FirebaseAuth.getInstance();

        name = (EditText) findViewById(R.id.PersonName);
        email = (EditText) findViewById(R.id.inputEmail);
        phone = (EditText) findViewById(R.id.editTextPhone);
        password = (EditText) findViewById(R.id.inputPassword);
        confirm_password = (EditText) findViewById(R.id.inputConfirmPassword);
        register = (Button) findViewById(R.id.btnLogin);
        user = new User();
        reference = FirebaseDatabase.getInstance().getReference().child("User");


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(SetValidation()){
                    String userName = name.getText().toString();
                    String userEmail = email.getText().toString();
                    Long userPhone = Long.parseLong((phone.getText().toString().trim()));
                    String userPassword = password.getText().toString();

                    //storing in authentication
                    mFirebaseAuth.createUserWithEmailAndPassword(userEmail,userPassword).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(SignUpActivity.this, "Registration Unsuccessful", Toast.LENGTH_LONG).show();
                            }
                            else{
                                startActivity(new Intent(SignUpActivity.this,HomeActivity.class));
                                Toast.makeText(SignUpActivity.this, "Registered successfully", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                    //storing data in Real Time DB
                    user.setName(userName);
                    user.setEmail(userEmail);
                    user.setPhoneNumber(userPhone);
                    user.setPassword(userPassword);

                    reference.push().setValue(user);
                }
            }
        });
    }

    public Boolean SetValidation() {
        // Check for a valid name.
        if (name.getText().toString().isEmpty()) {
            name.setError(getResources().getString(R.string.name_error));
            isNameValid = false;
        } else  {
            isNameValid = true;
        }

        // Check for a valid email address.
        if (email.getText().toString().isEmpty()) {
            email.setError(getResources().getString(R.string.email_error));
            isEmailValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
            email.setError(getResources().getString(R.string.error_invalid_email));
            isEmailValid = false;
        } else  {
            isEmailValid = true;
        }

        // Check for a valid phone number.
        if (phone.getText().toString().isEmpty()) {
            phone.setError(getResources().getString(R.string.phone_error));
            isPhoneValid = false;
        } else  {
            isPhoneValid = true;
        }

        // Check for a valid password.
        if (password.getText().toString().isEmpty()) {
            password.setError(getResources().getString(R.string.password_error));
            isPasswordValid = false;
        } else if (password.getText().length() < 6) {
            password.setError(getResources().getString(R.string.error_invalid_password));
            isPasswordValid = false;
        } else  {
            isPasswordValid = true;
        }

        // Check for a confirmation of password.
        if (confirm_password.getText().toString().isEmpty()) {
            confirm_password.setError(getResources().getString(R.string.confirm_password_error));
            isConfirmPasswordValid = false;
        }
        else if (!(confirm_password.getText().toString().equals(password.getText().toString()))) {
            confirm_password.setError(getResources().getString(R.string.error_password_not_confirmed));
            isConfirmPasswordValid = false;
        }
        else  {
            isConfirmPasswordValid = true;
        }

        if (isNameValid && isEmailValid && isPhoneValid && isPasswordValid && isConfirmPasswordValid) {
            return true;
        }
        return false;
    }
}