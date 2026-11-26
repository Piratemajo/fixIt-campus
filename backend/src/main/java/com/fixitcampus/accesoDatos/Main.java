package com.fixitcampus.accesoDatos;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

/**
 * Ejecuta todo lo de Acceso a Datos:
 *   1. ExeclService: crea la plantilla vacía (Excel.xlsx). Se sobrescribe cada vez.
 *   2. ExeclService: inserta en la base de datos lo que hay en Excel_relleno.xlsx
 *      (una copia de la plantilla con las filas escritas).
 *   3. WordService: crea el informe Word (informe_fixit.docx).
 */
public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);
    private static final String EXCEL_RELLENO = "Excel_relleno.xlsx";

    public static void main(String[] args) {
        log.info("1. Plantilla Excel");
        ExcelService.main(args);

        try (Connection con = obtenerConexion()) {
            log.info("2. Insertar el Excel relleno");
            if (new File(EXCEL_RELLENO).exists()) {
                ExcelService.importarExcel(EXCEL_RELLENO, con);
            } else {
                log.warn("No existe {}: copia Excel.xlsx con ese nombre y rellénalo", EXCEL_RELLENO);
            }

            log.info("3. Informe Word");
            WordService word = new WordService(con);
            word.generarInforme("informe_fixit.docx");
        } catch (Exception e) {
            log.error("Error en Acceso a Datos", e);
        }
    }

    private static Connection obtenerConexion() throws Exception {
        Properties props = new Properties();
       try {
        
       
        try (InputStream input = Main.class.getClassLoader().getResourceAsStream("Properties.properties")) {
            if (input == null) {
                throw new RuntimeException("No se encontró Properties.properties en el classpath");
            }
            props.load(input);
        }
        

        

        
        } catch (Exception e) {
        log.error("Abrir y correr Docker");
       }
       String url = props.getProperty("db.url").trim();
        String user = props.getProperty("db.user").trim();
        String password = props.getProperty("db.password").trim();
       return DriverManager.getConnection(url, user, password);
    }
}