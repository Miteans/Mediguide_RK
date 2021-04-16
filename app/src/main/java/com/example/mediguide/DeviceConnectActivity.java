package com.example.mediguide;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;

public class DeviceConnectActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView tvEmpty;
    ArrayList<String> arrayList = new ArrayList<>();
    DeviceConnectAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.device_activity);
        super.onCreate(savedInstanceState);

        //Assign variable
        recyclerView = findViewById(R.id.recycler_view);
        tvEmpty = findViewById(R.id.tv_empty);

        //Add values to array list
        arrayList.addAll(Arrays.asList("One","Two","Three","Four","Five"));

        //set layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //initialise adapter
        adapter = new DeviceConnectAdapter(this,arrayList,tvEmpty);
        //set adapter
        recyclerView.setAdapter(adapter);
    }
}




