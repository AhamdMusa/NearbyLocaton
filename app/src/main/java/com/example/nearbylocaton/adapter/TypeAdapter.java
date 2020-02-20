package com.example.nearbylocaton.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nearbylocaton.R;
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
    public void onBindViewHolder(@NonNull TypeAdapter.ViewHolder holder, int position) {


        CardItems cardItem=products.get(position);

        holder.productImageIV.setImageResource(cardItem.getIcon());
        holder.productName.setText(cardItem.getTitle());
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView productName;
        private ImageView productImageIV;

        public ViewHolder(View view) {
            super(view);
            productImageIV = view.findViewById(R.id.cardicon);
            productName= view.findViewById(R.id.cardtitle);

        }


    }
}
