package com.example.geoinvaders.serverCommunication;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface GeoInvadersService {
    @POST("/auth/login/")
    Call<Token> login(@Body User user);

    @POST("/auth/registration/")
    Call<Token> registration(@Body User user);

    @GET("/regions/")
    Call<List<Region>> getRegions();

    @POST("/regions/add")
    Call<MessageBody> addRegion(@Body Region region);

    @POST("/regions/remove/")
    Call<MessageBody> removeTab(@Body Region region);

    @POST("/regions/edit/")
    Call<MessageBody> editTab(@Body Region region);
}
