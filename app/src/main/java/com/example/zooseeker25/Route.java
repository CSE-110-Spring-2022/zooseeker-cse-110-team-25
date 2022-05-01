package com.example.zooseeker25;

import java.util.List;

public class Route {
    public String start;
    public String end;
    public double totalDistance;
    public List<String> directions;

    public Route(String start, String end, double totalDistance, List<String> directions) {
        this.start = start;
        this.end = end;
        this.totalDistance = totalDistance;
        this.directions = directions;
    }
}
