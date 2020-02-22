package com.example.nearbylocaton.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nearbylocaton.R;
import com.example.nearbylocaton.adapter.PlaceRecyclerViewAdapter;
import com.example.nearbylocaton.models.MyPlaces;
import com.example.nearbylocaton.webApi.GoogleApiService;
import com.example.nearbylocaton.webApi.RetrofitBuilder;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationRV extends AppCompatActivity {

    private ImageView icon;
    private TextView itemName;
    private int iconint;
    private RecyclerView recyclerViewPlaces;
    double latitude;
    double longitude;
    private PlaceRecyclerViewAdapter placeRecyclerViewAdapter;

    ProgressDialog progressDialog;

    private FusedLocationProviderClient mFusedLocationClient;
    LocationManager lm;
    LocationManager locationManager;

    double lat = 23.7521850724;
    double lng = 90.3925169004;
    private String placeType = "";
    private GoogleApiService googleApiService;
    private MyPlaces myPlaces;
    private LinearLayout linearLayoutShowOnMap;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_rv);

        icon=findViewById(R.id.imageView2);
        itemName=findViewById(R.id.textView);
        recyclerViewPlaces=findViewById(R.id.recyclerViewPlaces);
        Intent intent=getIntent();
        placeType=intent.getStringExtra("placeName");
        Bundle bundle=this.getIntent().getExtras();
        iconint=bundle.getInt("icon");
        icon.setImageResource(iconint);
        itemName.setText(placeType+"s");
        // Toast.makeText(this, ""+placeType, Toast.LENGTH_SHORT).show();
        locationService();
        getNearbyPlaces();


    }
    private void locationService() {

        lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {

            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Please wait while fetching data from GPS .......");
            progressDialog.setCancelable(false);
            progressDialog.show();


            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

            final LocationListener locationListener = new LocationRV.MyLocationListener();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                progressDialog.dismiss();

                return;
            }

            progressDialog.dismiss();

            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

            mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {

                    if (location != null) {

                        lat = location.getLatitude();
                        lng = location.getLongitude();

                    } else {
                        if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                            if (ActivityCompat.checkSelfPermission(LocationRV.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(LocationRV.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                return;
                            }

                            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
                        } else if (lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {

                            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 10, locationListener);
                        }
                    }
                }
            });
        } else {
            Toast.makeText(LocationRV.this, "GPS off", Toast.LENGTH_SHORT).show();
        }
    }

    public class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location loc) {

            longitude = loc.getLongitude();
            latitude = loc.getLatitude();

            lat = loc.getLatitude();
            lng = loc.getLongitude();
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }

    private String buildUrl(double latitude, double longitude, String API_KEY) {
        StringBuilder urlString = new StringBuilder("api/place/search/json?");

        urlString.append("&location=");
        urlString.append(Double.toString(latitude));
        urlString.append(",");
        urlString.append(Double.toString(longitude));
        urlString.append("&radius=1000"); // places between 5 kilometer
        urlString.append("&types=" + placeType.toLowerCase().replaceAll(" ", "_"));
        urlString.append("&sensor=false&key=" + API_KEY);

        return urlString.toString();
    }

    private void getNearbyPlaces() {

        if (lat != 0 && lng != 0) {

            /*final ProgressDialog dialog = new ProgressDialog(LocationRV.this);
            dialog.setMessage("Loading...");
            dialog.setCancelable(false);
            dialog.setIndeterminate(false);
            dialog.show();*/

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

                    // dialog.dismiss();
                    placeRecyclerViewAdapter = new PlaceRecyclerViewAdapter(LocationRV.this, myPlaces, lat, lng);
                    recyclerViewPlaces.setLayoutManager(new LinearLayoutManager(LocationRV.this));
                    recyclerViewPlaces.setItemAnimator(new DefaultItemAnimator());
                    recyclerViewPlaces.setAdapter(placeRecyclerViewAdapter);
                    placeRecyclerViewAdapter.notifyDataSetChanged();
                    //linearLayoutShowOnMap.setVisibility(View.VISIBLE);
                }

                @Override
                public void onFailure(Call<MyPlaces> call, Throwable t) {
                    //   dialog.dismiss();
                    Toast.makeText(LocationRV.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        else Toast.makeText(this, "what", Toast.LENGTH_SHORT).show();
    }
}


