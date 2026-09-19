package edu.umg.programacion2.catalogolibros.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {

    private static final String URL =
            "jdbc:mysql://localhost:3306/catalogo_libros_db?useSSL=false&serverTimezone=UTC";

    private static final String USUARIO = "root";
    private static final String CONTRASENA = System.getenv("DB_PASSWORD");

    private ConexionDB() {
    }

    public static Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, CONTRASENA);
    }
}