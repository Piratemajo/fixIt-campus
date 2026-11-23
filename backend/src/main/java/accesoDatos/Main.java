package accesoDatos;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Ejecuta todo lo de Acceso a Datos:
 *   1. ExeclService: crea la plantilla vacía (Excel.xlsx). Se sobrescribe cada vez.
 *   2. ExeclService: inserta en la base de datos lo que hay en Excel_relleno.xlsx
 *      (una copia de la plantilla con las filas escritas).
 *   3. WordService: crea el informe Word (informe_fixit.docx).
 */
public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    // Datos de conexión a la base de datos (cámbialos por los vuestros)
    private static final String URL = "jdbc:postgresql://localhost:5432/fixit_campus";
    private static final String USUARIO = "root";
    private static final String PASSWORD = "root";

    private static final String EXCEL_RELLENO = "Excel_relleno.xlsx";

    public static void main(String[] args) {
        log.info("1. Plantilla Excel");
        ExeclService.main(args);

        try (Connection con = DriverManager.getConnection(URL, USUARIO, PASSWORD)) {
            log.info("2. Insertar el Excel relleno");
            if (new File(EXCEL_RELLENO).exists()) {
                ExeclService.importarExcel(EXCEL_RELLENO, con);
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
}
