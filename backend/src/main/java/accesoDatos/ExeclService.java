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
        importarExcel("Excel.xlsx");
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

            for (String h1 : hojas) {
                libro.createSheet(h1);
                libro.getSheet(h1);
            }
            libro.write(file);

        } catch (Exception e) {
            System.out.println("Error al crear el Excel " + e.getMessage());
        }

    }

    private static void importarExcel(String ruta){
        try{
            openConnection();
            FileInputStream file = new FileInputStream(ruta); Workbook libro = WorkbookFactory.create(file);

            Sheet hoja = libro.getSheet(ruta);
            for(Row row : hoja){
                if (row.getRowNum() == 0) continue;
            }

        }catch(Exception e){
            System.out.println("No se puede insertar el archivo " + e.getMessage());
        }
    }
}
