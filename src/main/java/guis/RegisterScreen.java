package guis;

import servicios.WindowManager;

import javax.swing.*;
import java.awt.*;

public class RegisterScreen extends JFrame {
    private final WindowManager windowManager;

    public RegisterScreen(WindowManager windowManager) {
        this.windowManager = windowManager;
        setTitle("Registro de Usuario");
        setSize(350, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initializeComponents();
    }

    private void initializeComponents() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);

        JLabel label = new JLabel("Pantalla de Registro", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 24));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(label);

        JButton backButton = new JButton("Volver");
        backButton.addActionListener(e -> windowManager.showLoginScreen());
        panel.add(backButton);

        add(panel);
    }
}
