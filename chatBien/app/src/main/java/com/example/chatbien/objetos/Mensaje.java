package com.example.chatbien.objetos;

import java.time.LocalDate;

public class Mensaje {
    private int Dni;
    private LocalDate fecha ;
    private String mensaje;

    public Mensaje(int dni, LocalDate fecha, String mensaje) {
        Dni = dni;
        this.fecha = fecha;
        this.mensaje = mensaje;
    }
    public Mensaje(int dni, String mensaje) {
        Dni = dni;
        this.mensaje = mensaje;
    }

    public int getDni() {
        return Dni;
    }

    public void setDni(int dni) {
        Dni = dni;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
}
