package com.example.zooseeker25;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {NodeItem.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class ItemDatabase extends RoomDatabase {
    public abstract NodeInfoDao nodeInfoDao();
}
