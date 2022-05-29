package com.example.zooseeker25;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RouteListAdapter extends RecyclerView.Adapter<RouteListAdapter.ViewHolder> {
    private List<Route> routeList = new ArrayList();

    public void setRouteList(List<Route> routeList) {
        this.routeList = routeList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RouteListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.route_list_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RouteListAdapter.ViewHolder holder, int position) {
        String route = routeList.get(position).intro;
        String direction = routeList.get(position).getDirections().get(0);

        holder.routeText.setText(route);
        holder.directionText.setText(direction);
    }

    @Override
    public int getItemCount() {
        return routeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView routeText;
        private TextView directionText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            routeText = itemView.findViewById(R.id.route_name);
            directionText = itemView.findViewById(R.id.direction_name);
        }
    }
}
