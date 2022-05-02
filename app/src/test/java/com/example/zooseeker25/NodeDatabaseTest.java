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

    /*@Before
    public void createDb(){
        Context context = ApplicationProvider.getApplicationContext();
        db =
        db = Room.inMemoryDatabaseBuilder(context, ItemDatabase.class)
                .allowMainThreadQueries()
                .build();
        dao = db.nodeInfoDao();
    }*/

    @Before
    public void resetDatabase(){
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, ItemDatabase.class)
                .allowMainThreadQueries()
                .build();
        ItemDatabase.injectTestDatabase(db);

        List<NodeItem> todos = NodeItem.loadJSON(context, "demo_todos.json");
        dao = db.nodeInfoDao();
        dao.insertAll(todos);
    }

    @Test
    public void testid() {
        Search s = new Search("elephant_odyssey");

        List<NodeItem> res = dao.findId("elephant_odyssey");
        List<String> tags = new ArrayList<>();
        tags.add("elephant");
        tags.add("mammal");
        tags.add("africa");
        NodeItem expected = new NodeItem("elephant_odyssey", NodeItem.Kind.EXHIBIT, "Elephant Odyssey", tags);
        assertEquals(expected, res);
    }

    @Test
    public void testFindName(){
        List<NodeItem> res = dao.findName("Lions");
        List<String> tags = new ArrayList<>();
        tags.add("lions");
        tags.add("cats");
        tags.add("mammal");
        tags.add("africa");
        NodeItem expected = new NodeItem("lions", NodeItem.Kind.EXHIBIT,"Lions", tags);
        assertEquals(res, expected);
    }

    /*@Test
    public void testFindKind(){
        List<NodeItem> res = dao.findKind(NodeItem.Kind.GATE);
        List<String> tags = new ArrayList<>();
        tags.add("enter");
        tags.add("leave");
        tags.add("start");
        tags.add("begin");
        tags.add("entrance");
        tags.add("exit");
        List<NodeItem> expected = new ArrayList<>();
        NodeItem nodeExpected = new NodeItem("entrance_exit_gate", NodeItem.Kind.GATE,"Entrance and Exit Gate", tags);
        expected.add(nodeExpected);
        assertEquals(res, expected);
    }

    @Test
    public void testFindTag(){
        List<NodeItem> res = dao.findTag("africa");
        List<NodeItem> expected = new ArrayList<>();
        List<String> tagsLion = new ArrayList<>();
        tagsLion.add("lions");
        tagsLion.add("cats");
        tagsLion.add("mammal");
        tagsLion.add("africa");
        NodeItem expectedLion = new NodeItem("lions", NodeItem.Kind.EXHIBIT,"Lions", tagsLion);
        List<String> tagsElephant = new ArrayList<>();
        tagsElephant.add("elephant");
        tagsElephant.add("mammal");
        tagsElephant.add("africa");
        NodeItem expectedElephant = new NodeItem("elephant_odyssey", NodeItem.Kind.EXHIBIT, "Elephant Odyssey", tagsElephant);
        expected.add(expectedLion);
        expected.add(expectedElephant);
        assertEquals(res, expected);
    }*/

    @After
    public void closeDb() throws IOException {
        db.close();
    }
}
