package com.fixitcampus.psp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Tarea en segundo plano 
 * Heredar de Thread.
 */

public class EnviadorCorreos extends Thread {

    private static final Logger log = LoggerFactory.getLogger(EnviadorCorreos.class);

    private BlockingQueue<String> cola = new LinkedBlockingQueue<>();
    private AtomicInteger enviados = new AtomicInteger(0);        
    private volatile boolean terminar = false;

    public EnviadorCorreos() {
        super("enviador-correos");
    }

    public void encolar(String correo) {
        cola.add(correo);
    }

    public void terminar() {
        terminar = true;
    }

    public int getEnviados() {
        return enviados.get();
    }

    public int getPendientes() {
        return cola.size();
    }

    @Override
    public void run() {
        try {
            while (!terminar || !cola.isEmpty()) {
                String correo = cola.poll(500, TimeUnit.MILLISECONDS);
                if (correo != null) {
                    Thread.sleep(300); 
                    enviados.incrementAndGet();
                    log.info("Correo enviado: {}", correo);
                }
            }
        } catch (InterruptedException e) {
            log.warn("Hilo de correos interrumpido");
        }
    }
}