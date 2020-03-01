package com.example.nearbylocaton.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nearbylocaton.R;
import com.example.nearbylocaton.modelplacedetails.Placedetails;
import com.example.nearbylocaton.modelplacedetails.Result;

public class ReviewRV extends RecyclerView.Adapter<ReviewRV.ViewHolder> {
    private Context context;
    private Placedetails placedetails;

    public ReviewRV(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_rv, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       // Result result=placedetails.getResult().get(position);

        }

    @Override
    public int getItemCount() {
        return 0;
        //placedetails.getResult().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView gideName, review,reviewTV;
        public ImageView gidePic;
        public RatingBar ratingBar;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            gideName=itemView.findViewById(R.id.gideName);
            gidePic=itemView.findViewById(R.id.gidePic);
            review=itemView.findViewById(R.id.rating);
            ratingBar=itemView.findViewById(R.id.rating_on_review);
            reviewTV=itemView.findViewById(R.id.reviewText);


        }
    }
}
