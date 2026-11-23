package accesoDatos;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.mindrot.jbcrypt.BCrypt;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class ExeclService{

    private static final Logger log = LoggerFactory.getLogger(ExeclService.class);

    public static void main(String[] args) {
        crearPlantillas("Excel.xlsx");
    }

    private static void crearPlantillas(String ruta) {
        log.info("Creando plantilla Excel en {}", ruta);
        try (XSSFWorkbook libro = new XSSFWorkbook(); FileOutputStream file = new FileOutputStream(ruta)) {

            String[] hojas = {
                    "rol",
                    "prioridad",
                    "estado_incidencia",
                    "categoria",
                    "usuario",
                    "edificio",
                    "espacio",
                    "proveedor",
                    "incidencia",
                    "comentario",
                    "historial_incidencia",
                    "log_operacion"
            };
            
            // estilo cabecera azul
            CellStyle headerStyle = libro.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            Font font = libro.createFont();
            font.setBold(true);
            font.setColor(IndexedColors.WHITE.getIndex());
            headerStyle.setFont(font);

            for (String h1 : hojas) {
                Sheet hoja = libro.createSheet(h1); 
                Row headerRow = hoja.createRow(0);
                String[] columnas = obtenerColumnasParaTabla(h1);
                log.debug("Hoja {} con {} columnas", h1, columnas.length);
                
                for (int i = 0; i < columnas.length; i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(columnas[i]);
                    cell.setCellStyle(headerStyle);
                }

                // autoajustar ancho
                for (int i = 0; i < columnas.length; i++) {
                    hoja.autoSizeColumn(i);
                }
            }

            libro.write(file);
            System.out.println("Plantilla Excel generada con éxito.");
            log.info("Plantilla Excel generada con éxito: {}", ruta);

        } catch (Exception e) {
            System.out.println("Error al crear el Excel: " + e.getMessage());
            log.error("Error al crear el Excel {}", ruta, e);
        }
    }

    public static void importarExcel(String ruta, java.sql.Connection con) throws Exception {
        log.info("Importando {}", ruta);
        // orden importante por las FK de la bd
        String[] hojas = {"rol", "prioridad", "estado_incidencia", "categoria", "usuario", "edificio",
                "espacio", "proveedor", "incidencia", "comentario", "historial_incidencia", "log_operacion"};
        
        DataFormatter formato = new DataFormatter(); // para leer todo como string y que no de fallo de tipo
        List<String> errores = new ArrayList<>();
        int insertadas = 0;

        con.setAutoCommit(false); // transaccion manual para hacer rollback si falla algo
        try (Workbook libro = WorkbookFactory.create(new File(ruta))) {
            for (String h1 : hojas) {
                Sheet hoja = libro.getSheet(h1);
                if (hoja == null) {
                    continue;
                }
                Row headerRow = hoja.getRow(0);

                // desde 1 porque 0 es la cabecera
                for (int f = 1; f <= hoja.getLastRowNum(); f++) {
                    Row fila = hoja.getRow(f);
                    if (fila == null) {
                        continue;
                    }
                    
                    // cogemos solo celdas con datos, las vacias (ej: id) las rellena postgres
                    List<String> columnas = new ArrayList<>();
                    List<String> valores = new ArrayList<>();
                    for (int i = 0; i < headerRow.getLastCellNum(); i++) {
                        String valor = formato.formatCellValue(fila.getCell(i)).trim();
                        if (!valor.isEmpty()) {
                            String columna = formato.formatCellValue(headerRow.getCell(i)).trim();
                            // encriptar contraseña antes de meterla
                            if (columna.equals("password_hash")) {
                                valor = BCrypt.hashpw(valor, BCrypt.gensalt());
                            }
                            columnas.add(columna);
                            valores.add(valor);
                        }
                    }
                    if (columnas.isEmpty()) {
                        continue;
                    }

                    String sql = "INSERT INTO " + h1 + " (" + String.join(", ", columnas) + ") VALUES ("
                            + "?, ".repeat(columnas.size() - 1) + "?)";

                    // savepoint para que si falla una fila no bloquee el resto y poder sacar todos los errores de golpe
                    Savepoint sp = con.setSavepoint();
                    try (PreparedStatement ps = con.prepareStatement(sql)) {
                        for (int i = 0; i < valores.size(); i++) {
                            ps.setObject(i + 1, valores.get(i), Types.OTHER); // Types.OTHER para que postgres castee solo los tipos
                        }
                        ps.executeUpdate();
                        con.releaseSavepoint(sp);
                        insertadas++;
                    } catch (SQLException e) {
                        con.rollback(sp);
                        String error = "Hoja " + h1 + ", fila " + (f + 1) + ": " + e.getMessage();
                        errores.add(error);
                        log.warn(error);
                    }
                }
            }

            if (errores.isEmpty()) {
                con.commit();
                log.info("COMMIT: {} filas insertadas", insertadas);
            } else {
                con.rollback();
                log.error("ROLLBACK: {} errores, no se ha insertado nada", errores.size());
            }
        } catch (Exception e) {
            con.rollback();
            throw e;
        } finally {
            con.setAutoCommit(true);
        }
    }

    private static String[] obtenerColumnasParaTabla(String tabla){
        switch (tabla) {
            case "rol": 
                return new String[]{"codigo", "descripcion"};
            case "prioridad": 
                return new String[]{"codigo", "orden", "plazo_dias_laborables", "descripcion"};
            case "estado_incidencia": 
                return new String[]{"codigo", "orden", "descripcion"};
            case "categoria": 
                return new String[]{"id", "nombre", "activa"};
            case "usuario": 
                return new String[]{"id", "nombre", "apellidos", "email", "password_hash", "rol", "activo", "fecha_alta"};
            case "edificio": 
                return new String[]{"id", "codigo", "nombre"};
            case "espacio": 
                return new String[]{"id", "codigo", "nombre", "edificio_id", "planta", "tipo", "activo"};
            case "proveedor": 
                return new String[]{"id", "razon_social", "nif", "especialidad_id", "email_contacto", "telefono", "precio_hora", "activo"};
            case "incidencia": 
                return new String[]{"id", "codigo", "descripcion", "categoria_id", "espacio_id", "riesgo_personas", "impide_uso", "prioridad", "estado", "foto_ruta", "creada_por", "tecnico_id", "proveedor_id", "fecha_creacion", "fecha_objetivo", "fecha_resolucion", "fecha_cierre", "version"};
            case "comentario": 
                return new String[]{"id", "incidencia_id", "autor_id", "texto", "fecha"};
            case "historial_incidencia": 
                return new String[]{"id", "incidencia_id", "tipo_cambio", "valor_anterior", "valor_nuevo", "usuario_id", "fecha", "observacion"};
            case "log_operacion": 
                return new String[]{"id", "fecha", "nivel", "origen", "operacion", "tabla", "registro_id", "usuario_id", "mensaje"};
            default: 
                log.warn("Tabla desconocida: {}", tabla);
                return new String[]{"id"};
        }
    }
}