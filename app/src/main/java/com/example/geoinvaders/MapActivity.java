package com.example.geoinvaders;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.geoinvaders.database.DBUserManager;
import com.example.geoinvaders.mapRegions.MapRegion;
import com.example.geoinvaders.mapRegions.RegionsHelp;
import com.example.geoinvaders.serverCommunication.Region;
import com.example.geoinvaders.serverCommunication.ServerMessages;
import com.example.geoinvaders.views.InformationView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.maps.android.PolyUtil;

import java.util.ArrayList;
import java.util.Random;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private String userName;

    private DBUserManager dbUserManager;

    private GoogleMap mMap;
    private LatLng userPosition;

    private ArrayList<ArrayList<MapRegion>> regions;
    private ArrayList<Polygon> polygons;
    private ArrayList<Region> usersRegions;
    private ArrayList<MapRegion> activeRegions;

    private LocationManager locationManager;

    private boolean moveMapUserPosition;

    private Button buttonSeize;
    private Button buttonLogOff;
    private Button rating;
    private TextView scoreTime;
    private TextView scoreText;
    ProgressBar progressBar;
    private RelativeLayout parentLayout;

    private RegionsHelp regionsHelp;

    private Handler mUiHandler = new Handler();

    private ServerMessages serverMessages;

    private int score;
    private int millisScore;
    private CountDownTimer countDownTimer;

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        userName = getIntent().getExtras().getString("userName");

        dbUserManager = DBUserManager.getInstance(this);

        score = 100;

        activeRegions = new ArrayList<>();
        polygons = new ArrayList<>();
        usersRegions = new ArrayList<>();

        progressBar = findViewById(R.id.progressBarMap);
        progressBar.setVisibility(ProgressBar.INVISIBLE);

        serverMessages = ServerMessages.getInstance();

        serverMessages.getRegions(this, usersRegions, progressBar, null);

//        serverMessages.....(userName, score, millisScore);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                userPosition = new LatLng(location.getLatitude(), location.getLongitude());

                if (moveMapUserPosition) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(userPosition));
                    moveMapUserPosition = false;
                }
            }

            @Override
            public void onProviderEnabled(@NonNull String provider) {

            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        moveMapUserPosition = true;

        buttonSeize = findViewById(R.id.btnSeize);
        parentLayout = findViewById(R.id.parent_layout);
        buttonLogOff = findViewById(R.id.logoff);
        scoreTime = findViewById(R.id.scoreTime);
        scoreText = findViewById(R.id.score);
        rating = findViewById(R.id.rating);
        buttonSeize.setOnClickListener(v -> {
            if (score < 20) {
                Toast.makeText(this, "У вас недостаточно энергии, для захвата нужно минимум 20 единиц", Toast.LENGTH_LONG).show();
                return;
            }
            serverMessages.getRegions(this, usersRegions, progressBar, null);
            boolean find = false;
            for (Polygon polygon : polygons) {
                boolean contains = PolyUtil.containsLocation(userPosition, polygon.getPoints(), false);
                if (contains) {
                    for(Region userRegion : usersRegions) {
                        LatLng latLng = new LatLng(userRegion.getLatitude(), userRegion.getLongitude());
                        boolean userContain = PolyUtil.containsLocation(latLng, polygon.getPoints(), false);

                        if (userContain && userName.equals(userRegion.getUsername())) {
                            find = true;
                            Toast.makeText(this, "Данная область уже захвачена вами", Toast.LENGTH_LONG).show();
                            break;
                        }
                    }
                }
                if (find) break;
            }

            if (!find) createTimer();
        });

        buttonLogOff.setOnClickListener(view -> {
            dbUserManager.deleteUser(userName);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

        rating.setOnClickListener(view -> {
            Intent intent = new Intent(this, RatingActivity.class);
            intent.putExtra("userName", userName);
            startActivity(intent);
        });

        scoreText.setText(score + "");

        regionsHelp = new RegionsHelp();

        countDownTimer = new CountDownTimer(millisScore, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {

            }
        };
    }

    public void createTimer() {
        InformationView informationView = new InformationView(this);
        ViewGroup.LayoutParams params = new ViewGroup.
                LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        parentLayout.addView(informationView, params);
        mMap.getUiSettings().setAllGesturesEnabled(false);
        buttonSeize.setClickable(false);

        Context context = this;

        new CountDownTimer(20000, 1000) {
            public void onTick(long millisUntilFinished) {
                informationView.setText("Осталось: "
                        + millisUntilFinished / 1000);
            }

            @SuppressLint("SetTextI18n")
            @RequiresApi(api = Build.VERSION_CODES.O)
            public void onFinish() {
                informationView.setText("Захвачено");
                for (Polygon polygon : polygons) {
                    boolean contains = PolyUtil.containsLocation(userPosition, polygon.getPoints(), false);
                    if (contains) {
                        Random rnd = new Random();
                        int red = rnd.nextInt(256);
                        int green = rnd.nextInt(256);
                        int blue = rnd.nextInt(256);
                        String color = red + "-" + green + "-" + blue;

                        for (Region userRegion : usersRegions) {
                            if (userName.equals(userRegion.getUsername())) {
                                color = userRegion.getRgb();
                                String[] rgb = color.split("-");
                                red = Integer.parseInt(rgb[0]);
                                green = Integer.parseInt(rgb[1]);
                                blue = Integer.parseInt(rgb[2]);
                            }
                        }
                        polygon.setFillColor(Color.argb(0.2F, red, green, blue));

                        LatLng center = new LatLng(0, 0);

                        for (int i = 0; i < regions.size(); i++) {
                            for (MapRegion region : regions.get(i)) {
                                boolean contain = PolyUtil.containsLocation(userPosition, region.getPoints(), false);
                                if (contain) {
                                    center = region.getCenter();
                                    break;
                                }
                            }
                        }

                        Region region = new Region(userName, center.latitude, center.longitude, color);
                        serverMessages.addRegion(context, region, usersRegions, progressBar);
                        break;
                    }
                }
                score -= 20;
                scoreText.setText(score + "");
                parentLayout.removeView(informationView);
                mMap.getUiSettings().setAllGesturesEnabled(true);
                buttonSeize.setClickable(true);
            }
        }.start();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        try {
            boolean success = mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(this, R.raw.style_json));
            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }

        mMap.setMinZoomPreference(16.0f);

        regions = regionsHelp.calculatePolygon();

        mMap.setOnCameraIdleListener(() -> {
            polygons.clear();
            activeRegions.clear();
            mMap.clear();
            mMap.setMinZoomPreference(16.0f);

            LatLngBounds bounds = mMap.getProjection().getVisibleRegion().latLngBounds;

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);

            Runnable runnable = () -> {
                int beginI = 0;
                int beginJ = 0;
                boolean find = false;
                for (int i = 0; i < regions.size(); i++) {
                    ArrayList<MapRegion> regionArrayList = regions.get(i);
                    for (int j = 0; j < regionArrayList.size(); j++) {
                        MapRegion region = regionArrayList.get(j);
                        boolean contains = PolyUtil.containsLocation(bounds.getCenter(), region.getPoints(), false);

                        if (contains) {
                            beginI = i;
                            beginJ = j;
                            find = true;
                        }
                    }
                    if (find) break;
                }
                drawPolygon(beginI, beginJ);
            };
            Thread thread = new Thread(runnable);
            thread.start();
        });
    }

    public void drawPolygon(int beginI, int beginJ) {
        int n = 3;
        while ((beginI + n) <= regions.size()) {
            if (beginI != 0) beginI--;
            if (beginJ != 0) beginJ--;
            for (int i = beginI; i < beginI + n; i++) {
                if ((beginJ + n) > regions.get(i).size()) break;
                for (int j = beginJ; j < beginJ + n; j++) {
                    MapRegion region = regions.get(i).get(j);
                    if (!activeRegions.contains(region)) {
                        PolygonOptions polygonOptions = new PolygonOptions().addAll(region.getPoints())
                                .strokeColor(Color.BLUE)
                                .strokeWidth(3);
                        mUiHandler.post(() -> {
                            Polygon polygon = mMap.addPolygon(polygonOptions);

                            for (Region userRegion : usersRegions) {
                                LatLng latLng = new LatLng(userRegion.getLatitude(), userRegion.getLongitude());
                                boolean userContain = PolyUtil.containsLocation(latLng, region.getPoints(), false);

                                if (userContain) {
                                    String[] rgb = userRegion.getRgb().split("-");
                                    polygon.setFillColor(Color.argb(0.2F, Integer.parseInt(rgb[0]),
                                            Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2])));
                                }
                            }

                            polygons.add(polygon);
                        });
                    }
                }
            }
            n += 2;
            if (n > 10) break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
