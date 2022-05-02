package com.example.zooseeker25;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

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

    @NonNull
    public String id;
    public Kind kind;
    public String name;
    public List<String> tags;


    NodeItem(@NonNull String id, Kind kind, String name, List<String> tags){
        this.id = id;
        this.kind = kind;
        this.name = name;
        this.tags = tags;
    }

    public List<String> getTags() {
        return this.tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
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
                ", tagString=" + this.tags +
                '}';
    }
}
