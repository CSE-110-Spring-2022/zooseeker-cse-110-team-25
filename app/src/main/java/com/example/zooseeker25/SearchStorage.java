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
        //clear previous resultsList
        resultsList.removeAll(resultsList);
        //add items fro newList and check to see if they have been selected previously
        for (String item : newList) {
            Boolean selected = selectedAnimals.contains(item);
            resultsList.add(new SearchResultsItem(item, selected));
        }
    }

    public Set<String> getSelectedAnimals(){
        return selectedAnimals;
    }

    public void addSelectedAnimal(String s) {
        selectedAnimals.add(s);
        observer.update(this, Integer.toString(selectedAnimals.size()));
    }
}
