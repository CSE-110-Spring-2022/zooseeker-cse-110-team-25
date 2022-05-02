package com.example.zooseeker25;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<NodeItem> nodes = NodeItem.loadJSON(this, "sample_node_info.json");
        Log.d("NodeActivity", nodes.get(1).toString());
    }
}