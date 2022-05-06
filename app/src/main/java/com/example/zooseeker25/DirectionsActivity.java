package com.example.zooseeker25;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class DirectionsActivity extends AppCompatActivity {
    private List<Route> routeList;
    private int currentExhibitCounter;
    private Route currRoute;

    private RecyclerView recyclerView;
    private TextView exhibitTitleText;
    private TextView exhibitCounterText;
    private Button prevBtn;
    private Button nextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directions);

        this.prevBtn = findViewById(R.id.prev_button);
        this.nextBtn = findViewById(R.id.next_button);
        this.exhibitCounterText = findViewById(R.id.direction_exhibit_counter);
        this.exhibitTitleText = findViewById(R.id.direction_exhibit_title);
        this.recyclerView = findViewById(R.id.directions_list_view);

        Bundle extras = getIntent().getExtras();
        this.currentExhibitCounter = extras.getInt("current_exhibit_counter");
        this.routeList = (List<Route>) extras.getSerializable("route_list");

        this.currRoute = routeList.get(currentExhibitCounter);

        this.exhibitCounterText.setText(routeList.size()-currentExhibitCounter-1);
        this.exhibitTitleText.setText(this.currRoute.end);

        setPrevBtn();
        setNextBtn();
        setAdapter();
    }

    private void setPrevBtn() {
        if (currentExhibitCounter != 0) {
            this.prevBtn.setVisibility(View.VISIBLE);
            this.prevBtn.setText("Previous");
        }
    }

    private void setNextBtn() {
        if (this.currentExhibitCounter+1 < this.routeList.size()-1) {
            this.nextBtn.setText(this.routeList.get(this.currentExhibitCounter + 1).end);
        } else {
            this.nextBtn.setText("Finish");
        }
    }

    private void setAdapter() {
        DirectionsAdapter adapter = new DirectionsAdapter();
        adapter.setDirectionsList(this.routeList.get(this.currentExhibitCounter).directions);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        this.recyclerView.setLayoutManager(layoutManager);
        this.recyclerView.setAdapter(adapter);
    }

    public void onNextExhibitClicked(View view) {
        Intent intent = new Intent(this, DirectionsActivity.class);
        intent.putExtra("current_exhibit_counter", currentExhibitCounter+1);
        intent.putExtra("route_list", (Parcelable) routeList);

        startActivity(intent);
    }

    public void onPrevExhibitClicked(View view) {
        finish();
    }
}