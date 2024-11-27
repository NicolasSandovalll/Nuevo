package gui;

import datos.ManejadorDetallesLogias;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PantallaDetallesLogias extends JFrame {
    private final ManejadorDetallesLogias manejadorDetallesLogias;
    private final servicios.ManejadorVentanas manejadorVentanas;

    public PantallaDetallesLogias(ManejadorDetallesLogias manejadorDetallesLogias, servicios.ManejadorVentanas manejadorVentanas) {
        this.manejadorDetallesLogias = manejadorDetallesLogias;
        this.manejadorVentanas = manejadorVentanas;
        setTitle("Detalles de las Logias");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        inicializarComponentes();
    }

    private void inicializarComponentes() {
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BorderLayout());

        // Título
        JLabel etiquetaTitulo = new JLabel("Detalles de las Logias", SwingConstants.CENTER);
        etiquetaTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        panelPrincipal.add(etiquetaTitulo, BorderLayout.NORTH);

        // Panel para mostrar los detalles
        JPanel panelDetalles = new JPanel();
        panelDetalles.setLayout(new GridLayout(0, 6, 10, 10)); // Filas automáticas, 6 columnas
        panelDetalles.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Encabezados
        panelDetalles.add(new JLabel("Miembros", SwingConstants.CENTER));
        panelDetalles.add(new JLabel("Sillas", SwingConstants.CENTER));
        panelDetalles.add(new JLabel("Mesa", SwingConstants.CENTER));
        panelDetalles.add(new JLabel("Televisor", SwingConstants.CENTER));
        panelDetalles.add(new JLabel("Pizarrón", SwingConstants.CENTER));
        panelDetalles.add(new JLabel("Computador", SwingConstants.CENTER));

        // Obtener los detalles de las logias
        List<JSONObject> detallesLogias = manejadorDetallesLogias.cargarDetallesLogias();

        // Agregar los datos a la tabla
        for (JSONObject detalle : detallesLogias) {
            panelDetalles.add(new JLabel(String.valueOf(detalle.getInt("minimoMiembros")), SwingConstants.CENTER));
            panelDetalles.add(new JLabel(String.valueOf(detalle.getInt("sillas")), SwingConstants.CENTER));
            panelDetalles.add(new JLabel(detalle.getString("mesa"), SwingConstants.CENTER));
            panelDetalles.add(new JLabel(String.valueOf(detalle.getInt("televisor")), SwingConstants.CENTER));
            panelDetalles.add(new JLabel(String.valueOf(detalle.getInt("pizarron")), SwingConstants.CENTER));
            panelDetalles.add(new JLabel(String.valueOf(detalle.getInt("computador")), SwingConstants.CENTER));
        }

        // Scroll para los datos
        JScrollPane scrollPane = new JScrollPane(panelDetalles);
        panelPrincipal.add(scrollPane, BorderLayout.CENTER);

        // Botón volver
        JButton botonVolver = new JButton("Volver");
        botonVolver.addActionListener(e -> manejadorVentanas.mostrarPantallaMenuEstudiante());
        panelPrincipal.add(botonVolver, BorderLayout.SOUTH);

        add(panelPrincipal);
    }
}