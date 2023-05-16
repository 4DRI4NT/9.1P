package com.example.a91p;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CurrentPlaceActivity extends Activity {

    ArrayList<String> placeNameList = new ArrayList<>();
    RecyclerView placesRecycler;
    PlaceRecyclerViewAdapter recyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_place);

        //collect list
        Intent intent = getIntent();
        placeNameList = intent.getStringArrayListExtra("places");

        //link recyclerview to element
        placesRecycler = findViewById(R.id.placesRecyclerView);

        //configure adapter and set to recycler with layout
        recyclerViewAdapter = new PlaceRecyclerViewAdapter(placeNameList, this, this::onItemClick);
        placesRecycler.setAdapter(recyclerViewAdapter);
        placesRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    private void onItemClick(int i) {
        //save place name
        String selection = placeNameList.get(i);

        //pass selection as result
        Intent intent = new Intent();
        intent.putExtra("selection", selection);
        setResult(Activity.RESULT_OK, intent);

        //close activity
        finish();
    }
}
