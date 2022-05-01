package com.example.zooseeker25;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {NodeItem.class}, version = 1)
public abstract class ItemDatabase extends RoomDatabase {
    public abstract NodeInfoDao nodeInfoDao();
}
