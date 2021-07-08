package com.its_omar.prototipo.controller;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EnvioUbicacion {

    ExecutorService executorService = Executors.newFixedThreadPool(4);
    private static volatile EnvioUbicacion instance;

    public static EnvioUbicacion getInstance() {
        if(instance == null) {
            instance = new EnvioUbicacion();
        }

        return instance;
    }


}
