package com.example.chatbien;

import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionSQLServer {
    private final String ip = "sql530.sql.dinaserver.com";
    private final int port = 1433;
    private final String BBDD = "reto2";
    private final String user = "useario";
    private final String passwd = "Puta2004-";
    private final String cadena = "jdbc:jtds:sqlserver://" + ip + ":" + port + ";databaseName=" + BBDD + ";user=" + user + ";password=" + passwd;
    private final String driver = "net.sourceforge.jtds.jdbc.Driver";
    public Connection con;

    public ConexionSQLServer() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }
    public void Conectar() {
        try {
            Log.d("ConexionSQLServer", "Intentando conectar...");
            Class.forName(driver);
            con = DriverManager.getConnection(cadena);
            Log.d("ConexionSQLServer", "Conexión establecida con éxito");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Log.e("ConexionSQLServer", "Error al conectar: No se encontró el controlador JDBC - " + e.getMessage());
        } catch (SQLException e) {
            e.printStackTrace();
            int errorCode = e.getErrorCode();

            // Manejo de excepciones específicas de SQL Server
            if (errorCode == 18456) {
                Log.e("ConexionSQLServer", "Error al conectar: Nombre de usuario o contraseña incorrectos - " + e.getMessage());
            } else if (errorCode == 4060) {
                Log.e("ConexionSQLServer", "Error al conectar: Base de datos no encontrada - " + e.getMessage());
            } else {
                Log.e("ConexionSQLServer", "Error al conectar: Código de error " + errorCode + " - " + e.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ConexionSQLServer", "Error inesperado al conectar: " + e.getMessage());
        }
    }

    public void Cerrar() {
        try {
            if (con != null && !con.isClosed()) {
                con.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
