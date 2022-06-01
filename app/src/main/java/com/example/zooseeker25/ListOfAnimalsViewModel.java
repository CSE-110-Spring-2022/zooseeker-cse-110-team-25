package com.example.zooseeker25;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ListOfAnimalsViewModel extends AndroidViewModel {
    private LiveData<List<String>> animalNames;

    public ListOfAnimalsViewModel(@NonNull Application application) {
        super(application);
        Context context = getApplication().getApplicationContext();
    }

    public void updateText(String animalNames, String newText){
        animalNames = newText;
    }
}
