package guis;

import dominio.Logia;
import dominio.TramoHora;
import servicios.LogiaService;
import servicios.ReservaService;
import servicios.WindowManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class StudentReserveScreen extends JFrame {
    private final LogiaService logiaService;
    private final ReservaService reservaService;
    private final WindowManager windowManager;
    private JTable logiasTable;
    private JComboBox<String> pisoSelector;
    private JComboBox<TramoHora> tramoSelector;

    public StudentReserveScreen(LogiaService logiaService, ReservaService reservaService, WindowManager windowManager) {
        this.logiaService = logiaService;
        this.reservaService = reservaService;
        this.windowManager = windowManager;

        setTitle("Reservar Logia");
        setSize(350, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initializeComponents();
    }

    private void initializeComponents() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);

        mainPanel.add(createTitleBar());
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel selectionPanel = createSelectionPanel();
        mainPanel.add(selectionPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JButton buscarButton = new JButton("Buscar Disponibilidad");
        buscarButton.addActionListener(e -> buscarLogiasDisponibles());
        buscarButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(buscarButton);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        logiasTable = createTable();
        JScrollPane scrollPane = new JScrollPane(logiasTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        mainPanel.add(scrollPane);

        mainPanel.add(createButtonPanel());

        add(mainPanel);
    }

    private JPanel createTitleBar() {
        JPanel titleBar = new JPanel();
        titleBar.setBackground(new Color(33, 0, 93));
        titleBar.setPreferredSize(new Dimension(getWidth(), 60));
        titleBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        titleBar.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Reservar una Logia", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleBar.add(titleLabel, BorderLayout.CENTER);

        return titleBar;
    }

    private JPanel createSelectionPanel() {
        JPanel selectionPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        selectionPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel pisoLabel = new JLabel("Piso:");
        pisoSelector = new JComboBox<>(new String[]{"Piso 2", "Piso 3"});
        JLabel tramoLabel = new JLabel("Tramo Horario:");
        tramoSelector = new JComboBox<>(TramoHora.values());

        selectionPanel.add(pisoLabel);
        selectionPanel.add(pisoSelector);
        selectionPanel.add(tramoLabel);
        selectionPanel.add(tramoSelector);

        return selectionPanel;
    }

    private JTable createTable() {
        String[] columnNames = {"ID Logia", "Capacidad", "Estado"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(tableModel);
        table.setRowHeight(25);
        return table;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        buttonPanel.setBackground(Color.WHITE);

        JButton confirmarButton = new JButton("Confirmar Reserva");
        confirmarButton.addActionListener(e -> confirmarReserva());
        buttonPanel.add(confirmarButton, BorderLayout.CENTER);

        JButton volverButton = new JButton("Volver");
        volverButton.addActionListener(e -> {
            this.dispose();
            windowManager.showStudentMenu();
        });
        buttonPanel.add(volverButton, BorderLayout.WEST);

        return buttonPanel;
    }

    private void buscarLogiasDisponibles() {
        int pisoSeleccionado = pisoSelector.getSelectedIndex() + 2; // Convierte el índice en el piso
        TramoHora tramoHoraSeleccionado = (TramoHora) tramoSelector.getSelectedItem();

        if (tramoHoraSeleccionado != null) {
            List<Logia> logiasDisponibles = logiaService.buscarDisponibles(pisoSeleccionado, 3, tramoHoraSeleccionado);

            if (logiasDisponibles.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No hay logias disponibles en este horario.", "Sin Disponibilidad", JOptionPane.INFORMATION_MESSAGE);
            }

            DefaultTableModel tableModel = (DefaultTableModel) logiasTable.getModel();
            tableModel.setRowCount(0);

            for (Logia logia : logiasDisponibles) {
                tableModel.addRow(new Object[]{
                        logia.getIdLogia(),
                        logia.getCapacidad(),
                        "Disponible"
                });
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un tramo horario válido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void confirmarReserva() {
        int selectedRow = logiasTable.getSelectedRow();

        if (selectedRow >= 0) {
            String idLogiaSeleccionada = (String) logiasTable.getValueAt(selectedRow, 0);
            TramoHora tramoHoraSeleccionado = (TramoHora) tramoSelector.getSelectedItem();
            int pisoSeleccionado = pisoSelector.getSelectedIndex() + 2; // Incluye el piso en la reserva

            try {
                String codigoReserva = reservaService.crearReserva(idLogiaSeleccionada, tramoHoraSeleccionado, pisoSeleccionado);
                JOptionPane.showMessageDialog(this, "Reserva confirmada con código: " + codigoReserva, "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (IllegalStateException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una logia.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
