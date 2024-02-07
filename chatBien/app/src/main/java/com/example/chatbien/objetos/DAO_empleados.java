package com.example.chatbien.objetos;

import com.example.chatbien.Conexion;

import java.sql.*;
import java.util.ArrayList;

public class DAO_empleados implements Patron_DAO<DTO_empleados> {

	public Connection conexion;

	public DAO_empleados() {
		this.conexion = Conexion.getInstancia().getCon();
	}

	@Override
	public boolean insertar(DTO_empleados empleado) {
		String query = "INSERT INTO empleados (dni, nombre, apellidos, antiguedad, n_seguridad_social, codigo_departamento, contrasena, foto) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

		try {
			PreparedStatement preparedStatement = conexion.prepareStatement(query);
			preparedStatement.setInt(1, empleado.getDni());
			preparedStatement.setString(2, empleado.getNombre());
			preparedStatement.setString(3, empleado.getApellidos());
			preparedStatement.setDate(4, empleado.getAntiguedad());
			preparedStatement.setInt(5, empleado.getN_seguridad_social());
			preparedStatement.setInt(6, empleado.getCodigo_departamento());
			preparedStatement.setString(7, empleado.getContrasena());
			preparedStatement.setBytes(8, empleado.getFoto());

			int filasAfectadas = preparedStatement.executeUpdate();
			return filasAfectadas > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean borrar(Object pk) {
		String query = "DELETE FROM empleados WHERE dni = ?";

		try {
			PreparedStatement preparedStatement = conexion.prepareStatement(query);
			preparedStatement.setInt(1, (int) pk);

			int filasAfectadas = preparedStatement.executeUpdate();
			return filasAfectadas > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean actualizar(DTO_empleados empleado) {
		String query = "UPDATE empleados SET nombre = ?, apellidos = ?, antiguedad = ?, n_seguridad_social = ?, codigo_departamento = ?, contrasena = ?, foto = ? WHERE dni = ?";

		try {
			PreparedStatement preparedStatement = conexion.prepareStatement(query);
			preparedStatement.setString(1, empleado.getNombre());
			preparedStatement.setString(2, empleado.getApellidos());
			preparedStatement.setDate(3, empleado.getAntiguedad());
			preparedStatement.setInt(4, empleado.getN_seguridad_social());
			preparedStatement.setInt(5, empleado.getCodigo_departamento());
			preparedStatement.setString(6, empleado.getContrasena());
			preparedStatement.setBytes(7, empleado.getFoto());
			preparedStatement.setInt(8, empleado.getDni());

			int filasAfectadas = preparedStatement.executeUpdate();
			return filasAfectadas > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public DTO_empleados buscar(Object pk) {
		String query = "SELECT * FROM empleados WHERE dni = ?";

		try {
			PreparedStatement preparedStatement = conexion.prepareStatement(query);
			preparedStatement.setInt(1, (int) pk);

			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				if (resultSet.next()) {
					int dni = resultSet.getInt("dni");
					String nombre = resultSet.getString("nombre");
					String apellidos = resultSet.getString("apellidos");
					Date antiguedad = resultSet.getDate("antiguedad");
					int n_seguridad_social = resultSet.getInt("n_seguridad_social");
					int codigo_departamento = resultSet.getInt("codigo_departamento");
					String contrasena = resultSet.getString("contrasena");
					byte[] foto = resultSet.getBytes("foto");

					return new DTO_empleados(dni, nombre, apellidos, antiguedad, n_seguridad_social,
							codigo_departamento, contrasena, foto);
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public ArrayList<DTO_empleados> listarTodos() {
		ArrayList<DTO_empleados> listaEmpleados = new ArrayList<>();
		String query = "SELECT * FROM empleados";

		try {
			PreparedStatement preparedStatement = conexion.prepareStatement(query);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				int dni = resultSet.getInt("dni");
				String nombre = resultSet.getString("nombre");
				String apellidos = resultSet.getString("apellidos");
				Date antiguedad = resultSet.getDate("antiguedad");
				int n_seguridad_social = resultSet.getInt("n_seguridad_social");
				int codigo_departamento = resultSet.getInt("codigo_departamento");
				String contrasena = resultSet.getString("contrasena");
				byte[] foto = resultSet.getBytes("foto");

				DTO_empleados empleado = new DTO_empleados(dni, nombre, apellidos, antiguedad, n_seguridad_social,
						codigo_departamento, contrasena, foto);
				listaEmpleados.add(empleado);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return listaEmpleados;
	}

	public String[] arrayNombres() {
		ArrayList<DTO_empleados> listaEmpleado = listarTodos();
		String[] nombres = new String[listaEmpleado.size()];
		int i = 0;
		for (DTO_empleados empleado : listaEmpleado) {
			String cadena = empleado.getDni()+"-" + empleado.getNombre();
			nombres[i] = (cadena.trim());
			i++;
		}
		return nombres;
	}
	public boolean validarCodigo(int codigo) {
		ArrayList<DTO_empleados> listaEmpleado = listarTodos();

		for (DTO_empleados empleado : listaEmpleado) {
			if (empleado.getDni() == codigo) {
				return true;
			}
		}
		return false;

	}
	public boolean login(String pasw, int dni){
		DTO_empleados empleado = buscar(dni);
		if(pasw.equals(empleado.getContrasena())){
			return true;
		}else {
			return false;
		}
	}
}
