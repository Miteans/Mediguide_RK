//package com.example.mediguide;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//import java.util.ArrayList;
//public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolder>{
//    private Context context;
//    private ArrayList<MedicineInformation> todayMedicines;
//    public HomeAdapter(Context context){
//        this.context = context;
//    }
//    public void setDataToMedicationAdapter(ArrayList<MedicineInformation> list){
//        todayMedicines = list;
//        notifyDataSetChanged();
//    }
//    @NonNull
//    @Override
//    public HomeAdapter.HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_card,parent,false);
//        return new HomeAdapter.HomeViewHolder(view);
//    }
//    @Override
//    public void onBindViewHolder(@NonNull HomeAdapter.HomeViewHolder holder, int position) {
//        MedicineInformation medicine = todayMedicines.get(position);
//        holder.medicineName.setText(medicine.getMedicineName());
//        holder.reasonForIntake.setText(medicine.getReasonForIntake());
//        holder.dosage.setText(medicine.getDosage());
//        holder.frequencyOfMedIntake.setText(medicine.getFrequencyOfMedIntake());
//        holder.otherInstruction.setText(medicine.getOtherInstruction());
//        holder.formOfMedicine.setText(medicine.getFormOfMedicine());
//        holder.instruction.setText(medicine.getInstruction());
//    }
//    @Override
//    public int getItemCount() {
//        return todayMedicines.size();
//    }
//    public class HomeViewHolder extends RecyclerView.ViewHolder{
//        private TextView medicineName, reasonForIntake, dosage,frequencyOfMedIntake, intakeTimes[];
//        private TextView otherInstruction, formOfMedicine, instruction;
//        public HomeViewHolder(@NonNull View homeView){
//            super(homeView);
//            medicineName = homeView.findViewById(R.id.medicineName);
//            reasonForIntake = homeView.findViewById(R.id.reasonForIntake);
//            dosage = homeView.findViewById(R.id.dosageMed);
//            frequencyOfMedIntake = homeView.findViewById(R.id.freqOfMedIntake);
//            otherInstruction = homeView.findViewById(R.id.otherInstructionMed);
//            formOfMedicine =homeView.findViewById(R.id.formOfMedicine);
//            instruction = homeView.findViewById(R.id.instructionMed);
//        }
//    }
//}
//
//public class HomeAdapter{
//
//}