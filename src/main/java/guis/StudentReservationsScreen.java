package guis;

import servicios.WindowManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StudentReservationsScreen extends JFrame implements ActionListener {
    private final WindowManager windowManager;

    public StudentReservationsScreen(WindowManager windowManager) {
        this.windowManager = windowManager;
        setTitle("Mi reserva");
        setSize(350, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initializeComponents();
        setVisible(true);
    }

    private void initializeComponents() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);

        // Título
        JLabel titleLabel = new JLabel("Mi reserva", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.BLACK);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(titleLabel);

        // Botón de volver
        JButton backButton = new JButton("Volver");
        backButton.addActionListener(this);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        mainPanel.add(backButton);

        add(mainPanel, BorderLayout.CENTER);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        windowManager.showStudentMenu();  // Vuelve al menú principal del estudiante
    }
}
