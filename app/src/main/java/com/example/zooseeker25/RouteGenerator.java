package com.example.zooseeker25;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

public class RouteGenerator {
    public static Map<Integer, String> nodeLookup = new HashMap<>();
    public static Map<String, Integer> integerLookup = new HashMap<>();
    public static List<List<Route>> routeData = new ArrayList<List<Route>>();
    private static String prevExhibit = "";

    public static void resetRoute() {
        RouteGenerator.nodeLookup = new HashMap<>();
        RouteGenerator.integerLookup = new HashMap<>();
        RouteGenerator.routeData = new ArrayList<List<Route>>();
        RouteGenerator.prevExhibit = "";
    }

    public static void populateRouteData (List<String> exhibits, Context context) {
        Log.d("RouteGenerator", exhibits.get(0));
        int i = 1;
        RouteGenerator.nodeLookup.put(0, "entrance_exit_gate");
        RouteGenerator.integerLookup.put("entrance_exit_gate", 0);
        for (String exhibit: exhibits) {
            RouteGenerator.nodeLookup.put(i, exhibit);
            RouteGenerator.integerLookup.put(exhibit, i);
            i++;
        }

        for (int row = 0; row < nodeLookup.size(); row++) {
            List<Route> routeList = new ArrayList<Route>();

            for (int col = 0; col < nodeLookup.size(); col++) {
                if (row == col) {
                    routeList.add(null);
                    continue;
                }
                Route route = RouteGenerator.generateRoute(context, nodeLookup.get(row), nodeLookup.get(col));
                routeList.add(route);
            }

            routeData.add(routeList);
        }
    }

    public static List<Route> generateFullRoute(List<String> exhibits, List<List<Route>> routeData, Map<String, Integer> node_lookup) {
        Log.d("RouteGenerator", exhibits.get(0));

        Set<String> visitedExhibits = new HashSet<String>();
        List<Route> fullRoute = new ArrayList<>();
        visitedExhibits.add("entrance_exit_gate");
        String currentExhibit = "entrance_exit_gate";
        while (visitedExhibits.size() != exhibits.size()+1) {
            Route closestExhibit = null;

            for (String exhibit: exhibits) {
                if (currentExhibit.compareTo(exhibit) == 0) { continue; }
                if (visitedExhibits.contains(exhibit)) { continue; }
                Route route = routeData.get(node_lookup.get(currentExhibit)).get(node_lookup.get(exhibit));
                if (closestExhibit == null || closestExhibit.totalDistance > route.totalDistance) {
                    closestExhibit = route;
                }
            }
            if (closestExhibit == null) { break; }
            fullRoute.add(closestExhibit);
            //closestExhibit.generateDirections();
            visitedExhibits.add(closestExhibit.end);
            currentExhibit = closestExhibit.end;
        }

        return fullRoute;
    }

    public static Route generateRoute(Context context, @NonNull String start, @NonNull String end) {
        // 1. Load the graph...
        Graph<String, IdentifiedWeightedEdge> g = ZooData.loadZooGraphJSON(context, "sample_zoo_graph.json");
        GraphPath<String, IdentifiedWeightedEdge> path = DijkstraShortestPath.findPathBetween(g, start, end);

        // 2. Load the information about our nodes and edges...
        Map<String, ZooData.VertexInfo> vInfo = ZooData.loadVertexInfoJSON(context, "sample_vertex_info.json");
        Map<String, ZooData.EdgeInfo> eInfo = ZooData.loadEdgeInfoJSON(context, "sample_edge_info.json");

        String intro = String.format("The shortest path from '%s' to '%s' is:\n", start, end);

        List<List<String>> routeDirections = new ArrayList<List<String>>();
        double totalDistance = 0;

        for (IdentifiedWeightedEdge e : path.getEdgeList()) {
            double edgeWeight = g.getEdgeWeight(e);
            List<String> route = new ArrayList<>();

            route.add(vInfo.get(g.getEdgeSource(e)).name);
            route.add(vInfo.get(g.getEdgeTarget(e)).name);
            route.add(String.valueOf(edgeWeight));
            route.add(eInfo.get(e.getId()).street);

            routeDirections.add(route);
            totalDistance += edgeWeight;
        }

        return new Route(routeDirections, start, end, totalDistance, intro, vInfo.get(end).name);
    }
}
