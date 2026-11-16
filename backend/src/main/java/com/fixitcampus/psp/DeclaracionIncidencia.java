package com.fixitcampus.psp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.concurrent.atomic.AtomicInteger;

/**
 * Un usuario declara una incidencia y se encarga el correo de aviso.
 * Forma 2: Implementar Runnable.
 */
public class DeclaracionIncidencia implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(DeclaracionIncidencia.class);
    private static AtomicInteger contador = new AtomicInteger(0);

    private EnviadorCorreos enviador;
    private String espacio;
    private String descripcion;

    public DeclaracionIncidencia(EnviadorCorreos enviador, String espacio, String descripcion) {
        this.enviador = enviador;
        this.espacio = espacio;
        this.descripcion = descripcion;
    }

    @Override
    public void run() {
        String codigo = "FIX-" + contador.incrementAndGet();
        enviador.encolar(codigo + " en " + espacio + ": " + descripcion);
        log.info("{} declarada; el correo se envia en segundo plano", codigo);
    }
}

