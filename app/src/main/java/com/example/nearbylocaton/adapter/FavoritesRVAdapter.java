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
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nearbylocaton.activity.PlaceDetailsActivity;
import com.example.nearbylocaton.dataBase.DatabaseOpenHelper;
import com.example.nearbylocaton.R;
import com.example.nearbylocaton.models.Photos;
import com.example.nearbylocaton.models.Results;
import com.example.nearbylocaton.pogos.Favorite;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FavoritesRVAdapter extends RecyclerView.Adapter<FavoritesRVAdapter.ViewHolder>{
    private Photos photos;
    private DatabaseOpenHelper helper;
    private Context context;
    private ArrayList<Favorite> favorites;

    private double latitute, longitude;

    public FavoritesRVAdapter( Context context, ArrayList<Favorite> favorites) {


        this.context = context;
        this.favorites = favorites;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_place_single, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Results results=new Results();
        Favorite favorite=favorites.get(position);
        holder.name.setText(favorite.getPlacename());
        holder.bind(favorite);
        holder.singleItemView.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PlaceDetailsActivity.class);
                intent.putExtra("result", results);
                intent.putExtra("lat", latitute);
                intent.putExtra("lng", longitude);
              //  Pair[] pairs=new Pair[1];
              //  pairs[0] =new Pair<View, String>(holder.placeIV, "imageTRMap");
                //pairs[1] =new Pair<View, String>(holder.name, "textTRMap");
               // ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation((Activity) context,pairs);
               // context.startActivity(intent, options.toBundle());
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return favorites.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name, address;
        public CardView singleItemView;
        ImageView placeIV;
        public ImageView placeImageView;
        public RatingBar ratingBar;

        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.textViewPlaceName);
            address = view.findViewById(R.id.textViewAddress);
            singleItemView = view.findViewById(R.id.singleItemView);
            placeImageView = view.findViewById(R.id.placeImageView);
            ratingBar=view.findViewById(R.id.rating_on_singleplace);
       }

    public void bind(Favorite favorite) {

           String photoURL=favorite.getIconURL();
            Picasso.get()
                    .load(photoURL)
                    .error(R.drawable.moon)
                    .into(placeImageView);


        ratingBar.setRating(Float.parseFloat(favorite.getRating()));

    }
}
}
