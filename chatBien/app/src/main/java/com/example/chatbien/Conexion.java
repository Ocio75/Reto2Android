package com.example.chatbien;

import android.util.Log;

import java.sql.*;

public class Conexion {
    private static Conexion instancia = null;
    private final String ip = "mysql-8001.dinaserver.com";
    private final int port = 3306;
    private final String BBDD = "reto2somo";
    private final String user = "somoCosto";
    private final String passwd = "NyTgj2^&=173";
    private final String cadena = "jdbc:mysql://" + ip + ":" + port + "/" + BBDD;
    private final String driver = "com.mysql.jdbc.Driver";
    protected static Connection con;

    private  Conexion(){
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
    }
    public static Conexion getInstancia(){
        if (instancia == null) instancia = new Conexion();
        return instancia;
    }
    public Connection getCon() {
        if (con == null) {
            System.err.println("Error: La conexión es nula.");
        }
        if(con!=null) {
            return con;
        }else {
            return null;
        }

    }
    public void Cerrar() throws SQLException{
        if (con != null) {
            if (!con.isClosed()) {
                con.close();
            }
        }
    }
}
