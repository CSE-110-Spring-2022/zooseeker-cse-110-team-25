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

/**
 * ItemDatabase that makes the database
 */
@Database(entities = {NodeItem.class}, version = 1, exportSchema=false)
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

    /** WRONG CASE
     * 1. getSingleton is called.
     * 2. singleton == null, so we call makeDatabase
     * 3. makeDatabase registers onCreate callback
     * 4. makeDatabase calls .build()
     * 5. .build() calls onCreate callback
     * 6. onCreate callback calls getSingleton(...)
     * 7. LOOP FOREVER
     * 8. ???
     * 9. makeDatabase returns DB to getSingleton
     * 10. getSingleton stores DB and returns it
     */

    /** GOOD CASE
     * 1. [Thread #1] getSingleton is called on Thread #1 (Main Thread).
     * 2. [Thread #1] singleton == null, so we call makeDatabase
     * 3. [Thread #1] makeDatabase registers onCreate callback
     * 4. [Thread #1] makeDatabase calls .build()
     * 5. .build() calls onCreate callback which runs its code on Thread #2
     * 6. [Thread #2] onCreate callback calls getSingleton(...)
     * 7. [Thread #2] BLOCKS waiting for getSingleton, which is synchronized
     *          (only one thread can call it at a time. #2 must wait for #1 to be done)
     * 8. [Thread #1] Executors.newSingleThread....execute(...) returns *immediately*.
     * 8. [Thread #1] makeDatabase returns DB to getSingleotn
     * 9. [Thread #1] getSingleton stores DB and returns it
     * 10. [Thread #2] no longer blocked waiting for synchronization.
     * 11. [Thread #2] calls getSingleton, gets the dao, inserts the nodes.
     */

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

    /**
     * no need to make a new database if already made one
     * @param testDatabase
     */
    @VisibleForTesting
    public static void injectTestDatabase(ItemDatabase testDatabase){
        if (singleton != null){
            singleton.close();
        }
        singleton = testDatabase;
    }
}
