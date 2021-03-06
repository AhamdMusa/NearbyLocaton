package com.example.nearbylocaton.activity;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.nearbylocaton.R;
import com.example.nearbylocaton.adapter.PlaceRecyclerViewAdapter;
import com.example.nearbylocaton.constants.PlacesConstant;
import com.example.nearbylocaton.models.MyPlaces;
import com.example.nearbylocaton.webApi.GoogleApiService;
import com.example.nearbylocaton.webApi.RetrofitBuilder;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationRV extends AppCompatActivity {

    //gdfgfgdfg


    private static LocationRV instance;
    public static LocationRV getInstance() {
        return instance;
    }
    private ImageView icon;
    private TextView itemName;
    private int iconint;
    private RecyclerView recyclerViewPlaces;
    private LocationRequest locationRequest;
    private LatLng myLocation;
    double latitude;
    double longitude;
    private PlaceRecyclerViewAdapter placeRecyclerViewAdapter;
    private CardView linearLayoutShowOnMap;

    ProgressDialog progressDialog;

    private FusedLocationProviderClient mFusedLocationClient;
    LocationManager lm;
    LocationManager locationManager;

    /*double lat=23.7521850724 ;
    double lng=90.3925169004;*/
    double lat;
    double lng;
    private String placeType = "";
    private GoogleApiService googleApiService;
    private MyPlaces myPlaces;
    private RatingBar ratingBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_rv);

        //locationService();

        instance=this;
        icon=findViewById(R.id.imageView2);
        itemName=findViewById(R.id.textView);
        recyclerViewPlaces=findViewById(R.id.recyclerViewPlaces);
        linearLayoutShowOnMap = findViewById(R.id.linearLayoutShowOnMap);


        Intent intent=getIntent();
        placeType=intent.getStringExtra("placeName");
        Bundle bundle=this.getIntent().getExtras();
        lat=bundle.getDouble("lat");
        lng=bundle.getDouble("lng");
        iconint=bundle.getInt("icon");

        icon.setImageResource(iconint);
        itemName.setText(placeType+"s");
        // Toast.makeText(this, ""+placeType, Toast.LENGTH_SHORT).show();
        //locationService();
        getNearbyPlaces();



        linearLayoutShowOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlacesConstant.results = myPlaces.getResults();
                Intent intent = new Intent(LocationRV.this, ShowPlacesOnMapActivity.class);
                startActivity(intent);
                Animatoo.animateSlideUp(LocationRV.this);
            }
        });

    }




    private String buildUrl(double latitude, double longitude, String API_KEY) {
        StringBuilder urlString = new StringBuilder("api/place/search/json?");

        urlString.append("&location=");
        urlString.append(latitude);
        urlString.append(",");
        urlString.append(longitude);
        urlString.append("&radius=10000"); // places between 5 kilometer
        urlString.append("&types=" + placeType.toLowerCase().replaceAll(" ", "_"));
        urlString.append("&sensor=false&key=" + API_KEY);

        return urlString.toString();
    }

    private void getNearbyPlaces() {

        if (lat != 0 && lng != 0) {

            final ProgressDialog dialog = new ProgressDialog(LocationRV.this);
            dialog.setMessage("Loading...");
            dialog.setCancelable(false);
            dialog.setIndeterminate(false);
            dialog.show();

            String apiKey = LocationRV.this.getResources().getString(R.string.google_maps_key);
            String url = buildUrl(lat, lng, apiKey);
            Log.d("finalUrl", url);

            googleApiService = RetrofitBuilder.builder().create(GoogleApiService.class);

            Call<MyPlaces> call = googleApiService.getMyNearByPlaces(url);

            call.enqueue(new Callback<MyPlaces>() {
                @Override
                public void onResponse(Call<MyPlaces> call, Response<MyPlaces> response) {
                    //Log.d("MyPlaces", response.body().toString());
                    myPlaces = response.body();
                    //  Log.d("MyPlaces", myPlaces.getResults().get(0).toString());

                    dialog.dismiss();

                    placeRecyclerViewAdapter = new PlaceRecyclerViewAdapter(LocationRV.this, myPlaces, lat, lng);
                    recyclerViewPlaces.setLayoutManager(new LinearLayoutManager(LocationRV.this));
                    recyclerViewPlaces.setItemAnimator(new DefaultItemAnimator());
                    recyclerViewPlaces.setAdapter(placeRecyclerViewAdapter);
                    placeRecyclerViewAdapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<MyPlaces> call, Throwable t) {
                    //   dialog.dismiss();
                    Toast.makeText(LocationRV.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
           // Toast.makeText(this, "lat lng not found", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, ""+latitude, Toast.LENGTH_SHORT).show();
        }
    }
}


