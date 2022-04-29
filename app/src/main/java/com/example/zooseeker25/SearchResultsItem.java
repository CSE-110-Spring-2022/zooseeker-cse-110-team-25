package com.example.zooseeker25;

import androidx.annotation.NonNull;

public class SearchResultsItem {
    public long id = 0;
    public String name;
    public boolean selected;
    public int order;

    SearchResultsItem(@NonNull String name, boolean selected, int order){
        this.name = name;
        this.selected = selected;
        this.order = order;
    }

}
