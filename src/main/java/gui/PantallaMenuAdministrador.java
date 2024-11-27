package gui;

import servicios.ManejadorVentanas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class PantallaMenuAdministrador extends JFrame {
    private final ManejadorVentanas manejadorVentanas;

    public PantallaMenuAdministrador(ManejadorVentanas manejadorVentanas) {
        this.manejadorVentanas = manejadorVentanas;

        setTitle("Menú Administrador");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initializeComponents();
    }

    private void initializeComponents() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Menú del Administrador", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JButton gestionarLogiasButton = new JButton("Gestionar Logias");
        gestionarLogiasButton.addActionListener(this::gestionarLogias);
        gestionarLogiasButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(gestionarLogiasButton);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JButton confirmarReservasButton = new JButton("Confirmar Reservas");
        confirmarReservasButton.addActionListener(this::confirmarReservas);
        confirmarReservasButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(confirmarReservasButton);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JButton cerrarSesionButton = new JButton("Cerrar Sesión");
        cerrarSesionButton.addActionListener(e -> cerrarSesion());
        cerrarSesionButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(cerrarSesionButton);

        add(mainPanel);
    }

    private void gestionarLogias(ActionEvent e) {
        manejadorVentanas.mostrarPantallaGestionarLogias();
    }

    private void confirmarReservas(ActionEvent e) {
        manejadorVentanas.mostrarPantallaConfirmarReservas();
    }

    private void cerrarSesion() {
        this.dispose();
        manejadorVentanas.mostrarPantallaInicioSesion();
    }
}