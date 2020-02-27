package com.example.nearbylocaton.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;

import com.example.nearbylocaton.R;
import com.example.nearbylocaton.adapter.FavoritesRVAdapter;
import com.example.nearbylocaton.dataBase.DatabaseOpenHelper;
import com.example.nearbylocaton.pogos.Favorite;

import java.util.ArrayList;

public class FavoriteRV extends AppCompatActivity {
    private RecyclerView favoriteRV;
    private DatabaseOpenHelper helper;
    private ArrayList<Favorite> allDatas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_rv);
        favoriteRV=findViewById(R.id.favoriteRV);

        handelshowData();
    }

    private void handelshowData() {
        allDatas = new ArrayList<>();
        helper = new DatabaseOpenHelper(this);
        FavoritesRVAdapter favoritesRVAdapter = new FavoritesRVAdapter(this,allDatas);
        favoriteRV.setLayoutManager(new LinearLayoutManager(this));
        favoriteRV.setAdapter(favoritesRVAdapter);

        DatabaseOpenHelper helper=new DatabaseOpenHelper(this);
        Cursor cursor =helper.getData();

        if(cursor.getCount() >= 1){
           while(cursor.moveToNext()){
                int id = cursor.getInt(cursor.getColumnIndex(helper.COL_ID));
                String icon=cursor.getString(cursor.getColumnIndex(helper.COL_ICON));
                String placrName = cursor.getString(cursor.getColumnIndex(helper.COL_NAME));
                double latitute = cursor.getDouble(cursor.getColumnIndex(helper.COL_LAT));
                double longitude = cursor.getDouble(cursor.getColumnIndex(helper.COL_LNG));
                String rating = cursor.getString(cursor.getColumnIndex(helper.COL_RATING));
                String time=cursor.getString(cursor.getColumnIndex(helper.COL_OPENTIME));

                allDatas.add(new Favorite(placrName,latitute,longitude,rating,icon,time));
                favoritesRVAdapter.notifyDataSetChanged();
//
            }}
    }
}
