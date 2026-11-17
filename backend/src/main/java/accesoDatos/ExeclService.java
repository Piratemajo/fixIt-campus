package accesoDatos;

import org.apache.commons.io.FileExistsException;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import connection.Connection;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExeclService extends Connection {

    static void main(){
        crearPlantillas("Excel.xlsx");
    }

    private static void crearPlantillas(String ruta) {
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
                

                for (int i = 0; i < columnas.length; i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(columnas[i]);
                    cell.setCellStyle(headerStyle);
                }

                for (int i = 0; i < columnas.length; i++) {
                    hoja.autoSizeColumn(i);
                }
            }

            
            libro.write(file);
            System.out.println("Plantilla Excel generada con éxito.");

            
        } catch (Exception e) {
            System.out.println("Error al crear el Excel: " + e.getMessage());
        } // <-- Aquí cierra el try-catch
    } // <-- Aquí cierra el método crearPlantillas

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
                return new String[]{"id"};
        }
    } // <-- Aquí cierra el método obtenerColumnasParaTabla

} // <-- Aquí cierra la clase ExeclService