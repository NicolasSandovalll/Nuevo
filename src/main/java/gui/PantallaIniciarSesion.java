package gui;

import dominio.Usuario;
import servicios.ServicioAutenticacion;
import servicios.ManejadorVentanas;
import servicios.Sesion;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class PantallaIniciarSesion extends JFrame implements ActionListener {
    private JButton botonIniciarSesion;
    private JButton botonRegistrar;
    private JTextField campoMatricula;
    private JPasswordField campoContrasena;
    private ServicioAutenticacion servicioAutenticacion;
    private ManejadorVentanas manejadorVentanas;

    public PantallaIniciarSesion(ManejadorVentanas manejadorVentanas) {
        this.manejadorVentanas = manejadorVentanas;
        setSize(350, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        servicioAutenticacion = new ServicioAutenticacion("src/main/resources/usuarios.json");

        inicializarComponentes();
    }

    private void inicializarComponentes() {
        JPanel panelPrincipal = crearPanelPrincipal();
        panelPrincipal.add(Box.createRigidArea(new Dimension(0, 20)));
        panelPrincipal.add(crearBarraTitulo());
        panelPrincipal.add(Box.createRigidArea(new Dimension(0, 20)));
        panelPrincipal.add(crearLogo());
        panelPrincipal.add(Box.createRigidArea(new Dimension(0, 20)));
        panelPrincipal.add(crearCampoTexto("Matrícula del Estudiante", campoMatricula = new JTextField()));
        panelPrincipal.add(Box.createRigidArea(new Dimension(0, 15)));
        panelPrincipal.add(crearCampoContrasena("Contraseña", campoContrasena = new JPasswordField()));
        panelPrincipal.add(Box.createRigidArea(new Dimension(0, 20)));
        panelPrincipal.add(crearPanelBotones());

        add(panelPrincipal, BorderLayout.CENTER);
        add(crearPanelSalir(), BorderLayout.SOUTH);
    }

    private JPanel crearPanelPrincipal() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        return panel;
    }

    private JPanel crearBarraTitulo() {
        JPanel barraTitulo = new JPanel();
        barraTitulo.setBackground(new Color(48, 48, 54));
        barraTitulo.setPreferredSize(new Dimension(getWidth(), 45));
        barraTitulo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        barraTitulo.setLayout(new BorderLayout());

        JLabel etiquetaTitulo = new JLabel("EasyRooms", SwingConstants.CENTER);
        etiquetaTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        etiquetaTitulo.setForeground(Color.WHITE);
        barraTitulo.add(etiquetaTitulo, BorderLayout.CENTER);

        return barraTitulo;
    }

    private JLabel crearLogo() {
        ImageIcon logoIcono = new ImageIcon(Objects.requireNonNull(getClass().getResource("/images/ufro_logo.png")));
        Image imagenLogo = logoIcono.getImage().getScaledInstance(250, 250, Image.SCALE_SMOOTH);
        JLabel etiquetaLogo = new JLabel(new ImageIcon(imagenLogo));
        etiquetaLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
        return etiquetaLogo;
    }

    private JTextField crearCampoTexto(String titulo, JTextField campo) {
        campo.setMaximumSize(new Dimension(250, 30));
        campo.setBorder(BorderFactory.createTitledBorder(titulo));
        campo.setBackground(new Color(224, 224, 224));
        campo.setOpaque(true);
        return campo;
    }

    private JPasswordField crearCampoContrasena(String titulo, JPasswordField campo) {
        campo.setMaximumSize(new Dimension(250, 30));
        campo.setBorder(BorderFactory.createTitledBorder(titulo));
        campo.setBackground(new Color(224, 224, 224));
        campo.setOpaque(true);
        return campo;
    }

    private JPanel crearPanelBotones() {
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        panelBotones.setBackground(Color.WHITE);

        botonIniciarSesion = crearBotonRedondeado("Iniciar Sesión", this);
        panelBotones.add(botonIniciarSesion);

        botonRegistrar = crearBotonRedondeado("Registrar Usuario", this);
        panelBotones.add(botonRegistrar);

        return panelBotones;
    }

    private JButton crearBotonRedondeado(String texto, ActionListener listener) {
        JButton boton = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 37, 37);
                super.paintComponent(g);
                g2.dispose();
            }
        };
        boton.setBackground(new Color(33, 0, 93));
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorder(new EmptyBorder(10, 20, 10, 20));
        boton.addActionListener(listener);
        boton.setContentAreaFilled(false);
        return boton;
    }

    private JPanel crearPanelSalir() {
        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setBackground(Color.WHITE);
        panelInferior.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 0));

        JButton botonSalir = crearBotonRedondeado("Salir de la Aplicación", e -> System.exit(0));
        panelInferior.add(botonSalir, BorderLayout.WEST);

        return panelInferior;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == botonIniciarSesion) {
            manejarInicioSesion();
        } else if (e.getSource() == botonRegistrar) {
            manejarRegistro();
        }
    }

    private void manejarInicioSesion() {
        String matricula = campoMatricula.getText();
        String contrasena = new String(campoContrasena.getPassword());
        Usuario usuario = servicioAutenticacion.autenticarUsuario(matricula, contrasena);

        if (usuario != null) {
            Sesion.setUsuario(usuario);
            if (usuario.getTipo().equalsIgnoreCase("Administrador")) {
                manejadorVentanas.mostrarPantallaMenuAdministrador();
            } else {
                manejadorVentanas.mostrarPantallaMenuEstudiante();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Credenciales incorrectas", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void manejarRegistro() {
        JOptionPane.showMessageDialog(this, "Pantalla de registro en desarrollo", "Información", JOptionPane.INFORMATION_MESSAGE);
        manejadorVentanas.mostrarPantallaRegistro();
    }
}
