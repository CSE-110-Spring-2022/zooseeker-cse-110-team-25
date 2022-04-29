package com.example.zooseeker25;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Search_Display_Activity extends AppCompatActivity {
    String[] mockData = {"Gorillas", "Alligators", "Lions", "Elephant Odyssey", "Arctic Foxes"};
    List<SearchResultsItem> suggestions = new ArrayList<>();

    public RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_display);

        SearchView simpleSearchView = findViewById(R.id.searchView);
        TextView logoText = findViewById(R.id.title_text);
        TextView listCounter = findViewById(R.id.listCounterPlaceHolder);
        RecyclerView searchResults = findViewById(R.id.search_results);

        SearchResultsAdapter adapter = new SearchResultsAdapter();
        adapter.setHasStableIds(true);

        recyclerView = findViewById(R.id.search_results);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        for (String s : mockData) {
            suggestions.add(new SearchResultsItem(s, false, 0));
        }

        adapter.setSearchListItems(suggestions);

        simpleSearchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        simpleSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.equals("")) {
                    simpleSearchView.setBottom(300);
                    logoText.setVisibility(View.VISIBLE);
                    searchResults.setVisibility(View.INVISIBLE);
                } else {
                    simpleSearchView.setTop(listCounter.getBottom());
                    logoText.setVisibility(View.INVISIBLE);
                    searchResults.setVisibility(View.VISIBLE);
                }
                return false;
            }
        });
    }
}

//searchView.setOnClickListener(View.OnClickListener {
//        suggestions.forEachIndexed { index, suggestion ->
//        if (suggestion.matches("[a-zA-Z]+")) {
//        cursor.addRow(arrayOf(index, suggestion))
