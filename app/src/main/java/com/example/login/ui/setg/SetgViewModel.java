package com.example.login.ui.setg;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SetgViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public SetgViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is set geofence fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}