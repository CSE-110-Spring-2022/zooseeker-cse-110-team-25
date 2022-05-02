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

        List<NodeItem> todos = NodeItem.loadJSON(context, "sample_node_info.json");
        dao = db.nodeInfoDao();
        dao.insertAll(todos);
    }

    @Test
    public void testid() {
        Search s = new Search("elephant_odyssey", dao);

        List<String> res = s.searchAllCategory();

        List<String> expected = new ArrayList<>();
        expected.add("Elephant Odyssey");

        assertEquals(expected, res);
    }

    @Test
    public void testidsubstring() {
        Search s = new Search("elephant", dao);

        List<String> res = s.searchAllCategory();

        List<String> expected = new ArrayList<>();
        expected.add("Elephant Odyssey");

        assertEquals(expected, res);
    }

    @Test
    public void testmm() {
        Search s = new Search("mm", dao);

        List<String> res = s.searchAllCategory();

        List<String> expected = new ArrayList<>();
        expected.add("Gorillas");
        expected.add("Lions");
        expected.add("Elephant Odyssey");
        expected.add("Arctic Foxes");

        assertEquals(expected, res);
    }

    @Test
    public void testKind(){
        Search s = new Search("exhibit", dao);

        List<String> res = s.searchAllCategory();

        List<String> expected = new ArrayList<>();
        expected.add("Gorillas");
        expected.add("Alligators");
        expected.add("Lions");
        expected.add("Elephant Odyssey");
        expected.add("Arctic Foxes");
        assertEquals(expected, res);
    }



    @After
    public void closeDb() throws IOException {
        db.close();
    }
}
