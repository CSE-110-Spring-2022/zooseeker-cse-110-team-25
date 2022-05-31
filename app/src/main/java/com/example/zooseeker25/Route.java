package com.example.zooseeker25;

import static java.util.List.copyOf;

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
    private List<String> directions = new ArrayList<>();
    public static String prevExhibit = "";
    private String nextExhibit = "";

    //by default generates brief directions to the next exhibit
    public Route(List<List<String>> routeDirections, String start, String end, double totalDistance, String intro, String exhibit) {
        this.routeDirections = routeDirections;
        this.start = start;
        this.end = end;
        this.totalDistance = totalDistance;
        this.intro = intro;
        this.exhibit = exhibit;

        generateBriefDirections();
    }

    //generates directions to the next exhibit based on the detailed setting
    public void genNextDirections(int detailed) {
        if (detailed == 0) {
            generateBriefDirections();
        } else {
            generateDetailedDirections();
        }
    }

    //generates directions to the previous exhibit based on the detailed setting
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

    //generates detailed directions to the next exhibit
    private void generateDetailedDirections() {
        String source = "";
        String target = "";
        this.directions.clear();

        for (List<String> direction: this.routeDirections) {
            source = direction.get(0);
            target = direction.get(1);

            //flips the start and end nodes if necessary
            if (Route.prevExhibit.compareTo(target) == 0) {
                String temp = target;
                target = source;
                source = temp;
            }

            Route.prevExhibit = target;

            String d = String.format("Walk %s meters along %s from '%s' to '%s'.\n",
                    direction.get(2).substring(0, direction.get(2).length()-2),
                    direction.get(3),
                    source,
                    target
            );

            this.directions.add(d);
        }
    }

    //generates brief directions to the next exhibit
    private void generateBriefDirections() {
        if (routeDirections == null || routeDirections.size() == 0) {
            return;
        }
        String source = "";
        String target = "";
        this.directions.clear();

        //iterates through the routeDirections and compresses the directions by road names
        int totalDistance = Integer.parseInt(routeDirections.get(0).get(2).substring(0, routeDirections.get(0).get(2).length()-2));
        String prevRoad = routeDirections.get(0).get(3);
        String startSource = routeDirections.get(0).get(0);
        //setting up the flipping of prevExhibit
        if (Route.prevExhibit.compareTo("") == 0) {
            Route.prevExhibit = routeDirections.get(0).get(1);
        } else if (Route.prevExhibit.compareTo(routeDirections.get(0).get(1)) == 0) {
            startSource = routeDirections.get(0).get(1);
            Route.prevExhibit = routeDirections.get(0).get(0);
        }
        for (int i = 1; i < routeDirections.size(); i++) {
            List<String> direction = routeDirections.get(i);
            source = direction.get(0);
            target = direction.get(1);

            //flips the start and end nodes if necessary
            if (Route.prevExhibit.compareTo(target) == 0) {
                String temp = target;
                target = source;
                source = temp;
            }
            Route.prevExhibit = target;

            //comparing the current road to the previous road
            if (direction.get(3).compareTo(prevRoad) == 0) {
                totalDistance += Integer.parseInt(direction.get(2).substring(0, direction.get(2).length()-2));
                //handles the case of the last direction
                if (i == routeDirections.size()-1) {
                    String d = String.format("Walk %s meters along %s from '%s' to '%s'.\n",
                            totalDistance,
                            prevRoad,
                            startSource,
                            target
                    );
                    directions.add(d);
                }
            } else {
                String d = String.format("Walk %s meters along %s from '%s' to '%s'.\n",
                        totalDistance,
                        prevRoad,
                        startSource,
                        source
                );
                directions.add(d);
                totalDistance = Integer.parseInt(direction.get(2).substring(0, direction.get(2).length()-2));
                startSource = source;
                prevRoad = direction.get(3);
                //handles the case of the last direction
                if (i == routeDirections.size()-1) {
                    d = String.format("Walk %s meters along %s from '%s' to '%s'.\n",
                            direction.get(2).substring(0, direction.get(2).length()-2),
                            direction.get(3),
                            source,
                            target
                    );
                    directions.add(d);
                }
            }
        }
    }

    //generates detailed directions to the previous exhibit
    void generatePrevDetailedDirections(Route route, String startExhibit) {
        String source = "";
        String target = "";
        this.nextExhibit = startExhibit;
        this.directions.clear();

        //iterates through the routeDirections backwards and formats strings for each direction
        for (int i = route.routeDirections.size()-1; i >= 0; i--) {
            List<String> direction = route.routeDirections.get(i);
            source = direction.get(1);
            target = direction.get(0);

            //flips the start and end nodes if necessary
            if (this.nextExhibit.compareTo(target) == 0) {
                String temp = target;
                target = source;
                source = temp;
            }
            this.nextExhibit = target;

            String d = String.format("Walk %s meters along %s from '%s' to '%s'.\n",
                    direction.get(2).substring(0, direction.get(2).length()-2),
                    direction.get(3),
                    source,
                    target
            );
            this.directions.add(d);
        }
    }

    //generates brief directions to the previous exhibit
    private void generatePrevBriefDirections(Route route, String startExhibit) {
        String source = "";
        String target = "";
        //this.nextExhibit = startExhibit;
        this.directions.clear();

        routeDirections = route.routeDirections;

        //iterates through the routeDirections and compresses the directions by road names
        //then flips the start and end to create directions back
        int totalDistance = Integer.parseInt(routeDirections.get(0).get(2).substring(0, routeDirections.get(0).get(2).length()-2));
        String prevRoad = routeDirections.get(0).get(3);
        String startSource = routeDirections.get(0).get(0);
        //setting up the flipping of prevExhibit
        if (Route.prevExhibit.compareTo("") == 0) {
            Route.prevExhibit = routeDirections.get(0).get(1);
        } else if (Route.prevExhibit.compareTo(routeDirections.get(0).get(1)) == 0) {
            startSource = routeDirections.get(0).get(1);
            Route.prevExhibit = routeDirections.get(0).get(0);
        }

        for (int i = 1; i < routeDirections.size(); i++) {
            List<String> direction = routeDirections.get(i);
            source = direction.get(0);
            target = direction.get(1);

            //flips the start and end nodes if necessary
            if (Route.prevExhibit.compareTo(target) == 0) {
                String temp = target;
                target = source;
                source = temp;
            }
            Route.prevExhibit = target;

            //comparing the current road to the previous road
            if (direction.get(3).compareTo(prevRoad) == 0) {
                totalDistance += Integer.parseInt(direction.get(2).substring(0, direction.get(2).length()-2));
                //handles the case of the last direction
                if (i == routeDirections.size()-1) {
                    String d = String.format("Walk %s meters along %s from '%s' to '%s'.\n",
                            totalDistance,
                            prevRoad,
                            target,
                            startSource
                    );
                    directions.add(0, d);
                }
            } else {
                String d = String.format("Walk %s meters along %s from '%s' to '%s'.\n",
                        totalDistance,
                        prevRoad,
                        source,
                        startSource
                );
                directions.add(0, d);
                totalDistance = Integer.parseInt(direction.get(2).substring(0, direction.get(2).length()-2));
                startSource = source;
                prevRoad = direction.get(3);
                //handles the case of the last direction
                if (i == routeDirections.size()-1) {
                    d = String.format("Walk %s meters along %s from '%s' to '%s'.\n",
                            direction.get(2).substring(0, direction.get(2).length()-2),
                            direction.get(3),
                            target,
                            source
                    );
                    directions.add(0, d);
                }
            }
        }
    }
}
