package com.example.zooseeker25;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import org.w3c.dom.NodeList;

import java.util.List;
import java.util.concurrent.Executors;

@Database(entities = {NodeItem.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class ItemDatabase extends RoomDatabase {
    private static ItemDatabase singleton = null;

    public abstract NodeInfoDao nodeInfoDao();

    public synchronized static ItemDatabase getSingleton(Context context) {
        if (singleton == null) {
            singleton = ItemDatabase.makeDatabase(context);
        }
        return singleton;
    }

    private static ItemDatabase makeDatabase(Context context) {
        return Room.databaseBuilder(context, ItemDatabase.class, "Zoo_node_item.db")
                .allowMainThreadQueries()
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        Executors.newSingleThreadScheduledExecutor().execute(() -> {
                            List<NodeItem> nodes = NodeItem
                                    .loadJSON(context, "sample_node_info.json");
                            getSingleton(context).nodeInfoDao().insertAll(nodes);
                        });
                    }
                })
                .build();
    }

    @VisibleForTesting
    public static void injectTestDatabase(ItemDatabase testDatabase){
        if (singleton != null){
            singleton.close();
        }
        singleton = testDatabase;
    }
}
