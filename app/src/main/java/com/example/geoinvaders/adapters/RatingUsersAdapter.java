package com.example.geoinvaders.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.geoinvaders.R;
import com.example.geoinvaders.serverCommunication.Region;
import com.example.geoinvaders.serverCommunication.ServerMessages;

import java.util.ArrayList;
import java.util.HashMap;

public class RatingUsersAdapter extends RecyclerView.Adapter<RatingUsersAdapter.ViewHolder>{
    private ArrayList<Region> usersRegions;
    private ArrayList<String> usersNames;

    String userName;

    ServerMessages serverMessages;

    Context context;

    public RatingUsersAdapter(ArrayList<Region> usersRegions, String userName, Context context) {
        this.usersRegions = usersRegions;
        this.userName = userName;

        this.usersNames = new ArrayList<>();

        serverMessages = ServerMessages.getInstance();

        this.context = context;
    }

    @NonNull
    @Override
    public RatingUsersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_user_rating, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RatingUsersAdapter.ViewHolder holder, int position) {

        Region region = usersRegions.get(position);
        int count = 0;

        for (Region userRegion : usersRegions) {
            if (region.getUsername().equals(userRegion.getUsername())) count++;
        }

        if (!usersNames.contains(region.getUsername()) && count > 0) {
            System.out.println(region.getUsername());
            TextView userNameText = holder.userName;
            TextView userScore = holder.userScore;
            View view = holder.view;

            userNameText.setText(region.getUsername());
            userScore.setText(count + "");

            if (userName.equals(region.getUsername())) {
                view.setBackgroundColor(Color.rgb(67, 230, 64));
            }
            usersNames.add(region.getUsername());
        }
        else {
            holder.view.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return usersRegions == null ? 0 : usersRegions.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView userName;
        private final TextView userScore;
        private final View view;

        public ViewHolder(View view) {
            super(view);
            userName = view.findViewById(R.id.userName);
            userScore = view.findViewById(R.id.userScore);

            this.view = view;
        }
    }
}
