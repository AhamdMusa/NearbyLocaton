package com.example.nearbylocaton.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.nearbylocaton.R;

public class FavoriteRV extends AppCompatActivity {
    private RecyclerView favoriteRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_rv);
        favoriteRV=findViewById(R.id.favoriteRV);

    }
}
