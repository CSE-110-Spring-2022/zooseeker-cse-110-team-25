package com.example.zooseeker25;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class DirectionsAdapter extends RecyclerView.Adapter<DirectionsAdapter.ViewHolder> {
    private List<String> directionsList = new ArrayList<>();

    public void setDirectionsList(List<String> directionsList) {
        this.directionsList = directionsList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DirectionsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.direction_list_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DirectionsAdapter.ViewHolder holder, int position) {
        String direction_text = directionsList.get(position);

        holder.directionText.setText(direction_text);
    }

    @Override
    public int getItemCount() { return directionsList.size(); }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView directionText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            directionText = itemView.findViewById(R.id.direction_text);
        }
    }
}
