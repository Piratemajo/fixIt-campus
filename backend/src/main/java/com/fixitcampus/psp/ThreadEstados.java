package com.fixitcampus.psp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *  Fases del Hilo
 */
public class ThreadEstados {

private static final Logger log = LoggerFactory.getLogger(ThreadEstados.class);
    private static final Object cerrojo = new Object();

    public static void main(String[] args) throws InterruptedException {
        Thread hilo = new Thread(() -> {
            long fin = System.currentTimeMillis() + 300;
            while (System.currentTimeMillis() < fin) {
                // RUNNABLE
            }
            try {
                Thread.sleep(300);// TIMED_WAITING
                synchronized (cerrojo) {
                    cerrojo.wait();// WAITING
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "hilo-estados");

        mostrar(hilo);// NEW
        hilo.start();
        esperar(hilo, Thread.State.RUNNABLE);
        esperar(hilo, Thread.State.TIMED_WAITING);
        esperar(hilo, Thread.State.WAITING);
        synchronized (cerrojo) {
            cerrojo.notify();
            esperar(hilo, Thread.State.BLOCKED);  
        }
        hilo.join();
        mostrar(hilo); // TERMINATED
    }

    private static void esperar(Thread hilo, Thread.State estado) throws InterruptedException {
        while (hilo.getState() != estado) {
            Thread.sleep(5);
        }
        mostrar(hilo);
    }

    private static void mostrar(Thread hilo) {
        log.info("Estado: {}", hilo.getState());
    }
}
