package com.example.zooseeker25;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Route implements Serializable {
    public String start;
    public String end;
    public String intro;
    public String exhibit;
    public double totalDistance;
    public List<List<String>> routeDirections;
    public List<String> directions = new ArrayList<>();
    public static String prevExhibit = "";

    public Route(String start, String end, double totalDistance, List<List<String>> routeDirections, String intro, String exhibit) {
        this.start = start;
        this.end = end;
        this.totalDistance = totalDistance;
        this.routeDirections = routeDirections;
        this.intro = intro;
        this.exhibit = exhibit;
    }

    public void generateDirections() {
        String source = "";
        String target = "";
        for (List<String> direction: this.routeDirections) {
            source = direction.get(0);
            target = direction.get(1);

            if (Route.prevExhibit.compareTo("") == 0) {
                Route.prevExhibit = target;
            } else if (Route.prevExhibit.compareTo(target) == 0) {
                String temp = target;
                target = source;
                source = temp;
            }

            Route.prevExhibit = target;

            String d = String.format("Walk %s meters along %s from '%s' to '%s'.\n",
                    direction.get(2),
                    direction.get(3),
                    source,
                    target
            );
            this.directions.add(d);
        }
    }
}