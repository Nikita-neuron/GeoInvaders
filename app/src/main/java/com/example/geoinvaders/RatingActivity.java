package com.example.geoinvaders;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.geoinvaders.adapters.RatingUsersAdapter;
import com.example.geoinvaders.serverCommunication.Region;
import com.example.geoinvaders.serverCommunication.ServerMessages;

import java.util.ArrayList;

public class RatingActivity extends AppCompatActivity {
    private RatingUsersAdapter ratingUsersAdapter;

    private RecyclerView usersRating;
    private Button ratingBackBtn;

    private ProgressBar progressBar;

    private ServerMessages serverMessages;

    private ArrayList<Region> usersRegions;

    private String userName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        userName = getIntent().getExtras().getString("userName");

        serverMessages = ServerMessages.getInstance();

        progressBar = findViewById(R.id.progressBarRating);
        usersRating = findViewById(R.id.usersRating);
        ratingBackBtn = findViewById(R.id.ratingBack);

        usersRegions = new ArrayList<>();

        ratingUsersAdapter = new RatingUsersAdapter(usersRegions, userName, this);
        usersRating.setAdapter(ratingUsersAdapter);
        usersRating.setLayoutManager(new LinearLayoutManager(this));

        serverMessages.getRegions(this, usersRegions, progressBar, ratingUsersAdapter);

        ratingBackBtn.setOnClickListener(view -> {
            Intent intent = new Intent(this, MapActivity.class);
            intent.putExtra("userName", userName);
            startActivity(intent);
        });
    }
}
