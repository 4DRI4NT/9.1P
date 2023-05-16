package com.example.a91p;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button createAdvert, showAdverts, showMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //link elements to id
        createAdvert = findViewById(R.id.newAdvertButton);
        showAdverts = findViewById(R.id.showAdvertsButton);
        showMap = findViewById(R.id.showMapButton);

        createAdvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create and start newAdvertActivity
                Intent advertIntent = new Intent(MainActivity.this, NewAdvertActivity.class);
                startActivity(advertIntent);
            }
        });

        showAdverts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create and start lostFoundActivity
                Intent advertIntent = new Intent(MainActivity.this, LostFoundActivity.class);
                startActivity(advertIntent);
            }
        });

        showMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create and start mapsActivity
                Intent mapsIntent = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(mapsIntent);
            }
        });
    }
}