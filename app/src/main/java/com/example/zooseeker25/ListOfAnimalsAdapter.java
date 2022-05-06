package com.example.zooseeker25;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ListOfAnimalsAdapter extends RecyclerView.Adapter<ListOfAnimalsAdapter.ViewHolder>{
    private List<String> selectedAnimals = Collections.emptyList();

    public ListOfAnimalsAdapter() {}

    public void setSelectedAnimals (List<String> selectedAnimals) {
        this.selectedAnimals = selectedAnimals;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ListOfAnimalsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.selected_animal_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setSelectedAnimal(selectedAnimals.get(position));
    }

    @Override
    public int getItemCount() {
        return selectedAnimals.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.selected_animal_name);
        }

        public void setSelectedAnimal(String s) {
            textView.setText(s);
        }
    }
}
