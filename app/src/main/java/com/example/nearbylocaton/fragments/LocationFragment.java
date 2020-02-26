package com.example.nearbylocaton.fragments;


import android.Manifest;
import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.nearbylocaton.R;
import com.example.nearbylocaton.activity.MainActivity;
import com.example.nearbylocaton.activity.PlaceDetailsActivity;
import com.example.nearbylocaton.activity.ShowMeOnMap;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class LocationFragment extends Fragment {

    //Shared Preference
    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private FusedLocationProviderClient mFusedLocationClient;
    LocationManager lm;
    LocationManager locationManager;
    private GoogleMap mMap;


    public double lat, lng;

    String longitude, latitude;

    ProgressDialog progressDialog;
    private TextView textViewAddress;
    private TextView textViewBigText;
    private TextView textViewCity;
    private TextView textViewPostalCode;
    private TextView textViewLongitude;
    private TextView textViewLatitude;
    private TextView textViewCountryCode;
    private TextView textViewCountry;
    private TextView textViewDivision;
    private LinearLayout linearLayoutViewMe;


    public LocationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_location, container, false);

        //Init Shared Preference
        sharedPreferences = context.getSharedPreferences("location_data", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        textViewAddress = view.findViewById(R.id.address);
        textViewCity = view.findViewById(R.id.city);
        textViewLongitude = view.findViewById(R.id.textViewLongitude);
        textViewLatitude = view.findViewById(R.id.textViewLatitude);
        textViewCountryCode = view.findViewById(R.id.textViewCountryCode);
        textViewCountry = view.findViewById(R.id.textViewCountry);
        textViewDivision = view.findViewById(R.id.textViewDivision);
        textViewPostalCode = view.findViewById(R.id.postalCode);
        linearLayoutViewMe = view.findViewById(R.id.linearLayoutViewMe);
        textViewBigText = view.findViewById(R.id.bigTaxt);





        locationService();

        linearLayoutViewMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ShowMeOnMap.class);
                startActivity(i);
                getActivity().overridePendingTransition(0, 0);

            }
        });

        if (lat != 0 && lng != 0) {
            getAddress();
        }

        return view;
    }


    private void locationService() {

        lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {

            progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("Please wait while getting data from GPS .......");
            progressDialog.setCancelable(false);
            progressDialog.show();


            locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

            final LocationListener locationListener = new MyLocationListener();
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
                        textViewLatitude.setText(String.valueOf(lat));
                        textViewLongitude.setText(String.valueOf(lng));

                        editor.putFloat("lat", (float) lat).apply();
                        editor.putFloat("lng", (float) lng).apply();


                  /*      NearByFragment fragment = new NearByFragment();
                        Bundle bundle = new Bundle();
                        bundle.putDouble("lat", lat);
                        bundle.putDouble("lng", lng);
                        fragment.setArguments(bundle);
                        Toast.makeText(getContext(), ""+lat, Toast.LENGTH_SHORT).show();
*/
                        getAddress();

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
            //checkGpsStatus();
            Toast.makeText(getContext(), "GPS off", Toast.LENGTH_SHORT).show();
        }
    }

    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location loc) {

            //longitude = String.valueOf(loc.getLongitude());
            //latitude = String.valueOf(+loc.getLatitude());

            //lat = loc.getLatitude();
            //lng = loc.getLongitude();
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

    private void getAddress() {

        String city = null;
        String address = null;
        String subCity = null;
        String postalCode = null;
        String division = null;
        String country = null;
        String countryCode = null;

        Geocoder gcd = new Geocoder(getContext(), Locale.getDefault());
        List<Address> addresses;

        try {
            addresses = gcd.getFromLocation(lat, lng, 1);
            if (addresses.size() > 0) {

                city = addresses.get(0).getLocality();
                address = addresses.get(0).getFeatureName();
                subCity = addresses.get(0).getSubLocality();
                postalCode = addresses.get(0).getPostalCode();
                division = addresses.get(0).getAdminArea();
                country = addresses.get(0).getCountryName();
                countryCode = addresses.get(0).getCountryCode();

                textViewAddress.setText(addresses.get(0).getAddressLine(0));
                textViewCountryCode.setText(countryCode);
                textViewCountry.setText(country);
                textViewDivision.setText(division);
                textViewPostalCode.setText(postalCode);
                textViewCity.setText(city);
                textViewBigText.setText(city);   //-----------MUSA-------------------------------/


                //Toast.makeText(getContext(), , Toast.LENGTH_SHORT).show();
                Log.d("LocationFragment", addresses.get(0).toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void moveToNewActivity() {


    }

}