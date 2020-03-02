package com.example.nearbylocaton.activity;

import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.example.nearbylocaton.R;
import com.example.nearbylocaton.models.Location;
import com.example.nearbylocaton.models.Results;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DirectionActivity extends AppCompatActivity implements OnMapReadyCallback, RoutingListener {
    private GoogleMap map;
    //  for device location
    private double latitude;
    private double longitude;
    private String locationName;
    //for destination location
    LatLng latLngApi;
    //  for infoWindow
   /* private ImageView infoWindowImage;
    private TextView infoWindowTitleTv, infoWindowSnippetTv;*/
    private ProgressBar progressBar;
    //  for api latitude and longitude
    private double latBus;
    private double lonBus;

    private Results results;
    private double lat, lng;
    private String type;
    private Location location1, location2;

    //  for polyline drawing
    private List<Polyline> polylines;
    private Button mapTypes;
    private static final int[] COLORS = new int[]{R.color.second_root_color, R.color.first_root_color, R.color.third_root_color, R.color.fbutton_color_green_sea, R.color.primary_dark_material_light};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direction);

        /*progressBar = findViewById(R.id.progressBarId);*/
        polylines = new ArrayList<>();
        loadMapWithFragment();

        //mapTypes=findViewById(R.id.typeButton);


        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            results = (Results) bundle.getSerializable("result");
            lat = bundle.getDouble("lat");
            lng = bundle.getDouble("lng");
            //type = bundle.getString("type");
            location2 = results.getGeometry().getLocation();

            //Toast.makeText(this, String.valueOf(lat), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Got Nothing!!", Toast.LENGTH_SHORT).show();
            return;
        }

    }
    private void loadMapWithFragment() {
        SupportMapFragment supportMapFragment = SupportMapFragment.newInstance();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.mapDirection,supportMapFragment);
        fragmentTransaction.commit();
        //supportMapFragment.getMapAsync(this);
        supportMapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        //getNearbyPlaceSearchMechanism(latitude, longitude);
        latitude = lat;
        longitude = lng;

        //Shahbag
        latBus =23.738298;
        lonBus =90.395855;

        latLngApi = new LatLng(latBus, lonBus);
        getRouteToMarker(latLngApi);

        map.getUiSettings().setMapToolbarEnabled(false);

        animateCamera(new LatLng(latitude, longitude), 17);
        map.getUiSettings().setMyLocationButtonEnabled(false);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        map.setMyLocationEnabled(true);
        //   infoWindow();
        mapBackgroundDesignChange(map);
        // mapBackgroundDesign(map);
        //onClicks();

    }
      /*  private void onClicks() {
        mapTypes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMapTypeSelectorDialog();
            }
        });

    }*/

    public static Bitmap createCustomMarker(Context context, @DrawableRes int resource) {

        View marker = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.custom_marker_layout, null);

        CircleImageView markerImage = (CircleImageView) marker.findViewById(R.id.markerIvId);
        markerImage.setImageResource(resource);
        TextView txt_name = (TextView) marker.findViewById(R.id.markerTvId);
        //  txt_name.setText(_name);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        marker.setLayoutParams(new ViewGroup.LayoutParams(52, ViewGroup.LayoutParams.WRAP_CONTENT));
        marker.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(marker.getMeasuredWidth(), marker.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        marker.draw(canvas);

        return bitmap;
    }
    /*--------------No Need This Code-----------------------*/
       /*  private void getNearbyPlaceSearchMechanism(double latitudeApi, double longitudeApi) {
        String url = String.format("BusStand/NearestBusStand?lat=%f&lon=%f", latitudeApi, longitudeApi);
        BusKothayNearbyBusStandApiService busKothayNearbyBusStandApiService = BusKothayRetrofitInstance.getRetrofitInstanceForBusKothayBusStandAPI().create(BusKothayNearbyBusStandApiService.class);
        Call<BusKothayNearbyBusStandResponse> callBusKothayNearbyBusStandResponse = busKothayNearbyBusStandApiService.getBusKothayNearbyBusStandResponse(url);

        callBusKothayNearbyBusStandResponse.enqueue(new Callback<BusKothayNearbyBusStandResponse>() {
            @Override
            public void onResponse(Call<BusKothayNearbyBusStandResponse> call, Response<BusKothayNearbyBusStandResponse> response) {

                if (response.code() == 200) {

                    final BusKothayNearbyBusStandResponse busKothayNearbyBusStandResponseList = response.body();
                    *//*  for (int i = 0; i < busKothayNearbyBusStandResponseList.size(); i++) {*//*
                    latBus = busKothayNearbyBusStandResponseList.getLatitude();
                    lonBus = busKothayNearbyBusStandResponseList.getLongitude();

                    final String busName = busKothayNearbyBusStandResponseList.getStandName();

                    LatLng latLngApi = new LatLng(latBus, lonBus);
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latLngApi)
                            .title(busName)
                            .icon(BitmapDescriptorFactory
                                    .fromBitmap(createCustomMarker(NearbyBusStopsSearchActivity.this,
                                            R.drawable.bus_stop_marker_icon)));

                    getRouteToMarker(latLngApi);

                    animateCamera(new LatLng(latBus, lonBus), 17);
                    map.addMarker(markerOptions).setTag(busKothayNearbyBusStandResponseList);

                }
                // }
            }

            @Override
            public void onFailure(Call<BusKothayNearbyBusStandResponse> call, Throwable t) {
                Toast.makeText(NearbyBusStopsSearchActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });
    }*/

    private void getRouteToMarker(LatLng latLngApi) {
        Routing routing = new Routing.Builder()
                .travelMode(AbstractRouting.TravelMode.DRIVING)
                .withListener(this)
                .alternativeRoutes(true)
                .waypoints(new LatLng(latitude, longitude), latLngApi)
                .key(DirectionActivity.this.getString(R.string.map_direction_polyLine_api_key))
                .build();
        routing.execute();
    }
    private void animateCamera(LatLng latLng, float zoom) {
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    private void setMarker(LatLng latLng,  BitmapDescriptor bitmapDescriptorFactory) {
        map.addMarker(new MarkerOptions()
                .position(latLng)
                .icon(bitmapDescriptorFactory));
    }

    @Override
    public void onRoutingFailure(RouteException e) {
        if (e != null) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Something went wrong, Try again", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRoutingStart() { }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {
        if (polylines.size() > 0) {
            for (Polyline poly : polylines) {
                poly.remove();
            }
        }

        polylines = new ArrayList<>();
        //add route(s) to the map.
        for (int i = 0; i < route.size(); i++) {

            //In case of more than 5 alternative routes
            int colorIndex = i % COLORS.length;

            //Toast.makeText(this, ""+COLORS.length, Toast.LENGTH_SHORT).show();

            PolylineOptions polyOptions = new PolylineOptions();
            polyOptions.color(getResources().getColor(COLORS[colorIndex]));
            polyOptions.width(10 + i * 3);
            polyOptions.addAll(route.get(i).getPoints());

            LatLng polylineLatLng = new LatLng(latitude, longitude);
            route.get(i).getPoints().add(polylineLatLng);

            List<LatLng> listOfLatLng = route.get(i).getPoints();

            LatLng newLatLng = listOfLatLng.get(0);


            //Toast.makeText(this, ""+newLatLng, Toast.LENGTH_SHORT).show();


            Integer disInMeters =  route.get(i).getDistanceValue();
            double kilometers = disInMeters * 0.001;

            Integer duration = route.get(i).getDurationValue();
            double minute = (int) (duration / 60);



            setMarker(polylineLatLng, BitmapDescriptorFactory
                    .fromBitmap(createCustomMarker(DirectionActivity.this,
                            R.drawable.custom_marker)));

            setMarker(latLngApi, BitmapDescriptorFactory
                    .fromBitmap(createCustomMarker(DirectionActivity.this,
                            R.drawable.marker_location)));


            if (disInMeters <= 999) {
                // kilometers = (int) (disInMeters * 1);
                //Toast.makeText(this, "Size Of LatLng"+listOfLatLng.size(), Toast.LENGTH_SHORT).show();

                int middlePoint = listOfLatLng.size()/2;

                newLatLng = listOfLatLng.get(middlePoint);

                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(newLatLng)
                        .icon(BitmapDescriptorFactory
                                .fromBitmap(customMarkerForPolyLine(DirectionActivity.this, R.drawable.transport, kilometers,minute)));

                map.animateCamera(CameraUpdateFactory.newLatLngZoom(newLatLng, 15));

                map.setMinZoomPreference(10.0f);
                map.setMaxZoomPreference(17.0f);

                map.addMarker(markerOptions);


            } else if(disInMeters > 999){

                kilometers = (disInMeters * 0.001);

                int middlePoint = listOfLatLng.size()/2;

                newLatLng = listOfLatLng.get(middlePoint);

                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(newLatLng)
                        .icon(BitmapDescriptorFactory
                                .fromBitmap(customMarkerForPolyLine(DirectionActivity.this, R.drawable.transport, kilometers,minute)));

                map.animateCamera(CameraUpdateFactory.newLatLngZoom(newLatLng, 15));

                map.setMinZoomPreference(10.0f);
                map.setMaxZoomPreference(17.0f);

                map.addMarker(markerOptions);

            }
            animateCamera(polylineLatLng, 17);

            Polyline polyline = map.addPolyline(polyOptions);
            //polyline.setColor(ContextCompat.getColor(this, R.color.second_root_color));
            polyline.setClickable(true);
            polylines.add(polyline);
            //Toast.makeText(getApplicationContext(), "Route " + (i + 1) + ": distance - " + route.get(i).getDistanceValue() + ": duration - " + route.get(i).getDurationValue(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRoutingCancelled() { }

    public static Bitmap customMarkerForPolyLine(Context context, @DrawableRes int resource, double distance,double time) {

        View marker = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker_for_polyline, null);

        ImageView markerImage = marker.findViewById(R.id.custom_markerIV);
        markerImage.setImageResource(resource);

        TextView distanceCalculation = marker.findViewById(R.id.distanceTV);
        distanceCalculation.setText(String.valueOf(new DecimalFormat("##.#").format(distance))+" km by Driving");

        TextView timeCalculation = marker.findViewById(R.id.timeTV);
        timeCalculation.setText(String.valueOf(new DecimalFormat("##.#").format(time))+" minutes");

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        marker.setLayoutParams(new ViewGroup.LayoutParams(52, ViewGroup.LayoutParams.WRAP_CONTENT));
        marker.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(marker.getMeasuredWidth(), marker.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        marker.draw(canvas);

        return bitmap;
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Animatoo.animateSlideRight(DirectionActivity.this);
    }
    // -------- Code For Background---START -----------
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

    private void mapBackgroundDesignChange(GoogleMap googleMap) {
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.changemapdesignapi));

            if (!success) {
                Log.e("MainActivity", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("MainActivity", "Can't find style. Error: ", e);
        }
    }
    // -------- Code For Background---END -----------

    /* ------------------- Code For Changing Map Style------------- */
/*
    private static final CharSequence[] MAP_TYPE_ITEMS =
            {"Road Map", "Hybrid", "Satellite", "Terrain"};

    private void showMapTypeSelectorDialog() {
        // Prepare the dialog by setting up a Builder.
        final String fDialogTitle = "Select Map Type";
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(fDialogTitle);

        // Find the current map type to pre-check the item representing the current state.
        int checkItem = map.getMapType() - 1;

        // Add an OnClickListener to the dialog, so that the selection will be handled.
        builder.setSingleChoiceItems(MAP_TYPE_ITEMS,checkItem,
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int item) {
                        // Locally create a finalised object.

                        // Perform an action depending on which item was selected.
                        switch (item) {
                            case 1:
                                map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                                break;
                            case 2:
                                map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                                break;
                            case 3:
                                map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                                break;
                            default:
                                map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
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
*/
}
