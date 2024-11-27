package launcher;

import servicios.ManejadorVentanas;

import javax.swing.*;

public class Principal {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ManejadorVentanas windowManager = new ManejadorVentanas();
            windowManager.mostrarPantallaInicioSesion();
        });
    }
}