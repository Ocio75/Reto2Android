package com.example.chatbien.objetos;

import java.util.ArrayList;


/**
 * Interfaz que define el patr�n DAO (Data Access Object) para operaciones CRUD
 * en una tabla de base de datos.
 *
 * @param <TipoGen> Tipo gen�rico que representa la entidad de la base de datos.
 */
public interface Patron_DAO<TipoGen> {

    /**
     * Inserta un nuevo registro en la base de datos.
     *
     * @param t Objeto que representa el registro a ser insertado.
     * @return true si la inserci�n fue exitosa, false en caso contrario.
     */
    public boolean insertar(TipoGen t);

    /**
     * Elimina un registro de la base de datos identificado por su clave primaria.
     *
     * @param pk Clave primaria del registro a ser eliminado.
     * @return true si la eliminaci�n fue exitosa, false en caso contrario.
     */
    public boolean borrar(Object pk);

    /**
     * Actualiza un registro existente en la base de datos.
     *
     * @param t Objeto que representa el registro actualizado.
     * @return true si la actualizaci�n fue exitosa, false en caso contrario.
     */
    public boolean actualizar(TipoGen t);

    /**
     * Busca un registro en la base de datos por su clave primaria.
     *
     * @param pk Clave primaria del registro a ser buscado.
     * @return Objeto que representa el registro encontrado, o null si no se encontr�.
     */
    public TipoGen buscar(Object pk);

    /**
     * Devuelve una lista de todos los registros de la tabla en la base de datos.
     *
     * @return ArrayList que contiene todos los registros de la tabla.
     */
    public ArrayList<TipoGen> listarTodos();

}
