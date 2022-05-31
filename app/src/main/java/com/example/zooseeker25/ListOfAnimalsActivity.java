package com.example.zooseeker25;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class ListOfAnimalsActivity extends AppCompatActivity {

    private SearchResultsViewModel viewModel;
    private RecyclerView recyclerView;
    private String[] selectedAnimalsNameStorage;
    private List<Route> detailedRouteList;
    private List<Route> briefRouteList;
    private List<String> exhibits;
    private String[] animalIds;
    private ListOfAnimalsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("ListOfAnimalsActivity", "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_animals);

        initializeAnimalStorage();
        initializeAnimalIds();

        initializeAdapter();

        setRecyclerView();
    }

    public void initializeAnimalStorage(){
        Object[] animalNames = (Object[])getIntent().getSerializableExtra("selected_list_names");
        selectedAnimalsNameStorage = Arrays.copyOf(animalNames, animalNames.length, String[].class);
    }

    public void initializeAnimalIds(){
        Object[] tempIds = (Object[])getIntent().getSerializableExtra("selected_list_ids");
        animalIds = Arrays.copyOf(tempIds, tempIds.length, String[].class);
    }

    public void initializeAdapter(){
        adapter = new ListOfAnimalsAdapter(selectedAnimalsNameStorage);
        adapter.setHasStableIds(true);
    }

    public void setRecyclerView(){
        recyclerView = findViewById(R.id.search_results);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    public void onGoBackClicked(View view) {
        finish();
    }

    public void onRouteGeneratedClicked(View view) {
        exhibits = new ArrayList<>(Arrays.asList(animalIds));
        RouteGenerator.populateRouteData(exhibits, this);

        detailedRouteList = RouteGenerator.generateFullRoute(exhibits, RouteGenerator.routeData, RouteGenerator.integerLookup);
        Intent intent = new Intent(this, OverViewActivity.class);
        intent.putExtra("route_list", detailedRouteList.toArray());

        startActivity(intent);
    }
}