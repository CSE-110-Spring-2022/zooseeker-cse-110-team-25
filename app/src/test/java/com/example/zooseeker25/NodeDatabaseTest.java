package com.example.zooseeker25;

import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class NodeDatabaseTest {
    private NodeInfoDao dao;
    private ItemDatabase db;

    @Before
    public void createDb(){
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, ItemDatabase.class)
                .allowMainThreadQueries()
                .build();
        dao = db.nodeInfoDao();
    }

    @Test
    public void testid() {
        List<NodeItem> res = dao.findId("elephant_odyssey");
        List<String> tags = new ArrayList<>();
        tags.add("elephant");
        tags.add("mammal");
        tags.add("africa");
        NodeItem expected = new NodeItem("elephant_odyssey", NodeItem.Kind.EXHIBIT, "Elephant Odyssey", tags);
        assertEquals(expected, res);
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }
}
