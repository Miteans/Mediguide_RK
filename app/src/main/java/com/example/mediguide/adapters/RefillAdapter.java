package com.example.mediguide.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mediguide.DeviceConnectActivity;
import com.example.mediguide.R;
import com.example.mediguide.RefillActivity;
import com.example.mediguide.data.MedicineInformation;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class  RefillAdapter extends RecyclerView.Adapter<RefillAdapter.RefillViewHolder>{
    private Context context;
    private ArrayList<MedicineInformation> activeMedicines;
    private ArrayList<RefillAdapter.RefillViewHolder> holders = new ArrayList<>();
    private RefillActivity refillActivity;


    public RefillAdapter(Context context){
        this.context = context;
        this.refillActivity = (RefillActivity) context;

    }

    public void setDataToRefillAdapter(ArrayList<MedicineInformation> list){
        activeMedicines = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RefillAdapter.RefillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.refill,parent,false);
        return new RefillAdapter.RefillViewHolder(view, refillActivity);
    }
    @Override
    public void onBindViewHolder(@NonNull RefillAdapter.RefillViewHolder holder, int position) {
        MedicineInformation medicine = activeMedicines.get(position);
        holder.medicineName.setText(medicine.getMedicineName());
        holder.refillCount.setText(medicine.getRefillCount());



    }

    @Override
    public int getItemCount() {
        return activeMedicines.size();
    }


    public class RefillViewHolder extends RecyclerView.ViewHolder {
        private TextView medicineName, refillCount;
        private RefillActivity refillActivity;

        public RefillViewHolder(@NonNull View medicineView, RefillActivity refillActivity) {
            super(medicineView);

            medicineName = medicineView.findViewById(R.id.medicineName);
            this.refillActivity = refillActivity;
            refillCount = medicineView.findViewById(R.id.refillcount);

        }
    }

}