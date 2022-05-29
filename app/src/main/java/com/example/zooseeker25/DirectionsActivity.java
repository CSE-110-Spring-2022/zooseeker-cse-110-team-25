package com.example.zooseeker25;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
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
    private final LocationPermissionChecker permissionsChecker = new LocationPermissionChecker( this );

    //temp behavior
    private TextView tempText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directions);

        //temp behavior
        tempText = (TextView) findViewById(R.id.tempText);
        tempText.setText("Brief");

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

        onResume();
        updateUI();
        //setting up permissions
        {
            if (permissionsChecker.ensurePermissions()) return;
            var locationManager = (LocationManager) this.getSystemService( Context.LOCATION_SERVICE );
            var provider = LocationManager.GPS_PROVIDER;
            if (ActivityCompat.checkSelfPermission( this, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission( this, Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                return;
            }
            Log.d( "ZooSeeker", String.format( "Location changed: %s", locationManager.getLastKnownLocation( provider ) ) );
        }

        //listen for location updates
        {
            var provider = LocationManager.GPS_PROVIDER;
            var locationManager = (LocationManager) this.getSystemService( Context.LOCATION_SERVICE );
            var locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {
                    Log.d( "ZooSeeker", String.format( "Location changed: %s", location ) );

//                    var marker = new MarkerOptions()
//                            .position( new LatLng(
//                                    location.getLatitude(),
//                                    location.getLongitude()
//                            ) )
//                            .title( "Navigation Step" );
//                    map.addMarker( marker );
                }
            };
            if (ActivityCompat.checkSelfPermission( this, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission( this, Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates( provider, 0, 0f, locationListener );
        }
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

    @Override
    protected void onPause(){
        super.onPause();
        saveCurrentExhibitCounter();
    }

    public void loadCurrentExhibitCounter(){
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        currentExhibitCounter = preferences.getInt("CurrentExhibitCounter", 0);
    }

    public void saveCurrentExhibitCounter(){
        Log.d("DirectionsActivity", Integer.toString(currentExhibitCounter));
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("CurrentExhibitCounter", currentExhibitCounter);
        editor.apply();
    }

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
            String nextBtnText = "Next: " + "\n" + nextExhibit.exhibit + "\n" + (int) nextExhibit.totalDistance + " m";
            this.routeList[this.currentExhibitCounter+1].directions = this.routeList[this.currentExhibitCounter+1].nextDirections;
            this.nextBtn.setText(nextBtnText);
        } else {
            this.nextBtn.setText("Finish");
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        loadCurrentExhibitCounter();
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
        } else {
            RouteGenerator.resetRoute();
            finish();
        }
    }

    public void onPrevExhibitClicked(View view) {
        if (this.routeList[this.currentExhibitCounter-1].prevDirections.size() == 0) {
            this.routeList[this.currentExhibitCounter-1].generatePrevDirections(this.currRoute, this.routeList[currentExhibitCounter+1].exhibit);
        }
        this.routeList[this.currentExhibitCounter-1].directions = this.routeList[this.currentExhibitCounter-1].prevDirections;
        this.currentExhibitCounter--;
        updateUI();
    }

    public void checkSkip(boolean didSkip, Route[] newRouteList) {
        if (didSkip) {
            this.routeList = newRouteList;
            Route.prevExhibit = currRoute.exhibit;
            this.routeList[currentExhibitCounter + 1].generateDirections();
            this.routeList[currentExhibitCounter].generatePrevDirections(this.routeList[currentExhibitCounter+1], this.routeList[currentExhibitCounter+1].exhibit);
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

        newRouteList[this.currentExhibitCounter+1] = RouteGenerator.generateRoute(this, newRouteList[currentExhibitCounter].end, newRouteList[currentExhibitCounter+1].end);

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

        //temporary example for how to use the results
        if (resultCode == 0) {
            tempText.setText("Brief");
        } else {
            tempText.setText("Detailed");
        }
    }
}