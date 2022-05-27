package com.example.zooseeker25;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
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
    private int currentExhibitCounter = 0;
    private Route currRoute;

    private RecyclerView recyclerView;
    private TextView exhibitTitleText;
    private TextView exhibitCounterText;
    private Button prevBtn;
    private Button nextBtn;
    private Button skipBtn;

    private int detailedDirections = 0;

    //temp behavior
    private TextView tempText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directions);

        this.prevBtn = (Button) findViewById(R.id.prev_button);
        this.nextBtn = (Button) findViewById(R.id.next_button);
        this.skipBtn = (Button) findViewById(R.id.skip_next_button);
        this.exhibitCounterText = (TextView) findViewById(R.id.direction_exhibit_counter);
        this.exhibitTitleText = (TextView) findViewById(R.id.direction_exhibit_title);
        this.recyclerView = (RecyclerView) findViewById(R.id.directions_list_view);

        Object[] temp = (Object[]) getIntent().getSerializableExtra("route_list");
        this.routeList = Arrays.copyOf(temp, temp.length, Route[].class);

        List<Route> list = new ArrayList<>(Arrays.asList(this.routeList));
        Route exitRoute = RouteGenerator.generateRoute(this, list.get(list.size()-1).end, "entrance_exit_gate");
        exitRoute.generateDirections();
        list.add(exitRoute);
        this.routeList = list.toArray(new Route[0]);

        updateUI();
    }


    private void updateUI() {
        this.currRoute = routeList[currentExhibitCounter];
        this.exhibitCounterText.setText(String.valueOf(routeList.length-currentExhibitCounter-1));
        this.exhibitTitleText.setText(this.currRoute.exhibit);

        setPrevBtn();
        setNextBtn();
        setSkipBtn();
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

    private void setSkipBtn() {
        if (this.currentExhibitCounter >= this.routeList.length-2) {
            this.skipBtn.setVisibility(View.INVISIBLE);
        } else {
            this.skipBtn.setVisibility(View.VISIBLE);
        }
    }

    private void setNextBtn() {
        if (this.currentExhibitCounter < this.routeList.length-1) {
            Route nextExhibit = routeList[currentExhibitCounter+1];
            String nextBtnText =
                    "Next: " + "\n" + nextExhibit.exhibit + "\n" + (int) nextExhibit.totalDistance + " m";
            this.nextBtn.setText(nextBtnText);
        } else {
            this.nextBtn.setText("Finish");
        }
    }

    private void setAdapter() {
        DirectionsAdapter adapter = new DirectionsAdapter();
        if (detailedDirections == 0) {
            adapter.setDirectionsList(this.currRoute.briefDirections);
        } else {
            adapter.setDirectionsList(this.currRoute.detailedDirections);
        }
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        this.recyclerView.setLayoutManager(layoutManager);
        this.recyclerView.setAdapter(adapter);
    }

    public void onNextExhibitClicked(View view) {
        if (this.currentExhibitCounter < this.routeList.length-1) {
            this.currentExhibitCounter++;
            updateUI();
        } else {
            RouteGenerator.resetRoute();
            finish();
        }
    }

    public void onPrevExhibitClicked(View view) {
        this.currentExhibitCounter--;
        updateUI();
    }

    public void checkSkip(boolean didSkip, Route[] newRouteList) {
        if (didSkip) {
            this.routeList = newRouteList;
            Route.prevExhibit = currRoute.exhibit;
            this.routeList[currentExhibitCounter + 1].generateDirections();
            this.currentExhibitCounter++;
            updateUI();
        }
    }

    public void onSkipNextBtnClicked(View view) {
        // convert routeList array to a list
        List<Route> list = new ArrayList<>(Arrays.asList(this.routeList));
        // remove the next exhibit
        list.remove(currentExhibitCounter+1);
        // convert list back to array
        Route[] newRouteList = list.toArray(new Route[0]);
        // generate directions from current exhibit to next exhibit
        newRouteList[currentExhibitCounter+1] = RouteGenerator.generateRoute(this, newRouteList[currentExhibitCounter].end, newRouteList[currentExhibitCounter+1].end);

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this)
                .setTitle("Skipping Next Exhibit:")
                .setMessage(this.routeList[currentExhibitCounter+1].exhibit + "\n" + (int) this.routeList[currentExhibitCounter+1].totalDistance + " m")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        checkSkip(true, newRouteList);
                        updateUI();
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }
  
    //passes the currently selected detailedDirections value to the new activity
    //and asks for a result
    public void onSettingsClicked(View view) {
        Intent intent = new Intent(this, Settings.class);
        intent.putExtra("detailedDirections", detailedDirections);
        startActivityForResult(intent, detailedDirections);
    }

    //getting the result from the closed settings activity
    //resultCode stores the new value for detailedDirections
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        detailedDirections = resultCode;
        updateUI();
    }
}