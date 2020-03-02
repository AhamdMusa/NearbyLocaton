package com.example.nearbylocaton.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nearbylocaton.R;
import com.example.nearbylocaton.activity.PlaceDetailsActivity;
import com.example.nearbylocaton.models.MyPlaces;
import com.example.nearbylocaton.models.Photos;
import com.example.nearbylocaton.models.Results;
import com.squareup.picasso.Picasso;

public class PlaceRecyclerViewAdapter extends RecyclerView.Adapter<PlaceRecyclerViewAdapter.ViewHolder> {
    private Photos photos;

    private Context context;
    private MyPlaces myPlaces;
    private double lat, lng;


    public PlaceRecyclerViewAdapter(Context context, MyPlaces myPlaces, double lat, double lng) {
        this.context = context;
        this.myPlaces = myPlaces;
        this.lat = lat;
        this.lng = lng;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_place_single, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Results results = myPlaces.getResults().get(position);
        holder.bind(results);
        holder.singleItemView.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PlaceDetailsActivity.class);
                intent.putExtra("result", results);
                intent.putExtra("lat", lat);
                intent.putExtra("lng", lng);
                Pair[] pairs=new Pair[1];
                pairs[0] =new Pair<View, String>(holder.placeIV, "imageTRMap");
                //pairs[1] =new Pair<View, String>(holder.name, "textTRMap");
                ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation((Activity) context,pairs);
                context.startActivity(intent, options.toBundle());
            }
        });
    }

    @Override
    public int getItemCount() {
        return myPlaces.getResults().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name, address;
        public CardView singleItemView;
        ImageView placeIV;
        public ImageView placeImageView;
        public ImageButton favorite;
        public RatingBar ratingBar;

        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.textViewPlaceName);
            address = view.findViewById(R.id.textViewAddress);
            placeIV = view.findViewById(R.id.placeImageView);
            singleItemView = view.findViewById(R.id.singleItemView);
            placeImageView = view.findViewById(R.id.placeImageView);
            ratingBar=view.findViewById(R.id.rating_on_singleplace);
        }

        public void bind(Results results) {
            try {
                // get photo
                photos = results.getPhotos()[0];
                String photoUrl = String.format("https://maps.googleapis.com/maps/api/place/photo?maxwidth=%s&photoreference=%s&key=%s", 400, photos.getPhoto_reference(), context.getResources().getString(R.string.google_maps_key));
                Log.d("photoUrl", photoUrl);
                Picasso
                        .get()
                        .load(photoUrl)
                        .into(placeImageView);
            } catch (Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                Picasso
                        .get()
                        .load(R.drawable.photo)
                        .into(placeImageView);
            }
            name.setText(results.getName());
            Log.d("rwere",results.getRating());
            ratingBar.setRating(Float.parseFloat(results.getRating()));
            if (results.getOpeningHours() != null) {
                address.setText(results.getOpeningHours().getOpenNow() == false ? "Closed" : "Open");
            } else {
                address.setText("Not found!");
            }



        }
    }
}
