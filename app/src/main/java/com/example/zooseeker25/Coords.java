package com.example.zooseeker25;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Coords {
    public static Map<String, Coord> coordLookup;

    public static class VertexInfo {
        public static enum Kind {
            // The SerializedName annotation tells GSON how to convert
            // from the strings in our JSON to this Enum.
            @SerializedName("gate") GATE,
            @SerializedName("exhibit") EXHIBIT,
            @SerializedName("intersection") INTERSECTION,
            @SerializedName("exhibit_group") EXHIBIT_GROUP,
        }

        public String id;
        public Coords.VertexInfo.Kind kind;
        public String name;
        public String group_id;
        public Double lat;
        public Double lng;
        public List<String> tags;
    }

    public static void populateCoordLookup(Context context) {
        coordLookup = new HashMap<String, Coord>();

        try {
            InputStream inputStream = context.getAssets().open("exhibit_info.json");
            Reader reader = new InputStreamReader(inputStream);

            Gson gson = new Gson();
            Type type = new TypeToken<List<Coords.VertexInfo>>(){}.getType();
            List<Coords.VertexInfo> vertexData = gson.fromJson(reader, type);

            Map<String, Coords.VertexInfo> indexedVertexData = vertexData
                    .stream()
                    .collect(Collectors.toMap(v -> v.id, datum -> datum));

            for (Coords.VertexInfo vertex: vertexData) {
                if (vertex.group_id == null) {
                    coordLookup.put(vertex.name, new Coord(vertex.lat, vertex.lng));
                }
            }

            for (Coords.VertexInfo vertex: vertexData) {
                if (vertex.group_id != null) {
                    coordLookup.put(vertex.name, coordLookup.get(indexedVertexData.get(vertex.group_id).name));
                }
            }

            Log.i("awkjesf", coordLookup.get("Toucan").toString());
            Log.i("awkjesf", coordLookup.get("Parker Aviary").toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param p1 first coordinate
     * @param p2 second coordinate
     * @return midpoint between p1 and p2
     */
    public static Coord midpoint(Coord p1, Coord p2) {
        return Coord.of((p1.lat + p2.lat) / 2, (p1.lng + p2.lng) / 2);
    }

    /**
     * @param p1 start coordinate
     * @param p2 end coordinate
     * @param n number of points between to interpolate.
     * @return a list of evenly spaced points between p1 and p2 (including p1 and p2).
     */
    public static Stream<Coord> interpolate(Coord p1, Coord p2, int n) {
        // Map from i={0, 1, ... n} to t={0.0, 0.1, ..., 1.0} with n divisions.
        ///     t(i; n) = i / n
        // Linear interpolate between l1 and l2 using t:
        //      p(t) = p1 + (p2 - p1) * t

        return IntStream.range(0, n)
                .mapToDouble(i -> (double) i / (double) n)
                .mapToObj(t -> Coord.of(
                        p1.lat + (p2.lat - p1.lat) * t,
                        p1.lng + (p2.lng - p1.lng) * t
                ));
    }
}