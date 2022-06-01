package com.example.zooseeker25;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;

public class ListOfAnimalsAdapter extends RecyclerView.Adapter<ListOfAnimalsAdapter.ViewHolder>{
    private List<String> selectedAnimals = Collections.emptyList();

    public ListOfAnimalsAdapter(String[] newSelectedAnimals) {
        this.selectedAnimals.clear();
        selectedAnimals = new ArrayList<>(Arrays.asList(newSelectedAnimals));
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ListOfAnimalsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("ListOfAnimalsAdapter","onCreateViewHolder");
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.selected_animal_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("ListOfAnimalsAdapter", "onBindViewHolder");
        holder.setSelectedAnimal(selectedAnimals.get(position));
        Log.d("ListOfAnimalsAdapter", selectedAnimals.get(position));
    }


    @Override
    public int getItemCount() {
        return selectedAnimals.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView textView;
        private String animalName;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textView = itemView.findViewById(R.id.selected_animal_name);
        }


        public void setSelectedAnimal(String s) {
            this.animalName = s;
            textView.setText(s);
        }
    }

    public void clear(){
        this.selectedAnimals.clear();
    }


}
