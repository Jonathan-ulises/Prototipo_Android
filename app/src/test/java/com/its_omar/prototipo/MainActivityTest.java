package com.its_omar.prototipo;

import com.its_omar.prototipo.api.ServiceRetrofit;
import com.its_omar.prototipo.api.WebService;
import com.its_omar.prototipo.model.Result;

import org.junit.Test;

import java.io.IOException;

import retrofit2.Response;

import static org.junit.Assert.*;

public class MainActivityTest {

    @Test
    public void verificarConexionServidor() throws IOException {
        WebService sd = ServiceRetrofit.getInstance().getSevices();

        Response<Result> response = sd.verificarConexion().execute();

        assertTrue(response.isSuccessful());

        assert response.body() != null;
        assertEquals("Servicios están funcionando", response.body().getMensaje());
    }
}