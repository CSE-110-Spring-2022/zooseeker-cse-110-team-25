package com.example.zooseeker25;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

public class RouteGenerator {
    public static List<Route> generateFullRoute(Context context, List<String> exhibits) {
        Set<String> visitedExhibits = new HashSet<String>();
        List<Route> fullRoute = new ArrayList<>();
        visitedExhibits.add("entrance_exit_gate");
        String currentExhibit = "entrance_exit_gate";

        while (visitedExhibits.size() != exhibits.size()) {
            Route closestExhibit = null;

            for (String exhibit: exhibits) {
                if (currentExhibit.compareTo(exhibit) == 0) { continue; }
                if (visitedExhibits.contains(exhibit)) { continue; }
                Route route = RouteGenerator.generateRoute(context, currentExhibit, exhibit);

                if (closestExhibit == null) {
                    closestExhibit = route;
                } else if (closestExhibit.totalDistance > route.totalDistance) {
                    closestExhibit = route;
                }
            }
            fullRoute.add(closestExhibit);
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
        Map<String, ZooData.VertexInfo> vInfo = ZooData.loadVertexInfoJSON(context, "sample_node_info.json");
        Map<String, ZooData.EdgeInfo> eInfo = ZooData.loadEdgeInfoJSON(context, "sample_edge_info.json");

        String intro = String.format("The shortest path from '%s' to '%s' is:\n", start, end);
        Log.i("", intro);
        int i = 1;

        List<String> directions = new ArrayList<>();
        double totalDistance = 0;

        for (IdentifiedWeightedEdge e : path.getEdgeList()) {
            double edgeWeight = g.getEdgeWeight(e);
            String direction = String.format("Walk %.0f meters along %s from '%s' to '%s'.\n",
                    edgeWeight,
                    eInfo.get(e.getId()).street,
                    vInfo.get(g.getEdgeSource(e)).name,
                    vInfo.get(g.getEdgeTarget(e)).name
            );
            directions.add(direction);
            totalDistance += edgeWeight;

            Log.i(String.format("  Step %d", i), direction);
            i++;
        }

        return new Route(start, end, totalDistance, directions);
    }
}
