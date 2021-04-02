package com.example.mediguide;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ramotion.foldingcell.FoldingCell;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MedicationAdapter extends RecyclerView.Adapter<MedicationAdapter.MedicineViewHolder>{
    private Context context;
    private ArrayList<MedicineInformation> medicines;

    public MedicationAdapter(Context context){
        this.context = context;
    }

    public void setDataToMedicationAdapter(ArrayList<MedicineInformation> list){
        medicines = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MedicineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.med_card,parent,false);
        return new MedicineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicineViewHolder holder, int position) {
        MedicineInformation medicine = medicines.get(position);
        holder.medNum.setText(String.valueOf(position+1));
        holder.medicineName.setText(medicine.getMedicineName());
        holder.medicineNameTitle.setText(medicine.getMedicineName());
        holder.reasonForIntake.setText(medicine.getReasonForIntake());
        holder.dosage.setText(String.valueOf(medicine.getDosage()));
        holder.dosageTitle.setText(String.valueOf(medicine.getDosage()));
        holder.frequencyOfMedIntake.setText(medicine.getFrequencyOfMedIntake() + " Times a day");
        holder.setStartDate.setText(medicine.getSetStartDate());
        holder.duration.setText(medicine.getDuration() + " Days");
        holder.otherInstruction.setText(medicine.getOtherInstruction());
        holder.formOfMedicine.setText(medicine.getFormOfMedicine());
        if(!medicine.getInstruction().equals("Doesn't matter"))
            holder.instruction.setText("Take medicine " + medicine.getInstruction());
        else
            holder.instruction.setText(medicine.getInstruction());
        holder.instructionTitle.setText(medicine.getInstruction());
        //holder.refillCount.setText(medicine.getRefillCount());

        if(medicine.getEverydayMed())
            holder.isEverydayMedGrp.setText("Yes");
        else
            holder.isEverydayMedGrp.setText("No");

        try {
            Picasso.with(context)
                    .load(medicine.getImageUrl())
                    /*.placeholder(R.drawable.placeholder) //optional*/
                    .resize(158, 108)         //optional
                    /*.centerCrop()       */                 //optional
                    .into(holder.imageView);
        }
        catch (Exception e) {
            System.out.println(e);
        }

        for(String msg: medicine.getIntakeTimes()){
            holder.intakeTimes.addView(createTextView(msg));
        }

        holder.foldingCell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.foldingCell.toggle(false);
            }
        });
    }

    @Override
    public int getItemCount() {
        return medicines.size();
    }

    private TextView createTextView(String msg){
        TextView textView = new TextView(context);
        textView.setPadding(5, 0, 5, 0);
        textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
        textView.setTextColor(context.getResources().getColor(R.color.primary));
        textView.setTextSize(15);
        textView.setText(msg);
        return textView;
    }

    public class MedicineViewHolder extends RecyclerView.ViewHolder{
        private FoldingCell foldingCell;
        private TextView medicineName, medicineNameTitle, reasonForIntake, dosage,frequencyOfMedIntake, refillCount, medNum;
        private TextView setStartDate, dosageTitle, instructionTitle, duration, otherInstruction, formOfMedicine, instruction, isEverydayMedGrp;
        private ImageView imageView;
        private LinearLayout intakeTimes;

        public MedicineViewHolder(@NonNull View medicineView){
            super(medicineView);

            foldingCell = medicineView.findViewById(R.id.folding_cell);
            medNum = medicineView.findViewById(R.id.medNum);
            medicineNameTitle = medicineView.findViewById(R.id.medicineNameTitle);
            reasonForIntake = medicineView.findViewById(R.id.reasonForIntake);
            dosageTitle = medicineView.findViewById(R.id.dosageMedTitle);
            frequencyOfMedIntake = medicineView.findViewById(R.id.freqOfMedIntake);
            setStartDate = medicineView.findViewById(R.id.setStartDate);
            duration = medicineView.findViewById(R.id.durationMed);
            otherInstruction = medicineView.findViewById(R.id.otherInstructionMed);
            formOfMedicine = medicineView.findViewById(R.id.formOfMedicine);
            instructionTitle = medicineView.findViewById(R.id.instructionMedTitle);
            isEverydayMedGrp = medicineView.findViewById(R.id.isEverydayMed);
            //refillCount = medicineView.findViewById(R.id.refillCountMed);
            imageView = medicineView.findViewById(R.id.imageView);
            medicineName = medicineView.findViewById(R.id.medicineName);
            dosage = medicineView.findViewById(R.id.dosageMed);
            instruction = medicineView.findViewById(R.id.instructionMed);
            intakeTimes = medicineView.findViewById(R.id.intakeTimes);

        }
    }
}