package com.example.zooseeker25;

import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class sample2Test {
    private NodeInfoDao dao;
    private ItemDatabase db;

    @Before
    public void resetDatabase() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, ItemDatabase.class)
                .allowMainThreadQueries()
                .build();
        ItemDatabase.injectTestDatabase(db);

        List<NodeItem> todos = NodeItem.loadJSON(context, "sample2_node_info.json");
        dao = db.nodeInfoDao();
        dao.insertAll(todos);
    }

    @Test
    public void testid() {
        Search s = new Search("ori", dao);
        List<String> res = s.searchAllCategory();
        List<String> expected = new ArrayList<>();
        expected.add("gorillas");
        assertEquals(expected, res);
    }
}
