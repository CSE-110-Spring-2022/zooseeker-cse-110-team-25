package com.example.zooseeker25;

import android.util.Log;

import androidx.lifecycle.LiveData;

public class RoutePathChecker {
    public static boolean checkOffPath(LocationModel locationModel, Route[] routeList, Route currExhibit) {
        Coord currentLocation = locationModel.getLastKnownCoords().getValue();
        Coord exhibitCoords = Coords.coordLookup.get(currExhibit.exhibit);

        Log.i("currentLocation", currentLocation.toString());
        Log.i("exhibitLocation", exhibitCoords.toString());
        Double currDistance = Coords.distance(currentLocation, exhibitCoords);
        for (Route route: routeList) {
            Log.i("nextLocation", route.exhibit);
            Log.i("nextLocation", Coords.coordLookup.get(route.exhibit).toString());
            Double distance = Coords.distance(currentLocation, Coords.coordLookup.get(route.exhibit));

            if (distance < currDistance) {
                Log.i("closerLocation", route.exhibit);
                return true;
            }
        }
        return false;
    }
}
