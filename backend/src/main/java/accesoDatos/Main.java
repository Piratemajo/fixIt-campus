package accesoDatos;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Ejecuta los dos servicios de Acceso a Datos:
 *   1. ExeclService: crea la plantilla Excel (Excel.xlsx).
 *   2. WordService: crea el informe Word (informe_fixit.docx) con los datos de la base de datos.
 */
public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    // Datos de conexión a la base de datos (cámbialos por los vuestros)
    private static final String URL = "jdbc:postgresql://localhost:5432/fixit_campus";
    private static final String USUARIO = "root";
    private static final String PASSWORD = "root";

    public static void main(String[] args) {
        log.info("1. Plantilla Excel");
        ExeclService.main(args);

        log.info("2. Informe Word");
        try (Connection con = DriverManager.getConnection(URL, USUARIO, PASSWORD)) {
            WordService word = new WordService(con);
            word.generarInforme("informe_fixit.docx");
        } catch (Exception e) {
            log.error("Error al crear el informe Word", e);
        }
    }
}