package com.example.zooseeker25;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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

    //sets the list of results to be displayed
    public void setSearchListItems(List<SearchResultsItem> newSearchResults) {
        this.searchResults.clear();
        this.searchResults = newSearchResults;
        notifyDataSetChanged();
    }

//    public void setOnAnimalClickedHandler(Consumer<AnimalItem> onAnimalItemClicked){
//        this.onAnimalItemClicked = onAnimalItemClicked;
//    }

    @NonNull
    @Override
    //im not gonna lie i dont quite understand the intricacies of this function
    //i just know it somehow formats the recycler view to contain search_list_items
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


    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private SearchResultsItem searchResultsItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.search_item_text);



//            this.checkBox.setOnClickListener(view -> {
//                if(onCheckBoxClicked == null) return;
//                onCheckBoxClicked.accept(todoItem);
//            });
            //non-functional onclick behavior for search results
            this.textView.setOnClickListener(view -> {

                if(onAnimalItemClicked == null) return;
                onCheckBoxClicked.accept(todoItem);


                if (((ColorDrawable)textView.getBackground()).getColor() != Color.WHITE) {
                    textView.setBackgroundColor(Color.LTGRAY);
                } else {
                    textView.setBackgroundColor(Color.WHITE);
                }
            });
        }

        //gets the name of the searchResultsItems and set it as the
        //text in the textView of an individual search_list_item
        public void setSearchItem(SearchResultsItem searchResultsItem) {
            this.searchResultsItem = searchResultsItem;
            this.textView.setText(searchResultsItem.name);
        }

    }
}
