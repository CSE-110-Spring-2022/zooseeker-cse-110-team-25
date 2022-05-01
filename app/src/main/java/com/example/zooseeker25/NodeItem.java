package com.example.zooseeker25;

import android.content.Context;

import androidx.room.Entity;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

@Entity(tableName = "Zoo_node_item")
public class NodeItem {
    public static enum Kind {
        // The SerializedName annotation tells GSON how to convert
        // from the strings in our JSON to this Enum.
        @SerializedName("gate") GATE,
        @SerializedName("exhibit") EXHIBIT,
        @SerializedName("intersection") INTERSECTION
    }

    public String id;
    public Kind kind;
    public String name;
    // tags in a string format
    public String tags;

    NodeItem(String id, Kind kind, String name, List<String> tags){
        this.id = id;
        this.kind = kind;
        this.name = name;
        for (int i = 0; i < tags.size() - 1; i++){
            this.tags += tags.get(i);
            this.tags += ",";
        }
        this.tags += tags.get(tags.size() - 1);
    }

    public static List<NodeItem> loadJSON(Context context, String path){
        try{
            InputStream input = context.getAssets().open(path);
            Reader reader = new InputStreamReader(input);
            Gson gson = new Gson();
            Type type = new TypeToken<List<NodeItem>>(){}.getType();
            return gson.fromJson(reader, type);
        }
        catch (IOException e){
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public String toString() {
        return "NodeItem{" +
                "id=" + this.id +
                ", kind='" + this.kind + '\'' +
                ", name=" + this.name +
                ", tags=" + this.tags +
                '}';
    }
}
