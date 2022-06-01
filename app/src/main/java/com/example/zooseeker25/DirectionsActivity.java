package com.example.zooseeker25;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;
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
import android.telephony.data.RouteSelectionDescriptor;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    private Button clearRouteBtn;

    private Route closeExhibit;
    private boolean fromPrev = false; //false if the current directions didn't come from pressing previous

    private int detailedDirections = 0; //0 for brief, 1 for detailed

    private final LocationPermissionChecker permissionsChecker = new LocationPermissionChecker(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d("DirectionsActivity", "0");
        super.onCreate(savedInstanceState);
        Log.d("DirectionsActivity", "1");
        setContentView(R.layout.activity_directions);

        initializeTextView();
        Coords.populateCoordLookup(this);

        Object[] temp = (Object[]) getIntent().getSerializableExtra("route_list");
        this.routeList = Arrays.copyOf(temp, temp.length, Route[].class);
        Log.d("DirectionsActivity", this.routeList[0].exhibit);
        Log.d("DirectionsActivity", Integer.toString(this.routeList.length));
        addExitToRoute();
        Log.d("DirectionsActivity", Integer.toString(this.routeList.length));
        Log.d("DirectionsActivity", "2");
        updateUI();
        Log.d("DirectionsActivity", "3");
    }

    //initializes all textviews
    public void initializeTextView(){
        useMockLocation = getIntent().getBooleanExtra(EXTRA_USE_MOCK_LOCATION, false);

        this.prevBtn = (Button) findViewById(R.id.prev_button);
        this.nextBtn = (Button) findViewById(R.id.next_button);
        this.skipBtn = (Button) findViewById(R.id.skip_next_button);
        this.clearRouteBtn = (Button) findViewById(R.id.clearbutton);
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
        locationModel.activity = this;

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
            Log.d("DirectionsActivity", currRoute.exhibit);
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

    //recalculates a new route that excludes the next exhibit
    public Route[] skippedRoute(){
        Log.d("DirectionsActivity", "skippedRoute()");
        // convert routeList array to a list
        List<Route> list = new ArrayList<>(Arrays.asList(this.routeList));
        // remove the next exhibit
        list.remove(currentExhibitCounter+1);
        List<Route> visited = list.subList(0, currentExhibitCounter);
        List<Route> future = list.subList(currentExhibitCounter, list.size());

        RouteGenerator.setInit(future.get(0).end);
        List<String> newExhibits = new ArrayList<>();
        for (Route r : future) {
            newExhibits.add(r.end);
        }
        visited.addAll(recalculateRoute(newExhibits));
        routeList = visited.toArray(new Route[visited.size()]);
        return routeList;
    }

    public List<Route> recalculateRoute(List<String> newExhibits) {
        RouteGenerator.resetRoute();
        RouteGenerator.populateRouteData(newExhibits, this);
        List<Route> newRoute = RouteGenerator.generateFullRoute(newExhibits, RouteGenerator.routeData, RouteGenerator.integerLookup);
        newRoute.add(RouteGenerator.generateRoute(this, newRoute.get(newRoute.size()-1).end, "entrance_exit_gate"));
        return newRoute;
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

    public void wentOffRoute() {
        // when there are more than one exhibit left
        if (currentExhibitCounter < routeList.length-2) {
            List<Route> routes = Arrays.asList(routeList);
            routes = routes.subList(currentExhibitCounter+1, routeList.length-1);
            Route[] newRouteList = routes.toArray(new Route[0]);
            Route closestRoute = RoutePathChecker.checkOffPath(locationModel, newRouteList, currRoute);
            if (closestRoute != null) {
                closeExhibit = closestRoute;
                showReplanAlert();
            }
        }
    }

    public void generateNewRoute() {
        List<Route> newRoutes = Arrays.asList(routeList);
        List<Route> newRouteList = new ArrayList<>();
        List<String> newExhibits = new ArrayList<>();

        if (currentExhibitCounter-1 == -1) {
            RouteGenerator.setInit(closeExhibit.end);
            // generate route from begin to closest route
            newRouteList.add(RouteGenerator.generateRoute(this, "entrance_exit_gate", closeExhibit.end));

            // add current route to route list and remove the closest exhibit from the route list
            for (Route route: newRoutes.subList(0, newRoutes.size()-1)) {
                if (route.end.compareTo(closeExhibit.end) != 0 ) {
                    newExhibits.add(route.end);
                }
            }
        } else {
            RouteGenerator.setInit(closeExhibit.end);
            newRouteList.addAll(newRoutes.subList(0, currentExhibitCounter));
            // generate route from the start of current route to the closest route
            newRouteList.add(RouteGenerator.generateRoute(this, currRoute.start, closeExhibit.end));

            // add current route to route list but remove the closest exhibit
            newExhibits.add(currRoute.end);
            for (Route route: newRoutes.subList(currentExhibitCounter, routeList.length-1)) {
                if (route.end.compareTo(closeExhibit.end) != 0) {
                    newExhibits.add(route.end);
                }
            }
        }
        newRouteList.addAll(recalculateRoute(newExhibits));
        this.routeList = newRouteList.toArray(new Route[0]);
        directions = new ArrayList<>();
        this.currRoute = routeList[currentExhibitCounter];
        Route.prevExhibit = currRoute.exhibit;
        updateUI();
    }

    public void showReplanAlert() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this)
                .setTitle("Replan?")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // call replan function here
                        generateNewRoute();
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

    public void onMockLocationClicked(View view) {
        EditText coordsText = new EditText(this);
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this)
                .setTitle("Mock Route")
                .setMessage("Enter coord in the format lat,long")
                .setView(coordsText)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String text = coordsText.getText().toString();
                        String[] coords = text.split(",");
                        mockLocation(new Coord(Double.parseDouble(coords[0]), Double.parseDouble(coords[1])));
                        wentOffRoute();
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

    public void onClearRouteClick(View view){
        currentExhibitCounter = 0;
        this.skippedIndex = new ArrayList<>();
        Log.d("DirectionsActivity","finish()");
        onPause();
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
            Log.d("DirectionsActivity skipped null", Integer.toString(currentExhibitCounter));
            this.currRoute = routeList[currentExhibitCounter];
            Route.prevExhibit = currRoute.exhibit;
            updateUI();
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
        this.currRoute = routeList[currentExhibitCounter];
        Route.prevExhibit = currRoute.exhibit;
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


