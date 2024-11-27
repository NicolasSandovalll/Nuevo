package gui;

import dominio.Logia;
import dominio.TramoHora;
import dominio.EstadoLogia;
import servicios.ServicioLogias;
import servicios.ManejadorVentanas;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PantallaGestionarLogias extends JFrame {
    private final ServicioLogias servicioLogias;
    private final ManejadorVentanas manejadorVentanas;
    private JTable tablaLogias;

    public PantallaGestionarLogias(ServicioLogias servicioLogias, ManejadorVentanas manejadorVentanas) {
        this.servicioLogias = servicioLogias;
        this.manejadorVentanas = manejadorVentanas;

        setTitle("Gestionar Logias Ocupadas");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        inicializarComponentes();
        cargarLogias(); // Carga inicial de las logias al abrir la ventana
    }

    private void inicializarComponentes() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Título
        JLabel titulo = new JLabel("Logias Ocupadas", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        mainPanel.add(titulo, BorderLayout.NORTH);

        // Tabla para mostrar las logias
        tablaLogias = crearTabla();
        JScrollPane scrollPane = new JScrollPane(tablaLogias);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Panel de botones inferiores
        JPanel panelBotones = crearPanelBotones();
        mainPanel.add(panelBotones, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JTable crearTabla() {
        // Define las columnas de la tabla
        String[] columnas = {"ID Logia", "Capacidad", "Piso", "Periodo"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Desactiva la edición de celdas
            }
        };
        return new JTable(modelo);
    }

    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        // Botón para marcar como disponible
        JButton marcarDisponibleButton = new JButton("Marcar como Disponible");
        marcarDisponibleButton.addActionListener(e -> actualizarEstadoLogia());
        panel.add(marcarDisponibleButton);

        // Botón para volver al menú del administrador
        JButton volverButton = new JButton("Volver");
        volverButton.addActionListener(e -> manejadorVentanas.mostrarPantallaMenuAdministrador());
        panel.add(volverButton);

        return panel;
    }

    private void cargarLogias() {
        // Obtiene las logias ocupadas
        List<Logia> logias = servicioLogias.buscarPorEstado(EstadoLogia.OCUPADA);
        DefaultTableModel modelo = (DefaultTableModel) tablaLogias.getModel();
        modelo.setRowCount(0); // Limpia la tabla antes de cargar nuevos datos

        for (Logia logia : logias) {
            for (String tramo : logia.getDisponibilidad().keySet()) {
                String estado = logia.getDisponibilidad().getString(tramo);

                if (estado.equalsIgnoreCase("OCUPADA")) {
                    // Convierte el tramo a TramoHora y obtiene la descripción
                    TramoHora tramoHora = TramoHora.valueOf(tramo);
                    modelo.addRow(new Object[]{
                            logia.getIdLogia(),
                            logia.getCapacidad(),
                            logia.getUbicacion(),
                            tramoHora.getRange() // Muestra el rango legible
                    });
                }
            }
        }
    }

    private void actualizarEstadoLogia() {
        int filaSeleccionada = tablaLogias.getSelectedRow();

        if (filaSeleccionada >= 0) {
            // Obtiene el ID de la logia seleccionada, piso y periodo
            String idLogia = (String) tablaLogias.getValueAt(filaSeleccionada, 0);
            String piso = (String) tablaLogias.getValueAt(filaSeleccionada, 2);
            String periodo = (String) tablaLogias.getValueAt(filaSeleccionada, 3); // Obtiene el rango

            try {
                // Mapea el rango de vuelta al enum TramoHora
                TramoHora tramoHora = null;
                for (TramoHora t : TramoHora.values()) {
                    if (t.getRange().equals(periodo)) {
                        tramoHora = t;
                        break;
                    }
                }

                if (tramoHora == null) {
                    throw new IllegalArgumentException("No se pudo mapear el periodo seleccionado al enum TramoHora.");
                }

                // Actualiza el estado de la logia a "DISPONIBLE"
                servicioLogias.actualizarEstadoLogia(Integer.parseInt(piso.replace("Piso ", "")), idLogia, tramoHora, EstadoLogia.DISPONIBLE);
                JOptionPane.showMessageDialog(this, "Estado actualizado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

                cargarLogias(); // Refresca la tabla
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al actualizar el estado: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione una logia para actualizar.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}