package com.example.zooseeker25;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

//The settings page that allows you to switch app functionality
public class Settings extends AppCompatActivity {
    TextView detailedBtn;
    TextView briefBtn;
    private int detailedDirections;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        detailedBtn = findViewById(R.id.directions_detailed);
        briefBtn = findViewById(R.id.directions_brief);

        //gets currently selected detail option and highlights the correct button
        detailedDirections = (int) getIntent().getSerializableExtra("detailedDirections");
        if (detailedDirections == 0) {
            briefBtn.setBackgroundColor(Color.LTGRAY);
        } else {
            detailedBtn.setBackgroundColor(Color.LTGRAY);
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

    //selects detailed directions option
    public void onDetailedClicked(View view) {
        briefBtn.setBackgroundColor(Color.WHITE);
        detailedBtn.setBackgroundColor(Color.LTGRAY);
        detailedDirections = 1;
    }

    //selects brief directions option
    public void onBriefClicked(View view) {
        detailedBtn.setBackgroundColor(Color.WHITE);
        briefBtn.setBackgroundColor(Color.LTGRAY);
        detailedDirections = 0;
    }

    //sets the currently selected details option as the result and finishes
    public void onExitClicked(View view) {
        setResult(detailedDirections);
        finish();
    }
}
