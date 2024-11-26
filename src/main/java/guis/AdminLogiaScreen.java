package guis;

import dominio.Logia;
import dominio.TramoHora;
import servicios.LogiaService;
import servicios.WindowManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class AdminLogiaScreen extends JFrame {
    private final LogiaService logiaService;
    private final WindowManager windowManager;

    private DefaultListModel<String> disponiblesModel;
    private DefaultListModel<String> reservadasModel;
    private DefaultListModel<String> ocupadasModel;
    private JList<String> disponiblesList;
    private JList<String> reservadasList;
    private JList<String> ocupadasList;

    public AdminLogiaScreen(LogiaService logiaService, WindowManager windowManager) {
        this.logiaService = logiaService;
        this.windowManager = windowManager;

        setTitle("Administrar Logias");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initializeComponents();
        cargarLogias();
    }

    private void initializeComponents() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Administrar Logias", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel listsPanel = new JPanel();
        listsPanel.setLayout(new GridLayout(1, 3));

        disponiblesModel = new DefaultListModel<>();
        reservadasModel = new DefaultListModel<>();
        ocupadasModel = new DefaultListModel<>();

        disponiblesList = new JList<>(disponiblesModel);
        reservadasList = new JList<>(reservadasModel);
        ocupadasList = new JList<>(ocupadasModel);

        listsPanel.add(createScrollPane("Logias Disponibles", disponiblesList));
        listsPanel.add(createScrollPane("Logias Reservadas", reservadasList));
        listsPanel.add(createScrollPane("Logias Ocupadas", ocupadasList));

        mainPanel.add(listsPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        JButton cambiarEstadoButton = new JButton("Cambiar Estado");
        cambiarEstadoButton.addActionListener(this::cambiarEstado);
        buttonPanel.add(cambiarEstadoButton);

        JButton volverButton = new JButton("Volver");
        volverButton.addActionListener(e -> windowManager.showAdminMenu());
        buttonPanel.add(volverButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JScrollPane createScrollPane(String title, JList<String> list) {
        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setBorder(BorderFactory.createTitledBorder(title));
        return scrollPane;
    }

    private void cargarLogias() {
        disponiblesModel.clear();
        reservadasModel.clear();
        ocupadasModel.clear();

        List<Logia> disponibles = logiaService.buscarPorEstado("DISPONIBLE");
        List<Logia> reservadas = logiaService.buscarPorEstado("RESERVADA");
        List<Logia> ocupadas = logiaService.buscarPorEstado("OCUPADA");

        disponibles.forEach(logia -> disponiblesModel.addElement(formatLogia(logia)));
        reservadas.forEach(logia -> reservadasModel.addElement(formatLogia(logia)));
        ocupadas.forEach(logia -> ocupadasModel.addElement(formatLogia(logia)));
    }

    private String formatLogia(Logia logia) {
        String tramo = logia.getDisponibilidad().keySet().iterator().next();
        String estado = logia.getDisponibilidad().getString(tramo);
        return String.format("ID: %s | Piso: %s | Tramo: %s | Estado: %s",
                logia.getIdLogia(),
                logia.getUbicacion(),
                tramo,
                estado);
    }

    private void cambiarEstado(ActionEvent e) {
        String seleccion = null;
        if (!reservadasList.isSelectionEmpty()) {
            seleccion = reservadasList.getSelectedValue();
        } else if (!ocupadasList.isSelectionEmpty()) {
            seleccion = ocupadasList.getSelectedValue();
        }

        if (seleccion == null) {
            JOptionPane.showMessageDialog(this, "Seleccione una logia para cambiar su estado.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String[] partes = seleccion.split("\\|");
        String idLogia = partes[0].split(":")[1].trim();
        String piso = partes[1].split(":")[1].trim();
        String tramo = partes[2].split(":")[1].trim();

        int pisoNumero = Integer.parseInt(piso.split(" ")[1]);
        TramoHora tramoHora = TramoHora.valueOf(tramo);

        String[] opciones = {"DISPONIBLE", "RESERVADA", "OCUPADA"};
        String nuevoEstado = (String) JOptionPane.showInputDialog(this,
                "Seleccione el nuevo estado:",
                "Cambiar Estado",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]);

        if (nuevoEstado != null) {
            try {
                logiaService.actualizarEstadoLogia(pisoNumero, idLogia, tramoHora, nuevoEstado.toUpperCase());
                JOptionPane.showMessageDialog(this, "Estado actualizado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarLogias();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al actualizar el estado: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
