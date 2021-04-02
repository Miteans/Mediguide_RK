package com.example.mediguide;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ramotion.foldingcell.FoldingCell;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder>{
    private Context context;
    private ArrayList<Appointment> appointments;

    public AppointmentAdapter(Context context){
        this.context = context;
    }

    public void setDataToAppointmentAdapter(ArrayList<Appointment> list){
        appointments = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AppointmentAdapter.AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.appointment_card,parent,false);
        return new AppointmentAdapter.AppointmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentAdapter.AppointmentViewHolder holder, int position) {
        Appointment appointment = appointments.get(position);
        holder.medNum.setText(String.valueOf(position+1));
        holder.appointmentTitle.setText(appointment.getAppointment_title());
        holder.hospitalName.setText("Hospital : " +appointment.getHospital_name());
        holder.doctorName.setText("Doctor : " +appointment.getDoctor_name());
        holder.appointmentDate.setText("Date : " +appointment.getDate());
        holder.appointmentTime.setText("Time : " +appointment.getTime());
    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }

    public class AppointmentViewHolder extends RecyclerView.ViewHolder{
        private TextView appointmentTitle, hospitalName, doctorName;
        private TextView appointmentDate, appointmentTime, medNum;

        public AppointmentViewHolder(@NonNull View appointmentView){
            super(appointmentView);

            medNum = appointmentView.findViewById(R.id.medNum);
            appointmentTitle = appointmentView.findViewById(R.id.appointmentTitle);
            hospitalName = appointmentView.findViewById(R.id.hospitalName);
            doctorName = appointmentView.findViewById(R.id.doctorName);
            appointmentDate = appointmentView.findViewById(R.id.appointmentDate);
            appointmentTime = appointmentView.findViewById(R.id.appointmentTime);
        }
    }
}