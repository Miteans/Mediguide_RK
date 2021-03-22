package com.example.mediguide;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputLayout;


public class MedAddActivity extends AppCompatActivity {
    MaterialToolbar mToolbar;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.med_add_activity);

        final Fragment first = new med_add_first_fragment();
        final Fragment second = new med_add_second_fragment();
        final Fragment third = new med_add_third_fragment();

        final View view = findViewById(R.id.circle_one);
        final View view2 = findViewById(R.id.circle_two);
        final View view3 = findViewById(R.id.circle_three);
        view.setBackgroundResource(R.drawable.ic_baseline_lens_24);
        initiliseFragment(first);

        MaterialToolbar mToolbar = (MaterialToolbar) findViewById(R.id.topAppBar);
        mToolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MedAddActivity.this,HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setBackgroundResource(R.drawable.ic_baseline_lens_24);
                view2.setBackgroundResource(R.drawable.ic_baseline_radio_button_unchecked_24);
                view3.setBackgroundResource(R.drawable.ic_baseline_radio_button_unchecked_24);
                initiliseFragment(first);
            }
        });

        view2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view2.setBackgroundResource(R.drawable.ic_baseline_lens_24);
                view.setBackgroundResource(R.drawable.ic_baseline_radio_button_unchecked_24);
                view3.setBackgroundResource(R.drawable.ic_baseline_radio_button_unchecked_24);
                initiliseFragment(second);
            }
        });

        view3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view3.setBackgroundResource(R.drawable.ic_baseline_lens_24);
                view.setBackgroundResource(R.drawable.ic_baseline_radio_button_unchecked_24);
                view2.setBackgroundResource(R.drawable.ic_baseline_radio_button_unchecked_24);
                initiliseFragment(third);
            }
        });
    }

    public void initiliseFragment(Fragment fragmentToBeInitilised){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment, fragmentToBeInitilised);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


}