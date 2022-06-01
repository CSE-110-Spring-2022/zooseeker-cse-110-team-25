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

//handles the display of the search screen
public class Search_Display_Activity extends AppCompatActivity implements Observer {
    private final static String DATA_PATH = "sample_vertex_info.json";

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
        Log.d("Search_Display_Activity", "onCreate");

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search_display);
        viewModel = new ViewModelProvider(this)
                .get(SearchResultsViewModel.class);

        //initializing dao
        initializeDao();

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
                adapter.setSearchListItems(new ArrayList<SearchResultsItem>(searchStorage.getResultsList()));

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

    private void initializeDao() {
        db = Room.inMemoryDatabaseBuilder(this, ItemDatabase.class)
                .allowMainThreadQueries()
                .build();
        ItemDatabase.injectTestDatabase(db);
        List<NodeItem> todos = NodeItem.loadJSON(this, DATA_PATH);
        dao = db.nodeInfoDao();
        dao.insertAll(todos);
    }

    //passes the selected animal's names and ids to the following activity, ListOfAnimalsActivity
    public void onViewRouteClicked(View view) {
        Intent intent = new Intent(this, ListOfAnimalsActivity.class);
        ArrayList<String> tempNames = new ArrayList<>(searchStorage.getSelectedAnimalsNames());
        intent.putExtra("selected_list_names", tempNames.toArray());
        ArrayList<String> tempIDs = new ArrayList<>(searchStorage.getSelectedAnimalsIDs());
        intent.putExtra("selected_list_ids", tempIDs.toArray());
        startActivity(intent);
    }

    //Clears the selected animals
    public void onViewClearClicked (View view){
        Log.d("Search_Display", "onViewClearClicked");
        searchStorage.resetSearchStorage();
        viewRouteBtn.setVisibility(View.INVISIBLE);
    }

    //Updates the list counter whenever searchstorage is updated
    @Override
    public void update(Observable observable, Object o) {
        listCounter.setText((String)o);
        if (!listCounter.getText().equals("0")) {
            viewRouteBtn.setVisibility(View.VISIBLE);
        }
    }

    //Saves the selected animals
    @Override
    protected void onPause() {
        super.onPause();
        saveSearchStorage();
    }

    //Loads the selected animals
    @Override
    protected void onResume(){
        Log.d("Search_Display_Activity", "onResume");
        super.onResume();
        loadSearchStorage();
    }

    //compresses the selected list of animals into a stream and saves them using sharedpreferences
    public void saveSearchStorage(){
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        // Store selectedAnimalsNameStorage as a String, then put it into editor
        // Because SharedPreferences can only work with primitive type

        Set<String> names = searchStorage.getSelectedAnimalsNames();
        Set<String> ids = searchStorage.getSelectedAnimalsIDs();

        if (names.size()==0){
            editor.putString("names", null);

        }
        else {
            String resName = "";
            for (String name : names){
                resName += name + ",";
            }
            resName = resName.substring(0,resName.length()-1);
            editor.putString("names", resName);
        }

        if (ids.size() == 0){
            editor.putString("ids", null);
        }
        else {
            String resName = "";
            for (String name : names){
                resName += name + ",";
            }
            resName = resName.substring(0,resName.length()-1);
            editor.putString("names", resName);
        }

        if (ids.size() == 0){
            editor.putString("ids", null);
        }

        else{
            String resIds = "";
            for (String id : ids){
                resIds += id + ",";
            }
            resIds = resIds.substring(0, resIds.length()-1);
            editor.putString("ids", resIds);
        }

        editor.apply();
    }

    //fetches and decompresses the saved string of selected animals
    public void loadSearchStorage(){
        Log.d("Search_Display_Activity", "loadSearchStorage");
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);

        String resName = preferences.getString("names", null);
        //Log.d("Search_Display_Activity", resName);
        if (resName == null){
            return;
        }
        String[] names = resName.split("\\s*,\\s*");
        Log.d("Search_Display_Activity", Integer.toString(names.length));

        String resIds = preferences.getString("ids", null);
        if (resIds == null){
            return;
        }
        String[] ids = resIds.split("\\s*,\\s*");
        Log.d("Search_Display_Activity", Integer.toString(ids.length));


        for(int i=0; i<names.length; i++){
            searchStorage.addSelectedAnimal(names[i],ids[i]);
            Log.d("Search_Display_Activity","addSelected");
        }
    }
}
