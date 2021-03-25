package com.example.mediguide;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.SimpleTimeZone;

public class AppointmentActivity extends Activity {
    TextInputEditText title, hospital, doctor, appointment_date, appointment_time;
    Button appointment_form;
    boolean isTitleValid, isDateValid, isTimeValid;
    DatePickerDialog picker;
    TextInputEditText eTextLayout;
    TextView time;
    TimePicker firstTimePicker;

    DatabaseReference reference;
    Appointment appointment;
    long maxid = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appointment_activity);

        title = (TextInputEditText) findViewById(R.id.title_text);
        hospital = (TextInputEditText) findViewById(R.id.hospital_name_text);
        doctor = (TextInputEditText) findViewById(R.id.doctor_name_text);
        appointment_date = (TextInputEditText) findViewById(R.id.date_text);
        appointment_time = (TextInputEditText) findViewById(R.id.time_text);
        appointment_form = (Button) findViewById(R.id.apt_btn);

        appointment = new Appointment();
        reference = FirebaseDatabase.getInstance().getReference().child("Appointment");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                    maxid = (dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        appointment_form.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(SetValidation()){
                    try{
                        String str1 = appointment_date.getText().toString();
                        DateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");
                        Date date = format1.parse(str1);

                        String str2 = appointment_time.getText().toString();
                        String str3 = str2 + ":00";
                        DateFormat format2 = new SimpleDateFormat("HH:mm:ss");
                        Date time = format2.parse(str3);

                        String title_name = title.getText().toString();
                        String hospital_name = hospital.getText().toString();
                        String doctor_name = doctor.getText().toString();
                        Date get_date = date;
                        Date get_time = time;

                        appointment.setAppointment_title(title_name);
                        appointment.setHospital_name(hospital_name);
                        appointment.setDoctor_name(doctor_name);
                        appointment.setDate(get_date);
                        appointment.setTime(get_time);

                    }
                    catch (Exception e) {
                        System.out.println(e);
                    }

                    reference.child(String.valueOf(maxid+1)).setValue(appointment);

                    Toast.makeText(AppointmentActivity.this, "Saved successfully", Toast.LENGTH_LONG).show();

                }
            }
        });

        MaterialToolbar mToolbar = (MaterialToolbar) findViewById(R.id.topAppBar);
        mToolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AppointmentActivity.this,HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        appointment_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(AppointmentActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                appointment_date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        appointment_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(AppointmentActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        appointment_time.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

    }

    public Boolean SetValidation() {

        if (title.getText().toString().isEmpty()) {
            title.setError(getResources().getString(R.string.name_error));
            isTitleValid = false;
        } else  {
            isTitleValid = true;
        }

        if (appointment_date.getText().toString().isEmpty()) {
            appointment_date.setError(getResources().getString(R.string.email_error));
            isDateValid = false;
        } else  {
            isDateValid = true;
        }

        if (appointment_time.getText().toString().isEmpty()) {
            appointment_time.setError(getResources().getString(R.string.phone_error));
            isTimeValid = false;
        } else  {
            isTimeValid = true;
        }


        if (isTitleValid && isDateValid && isTimeValid) {
            return true;
        }
        return false;
    }

}