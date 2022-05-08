package com.example.zooseeker25;

import android.app.Activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.TreeSet;

public class SearchStorage extends Observable implements Serializable {
    private List<SearchResultsItem> resultsList;
    private Set<String> selectedAnimals;
    private Set<String> selectedAnimalsIDs;
    private Observer observer;

    public SearchStorage(Observer observer) {
        this.observer = observer;
        selectedAnimals = new TreeSet<>();
        selectedAnimalsIDs = new TreeSet<>();
        resultsList = new ArrayList<>();
    }

    public List<SearchResultsItem> getResultsList() {
        return resultsList;
    }

    public void updateResultsList(List<String> newList) {
        //clear previous resultsList
        resultsList.removeAll(resultsList);
        //add items fro newList and check to see if they have been selected previously
        for (String id : newList) {
            Boolean selected = selectedAnimals.contains(id);
            resultsList.add(new SearchResultsItem(id, selected));
        }
    }

    public Set<String> getSelectedAnimalsNames(){
        return selectedAnimals;
    }

    public Set<String> getSelectedAnimalsIDs(){
        return selectedAnimalsIDs;
    }

    public void addSelectedAnimal(String name, String id) {
        selectedAnimals.add(name);
        selectedAnimalsIDs.add(id);
        observer.update(this, Integer.toString(selectedAnimals.size()));
    }
}
