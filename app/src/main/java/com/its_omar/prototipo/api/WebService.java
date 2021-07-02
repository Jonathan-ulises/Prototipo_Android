package com.its_omar.prototipo.api;

import com.its_omar.prototipo.model.Bitacora;
import com.its_omar.prototipo.model.Empleado;
import com.its_omar.prototipo.model.Result;
import com.its_omar.prototipo.model.bodyJSONCliente.BodyJSONCliente;
import com.its_omar.prototipo.model.resultClienteService.ClientesJSONResult;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface WebService {

    @GET("verificarConexion")
    Call<Result> verificarConexion();

    @POST("loginMovil")
    @FormUrlEncoded
    Call<Result> loginApp(@Field("username") String u_name, @Field("password") String u_pass);

    @POST("logout")
    @FormUrlEncoded
    Call<Result> logoutApp(@Field("username") String nombreUsuario);

    @POST("registrarBitacora")
    Call<Result> registrarBitacora(@Body Bitacora bitacora);

    @POST("obtenerVisitas")
    Call<ClientesJSONResult> getClientesEmpleado(@Body Empleado idEmpleado);

    @POST("actualizarVisita")
    Call<Result> asignarVisita(@Body BodyJSONCliente request);
//deus370   12345
}
