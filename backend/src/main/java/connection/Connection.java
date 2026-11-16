package connection;

import java.sql.DriverManager;
import java.sql.SQLException;

public class Connection {

    private static String url = "jdbc:postgresql://localhost:5432/fixit_campus";
    private static String user = "";
    private static String password = "";


    protected static java.sql.Connection connection;
    
    protected static  void openConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(url, user, password);
            }


        } catch (SQLException e) {
            System.err.println("Error al intentar abrir la conexión: " + e.getMessage());
        }
    }

        protected void closeConnection () {
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.out.println("Error al intentar cerrar la conexion: " + e.getMessage());
            }
        }
}
