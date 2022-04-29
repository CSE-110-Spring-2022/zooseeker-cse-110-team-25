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

public class SearchResultsAdapter extends RecyclerView.Adapter<SearchResultsAdapter.ViewHolder> {
    private List<SearchResultsItem> searchResults = Collections.emptyList();

    public void setSearchListItems(List<SearchResultsItem> newSearchResults) {
        this.searchResults.clear();
        this.searchResults = newSearchResults;
        notifyDataSetChanged();
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

            //non-functional onclick behavior for search results
//            this.textView.setOnClickListener(view -> {
//                if (((ColorDrawable)textView.getBackground()).getColor() != Color.WHITE) {
//                    textView.setBackgroundColor(Color.LTGRAY);
//                } else {
//                    textView.setBackgroundColor(Color.WHITE);
//                }
//            });
        }

        public void setSearchItem(SearchResultsItem searchResultsItem) {
            this.searchResultsItem = searchResultsItem;
            this.textView.setText(searchResultsItem.name);
        }

    }
}
