package com.its_omar.prototipo.api;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceRetrofit {

    //BASE DE LA URL
    private static final String BASE_URL = "http://72.167.220.178/Prototipo/";
    //http://72.167.220.178/Prototipo/
    //Interface con los metodos para consumir servicios
    private WebService api;

    private static final ServiceRetrofit ourInstance = new ServiceRetrofit();

    /**
     * Obtiene una instancia del Serviio Retrofit
     * @return Objeto {@link ServiceRetrofit}
     */
    public static ServiceRetrofit getInstance() {
        return ourInstance;
    }

    private ServiceRetrofit(){}

    //Crea una instancia de retrofit asignando la base url y el convertidor de datos
    private final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(getRequestHeader())
            .build();

    /**
     * Obtiene los servicios disponibles
     * @return Interface WenService {@link WebService}
     */
    public WebService getSevices(){
        if(api == null){
            api = retrofit.create(WebService.class);
        }

        return api;
    }

    private OkHttpClient getRequestHeader() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(40, TimeUnit.SECONDS)
                .build();

        return okHttpClient;
    }
}
