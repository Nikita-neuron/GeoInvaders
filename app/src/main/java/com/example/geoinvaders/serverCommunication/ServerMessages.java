package com.example.geoinvaders.serverCommunication;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.geoinvaders.MainActivity;
import com.example.geoinvaders.MapActivity;
import com.example.geoinvaders.R;
import com.example.geoinvaders.adapters.RatingUsersAdapter;
import com.example.geoinvaders.database.DBUserManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServerMessages {
    private final GeoInvadersService service;

    private static ServerMessages serverMessages;

    public static ServerMessages getInstance() {
        if (serverMessages == null) {
            serverMessages = new ServerMessages();
        }
        return serverMessages;
    }

    private ServerMessages() {
        service = ServiceGenerator.createService(GeoInvadersService.class);
    }

    public void login(User user, Context context, ProgressBar progressBar, DBUserManager dbUserManager) {

        progressBar.setVisibility(ProgressBar.VISIBLE);

        Call<Token> callAsync = service.login(user);

        callAsync.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {

                if (response.code() != 200){
                    try {
                        assert response.errorBody() != null;
                        GsonBuilder builder = new GsonBuilder();
                        Gson gson = builder.create();
                        MessageBody messageBody = gson.fromJson(response.errorBody().string(), MessageBody.class);
                        String error = messageBody.getMessage();

                        String message = "";

                        if (error.contains("not found")) {
                            message = "Пользователь не найден";
                        }
                        else if (error.contains("Invalid password")) {
                            message = "Неправильный пароль";
                        }
                        if (!message.equals("")) Toast.makeText(context, message, Toast.LENGTH_LONG).show();

                        System.out.println(error);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    progressBar.setVisibility(ProgressBar.INVISIBLE);
                    return;
                }

                Token token = response.body();
                System.out.println(token);

                progressBar.setVisibility(ProgressBar.INVISIBLE);

                dbUserManager.addUser(user.getUsername(), user.getPassword());

                Intent intent = new Intent(context, MapActivity.class);
                intent.putExtra("userName", user.getUsername());
                context.startActivity(intent);
            }

            @Override
            public void onFailure(Call<Token> call, Throwable throwable) {
                throwable.printStackTrace();
            }
        });
    }

    public void registration(User user, Context context, ProgressBar progressBar, DBUserManager dbUserManager) {

        progressBar.setVisibility(ProgressBar.VISIBLE);

        Call<Token> callAsync = service.registration(user);

        callAsync.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {

                if (response.code() != 200){
                    try {
                        assert response.errorBody() != null;
                        GsonBuilder builder = new GsonBuilder();
                        Gson gson = builder.create();
                        MessageBody messageBody = gson.fromJson(response.errorBody().string(), MessageBody.class);
                        String error = messageBody.getMessage();

                        String message = "";

                        if (error.contains("exists")) {
                            message = "Подьзователь с таким именем уже существует";
                        }
                        else if (error.contains("Invalid password")) {
                            message = "Неправильный пароль";
                        }
                        else if (error.contains("must be more than 4 and less than 10 characters")) {
                            message = "Пароль должен быть от 4 до 10 символов";
                        }
                        if (!message.equals("")) Toast.makeText(context, message, Toast.LENGTH_LONG).show();

                        System.out.println(error);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    progressBar.setVisibility(ProgressBar.INVISIBLE);
                    return;
                }

                Token token = response.body();
                System.out.println(token);

                progressBar.setVisibility(ProgressBar.INVISIBLE);

                dbUserManager.addUser(user.getUsername(), user.getPassword());

                Intent intent = new Intent(context, MapActivity.class);
                intent.putExtra("userName", user.getUsername());
                context.startActivity(intent);
            }

            @Override
            public void onFailure(Call<Token> call, Throwable throwable) {
                throwable.printStackTrace();
            }
        });
    }

    public void getRegions(Context context, List<Region> usersRegions, ProgressBar progressBar,
                           RatingUsersAdapter ratingUsersAdapter) {
        progressBar.setVisibility(ProgressBar.VISIBLE);

        Call<List<Region>> callAsync = service.getRegions();

        callAsync.enqueue(new Callback<List<Region>>() {
            @SuppressLint("NotifyDataSetChanged")
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<List<Region>> call, Response<List<Region>> response) {
                if (response.code() != 200){
                    try {
                        assert response.errorBody() != null;
                        GsonBuilder builder = new GsonBuilder();
                        Gson gson = builder.create();
                        MessageBody messageBody = gson.fromJson(response.errorBody().string(), MessageBody.class);
                        String error = messageBody.getMessage();

                        Toast.makeText(context, "Error", Toast.LENGTH_LONG).show();

                        System.out.println(error);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    progressBar.setVisibility(ProgressBar.INVISIBLE);
                    return;
                }

                List<Region> regions = response.body();
                usersRegions.clear();
                assert regions != null;
                usersRegions.addAll(regions);

                progressBar.setVisibility(ProgressBar.INVISIBLE);

                if (ratingUsersAdapter != null) {
                    ratingUsersAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Region>> call, Throwable throwable) {
                throwable.printStackTrace();
            }
        });
    }

    public void addRegion(Context context, Region region, List<Region> usersRegions, ProgressBar progressBar) {
        progressBar.setVisibility(ProgressBar.VISIBLE);

        Call<MessageBody> callAsync = service.addRegion(region);

        callAsync.enqueue(new Callback<MessageBody>() {
            @Override
            public void onResponse(Call<MessageBody> call, Response<MessageBody> response) {

                if (response.code() != 200){
                    try {
                        assert response.errorBody() != null;
                        GsonBuilder builder = new GsonBuilder();
                        Gson gson = builder.create();
                        MessageBody messageBody = gson.fromJson(response.errorBody().string(), MessageBody.class);
                        String error = messageBody.getMessage();

                        System.out.println(error);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    progressBar.setVisibility(ProgressBar.INVISIBLE);
                    return;
                }

                MessageBody messageBody = response.body();
                progressBar.setVisibility(ProgressBar.INVISIBLE);
                getRegions(context, usersRegions, progressBar, null);
            }

            @Override
            public void onFailure(Call<MessageBody> call, Throwable throwable) {
                throwable.printStackTrace();
            }
        });
    }

    public void removeRegion(Region region, Context context) {
        Call<MessageBody> callAsync = service.removeTab(region);

        callAsync.enqueue(new Callback<MessageBody>() {
            @Override
            public void onResponse(Call<MessageBody> call, Response<MessageBody> response) {

                if (response.code() != 200){
                    try {
                        assert response.errorBody() != null;
                        GsonBuilder builder = new GsonBuilder();
                        Gson gson = builder.create();
                        MessageBody messageBody = gson.fromJson(response.errorBody().string(), MessageBody.class);
                        String error = messageBody.getMessage();

                        System.out.println(error);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return;
                }

                MessageBody messageBody = response.body();
            }

            @Override
            public void onFailure(Call<MessageBody> call, Throwable throwable) {
                throwable.printStackTrace();
            }
        });
    }

    public void editRegion(Region region, Context context) {
        Call<MessageBody> callAsync = service.editTab(region);

        callAsync.enqueue(new Callback<MessageBody>() {
            @Override
            public void onResponse(Call<MessageBody> call, Response<MessageBody> response) {

                if (response.code() != 200){
                    try {
                        assert response.errorBody() != null;
                        GsonBuilder builder = new GsonBuilder();
                        Gson gson = builder.create();
                        MessageBody messageBody = gson.fromJson(response.errorBody().string(), MessageBody.class);
                        String error = messageBody.getMessage();

                        System.out.println(error);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return;
                }

                MessageBody messageBody = response.body();
            }

            @Override
            public void onFailure(Call<MessageBody> call, Throwable throwable) {
                throwable.printStackTrace();
            }
        });
    }

}
