package com.example.zooseeker25;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import java.util.Observable;
import java.util.Observer;

public class Search_Display_Activity extends AppCompatActivity implements Observer {
    private SearchResultsViewModel viewModel;
    public RecyclerView recyclerView;
    private SearchStorage searchStorage;
    SearchView simpleSearchView;
    TextView titleText;
    TextView listCounter;
    RecyclerView searchResults;
    TextView animalItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_display);

        viewModel = new ViewModelProvider(this)
                .get(SearchResultsViewModel.class);

        searchStorage = new SearchStorage(this);

        //getting all of the elements on the UI
        simpleSearchView = findViewById(R.id.searchView);
        titleText = findViewById(R.id.title_text);
        listCounter = findViewById(R.id.listCounterPlaceHolder);
        searchResults = findViewById(R.id.search_results);

        //initializing the adapter
        SearchResultsAdapter adapter = new SearchResultsAdapter(searchStorage);
        adapter.setHasStableIds(true);
        adapter.setOnAnimalClickedHandler(viewModel::selectAnimal);

        //getting the search results and assigning it to the adapter
        recyclerView = findViewById(R.id.search_results);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.setSearchListItems(searchStorage.getResultsList());

        simpleSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            //called every time you press "enter" inside the search bar
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            //called every time the query inside the search bar changes
            //with newText being the query
            public boolean onQueryTextChange(String newText) {
                if (newText.equals("")) {
                    //simpleSearchView.setTop(titleText.getBottom());
                    titleText.setVisibility(View.VISIBLE);
                    searchResults.setVisibility(View.INVISIBLE);
                } else {
                    //simpleSearchView.setTop(listCounter.getBottom());
                    titleText.setVisibility(View.INVISIBLE);
                    searchResults.setVisibility(View.VISIBLE);
                }
                return true;
            }
        });

        simpleSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                titleText.setVisibility(View.VISIBLE);
                searchResults.setVisibility(View.INVISIBLE);
                return true;
            }
        });

        this.animalItem = this.findViewById(R.id.search_item_text);
        listCounter.setText("0");
    }

    void onAnimalItemClicked(View view){
//        if (((ColorDrawable)textView.getBackground()).getColor() != Color.WHITE) {
//            textView.setBackgroundColor(Color.LTGRAY);
//        } else {
//            textView.setBackgroundColor(Color.WHITE);
//        }
    }

    @Override
    public void update(Observable observable, Object o) {
        listCounter.setText((String)o);
    }
}
