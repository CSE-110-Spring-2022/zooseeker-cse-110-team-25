package com.example.zooseeker25;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("%s", "testing");

        SearchView simpleSearchView = findViewById(R.id.searchView);
        TextView logoText = findViewById(R.id.title_text);
        TextView listCounter = findViewById(R.id.listCounterPlaceHolder);

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