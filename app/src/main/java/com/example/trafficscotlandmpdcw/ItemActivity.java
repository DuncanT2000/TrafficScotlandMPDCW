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

        addToJourneyBTN = findViewById(R.id.addToJourneyBTN);
        

        /*
        MapFragment fragment = new MapFragment();
        Bundle bundle = new Bundle();
        bundle.putString("itemLat", item.getLatStr());
        bundle.putString("itemLong", item.getLongStr());
        fragment.setArguments(bundle);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.map_fragment,fragment)
                 .commit();

        */

        addToJourneyBTN.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {

        if (view == addToJourneyBTN){

            Toast.makeText(getApplicationContext(),"Roadwork has been added to your journey!", Toast.LENGTH_SHORT).show();

        }

    }
}