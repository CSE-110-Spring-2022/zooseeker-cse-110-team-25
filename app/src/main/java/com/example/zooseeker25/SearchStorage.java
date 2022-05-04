package com.example.zooseeker25;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.TreeSet;

public class SearchStorage extends Observable {
    private List<SearchResultsItem> resultsList;
    private Set<String> selectedAnimals;
    private Observer observer;

    public SearchStorage(Observer observer) {
        this.observer = observer;
        selectedAnimals = new TreeSet<>();
        resultsList = new ArrayList<>();
    }

    public List<SearchResultsItem> getResultsList() {
        return resultsList;
    }

    public void updateResultsList(List<String> newList) {
        //if the current results list has results not present in the new list
        //remove them
        ArrayList<SearchResultsItem> toRemove = new ArrayList<>();
        for (SearchResultsItem item : resultsList) {
            if (!newList.contains(item)) {
                toRemove.add(item);
            }
        }
        resultsList.removeAll(toRemove);
        //if the new list has results not present in the current results list
        //add them and check to see if they have been selected previously
        for (String item : newList) {
            if (!resultsList.contains(item)) {
                resultsList.add(new SearchResultsItem(item, selectedAnimals.contains(item), 0));
            }
        }
    }

    public void addSelectedAnimal(String s) {
        selectedAnimals.add(s);
        observer.update(this, Integer.toString(selectedAnimals.size()));
    }
}
