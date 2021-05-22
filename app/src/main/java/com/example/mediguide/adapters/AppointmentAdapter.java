package com.example.mediguide.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mediguide.AppointmentActivity;
import com.example.mediguide.R;
import com.example.mediguide.data.Appointment;
import com.example.mediguide.forms.AppointmentForm;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder>{
    private final AppointmentActivity appointActivity;
    private Context context;
    private ArrayList<Appointment> appointments;

    public AppointmentAdapter(Context context){
        this.context = context;
        this.appointActivity = (AppointmentActivity) context;
    }

    public void setDataToAppointmentAdapter(ArrayList<Appointment> list){
        appointments = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.appointment_card,parent,false);
        return new AppointmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position) {
        Appointment appointment = appointments.get(position);
        holder.medNum.setText(String.valueOf(position+1));
        holder.appointmentTitle.setText(appointment.getAppointment_title());
        holder.hospitalName.setText("Hospital : " + appointment.getHospital_name());
        holder.doctorName.setText("Doctor : " +appointment.getDoctor_name());
        holder.appointmentDate.setText("Date : " + appointment.getDate());
        holder.appointmentTime.setText("Timinings : " + appointment.getTime());

        holder.deleteAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Confirm");
                builder.setMessage("Are you sure to delete this appointment ?")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Appointment");
                                FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
                                FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();

                                reference.orderByChild("userId").equalTo(mFirebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                if (snapshot.child("appointmentId").getValue().toString().equals(appointment.getAppointmentId())) {
                                                    snapshot.getRef().removeValue();
                                                    appointments.remove(position);
                                                    notifyDataSetChanged();
                                                    Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        }
                                    }
                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                    }
                                });
                            }
                        })
                        .setNegativeButton("Cancel",null);

                builder.show();

            }
        });

        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("inside update---------------------------------");
                String appointment_title = appointment.getAppointment_title();
                String hospital_name = appointment.getHospital_name();
                String doctor_name = appointment.getDoctor_name();
                String date = appointment.getDate();
                String time = appointment.getTime();
                String reminder_date = appointment.getReminderDate();
                String reminder_time = appointment.getReminderTime();
                boolean isReminderSet = appointment.getIsReminderSet();

                System.out.println(appointment.getAppointment_title());
                System.out.println(appointment.getIsReminderSet());
                System.out.println(appointment.getReminderDate());
                System.out.println(appointment.getReminderTime());

                Intent intent = new Intent(context, AppointmentForm.class);
                intent.putExtra("TITLE", appointment_title);
                intent.putExtra("HOSPITAL", hospital_name);
                intent.putExtra("DOCTOR", doctor_name);
                intent.putExtra("DATE", date);
                intent.putExtra("TIME", time);
                intent.putExtra("REM_DATE", reminder_date);
                intent.putExtra("REM_TIME", reminder_time);
                intent.putExtra("IS_REMINDER_SET", isReminderSet);

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }

    public class AppointmentViewHolder extends RecyclerView.ViewHolder{
        private TextView appointmentTitle, hospitalName, doctorName;
        private TextView appointmentDate, appointmentTime, medNum;
        private ImageButton deleteAppointment,update;


        public AppointmentViewHolder(@NonNull View appointmentView){
            super(appointmentView);

            medNum = appointmentView.findViewById(R.id.medNum);
            appointmentTitle = appointmentView.findViewById(R.id.appointmentTitle);
            hospitalName = appointmentView.findViewById(R.id.hospitalName);
            doctorName = appointmentView.findViewById(R.id.doctorName);
            appointmentDate = appointmentView.findViewById(R.id.appointmentDate);
            appointmentTime = appointmentView.findViewById(R.id.appointmentTime);
            deleteAppointment = appointmentView.findViewById(R.id.deleteAppointment);
            update = appointmentView.findViewById(R.id.updateAppointment);
        }
    }
}