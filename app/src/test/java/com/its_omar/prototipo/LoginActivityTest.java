package com.its_omar.prototipo;

import com.its_omar.prototipo.api.ServiceRetrofit;
import com.its_omar.prototipo.api.WebService;
import com.its_omar.prototipo.model.Veri_Con;

import org.junit.Test;

import java.io.IOException;

import retrofit2.Response;

import static org.junit.Assert.*;

public class LoginActivityTest {

    /**
     * Prueba de login, esta prueba saltara no pasara si ya hay una sesion registrada
     * en el back-end
     * @throws IOException
     */
    @Test
    public void verificarDatosLogin() throws IOException {
        WebService sd = ServiceRetrofit.getInstance().getSevices();

        Response<Veri_Con> response = sd.loginApp("ecortes", "emilio114s").execute();

        assertTrue(response.isSuccessful());

        assert response.body() != null;
        assertEquals("Sesión iniciada", response.body().getMensaje());
    }
}