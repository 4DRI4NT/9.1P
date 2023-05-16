package com.example.a91p;

import androidx.fragment.app.FragmentActivity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.Toast;

import com.example.a91p.data.Advert;
import com.example.a91p.data.DatabaseHelper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.a91p.databinding.ActivityMapsBinding;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    DatabaseHelper db;
    List<Advert> advertList = new ArrayList<>();
    private ActivityMapsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get adverts
        db = new DatabaseHelper(MapsActivity.this);
        advertList = db.getData();
        db.close();

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        //cycle through each advert
        for (int i=0; i<advertList.size(); i++) {
            //get LatLong
            LatLng latLng = getLatLng(advertList.get(i).getLocation());

            //catch null returns
            if (latLng == null) {
                Toast.makeText(MapsActivity.this, "null", Toast.LENGTH_SHORT).show();
            } else {
                //add marker and move camera in area
                googleMap.addMarker(new MarkerOptions().position(latLng).title(advertList.get(i).getDescription() + " in " + advertList.get(i).getLocation()));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 4));
            }
        }
    }

    public LatLng getLatLng(String placeName) {
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        List<Address> addressList;
        Address address;

        //get best matching addresses
        try {
            // !! removing restriction !!
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            addressList = geocoder.getFromLocationName(placeName, 5);
            if (addressList == null) {
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //return best match
        address = addressList.get(0);
        return new LatLng(address.getLatitude(), address.getLongitude());
    }
}