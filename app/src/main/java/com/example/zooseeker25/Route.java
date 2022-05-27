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
//    public List<String> nextDirections = new ArrayList<>();
//    public List<String> prevDirections = new ArrayList<>();
//    public List<String> detailedDirections = new ArrayList<>();
//    public List<String> briefDirections = new ArrayList<>();
    private List<String> directions = new ArrayList<>();
    private static String prevExhibit = "";
    private String nextExhibit = "";
    boolean detailedDirections;

    public Route(List<List<String>> routeDirections, String start, String end, double totalDistance, String intro, String exhibit) {
        this.routeDirections = routeDirections;
        this.start = start;
        this.end = end;
        this.totalDistance = totalDistance;
        this.intro = intro;
        this.exhibit = exhibit;
    }

    public void genNextDirections(int detailed) {
        if (detailed == 0) {
            generateBriefDirections();
        } else {
            generateDetailedDirections();
        }
    }

    public void genPrevDirections(int detailed, Route route, String startExhibit) {
        if (detailed == 0) {
            generatePrevBriefDirections(route, startExhibit);
        } else {
            generatePrevDetailedDirections(route, startExhibit);
        }
    }

    public List<String> getDirections() {
        return directions;
    }

    private void generateDetailedDirections() {
        String source = "";
        String target = "";
        this.directions.clear();

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

    private void generateBriefDirections() {
        String source = "";
        String target = "";
        this.directions.clear();

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
                this.directions.add(b);
                totalDistance = 0;
                prevRoad = direction.get(3);
            }
        }
    }

    private void generatePrevDetailedDirections(Route route, String startExhibit) {
        String source = "";
        String target = "";
        this.nextExhibit = startExhibit;
        this.directions.clear();

        for (int i = route.routeDirections.size()-1; i >= 0; i--) {
            List<String> direction = route.routeDirections.get(i);
            source = direction.get(1);
            target = direction.get(0);

            if (this.nextExhibit.compareTo(target) == 0) {
                String temp = target;
                target = source;
                source = temp;
            }
            this.nextExhibit = target;

            String d = String.format("Walk %s meters along %s from '%s' to '%s'.\n",
                    direction.get(2),
                    direction.get(3),
                    source,
                    target
            );
            this.directions.add(d);
        }
    }

    private void generatePrevBriefDirections(Route route, String startExhibit) {
        String source = "";
        String target = "";
        this.nextExhibit = startExhibit;
        this.directions.clear();

        Route.prevExhibit = "";
        int totalDistance = 0;
        String prevRoad = routeDirections.get(0).get(3);
        for (int i = route.routeDirections.size()-1; i >= 0; i--) {
            List<String> direction = route.routeDirections.get(i);
            source = direction.get(1);
            target = direction.get(0);

            if (this.nextExhibit.compareTo(target) == 0) {
                String temp = target;
                target = source;
                source = temp;
            }
            this.nextExhibit = target;
            Route.prevExhibit = target;

            totalDistance += Integer.parseInt(direction.get(2).substring(0, direction.get(2).length()-2));
            if (direction.get(3).compareTo(prevRoad) != 0) {
                String b = String.format("Walk %s meters along %s from '%s' to '%s'.\n",
                        totalDistance,
                        prevRoad,
                        source,
                        target
                );
                this.directions.add(b);
                totalDistance = 0;
                prevRoad = direction.get(3);
            }
        }
    }
}
