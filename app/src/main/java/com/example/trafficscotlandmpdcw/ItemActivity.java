package com.example.trafficscotlandmpdcw;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ItemActivity extends AppCompatActivity implements View.OnClickListener {

    private Button addToJourneyBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        int itemPos = getIntent().getIntExtra("itemPos", 0);



        addToJourneyBTN = findViewById(R.id.addToJourneyBTN);


        addToJourneyBTN.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {

        if (view == addToJourneyBTN){

            Toast.makeText(getApplicationContext(),"Roadwork has been added to your journey!", Toast.LENGTH_SHORT).show();

        }

    }
}