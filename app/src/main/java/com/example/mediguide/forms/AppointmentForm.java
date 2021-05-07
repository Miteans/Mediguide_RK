package com.example.mediguide.forms;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.mediguide.AppointmentActivity;
import com.example.mediguide.R;
import com.example.mediguide.data.Appointment;
import com.example.mediguide.notification.AppointmentNotification;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class AppointmentForm extends Activity {
    TextInputEditText title, hospital, doctor, appointment_date, appointment_time;
    Button appointment_form;
    boolean isTitleValid, isDateValid, isTimeValid;
    DatePickerDialog picker;
    SwitchMaterial set_reminder;

    DatabaseReference reference;
    Appointment appointment;
    ProgressDialog nDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appointment_form);

        title = (TextInputEditText) findViewById(R.id.title_text);
        hospital = (TextInputEditText) findViewById(R.id.hospital_name_text);
        doctor = (TextInputEditText) findViewById(R.id.doctor_name_text);
        appointment_date = (TextInputEditText) findViewById(R.id.date_text);
        appointment_time = (TextInputEditText) findViewById(R.id.time_text);
        appointment_form = (Button) findViewById(R.id.apt_btn);

        appointment = new Appointment();
        reference = FirebaseDatabase.getInstance().getReference().child("Appointment");

        appointment_form.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {

                if(SetValidation()){

                    nDialog = new ProgressDialog(AppointmentForm.this);
                    nDialog.setMessage("Loading..");
                    nDialog.setTitle("Saving Data");
                    nDialog.setIndeterminate(false);
                    nDialog.setCancelable(true);
                    nDialog.show();

                    String title_name = title.getText().toString();
                    String hospital_name = hospital.getText().toString();
                    String doctor_name = doctor.getText().toString();
                    String appointmentDate = appointment_date.getText().toString();
                    String appointmentTime = appointment_time.getText().toString();
                    set_reminder = (SwitchMaterial) findViewById(R.id.set_reminder);

                    appointment.setAppointment_title(title_name);
                    appointment.setHospital_name(hospital_name);
                    appointment.setDoctor_name(doctor_name);
                    appointment.setDate(appointmentDate);
                    appointment.setTime(appointmentTime);
                    if(set_reminder.isChecked())
                        appointment.setIsReminderSet(true);
                    else
                        appointment.setIsReminderSet(false);

                    //User Id set up
                    FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
                    String userId = currentFirebaseUser.getUid();
                    appointment.setUserId(userId);

                    reference.push().setValue(appointment);

                    if(appointment.getIsReminderSet())
                        setAppointmentReminder(appointment.getDate(), appointment.getTime());

                    nDialog.dismiss();
                    Toast.makeText(AppointmentForm.this, "Saved successfully", Toast.LENGTH_LONG).show();
                    clearForm();
                }
            }
        });

        MaterialToolbar mToolbar = (MaterialToolbar) findViewById(R.id.topAppBar);
        mToolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AppointmentForm.this, AppointmentActivity.class);
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
                picker = new DatePickerDialog(AppointmentForm.this,
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
                mTimePicker = new TimePickerDialog(AppointmentForm.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        appointment_time.setText(String.format("%02d:%02d", selectedHour, selectedMinute));

                    }
                }, hour, minute, false);//Yes 24 hour time
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

    public void clearForm(){
        finish();
        Intent intent = new Intent(AppointmentForm.this, AppointmentActivity.class);
        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setAppointmentReminder(String dateString, String timeString){
        Random random = new Random();
        int randomId1, randomId2;
        Date currentDate = new Date();

        randomId1 = random.nextInt();
        randomId2 = random.nextInt();

        while(randomId1 == randomId2){
            randomId1 = random.nextInt();
            randomId2 = random.nextInt();
        }

        Date reminderDate;
        String[] times = timeString.split(":");
        Date previousDay;
        Calendar prevCalendar = Calendar.getInstance();
        Calendar currCalendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        try{
            reminderDate = dateFormat.parse(dateString);
            previousDay = new Date(reminderDate.getTime() - 1);
        }
        catch (Exception e){
            reminderDate = null;
            previousDay = null;
        }

        prevCalendar.setTime(previousDay);
        currCalendar.setTime(reminderDate);

        prevCalendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(String.valueOf(times[0])));
        prevCalendar.set(Calendar.MINUTE, Integer.parseInt(String.valueOf(times[1])));

        currCalendar.set(Calendar.HOUR_OF_DAY, (Integer.parseInt(String.valueOf(times[0])) - 4));
        currCalendar.set(Calendar.MINUTE, Integer.parseInt(String.valueOf(times[1])));

        if(prevCalendar.getTime().compareTo(currentDate) < 0) {
            Intent previousDayIntent = new Intent(this, AppointmentNotification.class);
            previousDayIntent.putExtra("appointmentName", appointment.getAppointment_title());
            previousDayIntent.putExtra("hospitalName", String.valueOf(appointment.getHospital_name()));
            previousDayIntent.putExtra("day", String.valueOf(prevCalendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.ENGLISH)));
            previousDayIntent.putExtra("time", timeString);
            previousDayIntent.putExtra("randomId", randomId1);
            PendingIntent prevPendingIntent = PendingIntent.getBroadcast(this, randomId1, previousDayIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            AlarmManager prevAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            long prevCalendarTimeInMillis = prevCalendar.getTimeInMillis();
            System.out.println(prevCalendarTimeInMillis);
            prevAlarmManager.set(AlarmManager.RTC_WAKEUP, prevCalendarTimeInMillis, prevPendingIntent);
        }

        if(currCalendar.getTime().compareTo(currentDate) <= 0) {
            Intent currentDayIntent = new Intent(this, AppointmentNotification.class);
            currentDayIntent.putExtra("appointmentName", appointment.getAppointment_title());
            currentDayIntent.putExtra("hospitalName", String.valueOf(appointment.getHospital_name()));
            currentDayIntent.putExtra("day", String.valueOf(currCalendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.ENGLISH)));
            currentDayIntent.putExtra("time", timeString);
            currentDayIntent.putExtra("randomId", randomId2);
            PendingIntent currentPendingIntent = PendingIntent.getBroadcast(this, randomId2, currentDayIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            AlarmManager currAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            long currentCalendarTimeMillis = currCalendar.getTimeInMillis();
            System.out.println(currentCalendarTimeMillis);
            currAlarmManager.set(AlarmManager.RTC_WAKEUP, currentCalendarTimeMillis, currentPendingIntent);
        }

    }

}