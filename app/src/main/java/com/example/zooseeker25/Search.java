package com.example.zooseeker25;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class Search {
    private List<String> animalNames;
    private NodeInfoDao nodedao;
    private String keyword;

    Search(String keyword, NodeInfoDao dao){
        this.keyword = keyword;
        this.animalNames = new ArrayList<String>();
        this.nodedao = dao;

        //Context context = ApplicationProvider.getApplicationContext();

        /*Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, ItemDatabase.class)
                .allowMainThreadQueries()
                .build();
        ItemDatabase.injectTestDatabase(db);

        List<NodeItem> todos = NodeItem.loadJSON(context, "sample_node_info.json");
        dao = db.nodeInfoDao();
        dao.insertAll(todos);
         */


    }

    private void searchID(){
        List<NodeItem> items = nodedao.findId(keyword);
        for (NodeItem item : items){
            if (!this.animalNames.contains(item.name)){
                this.animalNames.add(item.name);
            }
        }
    }

    private void searchKind(){
        List<NodeItem> items = nodedao.findKind(keyword);
        for (NodeItem item : items){
            if (!this.animalNames.contains(item.name)){
                this.animalNames.add(item.name);
            }
        }
    }

    private void searchName(){
        List<NodeItem> items = nodedao.findName(keyword);
        for (NodeItem item : items){
            if (!this.animalNames.contains(item.name)){
                this.animalNames.add(item.name);
            }
        }
    }

    private void searchTag(){
        List<NodeItem> items = nodedao.findTag(keyword);
        for (NodeItem item : items){
            if (!this.animalNames.contains(item.name)){
                this.animalNames.add(item.name);
            }
        }
    }

    public List<String> searchAllCategory(){
        this.searchName();
        this.searchID();
        this.searchTag();
        this.searchKind();
        return this.animalNames;
    }
}
