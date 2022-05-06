package com.example.zooseeker25;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ListOfAnimalsActivity extends AppCompatActivity {

    private SearchResultsViewModel viewModel;
    public RecyclerView recyclerView;
    public SearchStorage selectedAnimals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_animals);
        selectedAnimals = (SearchStorage) getIntent().getSerializableExtra("selected_list");
        loadSelectedAnimalList();
    }


    @Override
    protected void onDestroy(){
        super.onDestroy();
        saveProfile();
    }

    public void loadSelectedAnimalList(){






//        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
//
//        String s_name = preferences.getString("key_name", "User not found.");
//        String s_status = preferences.getString("key_status", "Status not found.");
//
//        TextView nameView = (TextView) findViewById(R.id.name_textview);
//        TextView statusView = (TextView) findViewById(R.id.status_textview);
//
//        nameView.setText(s_name);
//        statusView.setText(s_status);

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