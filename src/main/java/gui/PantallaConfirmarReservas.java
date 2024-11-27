package gui;

import dominio.Reserva;
import servicios.ServicioReservas;
import servicios.ManejadorVentanas;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PantallaConfirmarReservas extends JFrame {
    private final ServicioReservas servicioReservas;
    private final ManejadorVentanas manejadorVentanas;
    private JTable tablaReservas;

    public PantallaConfirmarReservas(ServicioReservas servicioReservas, ManejadorVentanas manejadorVentanas) {
        this.servicioReservas = servicioReservas;
        this.manejadorVentanas = manejadorVentanas;

        setTitle("Confirmar Reservas");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        inicializarComponentes();
        cargarReservasPendientes();
    }

    private void inicializarComponentes() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        JLabel titulo = new JLabel("Confirmar Reservas", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        mainPanel.add(titulo, BorderLayout.NORTH);

        tablaReservas = crearTabla();
        JScrollPane scrollPane = new JScrollPane(tablaReservas);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel panelBotones = crearPanelBotones();
        mainPanel.add(panelBotones, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JTable crearTabla() {
        String[] columnas = {"ID Reserva", "Matrícula", "ID Logia", "Piso", "Tramo Horario"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        return new JTable(modelo);
    }

    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton confirmarButton = new JButton("Confirmar");
        confirmarButton.addActionListener(e -> confirmarReserva());
        panel.add(confirmarButton);

        JButton volverButton = new JButton("Volver");
        volverButton.addActionListener(e -> manejadorVentanas.mostrarPantallaMenuAdministrador());
        panel.add(volverButton);

        return panel;
    }

    private void cargarReservasPendientes() {
        List<Reserva> reservas = servicioReservas.obtenerReservasPendientes();
        DefaultTableModel modelo = (DefaultTableModel) tablaReservas.getModel();
        modelo.setRowCount(0);

        for (Reserva reserva : reservas) {
            modelo.addRow(new Object[]{reserva.getIdReserva(), reserva.getMatriculaEstudiante(), reserva.getIdLogia(),
                    reserva.getPiso(), reserva.getTramoHora().name()});
        }
    }

    private void confirmarReserva() {
        int filaSeleccionada = tablaReservas.getSelectedRow();

        if (filaSeleccionada >= 0) {
            String idReserva = (String) tablaReservas.getValueAt(filaSeleccionada, 0);

            try {
                servicioReservas.confirmarReserva(idReserva);
                cargarReservasPendientes();
                JOptionPane.showMessageDialog(this, "Reserva confirmada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al confirmar la reserva: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione una reserva.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}