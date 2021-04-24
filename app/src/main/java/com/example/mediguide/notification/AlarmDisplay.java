package com.example.mediguide.notification;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mediguide.R;
import com.squareup.picasso.Picasso;

public class AlarmDisplay extends AppCompatActivity {
    TextView time, day, medicineName, instruction, otherInstruction, dosage;
    ImageView imageView;
    String dayString, timeString, medicineNameString, dosageString, instructionString, otherInstructionString, imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_display);

        medicineNameString = (String) getIntent().getStringExtra("medicineName");
        dosageString = (String) getIntent().getStringExtra("dosage");
        instructionString = (String) getIntent().getStringExtra("instruction");
        otherInstructionString = (String) getIntent().getStringExtra("otherInstruction");
        dayString = getIntent().getStringExtra("day");
        timeString = getIntent().getStringExtra("time");
        imageUrl = getIntent().getStringExtra("image");

        time = findViewById(R.id.time);
        day = findViewById(R.id.day);
        medicineName = findViewById(R.id.medicineName);
        instruction = findViewById(R.id.instruction);
        otherInstruction = findViewById(R.id.otherInstruction);
        dosage = findViewById(R.id.dosage);
        imageView = findViewById(R.id.imageView1);

        medicineName.setText(medicineNameString);
        instruction.setText("Instruction : " + instructionString);
        otherInstruction.setText("Other Instruction : " + otherInstructionString);
        dosage.setText("Dosage : " + dosageString);
        time.setText(timeString);
        day.setText(dayString);

        try {
            Picasso.with(this)
                    .load(imageUrl)
                    /*.placeholder(R.drawable.placeholder) //optional*/
                    .resize(320, 170)         //optional
                    /*.centerCrop()       */                 //optional
                    .into(imageView);
        }
        catch (Exception e) {
            System.out.println(e);
        }

    }
}