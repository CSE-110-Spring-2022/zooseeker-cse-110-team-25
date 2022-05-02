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

    //temp
    String[] mockData = {"Gorillas", "Alligators", "Lions", "Elephant Odyssey", "Arctic Foxes"};

    public SearchStorage(Observer observer) {
        this.observer = observer;
        selectedAnimals = new TreeSet<>();
        //temporary
        resultsList = new ArrayList<>();
        for (String s : mockData) {
            resultsList.add(new SearchResultsItem(s, false, 0));
        }
    }

    public List<SearchResultsItem> getResultsList() {
        return resultsList;
    }

    public void setResultsList(List<SearchResultsItem> newList) {
        resultsList = newList;
    }

    public void addSelectedAnimal(String s) {
        selectedAnimals.add(s);
        observer.update(this, Integer.toString(selectedAnimals.size()));
    }
}
