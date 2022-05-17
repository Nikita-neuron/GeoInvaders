package com.example.geoinvaders;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.geoinvaders.database.DBUserManager;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity{

    LocationManager locationManager;
    private static final int REQUEST_FINE_LOCATION = 1;
    private static final int REQUEST_COARSE_LOCATION = 2;
    private static final int REQUEST_WRITE_STORAGE = 3;

    // на github:
    // проект
    // презентация
    // readme
    //-------------

    DBUserManager dbUserManager;

    String userName;

    RelativeLayout mainLayout;

    Button loginBtn;
    Button infoBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_FINE_LOCATION);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_COARSE_LOCATION);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_STORAGE);
        }

        mainLayout = findViewById(R.id.mainLayout);

        loginBtn = findViewById(R.id.login);
        infoBtn = findViewById(R.id.info);

        loginBtn.setOnClickListener(view -> {
            Intent intent = new Intent(this, LogInActivity.class);
            intent.putExtra("userName", userName);
            startActivity(intent);
        });

        infoBtn.setOnClickListener(view -> {
            Toast.makeText(this, "Данная кнопка пока не работает", Toast.LENGTH_LONG).show();
        });

        dbUserManager = DBUserManager.getInstance(this);

        userName = "none";

        String user = dbUserManager.existUser();

        // если пользователь существует, то открываем карту
        if (!user.equals("")) {
            userName = user;
            Intent intent = new Intent(this, MapActivity.class);
            intent.putExtra("userName", userName);
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // проверка ответа пользователя по разрешениям
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_FINE_LOCATION) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Предоставляет дополнительную информацию, если разрешение
                // не было дано, а пользователь должен получить разъяснения
                Snackbar.make(mainLayout, "Без данного разрешения, вы не сможете захватывать территории", Snackbar.LENGTH_INDEFINITE)
                        .setAction("OK", view -> ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                                REQUEST_FINE_LOCATION)).show();
            }

        } else if (requestCode == REQUEST_COARSE_LOCATION){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
                // Предоставляет дополнительную информацию, если разрешение
                // не было дано, а пользователь должен получить разъяснения
                Snackbar.make(mainLayout, "Без данного разрешения, вы не сможете захватывать территории", Snackbar.LENGTH_INDEFINITE)
                        .setAction("OK", view -> ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_COARSE_LOCATION)).show();
            }
        } else if (requestCode == REQUEST_WRITE_STORAGE){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Предоставляет дополнительную информацию, если разрешение
                // не было дано, а пользователь должен получить разъяснения
                Snackbar.make(mainLayout, "Без данного разрешения, вы не сможете сохранять достижения", Snackbar.LENGTH_INDEFINITE)
                        .setAction("OK", view -> ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE)).show();
            }
        }
    }
}