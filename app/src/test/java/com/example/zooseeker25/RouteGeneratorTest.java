package com.example.zooseeker25;



import org.junit.Test;

import static org.junit.Assert.*;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RouteGeneratorTest {
        @Test
        public void test_simple_route() {
            List<List<Route>> routeData = new ArrayList<List<Route>>();
            Map<String, Integer> integerLookup = new HashMap<String, Integer>();

            List<String> exhibits = new ArrayList<>();
            exhibits.add("li");
            exhibits.add("ga");

            integerLookup.put("entrance_exit_gate", 0);
            for (int i = 1; i < exhibits.size()+1; i++) {
                integerLookup.put(exhibits.get(i-1), i);
            }

            List<Route> routeList = new ArrayList<>();
            routeList.add(null);
            routeList.add(new Route("entrance_exit_gate", "li", 10,  new ArrayList<String>(Arrays.asList("g")), ""));
            routeList.add(new Route("entrance_exit_gate", "ga", 30,  new ArrayList<String>(Arrays.asList("g")), ""));
            routeData.add(routeList);

            List<Route> routeList2 = new ArrayList<>();
            routeList2.add(new Route("li", "entrance_exit_gate", 10,  new ArrayList<String>(Arrays.asList("g")), ""));
            routeList2.add(null);
            routeList2.add(new Route("li", "ga", 20,  new ArrayList<String>(Arrays.asList("g")), ""));
            routeData.add(routeList2);

            List<Route> routeList3 = new ArrayList<>();
            routeList3.add(new Route("ga", "entrance_exit_gate", 30,  new ArrayList<String>(Arrays.asList("g")), ""));
            routeList3.add(new Route("ga", "li", 20,  new ArrayList<String>(Arrays.asList("g")), ""));
            routeList3.add(null);
            routeData.add(routeList3);

            List<Route> fullRoute = RouteGenerator.generateFullRoute(exhibits, routeData, integerLookup);

            assertEquals("li", fullRoute.get(0).end);
            assertEquals("ga", fullRoute.get(1).end);
    }
    
}
