package com.example.nearbylocaton.fragments;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nearbylocaton.R;
import com.example.nearbylocaton.activity.LocationRV;
import com.example.nearbylocaton.activity.MainActivity;
import com.example.nearbylocaton.adapter.DataPass;
import com.example.nearbylocaton.adapter.TypeAdapter;
import com.example.nearbylocaton.constants.CardItems;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class NearByFragment extends Fragment {


    private ImageView imageViewSearch;
    private Spinner spinner_nearby_choices;
    private Context context;
    private RecyclerView productRV;
    private List<CardItems> products;
    private TypeAdapter productAdapter;
    private CardView singleItemView;
    private MainActivity mainActivity;
    double lat;
    double lng;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_near_by, container, false);

        spinner_nearby_choices = view.findViewById(R.id.spinner_nearby_choices);
        singleItemView = view.findViewById(R.id.singleItemView);
        productRV = view.findViewById(R.id.nearbymeRV);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            lat = bundle.getDouble("lat");
            lng= bundle.getDouble("lng");
        }

        init();

        getAllProduct();

        spinner_nearby_choices.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                position = spinner_nearby_choices.getSelectedItemPosition();
                if (position == 0) {
                    //Toast.makeText(getContext(), "Please select valid type", Toast.LENGTH_SHORT).show();
                } else {
                    //---------------------On Clicke next Frag te dite hobe-----------------------//
                    String placeName = spinner_nearby_choices.getSelectedItem().toString();
                    Intent intent = new Intent(getActivity(), LocationRV.class);
                    intent.putExtra("placeName",placeName);
                    Bundle bundle=new Bundle();
                    bundle.putInt("icon",R.drawable.search);
                    bundle.putDouble("lat",lat);
                    bundle.putDouble("lng",lng);
                    intent.putExtras(bundle);
                    startActivity(intent);



                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        return view;
    }

    private void init() {

        products = new ArrayList<>();
        productAdapter = new TypeAdapter(getContext(), products);
        productRV.setLayoutManager(new GridLayoutManager(getContext(), 3));
        productRV.setAdapter(productAdapter);
    }

    private void getAllProduct() {
        //  products.add(new CardItems(R.drawable.nearbyicon, "A to Z"));
        products.add(new CardItems(R.drawable.airplane, "Airport"));
        products.add(new CardItems(R.drawable.atm, "ATM"));
        products.add(new CardItems(R.drawable.bank, "Bank"));
        products.add(new CardItems(R.drawable.bakery, "Bakery"));
        products.add(new CardItems(R.drawable.bar, "Bar"));
        products.add(new CardItems(R.drawable.bookstore, "Book Store"));
        products.add(new CardItems(R.drawable.busstation, "Bus Station"));
        products.add(new CardItems(R.drawable.carrental, "Car Rental"));
        products.add(new CardItems(R.drawable.carwash, "Car Wash"));
        products.add(new CardItems(R.drawable.coffeeshop, "Cafe"));
        products.add(new CardItems(R.drawable.clothingstore, "Clothing Store"));
        products.add(new CardItems(R.drawable.dentist, "Dentist"));
        products.add(new CardItems(R.drawable.doctor, "Doctor"));
        products.add(new CardItems(R.drawable.electronicsstore, "Electronics Store"));
        products.add(new CardItems(R.drawable.embassy, "Embassy"));
        products.add(new CardItems(R.drawable.fireservice, "Fire Station"));
        products.add(new CardItems(R.drawable.gasstation, "Gas Station"));
        products.add(new CardItems(R.drawable.govtoffice, "Govt Office"));
        products.add(new CardItems(R.drawable.gym, "Gym"));
        products.add(new CardItems(R.drawable.hospital, "Hospital"));
        products.add(new CardItems(R.drawable.jewelrystore, "Jewelry Store"));
        products.add(new CardItems(R.drawable.laundry, "Laundry"));
        products.add(new CardItems(R.drawable.mosque, "Mosque"));
        products.add(new CardItems(R.drawable.movietheater, "Movie Theater"));
        products.add(new CardItems(R.drawable.park, "Park"));
        products.add(new CardItems(R.drawable.pharmacy, "Pharmacy"));
        products.add(new CardItems(R.drawable.police, "Police"));
        products.add(new CardItems(R.drawable.postoffice, "Post Office"));
        products.add(new CardItems(R.drawable.restaurant, "Restaurant"));
        products.add(new CardItems(R.drawable.school, "School"));
        products.add(new CardItems(R.drawable.college, "Secondary School"));
        products.add(new CardItems(R.drawable.shoppingmall, "Shopping Mall"));
        products.add(new CardItems(R.drawable.university, "University"));


        productAdapter.notifyDataSetChanged();
    }


}
