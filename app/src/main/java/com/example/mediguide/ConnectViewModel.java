package com.example.mediguide;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ConnectViewModel extends ViewModel {
    //Initialise variable
    MutableLiveData<String> mutableLiveData = new MutableLiveData<>();

    //Create set text method
    public void setText(String s) {
        mutableLiveData.setValue(s);
    }

    public MutableLiveData<String> getText() {
        return mutableLiveData;
    }
}
