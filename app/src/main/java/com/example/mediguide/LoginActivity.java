package com.example.mediguide;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends Activity {
    TextView link;
    EditText email, password;
    Button login;
    FirebaseAuth mFirebaseAuth;

    boolean isEmailValid, isPasswordValid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        mFirebaseAuth = FirebaseAuth.getInstance();

        link = (TextView) findViewById(R.id.gotoRegister);
        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSignUpActivity();
            }
        });

        email = (EditText) findViewById(R.id.inputEmail);
        password = (EditText) findViewById(R.id.inputPassword);
        login = (Button) findViewById(R.id.btnLogin);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SetValidation()) {
                    String userEmail = email.getText().toString();
                    String userPassword = password.getText().toString();
                    mFirebaseAuth.signInWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "Incorrect Username or Password", Toast.LENGTH_LONG).show();
                            } else {
                                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                Toast.makeText(LoginActivity.this, "Logged in Successfully", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                }
            }
        });
    }
    public void openSignUpActivity() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    public Boolean SetValidation() {
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

        if (isEmailValid && isPasswordValid) {
            return true;
        }
        else{
            return false;
        }

    }
}
