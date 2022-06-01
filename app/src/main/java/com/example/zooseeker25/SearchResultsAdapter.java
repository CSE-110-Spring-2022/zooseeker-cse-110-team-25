package com.example.zooseeker25;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class SearchResultsAdapter extends RecyclerView.Adapter<SearchResultsAdapter.ViewHolder> {
    private List<SearchResultsItem> searchResults = Collections.emptyList();
    private Consumer<SearchResultsItem> onAnimalItemClicked;
    private SearchStorage searchStorage;
    private NodeInfoDao dao;

    public SearchResultsAdapter(SearchStorage searchStorage, NodeInfoDao dao) {
        this.searchStorage = searchStorage;
        this.dao = dao;
    }

    //sets the list of results to be displayed
    public void setSearchListItems(List<SearchResultsItem> newSearchResults) {
        this.searchResults = newSearchResults;
        notifyDataSetChanged();
    }

    //handles when an animal in the search is clicked
    public void setOnAnimalClickedHandler(Consumer<SearchResultsItem> onAnimalItemClicked){
        this.onAnimalItemClicked = onAnimalItemClicked;
    }

    @NonNull
    @Override
    public SearchResultsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.search_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    //sets the values in the Viewholder(defined below) in the adapter
    public void onBindViewHolder(@NonNull SearchResultsAdapter.ViewHolder holder, int position) {
        holder.setSearchItem(searchResults.get(position));
    }

    @Override
    public int getItemCount() {
        return searchResults.size();
    }

    //prevents the recyclerview from repeating results
    @Override
    public long getItemId(int position) {
        return position;
    }

    //prevents the recyclerview from repeating results

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private SearchResultsItem searchResultsItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.search_item_text);

            //adds the selected item to the search storage and calls the given Consumer
            this.textView.setOnClickListener(view -> {
                if(onAnimalItemClicked == null) return;
                if (dao.getIDFromName((String) textView.getText()) != null) {
                    onAnimalItemClicked.accept(searchResultsItem);
                    searchStorage.addSelectedAnimal((String) textView.getText(), dao.getIDFromName((String) textView.getText()));
                }
                setSearchItem(searchResultsItem);
            });
        }

        //gets the name of the searchResultsItems and set it as the
        //text in the textView of an individual search_list_item, and updates the color
        public void setSearchItem(SearchResultsItem searchResultsItem) {
            this.searchResultsItem = searchResultsItem;
            textView.setText(dao.getNameFromId(searchResultsItem.name));
            if (this.searchResultsItem.selected) {
                textView.setBackgroundColor(Color.LTGRAY);
            } else {
                textView.setBackgroundColor(Color.WHITE);
            }
        }
    }
}
