package com.example.nearbylocaton.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.nearbylocaton.DataBase.DatabaseOpenHelper;
import com.example.nearbylocaton.R;
import com.example.nearbylocaton.models.Photos;
import com.example.nearbylocaton.models.Results;
import com.squareup.picasso.Picasso;

public class PlaceDetailsActivity extends AppCompatActivity {

    private ImageView imageView;
    private Photos photos;
    private TextView textViewName;
    private TextView textViewRating;
    private TextView textViewAddress;
    private TextView textViewAvailability;
    private RatingBar ratingBar;
    private LinearLayout linearLayoutRating;
    private LinearLayout linearLayoutShowOnMap;
    private LinearLayout linearLayoutShowDistanceOnMap;
    //--------------FOR FAVORITE DATABASE----------//
    private ImageView favoritButton;
    private DatabaseOpenHelper helper;
    private String photoUrl;
    int i=0,databaseID;

    // variable
    private Results results;
    private double lat, lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_details);

        // init UI
        init();

        Bundle bundle = getIntent().getExtras();
        helper=new DatabaseOpenHelper(this);

        if (bundle != null) {
            results = (Results) bundle.getSerializable("result");
            lat = bundle.getDouble("lat");
            lng = bundle.getDouble("lng");
            //Toast.makeText(this, String.valueOf(results.getPhotos()[0].getPhoto_reference()), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Got Nothing!!", Toast.LENGTH_SHORT).show();
            return;
        }


        imageView = findViewById(R.id.imageView);
        favoritButton=findViewById(R.id.favorite_button);

        try {
            // get photo
            photos = results.getPhotos()[0];
            photoUrl= String.format("https://maps.googleapis.com/maps/api/place/photo?maxwidth=%s&photoreference=%s&key=%s", 400, photos.getPhoto_reference(), getResources().getString(R.string.google_maps_key));
            Log.d("photoUrl", photoUrl);
            Picasso
                    .get()
                    .load(photoUrl)
                    .into(imageView);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            Picasso
                    .get()
                    .load(R.drawable.moon)
                    .into(imageView);
        }

        textViewName.setText(results.getName());
        textViewAddress.setText(results.getVicinity());
        // check if ratings is available for the place
        if (results.getRating() != null) {
            linearLayoutRating.setVisibility(View.VISIBLE);
            textViewRating.setText(results.getRating());
            ratingBar.setRating(Float.valueOf(results.getRating()));
        }
        // check if opening hours is available
        if (results.getOpeningHours() != null) {
            textViewAvailability.setText(results.getOpeningHours().getOpenNow() == false ? "Closed" : "Open");
        } else {
            textViewAvailability.setText("Not found!");
        }

        linearLayoutShowOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PlaceDetailsActivity.this, PlaceOnMapActivity.class);
                intent.putExtra("result", results);
                intent.putExtra("lat", lat);
                intent.putExtra("lng", lng);
                intent.putExtra("type", "map");
                startActivity(intent);
                Animatoo.animateSlideRight(PlaceDetailsActivity.this);
            }
        });

        linearLayoutShowDistanceOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PlaceDetailsActivity.this, PlaceOnMapActivity.class);
                intent.putExtra("result", results);
                intent.putExtra("lat", lat);
                intent.putExtra("lng", lng);
                intent.putExtra("type", "distance");
                startActivity(intent);
                Animatoo.animateSlideLeft(PlaceDetailsActivity.this);
            }
        });

        favoritButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 i=i+1;
                 if (i%2!=0) {
                     favoritButton.setImageResource(R.drawable.ic_favorite_black_24dp);
                     //--------------FOR FAVORITE DATABASE----------//
                     String placrName=results.getName();
                     double latitute=lat;
                     double longitude= lng;
                     String rating=results.getRating();
                     String time=results.getOpeningHours().toString();
                     String icon=photoUrl;
                     long id=helper.addPlace(placrName,lat,lng,rating,time,icon);
                     databaseID= (int) id;
                     Toast.makeText(PlaceDetailsActivity.this, "Added to Favorite", Toast.LENGTH_SHORT).show();


                 }
                 else if (i%2==0) {
                     favoritButton.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                 helper.deleteData(databaseID);
                     Toast.makeText(PlaceDetailsActivity.this, "Removed from Favorite", Toast.LENGTH_SHORT).show();

                 }
            }
        });
    }

    private void init() {
        linearLayoutRating = findViewById(R.id.linearLayoutRating);
        linearLayoutShowOnMap = findViewById(R.id.linearLayoutShowOnMap);
        linearLayoutShowDistanceOnMap = findViewById(R.id.linearLayoutShowDistanceOnMap);
        textViewName = findViewById(R.id.textViewName);
        textViewRating = findViewById(R.id.textViewRating);
        textViewAddress = findViewById(R.id.textViewAddress);
        textViewAvailability = findViewById(R.id.addressTV);
        ratingBar = findViewById(R.id.ratingBar);
    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Animatoo.animateSlideLeft(PlaceDetailsActivity.this);
    }





}
