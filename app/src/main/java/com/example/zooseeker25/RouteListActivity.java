package com.example.zooseeker25;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Rect;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RouteListActivity extends AppCompatActivity {
    private List<Route> routeList;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_list);

        routeList = Collections.emptyList();
        recyclerView = findViewById(R.id.route_list_recycler);

        setRouteListInfo();
        setAdapter();
    }

    private void setAdapter() {
        RouteListAdapter adapter = new RouteListAdapter();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    private void setRouteListInfo() {
        routeList.add(new Route("p", "left 100 ft"));
        routeList.add(new Route("p", "left 100 ft"));
        routeList.add(new Route("p", "left 100 ft"));
    }
}