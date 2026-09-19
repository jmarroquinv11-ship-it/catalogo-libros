package edu.umg.programacion2.catalogolibros.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import edu.umg.programacion2.catalogolibros.config.ConexionDB;
import edu.umg.programacion2.catalogolibros.model.Libro;

public class LibroDAO {

    public boolean guardar(Libro libro) {

        String sql = "INSERT INTO libros "
                + "(titulo, autor, categoria, precio, existencias, anio_publicacion) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conexion = ConexionDB.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, libro.getTitulo());
            ps.setString(2, libro.getAutor());
            ps.setString(3, libro.getCategoria());
            ps.setBigDecimal(4, libro.getPrecio());
            ps.setInt(5, libro.getExistencias());
            ps.setInt(6, libro.getAnioPublicacion());

            int filas = ps.executeUpdate();

            if (filas > 0) {

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        libro.setId(rs.getInt(1));
                    }
                }

                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public List<Libro> listar() {

        List<Libro> libros = new ArrayList<>();

        String sql = "SELECT * FROM libros ORDER BY id";

        try (Connection conexion = ConexionDB.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Libro libro = new Libro();

                libro.setId(rs.getInt("id"));
                libro.setTitulo(rs.getString("titulo"));
                libro.setAutor(rs.getString("autor"));
                libro.setCategoria(rs.getString("categoria"));
                libro.setPrecio(rs.getBigDecimal("precio"));
                libro.setExistencias(rs.getInt("existencias"));
                libro.setAnioPublicacion(rs.getInt("anio_publicacion"));

                libros.add(libro);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return libros;
    }

    public Libro buscarPorId(int id) {

        String sql = "SELECT * FROM libros WHERE id = ?";

        try (Connection conexion = ConexionDB.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    Libro libro = new Libro();

                    libro.setId(rs.getInt("id"));
                    libro.setTitulo(rs.getString("titulo"));
                    libro.setAutor(rs.getString("autor"));
                    libro.setCategoria(rs.getString("categoria"));
                    libro.setPrecio(rs.getBigDecimal("precio"));
                    libro.setExistencias(rs.getInt("existencias"));
                    libro.setAnioPublicacion(rs.getInt("anio_publicacion"));

                    return libro;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean actualizar(Libro libro) {

        String sql = "UPDATE libros SET "
                + "titulo = ?, "
                + "autor = ?, "
                + "categoria = ?, "
                + "precio = ?, "
                + "existencias = ?, "
                + "anio_publicacion = ? "
                + "WHERE id = ?";

        try (Connection conexion = ConexionDB.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, libro.getTitulo());
            ps.setString(2, libro.getAutor());
            ps.setString(3, libro.getCategoria());
            ps.setBigDecimal(4, libro.getPrecio());
            ps.setInt(5, libro.getExistencias());
            ps.setInt(6, libro.getAnioPublicacion());
            ps.setInt(7, libro.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean eliminar(int id) {

        String sql = "DELETE FROM libros WHERE id = ?";

        try (Connection conexion = ConexionDB.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}