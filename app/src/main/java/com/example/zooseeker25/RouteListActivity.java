package com.example.zooseeker25;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RouteListActivity extends AppCompatActivity {
    private List<Route> routeList;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_list_view);

        routeList = new ArrayList();
        recyclerView = findViewById(R.id.route_list_recycler);

        setRouteListInfo();
        setAdapter();
    }

    private void setAdapter() {
        RouteListAdapter adapter = new RouteListAdapter();
        adapter.setRouteList(routeList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void setRouteListInfo() {
        List<String> exhibits = new ArrayList<>();
        exhibits.add("gorillas");
        exhibits.add("lions");
        exhibits.add("gators");

        RouteGenerator.populateRouteData(exhibits, this);
        routeList = RouteGenerator.generateFullRoute(exhibits, RouteGenerator.routeData, RouteGenerator.integerLookup);
    }
}