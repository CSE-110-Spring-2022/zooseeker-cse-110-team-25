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
    boolean detailedDirections;

    public Route(List<List<String>> routeDirections, String start, String end, double totalDistance, String intro, String exhibit) {
        this.routeDirections = routeDirections;
        this.start = start;
        this.end = end;
        this.totalDistance = totalDistance;
        this.intro = intro;
        this.exhibit = exhibit;

        generateBriefDirections();
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
                    direction.get(2).substring(0, direction.get(2).length()-2),
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

        int totalDistance = Integer.parseInt(routeDirections.get(0).get(2).substring(0, routeDirections.get(0).get(2).length()-2));
        String prevRoad = routeDirections.get(0).get(3);
        String startSource = routeDirections.get(0).get(0);
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

            if (Route.prevExhibit.compareTo("") == 0) {
                Route.prevExhibit = target;
            } else if (Route.prevExhibit.compareTo(target) == 0) {
                String temp = target;
                target = source;
                source = temp;
            }
            Route.prevExhibit = target;

            if (direction.get(3).compareTo(prevRoad) == 0) {
                totalDistance += Integer.parseInt(direction.get(2).substring(0, direction.get(2).length()-2));
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
                    direction.get(2).substring(0, direction.get(2).length()-2),
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

        int totalDistance = Integer.parseInt(route.routeDirections.get(route.routeDirections.size()-1).get(2).substring(0, route.routeDirections.get(route.routeDirections.size()-1).get(2).length()-2));
        String prevRoad = route.routeDirections.get(route.routeDirections.size()-1).get(3);
        String startSource = route.routeDirections.get(route.routeDirections.size()-1).get(0);
        if (Route.prevExhibit.compareTo("") == 0) {
            Route.prevExhibit = route.routeDirections.get(route.routeDirections.size()-1).get(1);
        } else if (Route.prevExhibit.compareTo(route.routeDirections.get(route.routeDirections.size()-1).get(1)) == 0) {
            startSource = route.routeDirections.get(route.routeDirections.size()-1).get(1);
            Route.prevExhibit = route.routeDirections.get(route.routeDirections.size()-1).get(0);
        }
        for (int i = route.routeDirections.size()-2; i >= 0; i--) {
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

            if (direction.get(3).compareTo(prevRoad) == 0) {
                totalDistance += Integer.parseInt(direction.get(2).substring(0, direction.get(2).length()-2));
                if (i == 0) {
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
                if (i == 0) {
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
}
