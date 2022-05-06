package com.example.zooseeker25;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class SearchResultsViewModel extends AndroidViewModel {

    private LiveData<List<SearchResultsItem>> SearchResultsItem;

    public SearchResultsViewModel(@NonNull Application application) {
        super(application);
    }

    public void selectAnimal(SearchResultsItem searchResultItem) {
        searchResultItem.selected = true;
    }
}
