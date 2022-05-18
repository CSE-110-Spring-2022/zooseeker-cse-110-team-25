package com.example.zooseeker25;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Settings extends AppCompatActivity {
    TextView detailedBtn;
    TextView briefBtn;
    int detailedDirections = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        detailedBtn = findViewById(R.id.directions_detailed);
        briefBtn = findViewById(R.id.directions_brief);

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

    public void onDetailedClicked(View view) {
        briefBtn.setBackgroundColor(Color.WHITE);
        detailedBtn.setBackgroundColor(Color.LTGRAY);
        detailedDirections = 1;
    }

    public void onBriefClicked(View view) {
        detailedBtn.setBackgroundColor(Color.WHITE);
        briefBtn.setBackgroundColor(Color.LTGRAY);
        detailedDirections = 0;
    }

    public void onExitClicked(View view) {
        setResult(detailedDirections);
        finish();
    }
}
