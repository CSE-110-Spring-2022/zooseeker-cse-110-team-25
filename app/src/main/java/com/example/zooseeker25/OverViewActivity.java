package com.example.zooseeker25;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OverViewActivity extends AppCompatActivity {

    private SearchResultsViewModel viewModel;
    private RecyclerView recyclerView;
    public OverviewAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_over_view );
        Object[] selectedExhibits = (Object[]) getIntent().getSerializableExtra("route_list");
        Route[] e  = Arrays.copyOf(selectedExhibits, selectedExhibits.length, Route[].class);
        Log.d("OverViewActivity", e[0].exhibit.toString());
        adapter = new OverviewAdapter(selectedExhibits);
        adapter.setHasStableIds(true);
        recyclerView = findViewById(R.id.Overview_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    //you want to get each route that is passed over and display the exhibit and totalDistance;

    public void onDirectionsClicked(View view) {
        Object[] temp = (Object[]) getIntent().getSerializableExtra("route_list");
        Intent intent = new Intent(this, DirectionsActivity.class);
        intent.putExtra("route_list", temp);
        startActivity(intent);
    }

    public void onBackClicked(View view) {
        finish();
    }


}