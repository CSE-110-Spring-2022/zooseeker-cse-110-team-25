package com.example.zooseeker25;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * The search class that performs the search operation.
 * Using the Dao interface to perform search
 * Search for substring overlap in each category, eliminate the overlapping Name
 */
public class Search {
    private List<String> animalNames;
    private NodeInfoDao nodedao;
    private String keyword;

    Search(String keyword, NodeInfoDao dao){
        this.keyword = keyword;
        this.animalNames = new ArrayList<>();
        this.nodedao = dao;
    }

    //searches for related ids and appends to animalNames
    private void searchID(){
        List<NodeItem> items = nodedao.findId(keyword);
        for (NodeItem item : items){
            if (item.kind != NodeItem.Kind.INTERSECTION){
                if (!this.animalNames.contains(item.id)){
                    this.animalNames.add(item.id);
                }
            }
        }
    }

    //searches for related kinds and appends to animalNames
    private void searchKind(){
        List<NodeItem> items = nodedao.findKind(keyword);
        for (NodeItem item : items){
            if (item.kind != NodeItem.Kind.INTERSECTION){
                if (!this.animalNames.contains(item.id)){
                    this.animalNames.add(item.id);
                }
            }
        }
    }

    //searches for related names and appends to animalNames
    private void searchName(){
        List<NodeItem> items = nodedao.findName(keyword);
        for (NodeItem item : items){
            if (item.kind != NodeItem.Kind.INTERSECTION){
                if (!this.animalNames.contains(item.id)){
                    this.animalNames.add(item.id);
                }
            }
        }
    }

    //searches for related tags and appends to animalNames
    private void searchTag(){
        List<NodeItem> items = nodedao.findTag(keyword);
        for (NodeItem item : items){
            if (item.kind != NodeItem.Kind.INTERSECTION){
                if (!this.animalNames.contains(item.id)){
                    this.animalNames.add(item.id);
                }
            }
        }
    }

    /**
     * Return the list of animalNames that corresponds to the substring
     * Return "Search Not Found" as the first element in the list no overlap found
     * @return List<String> animalNames
     */
    public List<String> searchAllCategory(){
        this.searchName();
        this.searchID();
        this.searchTag();
        this.searchKind();
        if (this.animalNames.size() == 0){
            this.animalNames.add("Search Not Found");
            return this.animalNames;
        }
        return this.animalNames;
    }
}
