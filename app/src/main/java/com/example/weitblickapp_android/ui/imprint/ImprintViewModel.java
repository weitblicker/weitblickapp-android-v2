package com.example.weitblickapp_android.ui.imprint;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ImprintViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ImprintViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Imprint fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
