package com.example.zooseeker25;


import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import androidx.appcompat.app.AlertDialog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
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

    private Boolean isMock = Boolean.parseBoolean( null );



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
        setIsMock();
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


        intent.putExtra("route_list", temp);
        intent.putExtra( "use_mock_location", isMock );

        startActivity(intent);


    }

    //Checking user location mock or live
    public void setIsMock(){

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this)
                .setTitle("Mock Location?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        isMock = true;
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        isMock = false;

                    }
                });
        AlertDialog alert = alertBuilder.create();
        alert.show();

    }


    public void onBackClicked(View view) {
        adapter.clear();
        recyclerView.setAdapter(null);

        finish();
    }


}