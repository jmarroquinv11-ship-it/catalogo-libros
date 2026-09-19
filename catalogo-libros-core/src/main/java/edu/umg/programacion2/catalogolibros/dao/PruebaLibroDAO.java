package edu.umg.programacion2.catalogolibros.dao;

import java.math.BigDecimal;
import java.util.List;

import edu.umg.programacion2.catalogolibros.model.Libro;

public class PruebaLibroDAO {

    public static void main(String[] args) {

        LibroDAO dao = new LibroDAO();

        Libro libro = new Libro(
                "Cien años de soledad",
                "Gabriel García Márquez",
                "Novela",
                new BigDecimal("125.00"),
                10,
                1967
        );

        boolean guardado = dao.guardar(libro);

        if (guardado) {
            System.out.println("LIBRO GUARDADO CORRECTAMENTE");
            System.out.println("ID generado: " + libro.getId());
        } else {
            System.out.println("NO SE PUDO GUARDAR EL LIBRO");
        }

        System.out.println();
        System.out.println("LISTA DE LIBROS");
        System.out.println("----------------------------");

        List<Libro> libros = dao.listar();

        for (Libro l : libros) {
            System.out.println("ID: " + l.getId());
            System.out.println("Titulo: " + l.getTitulo());
            System.out.println("Autor: " + l.getAutor());
            System.out.println("Categoria: " + l.getCategoria());
            System.out.println("Precio: Q" + l.getPrecio());
            System.out.println("Existencias: " + l.getExistencias());
            System.out.println("Año: " + l.getAnioPublicacion());
            System.out.println("----------------------------");
        }
    }
}