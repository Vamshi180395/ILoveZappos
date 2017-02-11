package com.example.ilovezappos;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RequestInterface {
    @GET("Search?")
    Call<JSONResponse> getJSON(@Query("term") String query, @Query("key") String key);
}
