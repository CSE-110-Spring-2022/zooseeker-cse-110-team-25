package com.example.zooseeker25;

import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ActivityScenario;
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

    /*@Test
    public void example() {
        final ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);


        scenario.onActivity(activity -> {

        });
    }*/

    @Test
    public void testid() {
        Search s = new Search("elephant_odyssey", dao);

        List<String> res = s.searchAllCategory();

        List<String> expected = new ArrayList<>();
        expected.add("elephant_odyssey");
        assertEquals(expected, res);
    }

    @Test
    public void testidsubstring() {
        Search s = new Search("elephant", dao);

        List<String> res = s.searchAllCategory();

        List<String> expected = new ArrayList<>();
        expected.add("elephant_odyssey");

        assertEquals(expected, res);
    }

    @Test
    public void testmm() {
        Search s = new Search("mm", dao);

        List<String> res = s.searchAllCategory();

        List<String> expected = new ArrayList<>();
        expected.add("gorillas");
        expected.add("lions");
        expected.add("elephant_odyssey");
        expected.add("arctic_foxes");

        assertEquals(expected, res);
    }

    @Test
    public void testKind(){
        Search s = new Search("exhibi", dao);

        List<String> res = s.searchAllCategory();

        List<String> expected = new ArrayList<>();
        expected.add("gorillas");
        expected.add("gators");
        expected.add("lions");
        expected.add("elephant_odyssey");
        expected.add("arctic_foxes");
        assertEquals(expected, res);
    }

    @Test
    public void testSearchLi(){
        Search s = new Search("Li", dao);

        List<String> res = s.searchAllCategory();

        List<String> expected = new ArrayList<>();
        expected.add("gators");
        expected.add("lions");
        assertEquals(expected, res);
    }
    @Test
    public void testSearchFox(){
        Search s = new Search("fox", dao);

        List<String> res = s.searchAllCategory();

        List<String> expected = new ArrayList<>();
        expected.add("arctic_foxes");
        assertEquals(expected, res);

        s = new Search("foxes", dao);
        res = s.searchAllCategory();
        assertEquals(expected, res);
    }

    @Test
    public void testSearchByTypeCat(){
        Search s = new Search("cat", dao);

        List<String> res = s.searchAllCategory();

        List<String> expected = new ArrayList<>();
        expected.add("lions");
        assertEquals(expected, res);
    }

    @Test
    public void testSearchByTypeMonkey(){
        Search s = new Search("mon", dao);

        List<String> res = s.searchAllCategory();

        List<String> expected = new ArrayList<>();
        expected.add("gorillas");
        assertEquals(expected, res);
    }

    @Test
    public void testSearchByTypeReptile(){
        Search s = new Search("rep", dao);

        List<String> res = s.searchAllCategory();

        List<String> expected = new ArrayList<>();
        expected.add("gators");
        assertEquals(expected, res);
    }

    @Test
    public void testInvalid(){
        Search s = new Search("foxs", dao);

        List<String> res = s.searchAllCategory();

        List<String> expected = new ArrayList<>();
        expected.add("Search Not Found");
        assertEquals(expected, res);

        s = new Search("mmals", dao);
        res = s.searchAllCategory();
        assertEquals(expected, res);
    }

    @Test
    public void testNotExhibit(){
        Search s = new Search("ga", dao);
        List<String> res = s.searchAllCategory();

        List<String> expected = new ArrayList<>();
        expected.add("gators");
        assertEquals(expected, res);
    }


    @After
    public void closeDb() throws IOException {
        db.close();
    }
}
