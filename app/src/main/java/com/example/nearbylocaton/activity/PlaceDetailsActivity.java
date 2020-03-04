package com.example.nearbylocaton.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.nearbylocaton.adapter.ReviewAdapter;
import com.example.nearbylocaton.dataBase.DatabaseOpenHelper;
import com.example.nearbylocaton.R;
import com.example.nearbylocaton.modelplacedetails.Placedetails;
import com.example.nearbylocaton.modelplacedetails.Result;
import com.example.nearbylocaton.models.Photos;
import com.example.nearbylocaton.models.Results;
import com.example.nearbylocaton.webApi.GoogleApiService;
import com.example.nearbylocaton.webApi.RetrofitBuilder;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaceDetailsActivity extends AppCompatActivity {

    //---------For Place Details --------
    private GoogleApiService googleApiService;
    private Placedetails placedetails;
    private Result result;
    private String placeid="";

    private ImageView imageView;
    private Photos photos;
    private TextView textViewName;
    private TextView textViewRating;
    private TextView textViewAddress;
    private TextView textViewAvailability;
    private TextView cartime,biketime,walktime;
    private LinearLayout call,website,share;
    private RatingBar ratingBar;
    private LinearLayout linearLayoutRating;
    private LinearLayout linearLayoutShowOnMap;
    private LinearLayout linearLayoutShowDistanceOnMap;

    private ReviewAdapter reviewAdapter;
    private RecyclerView commentsRV;
    //--------------FOR FAVORITE DATABASE----------//
    private ImageView favoritButton;
    private DatabaseOpenHelper helper;
    private String photoUrl;
    int i=0,databaseID;

    //----------------for place share----------//
    private String placeToShare;

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
                Intent intent = new Intent(PlaceDetailsActivity.this, DirectionActivity.class);
                intent.putExtra("result", results);
                intent.putExtra("lats", lat);
                intent.putExtra("lngs", lng);
              //  intent.putExtra("type", "distance");
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
                     String time= String.valueOf(results.getOpeningHours());
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

        website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placeToShare= result.getWebsite();
                if (placeToShare!=null){
                Intent intent=new Intent(Intent.ACTION_VIEW);
                    intent.setType("text/plain");
                    String shareSub ="A Place Location by NearbyLocation";
                    String shareBody= placeToShare;
                    intent.putExtra(Intent.EXTRA_SUBJECT,shareSub);
                    intent.putExtra(Intent.EXTRA_TEXT,shareBody);

                startActivity(Intent.createChooser(intent,"Open with"));}
                else {
                    Toast.makeText(PlaceDetailsActivity.this, "No Website found.", Toast.LENGTH_SHORT).show();
            }}
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String shareSub ="A Place Location by NearbyLocation";
                String shareBody= placeToShare;
                intent.putExtra(Intent.EXTRA_SUBJECT,shareSub);
                intent.putExtra(Intent.EXTRA_TEXT,shareBody);
                startActivity(Intent.createChooser(intent,"Share It By"));
            }
        });

        // ---------------- PlaceDetails API Calling here calling here --------------------

        placeid = results.getPlace_id();
        Toast.makeText(this, "" + placeid, Toast.LENGTH_SHORT).show();

        getPlaceDetailsAll();

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
        call=findViewById(R.id.makecall);
        website=findViewById(R.id.website);
        share=findViewById(R.id.share);
        cartime=findViewById(R.id.cartime);
        biketime=findViewById(R.id.biketime);
        walktime=findViewById(R.id.walktime);
        commentsRV=findViewById(R.id.commentsRV);
            }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Animatoo.animateSlideLeft(PlaceDetailsActivity.this);
    }

   // ---------Start METHOD  For Place details API -----------

    private String buildUrl(String place_id) {
        StringBuilder stringBuilder = new StringBuilder("api/place/details/json?");
        stringBuilder.append("place_id=");
        stringBuilder.append(place_id);
        stringBuilder.append("&fields=address_component,adr_address,formatted_address,geometry,icon,name,permanently_closed,photo,place_id,plus_code,type,url,utc_offset,vicinity,formatted_phone_number,international_phone_number,opening_hours,website,price_level,rating,review,user_ratings_total");
        stringBuilder.append("&key=AIzaSyAAA41TquXF2wjMM5xtLs9KQQS9TkGD1Fw");

        return stringBuilder.toString();

    }

    private void getPlaceDetailsAll() {

        if (placeid != null) {

            String url = buildUrl(placeid);
            Log.d("finalURL", url);

            googleApiService = RetrofitBuilder.builder().create(GoogleApiService.class);

            Call<Placedetails> call = googleApiService.getPlaceAllDetails(url);
            call.enqueue(new Callback<Placedetails>() {
                @Override
                public void onResponse(Call<Placedetails> call, Response<Placedetails> response) {

                    Log.d("abcde", response.body().getStatus());

                    placedetails = response.body();
                    result = placedetails.getResult();
                    String a = result.getWebsite();
                    placeToShare=result.getFormattedAddress();
                    textViewAvailability.setText(result.getOpeningHours().getWeekdayText().toString().replace(",","\n").replace("[","")
                            .replace("]","."));

                    String name = placedetails.getResult().getName();
                    Toast.makeText(PlaceDetailsActivity.this, ""+name, Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onFailure(Call<Placedetails> call, Throwable t) {
                    Toast.makeText(PlaceDetailsActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Toast.makeText(PlaceDetailsActivity.this, "Place Id Not found", Toast.LENGTH_SHORT).show();
        }

       /* reviewAdapter=new ReviewAdapter(PlaceDetailsActivity.this);
        commentsRV.setLayoutManager(new LinearLayoutManager(PlaceDetailsActivity.this));
        commentsRV.setItemAnimator(new DefaultItemAnimator());
        commentsRV.setAdapter(reviewAdapter);
        reviewAdapter.notifyDataSetChanged();*/

    }
    // ---------Start METHOD  For Place details API -----------
}
