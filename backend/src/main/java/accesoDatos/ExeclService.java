package accesoDatos;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class ExeclService {

    static void main() {

        //String rutaExcel;
        //String jdbcURL = "jdbc:postgresql://localhost:5432/yourdatabase";
        //String username = "yourusername";
        //String password = "yourpassword";


        //leerExcel();
        crearExcel();


    }

    private static void crearExcel(){

        try(XSSFWorkbook libro = new XSSFWorkbook()) {
            CellStyle estilo = libro.createCellStyle();
            CellStyle titulo = libro.createCellStyle();
            Font fontHeader = libro.createFont();
            fontHeader.setBold(true);
            fontHeader.setFontName("Algerian");
            fontHeader.setColor(IndexedColors.AUTOMATIC.getIndex());
            estilo.setFont(fontHeader);
            Font font = libro.createFont();
            font.setFontName("Comic Sans MS");
            font.setColor(IndexedColors.AUTOMATIC.getIndex());
            titulo.setFont(font);

            String[] hojas = {"Usuario", "Rol", "Proveedor", "Comentario", "Categoria", "Historial_incidencia","log_operacion0","Espacio","Edificio","Estado_incidencias","Priodridad","Incidencia"};
            for(String hoja1 : hojas) {


                Sheet hoja = libro.createSheet(hoja1);

                Row row0 = hoja.createRow(0);
                Row row1 = hoja.createRow(1);
                Row row2 = hoja.createRow(2);
                Row row3 = hoja.createRow(3);
                Row row4 = hoja.createRow(4);
                Row row5 = hoja.createRow(5);
                Row row6 = hoja.createRow(6);
                Row row7 = hoja.createRow(7);


                Cell c0 = row0.createCell(0);
                Cell c1 = row0.createCell(1);
                Cell c2 = row0.createCell(2);
                Cell c3 = row0.createCell(3);
                Cell c4 = row0.createCell(4);
                Cell c5 = row0.createCell(5);
                Cell c6 = row0.createCell(6);
                Cell c7 = row0.createCell(7);


                c0.setCellStyle(estilo);
                c1.setCellStyle(estilo);
                c2.setCellStyle(estilo);
                c3.setCellStyle(estilo);
                c4.setCellStyle(estilo);
                c5.setCellStyle(estilo);
                c6.setCellStyle(estilo);
                c7.setCellStyle(estilo);


                c0.setCellValue("ID");
                c1.setCellValue("NOMBRE");
                c2.setCellValue("APELLIDOS");
                c3.setCellValue("EMAIL");
                c4.setCellValue("PASSWORD");
                c5.setCellValue("ROL");
                c6.setCellValue("ACTIVO");
                c7.setCellValue("FECHA_CREACCION");

                String[][] datosUsuarios = {
                        {"1", "Carlos", "García Gómez", "carlos.garcia@email.com", "pass1234", "ADMIN", "Sí", "15/01/2025"},
                        {"2", "María", "López Pérez", "maria.lopez@email.com", "abc$9876", "USER", "Sí", "22/03/2025"},
                        {"3", "Alejandro", "Martín Sanz", "alejandro.ms@email.com", "segura#2025", "USER", "No", "10/06/2025"},
                        {"4", "Lucía", "Fernández Ruiz", "lucia.fr@email.com", "clavePass1", "TECNICO", "Sí", "05/09/2025"},
                        {"5", "David", "Sánchez Torres", "david.st@email.com", "qwerty789", "USER", "Sí", "12/11/2025"},
                        {"6", "Elena", "Jiménez Blanco", "elena.jb@email.com", "elena2026*", "USER", "No", "01/02/2026"},
                        {"7", " Javier", "Romero Gil", "javier.rg@email.com", "adminPass#", "ADMIN", "Sí", "20/04/2026"}
                };

                Row[] filas = {row1, row2, row3, row4, row5, row6, row7};

                for (int i = 0; i < filas.length; i++) {
                    Row filaActual = filas[i];
                    String[] datosFila = datosUsuarios[i];

                    for (int j = 0; j < datosFila.length; j++) {
                        Cell celda = filaActual.createCell(j);
                        celda.setCellStyle(titulo);
                        celda.setCellValue(datosFila[j]);
                    }
                    for (int i1 = 0; i1 < 8; i1++) {
                        hoja.autoSizeColumn(i1);
                    }
                }
                try (FileOutputStream libroGurdado = new FileOutputStream("Fixit.xlsx")) {
                    libro.write(libroGurdado);

                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }



    }


    private static void leerExcel() {


    }
}
