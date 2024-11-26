package guis;

import servicios.WindowManager;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StudentMenuScreen extends JFrame implements ActionListener {
    private WindowManager windowManager;

    public StudentMenuScreen(WindowManager windowManager) {
        this.windowManager = windowManager;
        setTitle("Menu Principal");
        setSize(350, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initializeComponents();
    }

    private void initializeComponents() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Menu Principal", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(new Color(48, 48, 54));
        titlePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        titlePanel.add(titleLabel, BorderLayout.CENTER);

        mainPanel.add(titlePanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Botón para ir directamente a reservar una logia (ya incluye la funcionalidad de ver disponibilidad)
        mainPanel.add(createButton("Ir a reservar una logia", e -> windowManager.showStudentReserveScreen()));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Botón para "Mis reservas"
        mainPanel.add(createButton("Mis reservas", e -> windowManager.showStudentReservationsScreen()));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Botón para volver al login
        JButton volverButton = new JButton("Volver");
        volverButton.setBackground(new Color(33, 0, 93));
        volverButton.setForeground(Color.WHITE);
        volverButton.addActionListener(e -> windowManager.showLoginScreen());
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(volverButton);

        add(mainPanel);
    }

    private JButton createButton(String text, ActionListener action) {
        JButton button = new JButton(text);
        button.setBackground(new Color(33, 0, 93));
        button.setForeground(Color.WHITE);
        button.addActionListener(action);
        return button;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Eventos adicionales si es necesario
    }
}