package com.example.zooseeker25;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
    private List<Route> routeList;
    private List<String> exhibits;
    private String[] animalIds;
    ListOfAnimalsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_animals);
        Object[] animalNames = (Object[])getIntent().getSerializableExtra("selected_list_names");
        Object[] tempIds = (Object[])getIntent().getSerializableExtra("selected_list_ids");
        selectedAnimalsNameStorage = Arrays.copyOf(animalNames, animalNames.length, String[].class);
        adapter = new ListOfAnimalsAdapter(selectedAnimalsNameStorage);
        adapter.setHasStableIds(true);
        animalIds = Arrays.copyOf(tempIds, tempIds.length, String[].class);
        recyclerView = findViewById(R.id.search_results);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        saveProfile();
    }


    public void saveProfile(){
        //TODO - data persistence once app is closed
//        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//
//        TextView nameView = (TextView) findViewById(R.id.name_textview);
//        TextView statusView = (TextView) findViewById(R.id.status_textview);
//
//
//        editor.putString("key_name", nameView.getText().toString());
//        editor.putString("key_status", statusView.getText().toString());
//
//        editor.apply();
    }

    public void onGoBackClicked(View view) {
        finish();
    }

    public void onRouteGeneratedClicked(View view) {
        exhibits = new ArrayList<>(Arrays.asList(animalIds));
        RouteGenerator.populateRouteData(exhibits, this);
        routeList = RouteGenerator.generateFullRoute(exhibits, RouteGenerator.routeData, RouteGenerator.integerLookup);
        Intent intent = new Intent(this, OverViewActivity.class);
        intent.putExtra("route_list", routeList.toArray());
        startActivity(intent);
    }
}