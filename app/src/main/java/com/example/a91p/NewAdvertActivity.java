package com.example.a91p;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.a91p.data.DatabaseHelper;
import com.example.a91p.data.Advert;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class NewAdvertActivity extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final int CURRENT_PLACE_REQUEST_CODE = 0;
    DatabaseHelper db;
    boolean locationPermissionGranted;
    ArrayList<String> placeNameList = new ArrayList<>();

    CheckBox lostCheck, foundCheck;
    EditText nameText, phoneText, descriptionText, dateText, locationText;
    Button saveButton, locationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_advert);

        db = new DatabaseHelper(this);

        lostCheck = findViewById(R.id.lostCheckBox);
        foundCheck = findViewById(R.id.foundCheckBox);
        nameText = findViewById(R.id.nameEditText);
        phoneText = findViewById(R.id.phoneEditText);
        descriptionText = findViewById(R.id.descriptionEditText);
        dateText = findViewById(R.id.dateEditText);
        locationText = findViewById(R.id.locationEditText);
        saveButton = findViewById(R.id.saveButton);
        locationButton = findViewById(R.id.locationButton);

        //initialize places if not done already
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), BuildConfig.MAPS_API_KEY);
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //check for identical post
                boolean duplicateCheck = db.advertDupCheck(nameText.getText().toString(), phoneText.getText().toString(), descriptionText.getText().toString(), dateText.getText().toString(), locationText.getText().toString());

                //catch invalid responses
                if (lostCheck.isChecked() == foundCheck.isChecked()) {
                    Toast.makeText(NewAdvertActivity.this, "Invalid post type", Toast.LENGTH_LONG).show();
                } else if ((TextUtils.isEmpty(nameText.getText())) | (TextUtils.isEmpty(phoneText.getText())) | (TextUtils.isEmpty(descriptionText.getText())) | (TextUtils.isEmpty(dateText.getText())) | (TextUtils.isEmpty(locationText.getText()))) {
                    Toast.makeText(NewAdvertActivity.this, "Fields cannot be empty", Toast.LENGTH_LONG).show();
                } else if (duplicateCheck) {
                    Toast.makeText(NewAdvertActivity.this, "Duplicate post", Toast.LENGTH_LONG).show();
                } else {
                    //define advert elements
                    String name, postType, phone, description, date, location;

                    //get advert element values
                    name = nameText.getText().toString();
                    if (lostCheck.isChecked()) {
                        postType = "Lost";
                    } else {
                        postType = "Found";
                    }
                    phone = phoneText.getText().toString();
                    description = descriptionText.getText().toString();
                    date = dateText.getText().toString();
                    location = locationText.getText().toString();

                    //combine to advert and add to database
                    long result = db.insertAdvert(new Advert(name, postType, phone, description, date, location));

                    //confirm result
                    if (result > 0) {
                        Toast.makeText(NewAdvertActivity.this, "Post successful", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(NewAdvertActivity.this, "Post unsuccessful", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        locationText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set the fields to specify which types of place data to
                // return after the user has made a selection.
                List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME);

                // Start the autocomplete intent.
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                        .build(getApplicationContext());
                startAutocomplete.launch(intent);
            }
        });

        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check permission
                if (locationPermissionGranted) {
                    getDeviceLocation();

                    if (placeNameList.size() != 0) {
                        //create and start CurrentPlaceActivity, passing place list
                        Intent intent = new Intent(NewAdvertActivity.this, CurrentPlaceActivity.class);
                        intent.putExtra("places", placeNameList);
                        startActivityForResult(intent, CURRENT_PLACE_REQUEST_CODE);
                    }
                }
                else {
                    getLocationPermission();
                }
            }
        });
    }

    private final ActivityResultLauncher<Intent> startAutocomplete = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent intent = result.getData();
                    if (intent != null) {
                        Place place = Autocomplete.getPlaceFromIntent(intent);
                        Log.i("TAG", "Place: ${place.getName()}, ${place.getId()}");

                        locationText.setText(place.getName());
                    }
                } else if (result.getResultCode() == Activity.RESULT_CANCELED) {
                    // The user canceled the operation.
                    Log.i("TAG", "User canceled autocomplete");
                }
            });

    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    private void getDeviceLocation() {
        //create client
        PlacesClient placesClient = Places.createClient(this);

        //define the data types to return
        List<Place.Field> placeFields = Collections.singletonList(Place.Field.NAME);

        //build request
        FindCurrentPlaceRequest request = FindCurrentPlaceRequest.newInstance(placeFields);

        //check permission
        if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //make request and get response
            Task<FindCurrentPlaceResponse> placeResponse = placesClient.findCurrentPlace(request);
            placeResponse.addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    //collect places
                    FindCurrentPlaceResponse response = task.getResult();
                    List<PlaceLikelihood> placeLikelihood = response.getPlaceLikelihoods();

                    //put names into ArrayList
                    for (int i=0; i<placeLikelihood.size(); i++) {
                        placeNameList.add(placeLikelihood.get(i).getPlace().getName());
                    }
                } else {
                    //notify for null response
                    Toast.makeText(this, "Location not found", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            getLocationPermission();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        locationPermissionGranted = false;
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {// cancelled request is empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationPermissionGranted = true;
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            //check for current place selection
            case (CURRENT_PLACE_REQUEST_CODE) : {
                if (resultCode == Activity.RESULT_OK) {
                    //obtain and display selection
                    String selection = data.getStringExtra("selection");
                    locationText.setText(selection);
                }
            }
        }
    }
}