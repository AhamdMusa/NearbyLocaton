package com.example.nearbylocaton.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nearbylocaton.R;
import com.example.nearbylocaton.activity.LocationRV;
import com.example.nearbylocaton.constants.CardItems;

import java.util.List;

public class TypeAdapter extends RecyclerView.Adapter<TypeAdapter.ViewHolder> {

    private Context context;
    private List<CardItems> products;



    public TypeAdapter(Context context, List<CardItems> products) {
        this.context = context;
        this.products = products;
    }

    @NonNull
    @Override
    public TypeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.card_desing, parent,false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final TypeAdapter.ViewHolder holder, int position) {


        final CardItems cardItem=products.get(position);

        holder.productImageIV.setImageResource(cardItem.getIcon());
        holder.productName.setText(cardItem.getTitle());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                String placeName = cardItem.getTitle();
                Intent intent = new Intent(context, LocationRV.class);
                intent.putExtra("placeName",placeName);
                Bundle bundle=new Bundle();
                bundle.putInt("icon",cardItem.getIcon());
                intent.putExtras(bundle);
                Pair[] pairs=new Pair[1];
                pairs[0] =new Pair<View, String>(holder.productImageIV, "imageTR");
                //pairs[1] =new Pair<View, String>(holder.productName, "textTR");
                ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation((Activity) context,pairs);
                context.startActivity(intent, options.toBundle());

            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView productName;
        private ImageView productImageIV;
        private CardView cardView;

        public ViewHolder(View view) {
            super(view);
            productImageIV = view.findViewById(R.id.cardicon);
            productName= view.findViewById(R.id.cardtitle);
            cardView=view.findViewById(R.id.typeCard);

        }


    }
}
