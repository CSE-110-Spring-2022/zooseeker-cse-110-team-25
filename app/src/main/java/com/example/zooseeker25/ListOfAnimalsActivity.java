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
    ListOfAnimalsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("ListOfAnimalsActivity", "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_animals);
        Object[] animalNames = (Object[])getIntent().getSerializableExtra("selected_list_names");

        Object[] tempIds = (Object[])getIntent().getSerializableExtra("selected_list_ids");
        selectedAnimalsNameStorage = Arrays.copyOf(animalNames, animalNames.length, String[].class);
        Log.d("ListOfAnimalsActivity", selectedAnimalsNameStorage[0]);

        //viewModel = new ViewModelProvider(this).get(ListOfAnimalsViewModel.class);

        adapter = new ListOfAnimalsAdapter(selectedAnimalsNameStorage);
        adapter.setHasStableIds(true);
        //adapter.setOnTextEditedHandler(viewModel::updateText);

        animalIds = Arrays.copyOf(tempIds, tempIds.length, String[].class);
        recyclerView = findViewById(R.id.search_results);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        updateUI();
    }

    private void updateUI(){

    }

    public void onGoBackClicked(View view) {
        finish();
    }

    public void onRouteGeneratedClicked(View view) {
        exhibits = new ArrayList<>(Arrays.asList(animalIds));
        Log.d("ListOfAnimalsActivity Clicked",exhibits.get(0));
        RouteGenerator.populateRouteData(exhibits, this);
        detailedRouteList = RouteGenerator.generateFullRoute(exhibits, RouteGenerator.routeData, RouteGenerator.integerLookup);
        Intent intent = new Intent(this, OverViewActivity.class);

        intent.putExtra("route_list", routeList.toArray());


        intent.putExtra("route_list", detailedRouteList.toArray());

        startActivity(intent);
    }
}