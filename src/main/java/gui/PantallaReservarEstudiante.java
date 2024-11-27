package gui;

import dominio.Logia;
import dominio.TramoHora;
import servicios.ServicioLogias;
import servicios.ServicioReservas;
import servicios.ManejadorVentanas;
import servicios.Sesion;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PantallaReservarEstudiante extends JFrame {
    private final ServicioLogias servicioLogias;
    private final ServicioReservas servicioReservas;
    private final ManejadorVentanas manejadorVentanas;
    private JTable tablaLogias;
    private JComboBox<String> selectorPiso;
    private JComboBox<TramoHora> selectorTramo;

    public PantallaReservarEstudiante(ServicioLogias servicioLogias, ServicioReservas servicioReservas, ManejadorVentanas manejadorVentanas) {
        this.servicioLogias = servicioLogias;
        this.servicioReservas = servicioReservas;
        this.manejadorVentanas = manejadorVentanas;

        setTitle("Reservar Logia");
        setSize(400, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        inicializarComponentes();
    }

    private void inicializarComponentes() {
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
        panelPrincipal.setBackground(Color.WHITE);

        panelPrincipal.add(crearBarraTitulo());
        panelPrincipal.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel panelSeleccion = crearPanelSeleccion();
        panelPrincipal.add(panelSeleccion);
        panelPrincipal.add(Box.createRigidArea(new Dimension(0, 10)));

        JButton botonBuscar = new JButton("Buscar Disponibilidad");
        botonBuscar.addActionListener(e -> buscarLogiasDisponibles());
        botonBuscar.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelPrincipal.add(botonBuscar);
        panelPrincipal.add(Box.createRigidArea(new Dimension(0, 20)));

        tablaLogias = crearTabla();
        JScrollPane scrollPane = new JScrollPane(tablaLogias);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        panelPrincipal.add(scrollPane);

        panelPrincipal.add(crearPanelBotones());

        add(panelPrincipal);
    }

    private JPanel crearBarraTitulo() {
        JPanel barraTitulo = new JPanel();
        barraTitulo.setBackground(new Color(33, 0, 93));
        barraTitulo.setPreferredSize(new Dimension(getWidth(), 60));
        barraTitulo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        barraTitulo.setLayout(new BorderLayout());

        JLabel etiquetaTitulo = new JLabel("Reservar una Logia", SwingConstants.CENTER);
        etiquetaTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        etiquetaTitulo.setForeground(Color.WHITE);
        barraTitulo.add(etiquetaTitulo, BorderLayout.CENTER);

        return barraTitulo;
    }

    private JPanel crearPanelSeleccion() {
        JPanel panelSeleccion = new JPanel(new GridLayout(2, 2, 10, 10));
        panelSeleccion.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel etiquetaPiso = new JLabel("Piso:");
        selectorPiso = new JComboBox<>(new String[]{"Piso 2", "Piso 3"});
        JLabel etiquetaTramo = new JLabel("Tramo Horario:");
        selectorTramo = new JComboBox<>(TramoHora.values());

        panelSeleccion.add(etiquetaPiso);
        panelSeleccion.add(selectorPiso);
        panelSeleccion.add(etiquetaTramo);
        panelSeleccion.add(selectorTramo);

        return panelSeleccion;
    }

    private JTable crearTabla() {
        String[] nombresColumnas = {"Seleccionar", "ID Logia", "Capacidad"};
        DefaultTableModel modeloTabla = new DefaultTableModel(nombresColumnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0; // Solo la columna de selección es editable
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) {
                    return Boolean.class; // La primera columna es de tipo Boolean para los checkboxes
                }
                return String.class;
            }
        };

        JTable tabla = new JTable(modeloTabla);
        tabla.setRowHeight(25);

        // Control para evitar ciclos infinitos
        final boolean[] isAdjusting = {false};

        // Lógica de selección única con control de ciclos
        tabla.getModel().addTableModelListener(e -> {
            if (!isAdjusting[0] && e.getColumn() == 0) { // Solo aplica la lógica cuando se edita la columna de selección
                isAdjusting[0] = true;
                int rowCount = tabla.getRowCount();
                for (int i = 0; i < rowCount; i++) {
                    if (i != e.getFirstRow()) { // Desmarca todas las demás filas
                        tabla.setValueAt(false, i, 0);
                    }
                }
                isAdjusting[0] = false;
            }
        });

        return tabla;
    }


    private JPanel crearPanelBotones() {
        JPanel panelBotones = new JPanel(new BorderLayout());
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        panelBotones.setBackground(Color.WHITE);

        JButton botonConfirmar = new JButton("Confirmar Reserva");
        botonConfirmar.addActionListener(e -> confirmarReserva());
        panelBotones.add(botonConfirmar, BorderLayout.CENTER);

        JButton botonVolver = new JButton("Volver");
        botonVolver.addActionListener(e -> {
            this.dispose();
            manejadorVentanas.mostrarPantallaMenuEstudiante();
        });
        panelBotones.add(botonVolver, BorderLayout.WEST);

        return panelBotones;
    }

    private void buscarLogiasDisponibles() {
        // Verificar si el usuario ya tiene una reserva activa
        if (servicioReservas.tieneReservaActiva(Sesion.getUsuario().getMatricula())) {
            JOptionPane.showMessageDialog(this,
                    "Ya tienes una reserva activa. Cancela tu reserva actual antes de intentar otra.",
                    "Reserva Activa",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int pisoSeleccionado = selectorPiso.getSelectedIndex() + 2;
        TramoHora tramoSeleccionado = (TramoHora) selectorTramo.getSelectedItem();

        if (tramoSeleccionado != null) {
            List<Logia> logiasDisponibles = servicioLogias.buscarDisponibles(pisoSeleccionado, 3, tramoSeleccionado);

            if (logiasDisponibles.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "No hay logias disponibles en este horario.",
                        "Sin Disponibilidad",
                        JOptionPane.INFORMATION_MESSAGE);
            }

            DefaultTableModel modeloTabla = (DefaultTableModel) tablaLogias.getModel();
            modeloTabla.setRowCount(0);

            for (Logia logia : logiasDisponibles) {
                modeloTabla.addRow(new Object[]{false, logia.getIdLogia(), logia.getCapacidad()});
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un tramo horario válido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void confirmarReserva() {
        DefaultTableModel modeloTabla = (DefaultTableModel) tablaLogias.getModel();

        for (int i = 0; i < modeloTabla.getRowCount(); i++) {
            Boolean seleccionado = (Boolean) modeloTabla.getValueAt(i, 0);
            if (seleccionado != null && seleccionado) {
                String idLogiaSeleccionada = (String) modeloTabla.getValueAt(i, 1);
                int capacidadSeleccionada = (int) modeloTabla.getValueAt(i, 2);
                TramoHora tramoSeleccionado = (TramoHora) selectorTramo.getSelectedItem();
                int pisoSeleccionado = selectorPiso.getSelectedIndex() + 2;

                try {
                    String codigoReserva = servicioReservas.crearReserva(idLogiaSeleccionada, tramoSeleccionado, pisoSeleccionado, capacidadSeleccionada);
                    JOptionPane.showMessageDialog(this,
                            "Reserva confirmada con código: " + codigoReserva,
                            "Éxito",
                            JOptionPane.INFORMATION_MESSAGE);
                    return;
                } catch (IllegalStateException e) {
                    JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        JOptionPane.showMessageDialog(this, "Debe seleccionar una logia.", "Error", JOptionPane.ERROR_MESSAGE);
    }
}
