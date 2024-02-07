package com.example.chatbien.objetos;

import java.sql.Date;
import java.util.Objects;

public class DTO_empleados {
	private int dni,n_seguridad_social,codigo_departamento;
	private String nombre,apellidos,contrasena;
	private Date antiguedad;
	private byte[] foto;
	
	public DTO_empleados() {
		super();
	}
	
	
	public DTO_empleados(int dni, String nombre, String apellidos, Date antiguedad, int n_seguridad_social,
			int codigo_departamento, String contrasena, byte[] foto) {
		super();
		this.dni = dni;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.antiguedad = antiguedad;
		this.n_seguridad_social = n_seguridad_social;
		this.codigo_departamento = codigo_departamento;
		this.contrasena = contrasena;
		this.foto = foto;
	}



	public int getDni() {
		return dni;
	}
	
	public void setDni(int dni) {
		this.dni = dni;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getApellidos() {
		return apellidos;
	}
	
	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}
	
	public Date getAntiguedad() {
		return antiguedad;
	}
	
	public void setAntiguedad(Date antiguedad) {
		this.antiguedad = antiguedad;
	}
	
	public int getN_seguridad_social() {
		return n_seguridad_social;
	}
	
	public void setN_seguridad_social(int n_seguridad_social) {
		this.n_seguridad_social = n_seguridad_social;
	}
	
	public int getCodigo_departamento() {
		return codigo_departamento;
	}
	
	public void setCodigo_departamento(int codigo_departamento) {
		this.codigo_departamento = codigo_departamento;
	}
	
	public String getContrasena() {
		return contrasena;
	}
	
	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}
	
	public byte[] getFoto() {
		return foto;
	}


	public void setFoto(byte[] foto) {
		this.foto = foto;
	}


	@Override
	public int hashCode() {
		return Objects.hash(apellidos, dni, nombre);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTO_empleados other = (DTO_empleados) obj;
		return Objects.equals(apellidos, other.apellidos) && dni == other.dni && Objects.equals(nombre, other.nombre);
	}
	
	

}
