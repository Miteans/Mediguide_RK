//package com.example.mediguide;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.ramotion.foldingcell.FoldingCell;
//import com.squareup.picasso.Picasso;
//
//import java.util.ArrayList;
//
//public class MedicationAdapter extends RecyclerView.Adapter<MedicationAdapter.MedicineViewHolder>{
//    private Context context;
//    private ArrayList<MedicineInformation> medicines;
//
//    public MedicationAdapter(Context context){
//        this.context = context;
//    }
//
//    public void setDataToMedicationAdapter(ArrayList<MedicineInformation> list){
//        medicines = list;
//        notifyDataSetChanged();
//    }
//
//    @NonNull
//    @Override
//    public MedicineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.med_card,parent,false);
//        return new MedicineViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull MedicineViewHolder holder, int position) {
//        MedicineInformation medicine = medicines.get(position);
//
//        holder.medicineName.setText(medicine.getMedicineName());
//        holder.reasonForIntake.setText(medicine.getReasonForIntake());
//        holder.dosage.setText(medicine.getDosage());
//        holder.frequencyOfMedIntake.setText(medicine.getFrequencyOfMedIntake());
//        holder.setStartDate.setText(medicine.getSetStartDate());
//        holder.duration.setText(medicine.getDuration());
//        holder.otherInstruction.setText(medicine.getOtherInstruction());
//        holder.formOfMedicine.setText(medicine.getFormOfMedicine());
//        holder.instruction.setText(medicine.getInstruction());
//        holder.refillCount.setText(medicine.getRefillCount());
//
//        if(medicine.getEverydayMed())
//            holder.isEverydayMedGrp.setText("Yes");
//        else
//            holder.isEverydayMedGrp.setText("No");
//
//        try {
//            Picasso.with(context)
//                    .load(medicine.getImageUrl())
//                    /*.placeholder(R.drawable.placeholder) //optional
//                    .resize(imgWidth, imgHeight)         //optional
//                    .centerCrop()       */                 //optional
//                    .into(holder.imageView);
//        }
//        catch (Exception e) {
//            System.out.println(e);
//        }
//
//        holder.foldingCell.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                holder.foldingCell.toggle(false);
//            }
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return medicines.size();
//    }
//
//    public class MedicineViewHolder extends RecyclerView.ViewHolder{
//        private FoldingCell foldingCell;
//        private TextView medicineName, reasonForIntake, dosage,frequencyOfMedIntake, intakeTimes[], refillCount;
//        private TextView setStartDate, duration, otherInstruction, formOfMedicine, instruction, isEverydayMedGrp;
//        private ImageView imageView;
//
//        public MedicineViewHolder(@NonNull View medicineView){
//            super(medicineView);
//
//            medicineName = medicineView.findViewById(R.id.medicineName);
//            reasonForIntake = medicineView.findViewById(R.id.reasonForIntake);
//            dosage = medicineView.findViewById(R.id.dosageMed);
//            frequencyOfMedIntake = medicineView.findViewById(R.id.freqOfMedIntake);
//            setStartDate = medicineView.findViewById(R.id.setStartDate);
//            duration = medicineView.findViewById(R.id.durationMed);
//            otherInstruction = medicineView.findViewById(R.id.otherInstructionMed);
//            formOfMedicine = medicineView.findViewById(R.id.formOfMedicine);
//            instruction = medicineView.findViewById(R.id.instructionMed);
//            isEverydayMedGrp = medicineView.findViewById(R.id.isEverydayMed);
//            refillCount = medicineView.findViewById(R.id.refillCountMed);
//            imageView = medicineView.findViewById(R.id.imageViewMed);
//        }
//    }
//}