package com.example.nearbylocaton.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.nearbylocaton.R;
import com.example.nearbylocaton.models.Results;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class ShowMeOnMap extends AppCompatActivity implements OnMapReadyCallback{
    private GoogleMap mMap;
    private TextView deviceLocationTV;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Boolean locationPermissionsGranted = false;
    private static final float DEFAULT_ZOOM = 15f;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;

    private double latitude;
    private double longitude;

    //--------musa--------------//
    private DrawerLayout drawer;
    private ImageView mapTypes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_me);

        deviceLocationTV = findViewById(R.id.deviceLocationTV);
        mapTypes=findViewById(R.id.typeButtononMe);

        deviceLocationTV.setText("Look!! I am here");



        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.maps, mapFragment);
        fragmentTransaction.commit();

        mapFragment.getMapAsync((OnMapReadyCallback) ShowMeOnMap.this);
        onClicks();


    }

    private void onClicks() {
        mapTypes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMapTypeSelectorDialog();
            }
        });

    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        int height = 100;
        int width = 100;

        mMap = googleMap;
        Bundle bundle = getIntent().getExtras();


        getDeviceLocation();

        /*// Add a marker in Sydney, Australia, and move the camera.
        LatLng KuakataSeeBeach = new LatLng(21.8228933, 90.118145);
        mMap.addMarker(new MarkerOptions().position(KuakataSeeBeach).title("Marker in Kuakata See Beach"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(KuakataSeeBeach, 10));*/

        //Method for change background
        mapBackgroundDesign(mMap);

        //------------------for Traffic-------------------//
        if (bundle!=null){
            String mapType=bundle.getString("type");
        if (mapType.equals("true")) {
            mMap.setTrafficEnabled(true);
           /* String type=bundle.getString("type");
            lat = bundle.getDouble("lat");
            lng = bundle.getDouble("lng");
            //Toast.makeText(this, String.valueOf(results.getPhotos()[0].getPhoto_reference()), Toast.LENGTH_SHORT).show();*/
        }} else {
            mMap.setTrafficEnabled(false);
            return;
        }



        //Method for Permission Request
        locationPermissionRequest();

        mMap.setMyLocationEnabled(false);

//         mMap.getUiSettings().setZoomControlsEnabled(true);

    }

    private void getDeviceLocation() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                /*.setInterval(3000)
                .setFastestInterval(1000);*/


        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                for (Location location : locationResult.getLocations()) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();

                    //For current location
                    LatLng latLng = new LatLng(latitude, longitude);


                    try {
                        List<Address> addresses = new Geocoder(ShowMeOnMap.this).getFromLocation(latitude, longitude, 1);

                        String myLocation = addresses.get(0).getAddressLine(0);
                        //  String phone = addresses.get(0).getPhone();
                        deviceLocationTV.setText(myLocation);

                        int height = 100;
                        int width = 100;
                        BitmapDrawable bitmapdraw = (BitmapDrawable)getResources().getDrawable(R.drawable.airplane);
                        Bitmap smallMarker = Bitmap.createScaledBitmap(bitmapdraw.getBitmap(), width, height, false);

                        mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                                .title("My Location").snippet(myLocation));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                        //Toast.makeText(MainActivity.this, ""+myLocation, Toast.LENGTH_SHORT).show();


                    } catch (IOException e) {
                        Toast.makeText(ShowMeOnMap.this, "" + e, Toast.LENGTH_SHORT).show();
                        //e.printStackTrace();
                    }



                 /*  LatLng latLng = new LatLng(latitude,longitude);
                    Toast.makeText(MainActivity.this, ""+latLng, Toast.LENGTH_SHORT).show();
*/

                }
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            return;
        }

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    private void moveCamera(LatLng latLng, float zoom) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    private void locationPermissionRequest() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "PERMISSION IS GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "PERMISSION IS DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private static final CharSequence[] MAP_TYPE_ITEMS =
            {"Road Map", "Hybrid", "Satellite", "Terrain"};

    private void showMapTypeSelectorDialog() {
        // Prepare the dialog by setting up a Builder.
        final String fDialogTitle = "Select Map Type";
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(fDialogTitle);

        // Find the current map type to pre-check the item representing the current state.
        int checkItem = mMap.getMapType() - 1;

        // Add an OnClickListener to the dialog, so that the selection will be handled.
        builder.setSingleChoiceItems(MAP_TYPE_ITEMS,checkItem,
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int item) {
                        // Locally create a finalised object.

                        // Perform an action depending on which item was selected.
                        switch (item) {
                            case 1:
                                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                                break;
                            case 2:
                                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                                break;
                            case 3:
                                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                                break;
                            default:
                                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        }
                        dialog.dismiss();
                    }
                }
        );

        // Build the dialog and show it.
        AlertDialog fMapTypeDialog = builder.create();
        fMapTypeDialog.setCanceledOnTouchOutside(true);
        fMapTypeDialog.show();
    }

    private void mapBackgroundDesign(GoogleMap mMap) {
        try {
            boolean success = mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.changemapdesignapi));
            if (!success) {
                Log.e("MapsActivity", "Style Parsing Failed");

            }

        } catch (Resources.NotFoundException e) {
            Log.e("MapsActivity", "Can not find style. Error: ", e);
        }
    }

    public void backMe(View view) {
   super.onBackPressed();
    }
}
