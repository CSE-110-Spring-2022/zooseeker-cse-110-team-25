package com.example.zooseeker25;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
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
    public String returnMessage = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_over_view );
        Object[] selectedExhibits = (Object[]) getIntent().getSerializableExtra("route_list");
        Route[] e  = Arrays.copyOf(selectedExhibits, selectedExhibits.length, Route[].class);
        adapter = new OverviewAdapter(selectedExhibits);
        adapter.setHasStableIds(true);
        recyclerView = findViewById(R.id.Overview_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    //you want to get each route that is passed over and display the exhibit and totalDistance;

    public void onDirectionsClicked(View view) {
        //adapter.clear();
        //recyclerView.setAdapter(null);
        Log.d("OverViewActivity", "onDirectionsClicked");
        Object[] temp = (Object[]) getIntent().getSerializableExtra("route_list");
        Intent intent = new Intent(this, DirectionsActivity.class);
        intent.putExtra("route_list", temp);
        //Log.d("OverViewActivity", temp
        startActivity(intent);
    }


    public void onBackClicked(View view) {
        adapter.clear();
        recyclerView.setAdapter(null);

        finish();
    }


}