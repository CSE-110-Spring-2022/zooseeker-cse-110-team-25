package com.example.zooseeker25;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Set;

public class ListOfAnimalsActivity extends AppCompatActivity {

    private SearchResultsViewModel viewModel;
    public RecyclerView recyclerView;
    public SearchStorage selectedAnimalsStorage;
    private Set<String> selectedAnimals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_animals);
        selectedAnimalsStorage = (SearchStorage) getIntent().getSerializableExtra("selected_list");
        loadSelectedAnimalList();
    }


    @Override
    protected void onDestroy(){
        super.onDestroy();
        saveProfile();
    }

    public void loadSelectedAnimalList(){

        for(String animal:selectedAnimals){

            //assign the animal to the selected animal item in the recycler view

        }

    }

    public void saveProfile(){

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
        //generate route and move to trip overview activity
    }
}