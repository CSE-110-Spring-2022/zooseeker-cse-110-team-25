package com.example.zooseeker25;

public class Route {
    private String routeName;
    private String directions;

    public Route(String routeName, String directions) {
        this.routeName = routeName;
        this.directions = directions;
    }

    public String getRouteName() {
        return this.routeName;
    }

    public String getDirections() {
        return this.directions;
    }
}
