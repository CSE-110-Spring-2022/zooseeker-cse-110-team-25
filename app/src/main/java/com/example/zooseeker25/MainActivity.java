package com.example.zooseeker25;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<String> exhibits = new ArrayList<>();
        exhibits.add("lions");
        exhibits.add("elephant_odyssey");
        exhibits.add("arctic_foxes");
        exhibits.add("gorillas");


        RouteGenerator.generateFullRoute(this, exhibits);
        Log.i("ee", "dee");
    }
}