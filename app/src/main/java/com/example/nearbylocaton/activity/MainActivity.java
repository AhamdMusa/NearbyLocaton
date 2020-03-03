package com.example.nearbylocaton.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.nearbylocaton.R;
import com.example.nearbylocaton.adapter.TabPagerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public TabLayout tabLayout;
    public ViewPager viewPager;
    private FrameLayout frameLayout;
    private FragmentManager fm;
    private FragmentTransaction ft;
    //-----for DrawerLayout--------//
    private DrawerLayout drawer;
    private LottieAnimationView dot;
    private LinearLayout traffic,speedometer,area,route,compass;
    private ShowMeOnMap showMeOnMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            checkPermission();
        }

        tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.viewpager);
        frameLayout= findViewById(R.id.mainFrame);
        drawer = findViewById(R.id.drawer_layout);
        dot=findViewById(R.id.dot_dot);
        traffic=findViewById(R.id.trafficLL);
        speedometer=findViewById(R.id.speedometerLL);
        area=findViewById(R.id.area_measurementLL);
        route=findViewById(R.id.routeLL);
        compass=findViewById(R.id.fab);
       // new Progerss().execute();

        viewPager.setAdapter(new TabPagerAdapter(getSupportFragmentManager()));

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });

        onCliks();
        init();




    }

    //-----------------------for voice input,transfer it to route Activity---------------------------//
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String txvResult=(result.get(0));
                    Toast.makeText(showMeOnMap, ""+txvResult, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    //-----------------------for voice input,transfer it to route Activity---------------------------//

    private void onCliks() {
        dot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);
               // Toast.makeText(MainActivity.this, "OK", Toast.LENGTH_SHORT).show();
            }
        });
        route.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //-----------------------for voice input,transfer it to route Activity---------------------------//
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"Speach to Text ");

                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent,1);
                } else {
                    Toast.makeText(MainActivity.this, "Your Device Don't Support Speech Input", Toast.LENGTH_SHORT).show();
                }
                //-----------------------for voice input,transfer it to route Activity---------------------------//

            }
        });


        area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // Toast.makeText(MainActivity.this, "OK", Toast.LENGTH_SHORT).show();

            }
        });
        traffic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ShowMeOnMap.class);
                intent.putExtra("type", "true");
                startActivity(intent);
                }
        });
        speedometer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent speedometer = new Intent(MainActivity.this, SpeedometerActivity.class);
                startActivity(speedometer);
            }
        });
        compass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent compass = new Intent(MainActivity.this, CompassActivity.class);
                startActivity(compass);
            }
        });
    }

    private void init() {}


    private void checkPermission() {
        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.ACCESS_NETWORK_STATE,
        };

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
    }

    boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.nav_favorite:
                Intent favoriteRV = new Intent(this, FavoriteRV.class);
                startActivity(favoriteRV);
                break;
            case R.id.nav_share:
                Intent intent=new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String shareSub ="NearbyLocation";
                String shareBody= "NearbyLocation App can find anything anywhere (almost true)";
                intent.putExtra(Intent.EXTRA_SUBJECT,shareSub);
                intent.putExtra(Intent.EXTRA_TEXT,shareBody);
                startActivity(Intent.createChooser(intent,"Share It By"));
                break;
            case R.id.nav_faq:
                Toast.makeText(this, "Coming Soon", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_about_us:
                Toast.makeText(this, "Coming Soon", Toast.LENGTH_SHORT).show();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
   /* class Progerss extends AsyncTask<Void,Integer,Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            for (int i=1; i<=10;i++){
                try {
                    Thread.sleep(3000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                publishProgress(i);

            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            Animation animator = AnimationUtils.loadAnimation(MainActivity.this,R.anim.bounce);
            dot.startAnimation(animator);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }
*/
}

