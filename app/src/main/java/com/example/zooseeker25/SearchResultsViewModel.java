package com.example.zooseeker25;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class SearchResultsViewModel extends AndroidViewModel {

    public SearchResultsViewModel(@NonNull Application application) {
        super(application);
    }

    //used to update the selected field on a SearchResultsItem when its clicked
    public void selectAnimal(SearchResultsItem searchResultItem) {
        searchResultItem.selected = true;
    }
}
