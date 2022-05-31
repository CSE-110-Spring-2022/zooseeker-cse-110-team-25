package com.example.zooseeker25;

import android.util.Log;

import androidx.lifecycle.LiveData;

public class RoutePathChecker {
    public static boolean checkOnPath(LocationModel locationModel) {
        Coord currentLocation = locationModel.getLastKnownCoords().getValue();
        return true;
    }
}
