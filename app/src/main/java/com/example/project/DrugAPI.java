package com.example.project;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface DrugAPI {
    @GET("druginfo")
    Call<ArrayList<Drug>> getDrugInfo(
            @Header("X-RapidAPI-Key") String apiKey,
            @Header("X-RapidAPI-Host") String apiHost,
            @Query("drug") String drugName
    );
}
