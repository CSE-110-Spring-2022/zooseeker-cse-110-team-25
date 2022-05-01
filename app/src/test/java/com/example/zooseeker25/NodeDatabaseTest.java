package com.example.zooseeker25;

import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    public void testid(){
        List<NodeItem> res = dao.findId("odyssey");
        List<String> tags = new ArrayList<>();
        tags.add("elephant");
        tags.add("mammal");
        tags.add("africa");
        NodeItem expected = new NodeItem("elephant_odyssey", NodeItem.Kind.EXHIBIT,"Elephant Odyssey", tags);
        assertEquals(res, expected);
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }
}
