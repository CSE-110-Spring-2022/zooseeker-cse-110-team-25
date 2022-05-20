package com.example.zooseeker25;

import androidx.annotation.NonNull;

public class SearchResultsItem {
    public long id = 0;
    public String name;
    public boolean selected;

    SearchResultsItem(@NonNull String name, boolean selected){
        this.name = name;
        this.selected = selected;
    }
}
