package com.example.a91p;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.a91p.data.DatabaseHelper;
import com.example.a91p.data.Advert;

import java.util.ArrayList;
import java.util.List;

public class LostFoundActivity extends AppCompatActivity {

    List<Advert> advertList = new ArrayList<>();
    RecyclerView lostFoundRecycler;
    RecyclerViewAdapter recyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_found);

        //access database
        advertList = new DatabaseHelper(LostFoundActivity.this).getData();

        //link recyclerview to element
        lostFoundRecycler = findViewById(R.id.lostFoundRecyclerView);

        //configure adapter and set to recycler with layout
        recyclerViewAdapter = new RecyclerViewAdapter(advertList, this, this::onItemClick);
        lostFoundRecycler.setAdapter(recyclerViewAdapter);
        lostFoundRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        //create and hide fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        ItemsFragment fragment = (ItemsFragment) fragmentManager.findFragmentById(R.id.fragmentContainerView);
        fragmentTransaction.hide(fragment).commit();
    }

    //back button finishes activity, so when data is updated when recreated
    public void onBackPressed() {
        finish();
    }

    public void onItemClick(int position) {
        //create fragment
        ItemsFragment itemsFragment = new ItemsFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();

        //bundle selection for fragment
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        itemsFragment.setArguments(bundle);

        //show fragment
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerView, itemsFragment).addToBackStack("").commit();
    }
}