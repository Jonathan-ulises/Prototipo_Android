package com.its_omar.prototipo.api;

import com.its_omar.prototipo.model.Veri_Con;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface WebService {

    @GET("verificarConexion")
    Call<Veri_Con> verificarConexion();

    @POST("loginMovil")
    @FormUrlEncoded
    Call<Veri_Con> loginApp(@Field("username") String u_name, @Field("password") String u_pass);

//deus370   12345
}
