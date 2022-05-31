package com.example.zooseeker25;

import android.app.Activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.TreeSet;

//Stores the list of animals to display under the search bar, and the selected animals
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

    //returns the list of animals to be displayed
    public List<SearchResultsItem> getResultsList() {
        return resultsList;
    }

    //updates the list of animals to be displayed
    public void updateResultsList(List<String> newList) {
        //clear previous resultsList
        resultsList.removeAll(resultsList);
        //add items fro newList and check to see if they have been selected previously
        for (String id : newList) {
            Boolean selected = selectedAnimalsIDs.contains(id);
            resultsList.add(new SearchResultsItem(id, selected));
        }
    }

    //returns the names of the selected animals
    public Set<String> getSelectedAnimalsNames(){
        return selectedAnimals;
    }

    //returns the ids of the selected animals
    public Set<String> getSelectedAnimalsIDs(){
        return selectedAnimalsIDs;
    }

    //adds the name and id of a newly selected animal to their respective sets and updates the list counter
    public void addSelectedAnimal(String name, String id) {
        selectedAnimals.add(name);
        selectedAnimalsIDs.add(id);
        observer.update(this, Integer.toString(selectedAnimals.size()));
    }

    //clears all lists to reset
    public void resetSearchStorage(){
        selectedAnimals.clear();
        selectedAnimalsIDs.clear();
        resultsList.clear();
        observer.update(this, Integer.toString(0));
    }
}
