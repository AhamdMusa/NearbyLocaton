package com.example.nearbylocaton.fragments;

import android.Manifest;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.nearbylocaton.R;
import com.example.nearbylocaton.activity.ShowPlacesOnMapActivity;
import com.example.nearbylocaton.adapter.DataPass;
import com.example.nearbylocaton.adapter.PlaceRecyclerViewAdapter;

import com.example.nearbylocaton.constants.PlacesConstant;
import com.example.nearbylocaton.models.MyPlaces;
import com.example.nearbylocaton.webApi.GoogleApiService;
import com.example.nearbylocaton.webApi.RetrofitBuilder;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LocationRV extends Fragment {

    private RecyclerView recyclerViewPlaces;
    double latitude;
    double longitude;

    ProgressDialog progressDialog;

    private FusedLocationProviderClient mFusedLocationClient;
    LocationManager lm;
    LocationManager locationManager;

    double lat = 0;
    double lng = 0;
    private String placeType = "";
    private GoogleApiService googleApiService;
    private MyPlaces myPlaces;
    private LinearLayout linearLayoutShowOnMap;



    public LocationRV() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View locationRVView=inflater.inflate(R.layout.fragment_location_rv, container, false);
        recyclerViewPlaces =locationRVView.findViewById(R.id.recyclerViewPlaces);
        linearLayoutShowOnMap = locationRVView.findViewById(R.id.linearLayoutShowOnMap);
        locationService();

        linearLayoutShowOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlacesConstant.results = myPlaces.getResults();
                Intent intent = new Intent(getContext(), ShowPlacesOnMapActivity.class);
                startActivity(intent);
            }
        });

        return locationRVView;
    }
    private void locationService() {

        lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {

            progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("Please wait while fetching data from GPS .......");
            progressDialog.setCancelable(false);
            progressDialog.show();


            locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

            final LocationListener locationListener = new LocationRV.MyLocationListener();
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                progressDialog.dismiss();

                return;
            }

            progressDialog.dismiss();

            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());

            mFusedLocationClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {

                    if (location != null) {

                        lat = location.getLatitude();
                        lng = location.getLongitude();

                    } else {
                        if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
            Toast.makeText(getContext(), "GPS off", Toast.LENGTH_SHORT).show();
        }
    }

    private class MyLocationListener implements LocationListener {

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
        urlString.append("&radius=5000"); // places between 5 kilometer
        urlString.append("&types=" + placeType.toLowerCase());
        urlString.append("&sensor=false&key=" + API_KEY);

        return urlString.toString();
    }

    private void getNearbyPlaces() {

        if (lat != 0 && lng != 0) {

            final ProgressDialog dialog = new ProgressDialog(getContext());
            dialog.setMessage("Loading...");
            dialog.setCancelable(false);
            dialog.setIndeterminate(false);
            dialog.show();

            String apiKey = getContext().getResources().getString(R.string.google_maps_key);
            String url = buildUrl(lat, lng, apiKey);
            Log.d("finalUrl", url);

            googleApiService = RetrofitBuilder.builder().create(GoogleApiService.class);

            Call<MyPlaces> call = googleApiService.getMyNearByPlaces(url);

            call.enqueue(new Callback<MyPlaces>() {
                @Override
                public void onResponse(Call<MyPlaces> call, Response<MyPlaces> response) {
                    //Log.d("MyPlaces", response.body().toString());
                    myPlaces = response.body();
                    Log.d("MyPlaces", myPlaces.getResults().get(0).toString());

                    dialog.dismiss();
                    PlaceRecyclerViewAdapter adapter = new PlaceRecyclerViewAdapter(getContext(), myPlaces, lat, lng);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                    recyclerViewPlaces.setLayoutManager(layoutManager);
                    recyclerViewPlaces.setItemAnimator(new DefaultItemAnimator());
                    recyclerViewPlaces.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    linearLayoutShowOnMap.setVisibility(View.VISIBLE);
                }

                @Override
                public void onFailure(Call<MyPlaces> call, Throwable t) {
                    dialog.dismiss();
                    Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        DataPass dataPass= (DataPass) getArguments().getSerializable("data");

                placeType= String.valueOf(dataPass.getName());
                getNearbyPlaces();


    }
}
