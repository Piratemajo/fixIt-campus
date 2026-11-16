package com.fixitcampus.psp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Main
 * Correos en segundo plano, clase anónima, ExecutorService y Future.
 */
public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        log.info("Esto arranca");
        // Forma 1 (extends Thread)
        EnviadorCorreos enviador = new EnviadorCorreos();
        enviador.start();

        // Forma 4 (clase anonima)
        Thread vigilante = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        log.info("Vigilante: {} correos pendientes", enviador.getPendientes());
                        Thread.sleep(1000);
                    }
                } catch (InterruptedException e) {
                    log.info("Vigilante parado");
                }
            }
        }, "vigilante");
        vigilante.start();

        // ExecutorService
        String[][] avisos = {
            {"A-01", "El proyector no enciende"},
            {"B-T1", "Cable suelto junto al cuadro"},
            {"A-B1", "Grifo que gotea"},
            {"B-12", "Silla rota"},
            {"A-L1", "Ordenador sin red"},
        };
        ExecutorService pool = Executors.newFixedThreadPool(3);
        for (String[] aviso : avisos) {
            pool.submit(new DeclaracionIncidencia(enviador, aviso[0], aviso[1]));
        }

        // Forma 3 (lambda)
        Callable<Integer> contarPendientes = () -> enviador.getPendientes();
        Future<Integer> resultado = pool.submit(contarPendientes);
        log.info("Correos pendientes según el Future: {}", resultado.get());

        pool.shutdown();
        pool.awaitTermination(10, TimeUnit.SECONDS);

        enviador.terminar();
        enviador.join();       
        vigilante.interrupt();

        log.info("Correos enviados: {}. Hilo de correos: {}", enviador.getEnviados(), enviador.getState());
    }
}
