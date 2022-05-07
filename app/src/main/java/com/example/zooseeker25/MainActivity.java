package com.example.zooseeker25;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Intent intent = new Intent(this, Search_Display_Activity.class);
//        startActivity(intent);

        List<Route> routeList;
        List<String> exhibits = new ArrayList<>();
        exhibits.add("lions");
        exhibits.add("gators");
        exhibits.add("arctic_foxes");

        RouteGenerator.populateRouteData(exhibits, this);
        routeList = RouteGenerator.generateFullRoute(exhibits, RouteGenerator.routeData, RouteGenerator.integerLookup);

        Intent intent = new Intent(this, DirectionsActivity.class);
        intent.putExtra("current_exhibit_counter", 0);
        intent.putExtra("route_list", routeList.toArray());
        startActivity(intent);

    }
}