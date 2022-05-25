package com.example.zooseeker25;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

public class Search_Display_Activity extends AppCompatActivity implements Observer {
    private final static String DATA_PATH = "sample_node_info.json";

    private SearchResultsViewModel viewModel;
    public RecyclerView recyclerView;
    private SearchStorage searchStorage;
    private NodeInfoDao dao;
    private ItemDatabase db;
    private Search search;
    SearchView simpleSearchView;
    TextView titleText;
    TextView listCounter;
    RecyclerView searchResults;
    TextView animalItem;
    Button viewRouteBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search_display);
        viewModel = new ViewModelProvider(this)
                .get(SearchResultsViewModel.class);

        //initializing dao
        db = Room.inMemoryDatabaseBuilder(this, ItemDatabase.class)
                .allowMainThreadQueries()
                .build();
        ItemDatabase.injectTestDatabase(db);
        List<NodeItem> todos = NodeItem.loadJSON(this, DATA_PATH);
        dao = db.nodeInfoDao();
        dao.insertAll(todos);

        searchStorage = new SearchStorage(this);


        //getting all of the elements on the UI
        simpleSearchView = findViewById(R.id.searchView);
        titleText = findViewById(R.id.title_text);
        listCounter = findViewById(R.id.listCounterPlaceHolder);
        searchResults = findViewById(R.id.search_results);
        viewRouteBtn = findViewById(R.id.view_route_btn);
        //loadSearchStorage();
        //initializing the adapter
        SearchResultsAdapter adapter = new SearchResultsAdapter(searchStorage, dao);
        adapter.setHasStableIds(true);
        adapter.setOnAnimalClickedHandler(viewModel::selectAnimal);

        //getting the search results and assigning it to the adapter
        recyclerView = findViewById(R.id.search_results);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);



        //handling changes to search bar query
        simpleSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            //called every time you press "enter" inside the search bar
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            //called every time the query inside the search bar changes
            //with newText being the query
            public boolean onQueryTextChange(String newText) {
                search = new Search(newText, dao);
                searchStorage.updateResultsList(search.searchAllCategory());
                adapter.setSearchListItems(searchStorage.getResultsList());

                if (newText.equals("")) {
                    titleText.setVisibility(View.VISIBLE);
                    searchResults.setVisibility(View.INVISIBLE);
                } else {
                    titleText.setVisibility(View.INVISIBLE);
                    searchResults.setVisibility(View.VISIBLE);
                }
                return true;
            }
        });

        //called every time you close the search bar through the "x" button
        simpleSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                titleText.setVisibility(View.VISIBLE);
                searchResults.setVisibility(View.INVISIBLE);
                return true;
            }
        });
        this.animalItem = this.findViewById(R.id.search_item_text);
        listCounter.setText("0");
    }
    @Override
    protected void onResume(){
        super.onResume();
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this)
                .setTitle("Load Previous Instance?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        loadSearchStorage();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        return;
                    }
                });
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }
    public void onViewRouteClicked(View view) {
        Intent intent = new Intent(this, ListOfAnimalsActivity.class);
        ArrayList<String> tempNames = new ArrayList<>(searchStorage.getSelectedAnimalsNames());
        intent.putExtra("selected_list_names", tempNames.toArray());
        ArrayList<String> tempIDs = new ArrayList<>(searchStorage.getSelectedAnimalsIDs());
        intent.putExtra("selected_list_ids", tempIDs.toArray());
        startActivity(intent);
    }

    @Override
    public void update(Observable observable, Object o) {
        listCounter.setText((String)o);
        if (!listCounter.getText().equals("0")) {
            viewRouteBtn.setVisibility(View.VISIBLE);
        }
    }

    protected void onPause() {
        super.onPause();
        saveSearchStorage();
    }
    public void saveSearchStorage(){
        SharedPreferences preferences = getSharedPreferences("test",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        Set<String> names = searchStorage.getSelectedAnimalsNames();
        Set<String> ids = searchStorage.getSelectedAnimalsIDs();

        String namesstring ="",idsstring = "";
        for(String s : names){
            namesstring = s+"#"+namesstring;

        }
        for(String t : ids){
            idsstring = t+"#"+namesstring;
        }
        if(namesstring.length()!=0){
            namesstring = namesstring.substring(0,namesstring.length()-1);
            idsstring = idsstring.substring(0,idsstring.length()-1);
        }

        editor.putString("storenames",namesstring);
        editor.putString("storeids",idsstring);
        editor.apply();
        Log.i("close",preferences.getString("storenames","none"));
    }

    public void loadSearchStorage(){
        Log.i(".","starting load");
        SharedPreferences preferences = getSharedPreferences("test",MODE_PRIVATE);
        Log.i("starting",preferences.getString("storenames","cant find") + "::::");

        String namesstring = preferences.getString("storenames",null);
        String idsstring = preferences.getString("storeids",null);

        if (namesstring == null || idsstring == null) {
            return;
        }

        String[] names = namesstring.split("#");
        String[] ids = idsstring.split("#");

        for(int i=0; i<names.length; i++){
            searchStorage.addSelectedAnimal(names[i],ids[i]);
        }
    }
}
