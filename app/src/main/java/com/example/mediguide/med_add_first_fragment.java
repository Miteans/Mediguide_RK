package com.example.mediguide;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.textfield.TextInputLayout;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link med_add_first_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class med_add_first_fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String formOfMedicine;
    ImageView imageView;
    Button camera;

    public med_add_first_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment med_add_first_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static med_add_first_fragment newInstance(String param1, String param2) {
        med_add_first_fragment fragment = new med_add_first_fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View  view = inflater.inflate(R.layout.med_add_page_one, container, false);
        String[] options = {"Pill","Insulin","Solution","Powder","Drops","Other"};
        AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) view.findViewById(R.id.form_of_med_dropdown);

        autoCompleteTextView.setAdapter(new ArrayAdapter<String>(view.getContext(),
                R.layout.option_items, options));


        imageView = view.findViewById(R.id.image_view);
        camera = view.findViewById(R.id.open_camera);

        if (ContextCompat.checkSelfPermission(view.getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),
                    new String[] {
                            Manifest.permission.CAMERA
                    },
                    100);
        }

        camera.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 100);
            }
        });

        Button btn = view.findViewById(R.id.second_fragment);

        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final Fragment second = new med_add_second_fragment();
                initiaseFragment(second);
            }
        });

        TextInputLayout dosage = (TextInputLayout) view.findViewById(R.id.dosage);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                formOfMedicine = (String) adapterView.getItemAtPosition(i);
                if(formOfMedicine=="Pill"){
                    dosage.setHelperText("Enter number of pills");
                }
                else if(formOfMedicine=="Drops"){
                    dosage.setHelperText("Enter number of drops");
                }

            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        if (requestCode == 100){
            imageView.setVisibility(View.VISIBLE);
            Bitmap captureImage = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(captureImage);

        }
    }

    public void initiaseFragment(Fragment fragment){
        FragmentManager fm = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}