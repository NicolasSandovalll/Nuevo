package gui;

import dominio.Reserva;
import servicios.ManejadorVentanas;
import servicios.ServicioReservas;

import javax.swing.*;
import java.awt.*;

public class PantallaReservasEstudiante extends JFrame {
    private final ManejadorVentanas manejadorVentanas;
    private final Reserva reserva;
    private final ServicioReservas servicioReservas; // Servicio para manejar reservas

    public PantallaReservasEstudiante(ManejadorVentanas manejadorVentanas, Reserva reserva, ServicioReservas servicioReservas) {
        this.manejadorVentanas = manejadorVentanas;
        this.reserva = reserva; // Información de la reserva del estudiante
        this.servicioReservas = servicioReservas; // Servicio para cancelar reservas
        setTitle("Mis Reservas");
        setSize(350, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        inicializarComponentes();
        setVisible(true);
    }

    private void inicializarComponentes() {
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
        panelPrincipal.setBackground(Color.WHITE);

        // Título
        JLabel etiquetaTitulo = new JLabel("Detalles de mi Reserva", SwingConstants.CENTER);
        etiquetaTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        etiquetaTitulo.setForeground(Color.BLACK);
        panelPrincipal.add(Box.createRigidArea(new Dimension(0, 20)));
        panelPrincipal.add(etiquetaTitulo);

        // Panel de información de la reserva
        JPanel panelDatosReserva = new JPanel();
        panelDatosReserva.setLayout(new GridLayout(5, 2, 10, 10)); // Filas, columnas, hgap, vgap
        panelDatosReserva.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelDatosReserva.setBackground(Color.WHITE);

        // Agregar los datos al panel
        panelDatosReserva.add(new JLabel("HORA:", SwingConstants.RIGHT));
        panelDatosReserva.add(new JLabel(reserva.getTramoHora().toString()));

        panelDatosReserva.add(new JLabel("PISO:", SwingConstants.RIGHT));
        panelDatosReserva.add(new JLabel(String.valueOf(reserva.getPiso())));

        panelDatosReserva.add(new JLabel("NÚMERO DE LOGIA:", SwingConstants.RIGHT));
        panelDatosReserva.add(new JLabel(reserva.getIdLogia()));

        panelDatosReserva.add(new JLabel("CAPACIDAD:", SwingConstants.RIGHT));
        panelDatosReserva.add(new JLabel(String.valueOf(reserva.getCapacidad())));

        panelDatosReserva.add(new JLabel("CÓDIGO DE RESERVA:", SwingConstants.RIGHT));
        panelDatosReserva.add(new JLabel(reserva.getIdReserva()));

        panelPrincipal.add(panelDatosReserva);

        // Botón de cancelar reserva
        JButton botonCancelar = new JButton("Cancelar Reserva");
        botonCancelar.addActionListener(e -> cancelarReserva());
        panelPrincipal.add(Box.createRigidArea(new Dimension(0, 10)));
        panelPrincipal.add(botonCancelar);

        // Botón de volver
        JButton botonVolver = new JButton("Volver");
        botonVolver.addActionListener(e -> manejadorVentanas.mostrarPantallaMenuEstudiante());
        panelPrincipal.add(Box.createRigidArea(new Dimension(0, 10)));
        panelPrincipal.add(botonVolver);

        add(panelPrincipal, BorderLayout.CENTER);
    }

    private void cancelarReserva() {
        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Estás seguro de que deseas cancelar esta reserva?",
                "Confirmar Cancelación",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                servicioReservas.cancelarReserva(reserva.getIdReserva());
                JOptionPane.showMessageDialog(this, "Reserva cancelada con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                manejadorVentanas.mostrarPantallaMenuEstudiante();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al cancelar la reserva: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

}