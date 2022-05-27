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
    public List<String> detailedDirections = new ArrayList<>();
    public List<String> briefDirections = new ArrayList<>();
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
        generateDetailedDirections();
        generateBriefDirections();
    }

    private void generateDetailedDirections() {
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

            this.detailedDirections.add(d);
        }
    }

    private void generateBriefDirections() {
        String source = "";
        String target = "";

        Route.prevExhibit = "";
        int totalDistance = 0;
        String prevRoad = routeDirections.get(0).get(3);
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

            totalDistance += Integer.parseInt(direction.get(2).substring(0, direction.get(2).length()-2));
            if (direction.get(3).compareTo(prevRoad) != 0) {
                String b = String.format("Walk %s meters along %s from '%s' to '%s'.\n",
                        totalDistance,
                        prevRoad,
                        source,
                        target
                );
                this.briefDirections.add(b);
                totalDistance = 0;
                prevRoad = direction.get(3);
            }
        }
    }

}