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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DirectionsActivity extends AppCompatActivity {
    private Route[] routeList;
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

        this.prevBtn = (Button) findViewById(R.id.prev_button);
        this.nextBtn = (Button) findViewById(R.id.next_button);
        this.exhibitCounterText = (TextView) findViewById(R.id.direction_exhibit_counter);
        this.exhibitTitleText = (TextView) findViewById(R.id.direction_exhibit_title);
        this.recyclerView = (RecyclerView) findViewById(R.id.directions_list_view);

        this.currentExhibitCounter = getIntent().getExtras().getInt("current_exhibit_counter");
        Object[] temp = (Object[]) getIntent().getSerializableExtra("route_list");
        this.routeList = Arrays.copyOf(temp, temp.length, Route[].class);

        updateUI();
    }


    private void updateUI() {
        this.currRoute = routeList[currentExhibitCounter];
        this.exhibitCounterText.setText(String.valueOf(routeList.length-currentExhibitCounter-1));
        this.exhibitTitleText.setText(this.currRoute.exhibit);

        setPrevBtn();
        setNextBtn();
        setAdapter();
    }

    private void setPrevBtn() {
        if (currentExhibitCounter != 0) {
            this.prevBtn.setVisibility(View.VISIBLE);
            this.prevBtn.setText("Previous");
        } else {
            this.prevBtn.setVisibility(View.INVISIBLE);
        }
    }

    private void setNextBtn() {
        if (this.currentExhibitCounter < this.routeList.length-1) {
            Route nextExhibit = routeList[currentExhibitCounter+1];
            String nextBtnText =
                    nextExhibit.exhibit + "\n" + (int) nextExhibit.totalDistance + " m";
            this.nextBtn.setText(nextBtnText);
        } else {
            this.nextBtn.setText("Finish");
        }
    }

    private void setAdapter() {
        DirectionsAdapter adapter = new DirectionsAdapter();
        adapter.setDirectionsList(this.currRoute.directions);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        this.recyclerView.setLayoutManager(layoutManager);
        this.recyclerView.setAdapter(adapter);
    }

    public void onNextExhibitClicked(View view) {
        if (this.currentExhibitCounter < this.routeList.length-1) {
            this.currentExhibitCounter++;
            updateUI();
        }
    }

    public void onPrevExhibitClicked(View view) {
        this.currentExhibitCounter--;
        updateUI();
    }
}