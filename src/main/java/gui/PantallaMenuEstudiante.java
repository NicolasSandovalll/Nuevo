package gui;

import dominio.Reserva;
import servicios.ManejadorVentanas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PantallaMenuEstudiante extends JFrame implements ActionListener {
    private final ManejadorVentanas manejadorVentanas;

    public PantallaMenuEstudiante(ManejadorVentanas manejadorVentanas) {
        this.manejadorVentanas = manejadorVentanas;
        setTitle("Menú Principal");
        setSize(350, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        inicializarComponentes();
    }

    private void inicializarComponentes() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);

        // Título
        JLabel titleLabel = new JLabel("Menú Principal", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(new Color(48, 48, 54));
        titlePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        titlePanel.add(titleLabel, BorderLayout.CENTER);

        mainPanel.add(titlePanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Botón para ir a reservar una logia
        mainPanel.add(crearBoton("Reservar una Logia", e -> manejadorVentanas.mostrarPantallaReservarEstudiante()));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Botón para ir a "Mis Reservas"
        mainPanel.add(crearBoton("Mis Reservas", e -> {
            Reserva reservaActual = manejadorVentanas.obtenerReservaEstudianteActual();
            if (reservaActual != null) {
                manejadorVentanas.mostrarPantallaReservasEstudiante(reservaActual);
            } else {
                JOptionPane.showMessageDialog(this, "No tienes reservas activas.", "Información", JOptionPane.INFORMATION_MESSAGE);
            }
        }));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Botón para ver detalles de las logias
        mainPanel.add(crearBoton("Ver detalles de las logias", e -> manejadorVentanas.mostrarPantallaDetallesLogias()));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Botón para cerrar sesión
        JButton botonCerrarSesion = new JButton("Cerrar Sesión");
        botonCerrarSesion.setBackground(new Color(33, 0, 93));
        botonCerrarSesion.setForeground(Color.WHITE);
        botonCerrarSesion.addActionListener(e -> {
            manejadorVentanas.mostrarPantallaInicioSesion();
            dispose(); // Cerrar la ventana actual
        });
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(botonCerrarSesion);

        add(mainPanel);
    }

    private JButton crearBoton(String texto, ActionListener accion) {
        JButton boton = new JButton(texto);
        boton.setBackground(new Color(33, 0, 93));
        boton.setForeground(Color.WHITE);
        boton.addActionListener(accion);
        boton.setFocusPainted(false);
        boton.setAlignmentX(Component.CENTER_ALIGNMENT);
        boton.setMaximumSize(new Dimension(300, 40)); // Tamaño máximo del botón
        return boton;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Aquí puedes manejar eventos adicionales si es necesario
    }
}