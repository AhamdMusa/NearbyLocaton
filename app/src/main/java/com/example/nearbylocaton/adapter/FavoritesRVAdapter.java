package com.example.nearbylocaton.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nearbylocaton.DataBase.DatabaseOpenHelper;
import com.example.nearbylocaton.R;
import com.example.nearbylocaton.models.MyPlaces;
import com.example.nearbylocaton.models.Photos;
import com.example.nearbylocaton.models.Results;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FavoritesRVAdapter  {

    //-------------------------------------Have To Do All Work-------------------------------------//

/*extends RecyclerView.Adapter<FavoritesRVAdapter.ViewHolder>
    private Photos photos;
    private DatabaseOpenHelper helper;
    private Context context;
    private ArrayList<MyPlaces> myPlaces;

    public FavoritesRVAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_place_single, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final MyPlaces dataAll = myPlaces.get(position);
        holder.name.setText(dataAll.g);
    }

    @Override
    public int getItemCount() {
        return myPlaces.size();
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
            ratingBar=view.findViewById(R.id.ratingBar);
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
        name.setText(data.getName());
        address.setText(results.getRating());

    }
}*/
}
