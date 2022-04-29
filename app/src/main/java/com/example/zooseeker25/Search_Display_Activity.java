package com.example.zooseeker25;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

public class Search_Display_Activity extends AppCompatActivity {
    List<String> suggestions = Arrays.asList(new String[]{"Gorillas", "Alligators", "Lions", "Elephant Odyssey", "Arctic Foxes"});

    //Cursor cursor =

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_display);

        SearchView simpleSearchView = findViewById(R.id.searchView);
        TextView logoText = findViewById(R.id.title_text);
        TextView listCounter = findViewById(R.id.listCounterPlaceHolder);

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
                } else {
                    simpleSearchView.setTop(listCounter.getBottom());
                    logoText.setVisibility(View.INVISIBLE);
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
