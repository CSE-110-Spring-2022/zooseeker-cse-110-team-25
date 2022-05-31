package com.example.zooseeker25;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class DirectionsActivity extends AppCompatActivity {
    private int currentExhibitCounter = 0;
    private final String EXTRA_USE_MOCK_LOCATION = "use_mock_location";
    private final String MOCK_ROUTE = "mockRouteLocations.json";
    public boolean useMockLocation;
    private Route currRoute;

    private List<Integer> skippedIndex = new ArrayList<>();

    private Route[] routeList;
    private LocationModel locationModel;

    private List<String> directions;

    private RecyclerView recyclerView;
    private TextView exhibitTitleText;
    private TextView exhibitCounterText;
    private Button prevBtn;
    private Button nextBtn;
    private Button skipBtn;
    private boolean fromPrev = false; //false if the current directions didn't come from pressing previous

    private int detailedDirections = 0; //0 for brief, 1 for detailed

    private final LocationPermissionChecker permissionsChecker = new LocationPermissionChecker(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directions);

        initializeTextView();

        Object[] temp = (Object[]) getIntent().getSerializableExtra("route_list");
        this.routeList = Arrays.copyOf(temp, temp.length, Route[].class);

        addExitToRoute();

        updateUI();
    }

    //initializes all textviews
    public void initializeTextView(){
        useMockLocation = getIntent().getBooleanExtra(EXTRA_USE_MOCK_LOCATION, false);

        this.prevBtn = (Button) findViewById(R.id.prev_button);
        this.nextBtn = (Button) findViewById(R.id.next_button);
        this.skipBtn = (Button) findViewById(R.id.skip_next_button);
        this.exhibitCounterText = (TextView) findViewById(R.id.direction_exhibit_counter);
        this.exhibitTitleText = (TextView) findViewById(R.id.direction_exhibit_title);
        this.recyclerView = (RecyclerView) findViewById(R.id.directions_list_view);
    }

    //appends entrance/exit gate to the end of the route
    //initializes UI and location services
    public void addExitToRoute(){
        updateRouteList();
        updateUI();
        setLocationServices();
    }

    //appends entrance/exit gate to the end of the route
    private void updateRouteList() {
        Object[] temp = (Object[]) getIntent().getSerializableExtra("route_list");
        this.routeList = Arrays.copyOf(temp, temp.length, Route[].class);

        List<Route> list = new ArrayList<>(Arrays.asList(this.routeList));
        Route exitRoute = RouteGenerator.generateRoute(this, list.get(list.size()-1).end, "entrance_exit_gate");
        exitRoute.genNextDirections(detailedDirections);
        list.add(exitRoute);
        this.routeList = list.toArray(new Route[0]);
        directions = new ArrayList<>();
        this.currRoute = routeList[currentExhibitCounter];
        Route.prevExhibit = currRoute.exhibit;
    }

    private void setLocationServices() {
        locationModel = new ViewModelProvider(this).get(LocationModel.class);

        if (useMockLocation){
            //read a sequence of locations from a JSON file
            List<Coord> route = Coord.loadJSON(this, MOCK_ROUTE);
            mockRoute( route, 2000, TimeUnit.MILLISECONDS);
            //call mock route here?
            return;
        }

        if (permissionsChecker.ensurePermissions()) return;
        var locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        var provider = LocationManager.GPS_PROVIDER;
        locationModel.addLocationProviderSource(locationManager, provider);
    }

    @VisibleForTesting
    public void mockLocation(Coord coords) {
        locationModel.mockLocation(coords);
    }

    @VisibleForTesting
    public Future<?> mockRoute(List<Coord> route, long delay, TimeUnit unit) {
        return locationModel.mockRoute(route, delay, unit);
    }

    //updates all UI elements
    private void updateUI() {
        this.exhibitCounterText.setText(String.valueOf(routeList.length-currentExhibitCounter-1));
        this.exhibitTitleText.setText(this.currRoute.exhibit);

        setPrevBtn();
        setNextBtn();
        setSkipBtn();
        setAdapter();
    }

    //if it is not the first directions, set the text of the previous button
    private void setPrevBtn() {
        if (currentExhibitCounter != 0) {
            this.prevBtn.setVisibility(View.VISIBLE);
            Route prevExhibit = routeList[currentExhibitCounter-1];
            String prevBtnText =
                    "Previous:\n" + prevExhibit.exhibit + "\n" + (int) prevExhibit.totalDistance + " m";
            this.prevBtn.setText(prevBtnText);
        } else {
            this.prevBtn.setVisibility(View.INVISIBLE);
        }

    }

    //sets the text of the skip button if there are exhibits to skip
    private void setSkipBtn() {
        if (this.currentExhibitCounter >= this.routeList.length-2) {
            this.skipBtn.setVisibility(View.INVISIBLE);
        } else {
            this.skipBtn.setVisibility(View.VISIBLE);
        }
    }

    //sets the text of the next button, and changes it to finish if its the last direction
    private void setNextBtn() {
        Log.d("DirectionsActivity", "setNextBtn()");
        if (this.currentExhibitCounter < this.routeList.length-1) {
            Route nextExhibit = routeList[currentExhibitCounter+1];
            String nextBtnText = "Next: " + "\n" + nextExhibit.exhibit + "\n" + (int) nextExhibit.totalDistance + " m";
            this.nextBtn.setText(nextBtnText);
        } else {
            this.nextBtn.setText("Finish");
        }
    }

    //sets the adapter to the corresponding recyclerview
    private void setAdapter() {
        DirectionsAdapter adapter = new DirectionsAdapter();
        adapter.setDirectionsList(currRoute.getDirections());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        this.recyclerView.setLayoutManager(layoutManager);
        this.recyclerView.setAdapter(adapter);
    }

    //generates new directions for the next exhibit, or closes the activity
    public void onNextExhibitClicked(View view) {
        fromPrev = false;
        if (this.currentExhibitCounter < this.routeList.length-1) {
            //increments the current exhibit, and generates new directions
            Route.prevExhibit = currRoute.exhibit;
            this.currentExhibitCounter++;
            this.currRoute = routeList[currentExhibitCounter];
            currRoute.genNextDirections(detailedDirections);
            updateUI();
        } else {
            //resets the route and closes
            RouteGenerator.resetRoute();
            this.currentExhibitCounter = 0;
            this.skippedIndex = new ArrayList<>();
            Log.d("DirectionsActivity","finish()");
            onPause();
            finish();
        }
    }

    //generates new directions to return to the previous exhibit
    public void onPrevExhibitClicked(View view) {
        fromPrev = true;
        //decrements current route
        this.currentExhibitCounter--;
        this.currRoute = routeList[currentExhibitCounter];
        Route.prevExhibit = currRoute.exhibit;
        currRoute.genPrevDirections(detailedDirections, routeList[currentExhibitCounter+1], this.routeList[currentExhibitCounter+1].exhibit);
        updateUI();
    }

    //takes a modified routelist and generates the next directions
    public void checkSkip(Route[] newRouteList) {
        routeList = newRouteList;
        Route.prevExhibit = currRoute.exhibit;
        currentExhibitCounter++;
        currRoute = routeList[currentExhibitCounter];
        currRoute.genNextDirections(detailedDirections);
        updateUI();
    }

    //saves at what points skips occurred
    public void recordSkippedIndex(int i){
        skippedIndex.add(i);
        Log.d("DirectionsActivity recordSkippedIndex", Integer.toString(i));
    }

    //modifies the routelist and generates next directions to skip an exhibit
    public void onSkipNextBtnClicked(View view) {
        recordSkippedIndex(this.currentExhibitCounter);

        //modifies the routelist
        Route[] newRouteList = skippedRoute();

        //confirms with the user that they want to skip
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this)
                .setTitle("Skipping Next Exhibit:")
                .setMessage(this.routeList[currentExhibitCounter+1].exhibit + "\n" +
                        (int) this.routeList[currentExhibitCounter+1].totalDistance + " m")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //updates the routelist and increments current route
                        routeList = newRouteList;
                        Route.prevExhibit = currRoute.exhibit;
                        currentExhibitCounter++;
                        currRoute = routeList[currentExhibitCounter];
                        //generate directions to new next exhibit
                        currRoute.genNextDirections(detailedDirections);
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

    //generates a new route that excludes the next exhibit
    public Route[] skippedRoute(){
        Log.d("DirectionsActivity", "skippedRoute()");
        // convert routeList array to a list
        List<Route> list = new ArrayList<>(Arrays.asList(this.routeList));
        // remove the next exhibit
        list.remove(currentExhibitCounter+1);
        // convert list back to array
        Route[] newRouteList = list.toArray(new Route[0]);
        // generate directions from current exhibit to next exhibit

        newRouteList[this.currentExhibitCounter+1] = RouteGenerator.
                generateRoute(this, newRouteList[currentExhibitCounter].end, newRouteList[currentExhibitCounter+1].end);
        return newRouteList;
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
        if (currentExhibitCounter > 0) {
            Route.prevExhibit = routeList[currentExhibitCounter - 1].exhibit;
        }
        if (!fromPrev) {
            currRoute.genNextDirections(detailedDirections);
        } else {
            currRoute.genPrevDirections(detailedDirections, routeList[currentExhibitCounter+1], routeList[currentExhibitCounter+1].exhibit);
        }
        updateUI();
    }

    public void onClearRouteClick(View view){
        currentExhibitCounter = 0;
        this.skippedIndex = new ArrayList<>();
        Log.d("DirectionsActivity","finish()");
        finish();
    }

    @Override
    protected void onResume(){
        super.onResume();
        loadCurrentExhibitCounter();
    }

    @Override
    protected void onPause(){
        super.onPause();
        saveCurrentExhibitCounter();
    }

    //loads which exhibit the app was closed on
    public void loadCurrentExhibitCounter(){
        Log.d("DirectionsActivity", "in loadCurrentExhibitCounter");
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);

        String index = preferences.getString("skippedIndex", null);

        if (index == null){
            currentExhibitCounter = preferences.getInt("CurrentExhibitCounter", 0);
            return;
        }
        Log.d("DirectionsActivity load", index);
        String[] indexString = index.split("\\s*,\\s*");

        for (int i = 0; i < indexString.length; i++){
            currentExhibitCounter = Integer.parseInt(indexString[i]);
            Route[] newRouteList = skippedRoute();
            checkSkip(newRouteList);
        }

        currentExhibitCounter = preferences.getInt("CurrentExhibitCounter", 0);

        Log.d("DirectionsActivity load", Integer.toString(currentExhibitCounter));
    }

    //saves which exhibit the app is currently on
    public void saveCurrentExhibitCounter(){
        Log.d("DirectionsActivity", Integer.toString(currentExhibitCounter));
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("CurrentExhibitCounter", currentExhibitCounter);
        Log.d("DirectionsActivity save", Integer.toString(currentExhibitCounter));

        if (this.skippedIndex.size() == 0){
            editor.putString("skippedIndex", null);
        }
        else{
            String index = "";
            for (int ind : this.skippedIndex){
                index += ind + ",";
            }
            index = index.substring(0, index.length()-1);
            editor.putString("skippedIndex", index);
            Log.d("DirectionsActivity skippedIndex", index);
        }

        editor.apply();
    }

}


