package com.example.zooseeker25;

import android.util.Log;

import androidx.lifecycle.LiveData;

public class RoutePathChecker {
    public static boolean checkOffPath(LocationModel locationModel, Route[] routeList, Route currExhibit) {
        Coord currentLocation = locationModel.getLastKnownCoords().getValue();
        Coord exhibitCoords = Coords.coordLookup.get(currExhibit.exhibit);

        Double currDistance = Coords.distance(currentLocation, exhibitCoords);
        for (Route route: routeList) {
            Double distance = Coords.distance(currentLocation, Coords.coordLookup.get(route.exhibit));

            if (distance < currDistance) {
                return true;
            }
        }
        return false;
    }
}
