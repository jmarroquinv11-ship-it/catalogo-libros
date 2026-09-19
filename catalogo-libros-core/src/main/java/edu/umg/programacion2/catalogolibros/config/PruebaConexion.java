package edu.umg.programacion2.catalogolibros.config;

import java.sql.Connection;

public class PruebaConexion {

    public static void main(String[] args) {

        try (Connection conexion = ConexionDB.obtenerConexion()) {

            if (conexion != null) {
                System.out.println("CONEXION EXITOSA A MYSQL");
                System.out.println("Base de datos: catalogo_libros_db");
            }

        } catch (Exception e) {
            System.out.println("ERROR AL CONECTAR A MYSQL");
            e.printStackTrace();
        }
    }
}