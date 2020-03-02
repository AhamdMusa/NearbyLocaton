package com.example.nearbylocaton.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nearbylocaton.R;
import com.example.nearbylocaton.modelplacedetails.Placedetails;
import com.example.nearbylocaton.modelplacedetails.Review;
import com.squareup.picasso.Picasso;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
    private Context context;
    private Placedetails placedetails;

    public ReviewAdapter(Context context) {
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
       Review review =placedetails.getResult().getReviews().get(position);
       holder.gideName.setText(review.getAuthorName());
       holder.bind(review);
       holder.review.setText(review.getRating().toString());
       holder.ratingBar.setRating(review.getRating());
       if(review.getText()!=null){
       holder.reviewTV.setText(review.getText()); }

        }

    @Override
    public int getItemCount() {
        return placedetails.getResult().getReviews().size();
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

        public void bind (Review review){
            try {
                String photoURL=review.getProfilePhotoUrl();
                Picasso
                        .get()
                        .load(photoURL)
                        .into(gidePic);
            }catch (Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                Picasso
                        .get()
                        .load(R.drawable.photo)
                        .into(gidePic);
            }
        }
    }
}
