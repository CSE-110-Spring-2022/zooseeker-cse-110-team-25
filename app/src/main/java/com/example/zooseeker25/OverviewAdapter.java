package com.example.zooseeker25;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.List;

public class OverviewAdapter extends RecyclerView.Adapter<OverviewAdapter.ViewHolder> {

    private Route[] selectedExhibits;
    int totalDistance = 0;

    public OverviewAdapter(Object[] temp) {
        this.selectedExhibits = Arrays.copyOf(temp, temp.length, Route[].class);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.selected_exhibit_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        totalDistance += (int)selectedExhibits[position].totalDistance;
        holder.setSelectedExhibit((position + 1) + ") "  + selectedExhibits[position].exhibit + " : " + totalDistance +  " Ft.");
    }

    @Override
    public int getItemCount() {
        return selectedExhibits.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.selected_exhibit_name);
        }

        public void setSelectedExhibit(String s) {
            textView.setText(s);
        }

    }
}