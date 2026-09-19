package edu.umg.programacion2.catalogolibros.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import edu.umg.programacion2.catalogolibros.dao.LibroDAO;
import edu.umg.programacion2.catalogolibros.model.Libro;

public class VentanaLibros extends JFrame {

    private static final long serialVersionUID = 1L;

    private JTextField txtTitulo;
    private JTextField txtAutor;
    private JTextField txtCategoria;
    private JTextField txtPrecio;
    private JTextField txtExistencias;
    private JTextField txtAnio;
    private JTextField txtFechaIngreso;

    private JButton btnGuardar;
    private JButton btnActualizar;
    private JButton btnEliminar;
    private JButton btnLimpiar;
    private JButton btnVerResumen;

    private JTable tablaLibros;
    private DefaultTableModel modeloTabla;

    private LibroDAO libroDAO;

    private int idSeleccionado = -1;

    public VentanaLibros() {

        libroDAO = new LibroDAO();

        setTitle("Catálogo de Libros");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        iniciarComponentes();
        cargarLibros();
    }

    private void iniciarComponentes() {

        setLayout(new BorderLayout(10, 10));

        JPanel panelFormulario = new JPanel(new GridLayout(7, 2, 10, 10));
        panelFormulario.setBorder(
                BorderFactory.createTitledBorder("Datos del Libro"));

        txtTitulo = new JTextField();
        txtAutor = new JTextField();
        txtCategoria = new JTextField();
        txtPrecio = new JTextField();
        txtExistencias = new JTextField();
        txtAnio = new JTextField();
        txtFechaIngreso = new JTextField();

        panelFormulario.add(new JLabel("Título:"));
        panelFormulario.add(txtTitulo);

        panelFormulario.add(new JLabel("Autor:"));
        panelFormulario.add(txtAutor);

        panelFormulario.add(new JLabel("Categoría:"));
        panelFormulario.add(txtCategoria);

        panelFormulario.add(new JLabel("Precio:"));
        panelFormulario.add(txtPrecio);

        panelFormulario.add(new JLabel("Existencias:"));
        panelFormulario.add(txtExistencias);

        panelFormulario.add(new JLabel("Año de publicación:"));
        panelFormulario.add(txtAnio);

        panelFormulario.add(new JLabel("Fecha ingreso (AAAA-MM-DD):"));
        panelFormulario.add(txtFechaIngreso);

        add(panelFormulario, BorderLayout.NORTH);

        modeloTabla = new DefaultTableModel(
                new Object[] {
                        "ID",
                        "Título",
                        "Autor",
                        "Categoría",
                        "Precio",
                        "Existencias",
                        "Año",
                        "Fecha ingreso"
                }, 0) {

            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaLibros = new JTable(modeloTabla);

        JScrollPane scrollPane = new JScrollPane(tablaLibros);
        scrollPane.setBorder(
                BorderFactory.createTitledBorder("Lista de Libros"));

        add(scrollPane, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout());

        btnGuardar = new JButton("Guardar");
        btnActualizar = new JButton("Actualizar");
        btnEliminar = new JButton("Eliminar");
        btnLimpiar = new JButton("Limpiar");
        btnVerResumen = new JButton("Ver resumen");
        
        panelBotones.add(btnGuardar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);
        panelBotones.add(btnVerResumen);

        add(panelBotones, BorderLayout.SOUTH);

        btnGuardar.addActionListener(e -> guardarLibro());
        btnActualizar.addActionListener(e -> actualizarLibro());
        btnEliminar.addActionListener(e -> eliminarLibro());
        btnLimpiar.addActionListener(e -> limpiarCampos());
        btnVerResumen.addActionListener(e -> verResumen());

        tablaLibros.getSelectionModel().addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()) {
                seleccionarLibro();
            }
        });
    }

    private void guardarLibro() {

        try {

            String titulo = txtTitulo.getText().trim();
            String autor = txtAutor.getText().trim();
            String categoria = txtCategoria.getText().trim();
            String fechaTexto = txtFechaIngreso.getText().trim();

            BigDecimal precio =
                    new BigDecimal(txtPrecio.getText().trim());

            int existencias =
                    Integer.parseInt(txtExistencias.getText().trim());

            int anio =
                    Integer.parseInt(txtAnio.getText().trim());

            if (titulo.isEmpty()
                    || autor.isEmpty()
                    || categoria.isEmpty()
                    || fechaTexto.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Debe completar todos los campos."
                );

                return;
            }

            LocalDate fechaIngreso = LocalDate.parse(fechaTexto);

            Libro libro = new Libro(
                    titulo,
                    autor,
                    categoria,
                    precio,
                    existencias,
                    anio
            );

            libro.setFechaIngresoCatalogo(fechaIngreso);

            if (libroDAO.guardar(libro)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Libro guardado correctamente."
                );

                cargarLibros();
                limpiarCampos();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "No se pudo guardar el libro."
                );
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Precio, existencias y año deben ser valores numéricos."
            );

        } catch (DateTimeParseException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "La fecha debe tener el formato AAAA-MM-DD."
            );
        }
    }

    private void actualizarLibro() {

        if (idSeleccionado == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un libro de la tabla."
            );

            return;
        }

        try {

            Libro libro = new Libro();

            libro.setId(idSeleccionado);
            libro.setTitulo(txtTitulo.getText().trim());
            libro.setAutor(txtAutor.getText().trim());
            libro.setCategoria(txtCategoria.getText().trim());

            libro.setPrecio(
                    new BigDecimal(txtPrecio.getText().trim())
            );

            libro.setExistencias(
                    Integer.parseInt(
                            txtExistencias.getText().trim()
                    )
            );

            libro.setAnioPublicacion(
                    Integer.parseInt(
                            txtAnio.getText().trim()
                    )
            );

            libro.setFechaIngresoCatalogo(
                    LocalDate.parse(txtFechaIngreso.getText().trim())
            );

            if (libroDAO.actualizar(libro)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Libro actualizado correctamente."
                );

                cargarLibros();
                limpiarCampos();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "No se pudo actualizar el libro."
                );
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Verifique los valores numéricos."
            );

        } catch (DateTimeParseException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "La fecha debe tener el formato AAAA-MM-DD."
            );
        }
    }

    private void eliminarLibro() {

        if (idSeleccionado == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un libro de la tabla."
            );

            return;
        }

        int opcion = JOptionPane.showConfirmDialog(
                this,
                "¿Desea eliminar el libro seleccionado?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
        );

        if (opcion == JOptionPane.YES_OPTION) {

            if (libroDAO.eliminar(idSeleccionado)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Libro eliminado correctamente."
                );

                cargarLibros();
                limpiarCampos();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "No se pudo eliminar el libro."
                );
            }
        }
    }

    private void seleccionarLibro() {

        int fila = tablaLibros.getSelectedRow();

        if (fila != -1) {

            idSeleccionado =
                    Integer.parseInt(
                            modeloTabla.getValueAt(fila, 0).toString()
                    );

            txtTitulo.setText(
                    modeloTabla.getValueAt(fila, 1).toString()
            );

            txtAutor.setText(
                    modeloTabla.getValueAt(fila, 2).toString()
            );

            txtCategoria.setText(
                    modeloTabla.getValueAt(fila, 3).toString()
            );

            txtPrecio.setText(
                    modeloTabla.getValueAt(fila, 4).toString()
            );

            txtExistencias.setText(
                    modeloTabla.getValueAt(fila, 5).toString()
            );

            txtAnio.setText(
                    modeloTabla.getValueAt(fila, 6).toString()
            );

            Object fecha = modeloTabla.getValueAt(fila, 7);

            txtFechaIngreso.setText(
                    fecha == null ? "" : fecha.toString()
            );
        }
    }

    private void cargarLibros() {

        modeloTabla.setRowCount(0);

        List<Libro> libros = libroDAO.listar();

        for (Libro libro : libros) {

            modeloTabla.addRow(
                    new Object[] {
                            libro.getId(),
                            libro.getTitulo(),
                            libro.getAutor(),
                            libro.getCategoria(),
                            libro.getPrecio(),
                            libro.getExistencias(),
                            libro.getAnioPublicacion(),
                            libro.getFechaIngresoCatalogo()
                    }
            );
        }
    }

    private void limpiarCampos() {

        txtTitulo.setText("");
        txtAutor.setText("");
        txtCategoria.setText("");
        txtPrecio.setText("");
        txtExistencias.setText("");
        txtAnio.setText("");
        txtFechaIngreso.setText("");

        idSeleccionado = -1;

        tablaLibros.clearSelection();

        txtTitulo.requestFocus();
    }
    
    private void verResumen() {

        List<Libro> libros = libroDAO.listar();

        int totalLibros = libros.size();
        int librosConExistencias = 0;

        for (Libro libro : libros) {
            if (libro.getExistencias() > 0) {
                librosConExistencias++;
            }
        }

        JOptionPane.showMessageDialog(
            this,
            "Total de libros registrados: " + totalLibros
            + "\nLibros con existencias: " + librosConExistencias,
            "Resumen del catálogo",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
    
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            VentanaLibros ventana = new VentanaLibros();

            ventana.setVisible(true);
        });
    }
}