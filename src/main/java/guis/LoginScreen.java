package guis;

import dominio.Usuario;
import servicios.AuthService;
import servicios.WindowManager;
import servicios.Session;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class LoginScreen extends JFrame implements ActionListener {
    private JButton loginButton;
    private JButton registerButton;
    private JTextField matriculaField;
    private JPasswordField passwordField;
    private AuthService authService;
    private WindowManager windowManager;

    public LoginScreen(WindowManager windowManager) {
        this.windowManager = windowManager;
        setSize(350, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        authService = new AuthService("src/main/resources/usuarios.json");

        initializeComponents();
    }

    private void initializeComponents() {
        JPanel mainPanel = createMainPanel();
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(createTitleBar());
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(createLogo());
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(createTextField("Matrícula", matriculaField = new JTextField()));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        mainPanel.add(createPasswordField("Contraseña", passwordField = new JPasswordField()));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(createButtonPanel());

        add(mainPanel, BorderLayout.CENTER);
        add(createExitButtonPanel(), BorderLayout.SOUTH);
    }

    private JPanel createMainPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        return panel;
    }

    private JPanel createTitleBar() {
        JPanel titleBar = new JPanel();
        titleBar.setBackground(new Color(48, 48, 54));
        titleBar.setPreferredSize(new Dimension(getWidth(), 45));
        titleBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        titleBar.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("EasyRooms", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleBar.add(titleLabel, BorderLayout.CENTER);

        return titleBar;
    }

    private JLabel createLogo() {
        ImageIcon logoIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/images/ufro_logo.png")));
        Image logoImage = logoIcon.getImage().getScaledInstance(250, 250, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(logoImage));
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        return logoLabel;
    }

    private JTextField createTextField(String title, JTextField field) {
        field.setMaximumSize(new Dimension(250, 30));
        field.setBorder(BorderFactory.createTitledBorder(title));
        field.setBackground(new Color(224, 224, 224)); // Fondo gris claro para los campos
        field.setOpaque(true);
        return field;
    }

    private JPasswordField createPasswordField(String title, JPasswordField field) {
        field.setMaximumSize(new Dimension(250, 30));
        field.setBorder(BorderFactory.createTitledBorder(title));
        field.setBackground(new Color(224, 224, 224)); // Fondo gris claro para los campos
        field.setOpaque(true);
        return field;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(Color.WHITE);

        loginButton = createOvalButton("Iniciar Sesión", this);
        buttonPanel.add(loginButton);

        registerButton = createOvalButton("Registrar", this);
        buttonPanel.add(registerButton);

        return buttonPanel;
    }

    private JButton createOvalButton(String text, ActionListener listener) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 37, 37); // Ajuste de redondeo
                super.paintComponent(g);
                g2.dispose();
            }
        };
        button.setBackground(new Color(33, 0, 93));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(10, 20, 10, 20));
        button.addActionListener(listener);
        button.setContentAreaFilled(false);
        return button;
    }

    private JPanel createExitButtonPanel() {
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 0));

        JButton exitButton = createOvalButton("Salir", e -> System.exit(0));
        bottomPanel.add(exitButton, BorderLayout.WEST);

        return bottomPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            handleLogin();
        } else if (e.getSource() == registerButton) {
            handleRegister();
        }
    }

    private void handleLogin() {
        String matricula = matriculaField.getText();
        String password = new String(passwordField.getPassword());
        Usuario usuario = authService.autenticarUsuario(matricula, password);

        if (usuario != null) {
            Session.setUsuario(usuario); // Guardar usuario en la sesión
            if (usuario.getTipo().equalsIgnoreCase("Administrador")) {
                windowManager.showAdminMenu();
            } else {
                windowManager.showStudentMenu();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Credenciales incorrectas");
        }
    }

    private void handleRegister() {
        JOptionPane.showMessageDialog(this, "Registro de nuevo usuario");
        setVisible(false);  // Oculta la ventana actual en lugar de cerrarla
        windowManager.showRegisterScreen();
    }
}
