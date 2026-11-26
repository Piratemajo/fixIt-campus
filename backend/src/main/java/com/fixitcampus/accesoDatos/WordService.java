package com.fixitcampus.accesoDatos;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.time.LocalDateTime;

/**
 * Servicio que crea un informe Word (.docx) con lo que hay en la base de datos.
 *
 * Uso:
 *   WordService word = new WordService(conexion);
 *   word.generarInforme("informe_fixit.docx");
 */
public class WordService {

    private static final Logger log = LoggerFactory.getLogger(WordService.class);

    private final Connection con;

    public WordService(Connection con) {
        this.con = con;
    }

    public void generarInforme(String fichero) throws Exception {
        try (XWPFDocument doc = new XWPFDocument()) {
            parrafo(doc, "Informe FixIt Campus", 20, true);
            parrafo(doc, "Generado el " + LocalDateTime.now().withNano(0), 11, false);

            seccion(doc, "Incidencias por estado",
                    "SELECT estado, total FROM v_resumen_estado ORDER BY orden");
            seccion(doc, "Incidencias por prioridad",
                    "SELECT prioridad, total FROM v_resumen_prioridad ORDER BY orden");
            seccion(doc, "Incidencias fuera de plazo",
                    "SELECT codigo, espacio, prioridad, estado, dias_de_retraso FROM v_incidencias_fuera_plazo");
            seccion(doc, "Operaciones registradas",
                    "SELECT tabla, operacion, COUNT(*) AS total FROM log_operacion GROUP BY tabla, operacion ORDER BY tabla");
            seccion(doc, "Últimas 20 operaciones",
                    "SELECT fecha, nivel, operacion, tabla, mensaje FROM log_operacion ORDER BY fecha DESC LIMIT 20");

            try (FileOutputStream out = new FileOutputStream(fichero)) {
                doc.write(out);
            }
        }
        log.info("Informe creado: {}", fichero);
    }

    /** Título y una tabla con el resultado de la consulta. */
    private void seccion(XWPFDocument doc, String titulo, String sql) throws Exception {
        parrafo(doc, titulo, 14, true);
        try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            ResultSetMetaData md = rs.getMetaData();
            int n = md.getColumnCount();

            XWPFTable tabla = doc.createTable(1, n);
            for (int i = 0; i < n; i++) {
                tabla.getRows().get(0).getCell(i).setText(md.getColumnLabel(i + 1));
            }
            while (rs.next()) {
                XWPFTableRow fila = tabla.createRow();
                for (int i = 0; i < n; i++) {
                    String valor = rs.getString(i + 1);
                    fila.getCell(i).setText(valor == null ? "" : valor);
                }
            }
        }
    }

    private void parrafo(XWPFDocument doc, String texto, int tamano, boolean negrita) {
        XWPFRun run = doc.createParagraph().createRun();
        run.setText(texto);
        run.setFontSize(tamano);
        run.setBold(negrita);
    }
}
