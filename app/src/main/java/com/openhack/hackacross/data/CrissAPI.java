package com.openhack.hackacross.data;

import com.openhack.hackacross.models.CrissData;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CrissAPI {

    @GET("type/fires")
    Call<List<CrissData>> getFireData();

    @GET("type/accidents")
    Call<List<CrissData>> getAccidentsData();
}
