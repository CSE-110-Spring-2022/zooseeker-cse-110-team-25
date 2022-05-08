package com.example.zooseeker25;

import java.io.Serializable;
import java.util.List;

public class Route implements Serializable {
    public String start;
    public String end;
    public String intro;
    public String exhibit;
    public double totalDistance;
    public List<String> directions;

    public Route(String start, String end, double totalDistance, List<String> directions, String intro, String exhibit) {
        this.start = start;
        this.end = end;
        this.totalDistance = totalDistance;
        this.directions = directions;
        this.intro = intro;
        this.exhibit = exhibit;
    }
}
