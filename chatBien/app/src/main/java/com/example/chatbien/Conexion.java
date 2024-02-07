package com.example.chatbien;

import android.util.Log;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    private static Conexion instancia = null;
    private final String ip = "mysql-8001.dinaserver.com";
    private final int port = 3306;
    private final String BBDD = "reto2somo";
    private final String user = "somoCosto";
    private final String passwd = "NyTgj2^&=173";
    private final String cadena = "jdbc:mysql://" + ip + ":" + port + "/" + BBDD;
    private final String driver = "com.mysql.jdbc.Driver";
    protected static volatile Connection con;

    private Conexion(){
        new Thread(() -> {
                try {
                    Class.forName(driver);
                    con = DriverManager.getConnection(cadena, user, passwd);
                    if (con != null) {
                        Log.d("TAG", "Conexion a BD " + ip + " con exito");
                    } else {
                        Log.d("TAG", "Imposible conexión con " + ip);
                    }
                } catch (ClassNotFoundException ex) {
                    Log.d("TAG", "Excepción de clase no encontrada " + ex);
                } catch (SQLException ex) {
                    Log.d("TAG", "Excepción de SQL " + ex);
                }

        }).start();
    }

    public static Conexion getInstancia(){
        if (instancia == null) {
            synchronized (Conexion.class) {
                if (instancia == null) instancia = new Conexion();
            }
        }
        return instancia;
    }

    public Connection getCon() {
        if (con == null) {
            Log.e("TAG", "Error: La conexión es nula.");
        }
        return con;
    }

    public void Cerrar() throws SQLException {
        if (con != null) {
            if (!con.isClosed()) {
                con.close();
            }
        }
    }
}
