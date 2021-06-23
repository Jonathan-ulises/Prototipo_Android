package com.its_omar.prototipo.api;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface WebService {

    @GET("verificarConexion")
    Call<String> verificarConexion();

    @POST("login")
    Call<String> loginApp();


}
