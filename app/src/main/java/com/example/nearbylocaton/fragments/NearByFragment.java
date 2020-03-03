package com.example.nearbylocaton.fragments;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nearbylocaton.R;
import com.example.nearbylocaton.activity.LocationRV;
import com.example.nearbylocaton.activity.MainActivity;
import com.example.nearbylocaton.adapter.DataPass;
import com.example.nearbylocaton.adapter.TypeAdapter;
import com.example.nearbylocaton.constants.CardItems;
import com.example.nearbylocaton.models.MyPlaces;
import com.example.nearbylocaton.webApi.GoogleApiService;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class NearByFragment extends Fragment {

    //Shared Preference
    private Context context;
    private SharedPreferences sharedPreferences;

    ProgressDialog progressDialog;

    private FusedLocationProviderClient mFusedLocationClient;
    LocationManager lm;
    LocationManager locationManager;

    private double lat;
    private double lng;
    private String placeType = "";
    private GoogleApiService googleApiService;
    private MyPlaces myPlaces;


    private Spinner spinner_nearby_choices;
    private RecyclerView productRV;
    private List<CardItems> products;
    private TypeAdapter productAdapter;
    private CardView singleItemView;
    private MainActivity mainActivity;

    public NearByFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_near_by, container, false);

        //Init Shared Preference
        sharedPreferences = context.getSharedPreferences("location_data",Context.MODE_PRIVATE);

        //locationService();

        //spinner_nearby_choices = view.findViewById(R.id.spinner_nearby_choices);
        singleItemView = view.findViewById(R.id.singleItemView);
        productRV = view.findViewById(R.id.nearbymeRV);

        init();

        getAllProduct();

        /*spinner_nearby_choices.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                position = spinner_nearby_choices.getSelectedItemPosition();
                if (position == 0) {
                    //Toast.makeText(getContext(), "Please select valid type", Toast.LENGTH_SHORT).show();
                } else {
                    lat = sharedPreferences.getFloat("lat",0f);
                    lng = sharedPreferences.getFloat("lng",0f);

                    //---------------------On Clicke next Frag te dite hobe-----------------------//
                    String placeName = spinner_nearby_choices.getSelectedItem().toString();
                    Intent intent = new Intent(getActivity(), LocationRV.class);
                    intent.putExtra("placeName",placeName);
                    Bundle bundle=new Bundle();
                    bundle.putInt("icon",R.drawable.search);
                    bundle.putDouble("lat",lat);
                    bundle.putDouble("lng",lng);
                    intent.putExtras(bundle);
                    startActivity(intent);

                    Toast.makeText(getContext(), "Goooooot LatLng "+lat, Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
*/

        return view;
    }

    private void init() {

        products = new ArrayList<>();
        productAdapter = new TypeAdapter(getContext(), products);
        productRV.setLayoutManager(new GridLayoutManager(getContext(), 3));
        productRV.setAdapter(productAdapter);
    }

    private void getAllProduct() {
        //  products.add(new CardItems(R.drawable.nearbyicon, "A to Z"));
        products.add(new CardItems(R.drawable.airplane, "Airport"));
        products.add(new CardItems(R.drawable.atm, "ATM"));
        products.add(new CardItems(R.drawable.bank, "Bank"));
        products.add(new CardItems(R.drawable.bakery, "Bakery"));
        products.add(new CardItems(R.drawable.bar, "Bar"));
        products.add(new CardItems(R.drawable.bookstore, "Book Store"));
        products.add(new CardItems(R.drawable.busstation, "Bus Station"));
        products.add(new CardItems(R.drawable.carrental, "Car Rental"));
        products.add(new CardItems(R.drawable.carwash, "Car Wash"));
        products.add(new CardItems(R.drawable.coffeeshop, "Cafe"));
        products.add(new CardItems(R.drawable.clothingstore, "Clothing Store"));
        products.add(new CardItems(R.drawable.dentist, "Dentist"));
        products.add(new CardItems(R.drawable.doctor, "Doctor"));
        products.add(new CardItems(R.drawable.electronicsstore, "Electronics Store"));
        products.add(new CardItems(R.drawable.embassy, "Embassy"));
        products.add(new CardItems(R.drawable.fireservice, "Fire Station"));
        products.add(new CardItems(R.drawable.gasstation, "Gas Station"));
        products.add(new CardItems(R.drawable.govtoffice, "Govt Office"));
        products.add(new CardItems(R.drawable.gym, "Gym"));
        products.add(new CardItems(R.drawable.hospital, "Hospital"));
        products.add(new CardItems(R.drawable.jewelrystore, "Jewelry Store"));
        products.add(new CardItems(R.drawable.laundry, "Laundry"));
        products.add(new CardItems(R.drawable.mosque, "Mosque"));
        products.add(new CardItems(R.drawable.movietheater, "Movie Theater"));
        products.add(new CardItems(R.drawable.park, "Park"));
        products.add(new CardItems(R.drawable.pharmacy, "Pharmacy"));
        products.add(new CardItems(R.drawable.police, "Police"));
        products.add(new CardItems(R.drawable.postoffice, "Post Office"));
        products.add(new CardItems(R.drawable.restaurant, "Restaurant"));
        products.add(new CardItems(R.drawable.school, "School"));
        products.add(new CardItems(R.drawable.college, "Secondary School"));
        products.add(new CardItems(R.drawable.shoppingmall, "Shopping Mall"));
        products.add(new CardItems(R.drawable.university, "University"));


        productAdapter.notifyDataSetChanged();
    }

    //Code for Location
/*    private void locationService() {

        lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {

            progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("Please wait while fetching data from GPS .......");
            progressDialog.setCancelable(false);
            progressDialog.show();


            locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

            final LocationListener locationListener = new NearByFragment.MyLocationListener();
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

            *//*longitude = loc.getLongitude();
            latitude = loc.getLatitude();*//*

            lat = loc.getLatitude();
            lng = loc.getLongitude();

           // Toast.makeText(getContext(), ""+lat, Toast.LENGTH_SHORT).show();
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
    }*/


}
