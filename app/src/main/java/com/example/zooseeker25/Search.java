package com.example.zooseeker25;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import java.util.ArrayList;
import java.util.List;

public class Search {
    private List<String> animalNames;
    private NodeInfoDao nodedao;
    private String keyword;

    Search(String keyword){
        this.keyword = keyword;
        this.animalNames = new ArrayList<String>();
        Context context = ApplicationProvider.getApplicationContext();
        this.nodedao = ItemDatabase.getSingleton(context).nodeInfoDao();
    }

    private void searchID(){
        List<NodeItem> items = nodedao.findId(keyword);
        for (NodeItem item : items){
            if (!this.animalNames.contains(item)){
                this.animalNames.add(item.name);
            }
        }
    }

    private void searchKind(){
        List<NodeItem> items = nodedao.findKind(keyword);
        for (NodeItem item : items){
            if (!this.animalNames.contains(item)){
                this.animalNames.add(item.name);
            }
        }
    }

    private void searchName(){
        List<NodeItem> items = nodedao.findName(keyword);
        for (NodeItem item : items){
            if (!this.animalNames.contains(item)){
                this.animalNames.add(item.name);
            }
        }
    }

    private void searchTag(){
        List<NodeItem> items = nodedao.findTag(keyword);
        for (NodeItem item : items){
            if (!this.animalNames.contains(item)){
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
