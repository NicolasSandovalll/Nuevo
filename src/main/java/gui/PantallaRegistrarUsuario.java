package gui;

import servicios.ManejadorVentanas;

import javax.swing.*;
import java.awt.*;

public class PantallaRegistrarUsuario extends JFrame {
    private final ManejadorVentanas manejadorVentanas;

    public PantallaRegistrarUsuario(ManejadorVentanas manejadorVentanas) {
        this.manejadorVentanas = manejadorVentanas;
        setTitle("Registro de Usuario");
        setSize(350, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        inicializarComponentes();
    }

    private void inicializarComponentes() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);

        JLabel etiquetaTitulo = new JLabel("Registro de Usuario", SwingConstants.CENTER);
        etiquetaTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        etiquetaTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(etiquetaTitulo);

        JButton botonVolver = new JButton("Volver al Inicio de Sesión");
        botonVolver.addActionListener(e -> manejadorVentanas.mostrarPantallaInicioSesion());
        botonVolver.setAlignmentX(Component.CENTER_ALIGNMENT); // Centra el botón en el panel
        panel.add(Box.createRigidArea(new Dimension(0, 20))); // Espaciado entre el título y el botón
        panel.add(botonVolver);

        add(panel);
    }
}