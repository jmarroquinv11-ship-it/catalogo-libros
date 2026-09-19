package edu.umg.programacion2.catalogolibros.ui;

import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            VentanaLibros ventana = new VentanaLibros();
            ventana.setVisible(true);

        });
    }
}