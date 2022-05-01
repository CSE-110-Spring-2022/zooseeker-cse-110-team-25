package com.example.zooseeker25;

import androidx.room.TypeConverter;

import java.util.Arrays;
import java.util.List;

public class Converters {
    @TypeConverter
    public static String listToString(List<String> tags) {
        String value = "";
        for (String tag:tags)
            value += tag + ",";
        return value;
    }

    @TypeConverter
    public static List<String> stringToList(String s){
        return Arrays.asList(s.split("\\s*,\\s*"));
    }
}
