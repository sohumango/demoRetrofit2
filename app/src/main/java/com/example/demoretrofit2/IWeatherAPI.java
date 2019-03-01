package com.example.demoretrofit2;


import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IWeatherAPI {

    @GET("/forecast/webservice/json/v1")
    Single<WeatherResponse> getWhether(@Query("city") String cityId);
}
