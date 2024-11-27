package servicios;

import datos.ManejadorDetallesLogias;
import datos.ManejadorLogias;
import datos.ManejadorReservas;
import dominio.Reserva;
import dominio.Usuario;
import gui.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;

public class ManejadorVentanas {
    private static final Logger logger = LoggerFactory.getLogger(ManejadorVentanas.class);
    private JFrame ventanaActual;

    private final ManejadorLogias manejadorLogias = new ManejadorLogias("src/main/resources/logias.json");
    private final ManejadorReservas manejadorReservas = new ManejadorReservas("src/main/resources/reservas.json", manejadorLogias);
    private final ServicioLogias servicioLogias = new ServicioLogias(manejadorLogias, manejadorReservas);
    private final ServicioReservas servicioReservas = new ServicioReservas(manejadorReservas);
    private final ManejadorDetallesLogias manejadorDetallesLogias = new ManejadorDetallesLogias("src/main/resources/detalles_logias.json");

    private void mostrarVentana(JFrame nuevaVentana) {
        if (ventanaActual != null) {
            ventanaActual.dispose();
        }
        ventanaActual = nuevaVentana;
        ventanaActual.setVisible(true);
    }

    public void mostrarPantallaInicioSesion() {
        mostrarVentana(new PantallaIniciarSesion(this));
    }

    public void mostrarPantallaMenuEstudiante() {
        mostrarVentana(new PantallaMenuEstudiante(this));
    }

    public void mostrarPantallaReservarEstudiante() {
        mostrarVentana(new PantallaReservarEstudiante(servicioLogias, servicioReservas, this));
    }

    public void mostrarPantallaRegistro() {
        mostrarVentana(new PantallaRegistrarUsuario(this));
    }

    public void mostrarPantallaMenuAdministrador() {
        mostrarVentana(new PantallaMenuAdministrador(this));
    }

    public void mostrarPantallaGestionarLogias() {
        mostrarVentana(new PantallaGestionarLogias(servicioLogias, this));
    }

    public void mostrarPantallaConfirmarReservas() {
        mostrarVentana(new PantallaConfirmarReservas(servicioReservas, this));
    }

    public void mostrarPantallaReservasEstudiante(Reserva reserva) {
        mostrarVentana(new PantallaReservasEstudiante(this, reserva, servicioReservas));
    }

    public void mostrarPantallaDetallesLogias() {
        mostrarVentana(new PantallaDetallesLogias(manejadorDetallesLogias, this));
    }

    public Reserva obtenerReservaEstudianteActual() {
        Usuario usuarioActual = Sesion.getUsuario(); // Obtener el usuario actual de la sesión
        if (usuarioActual != null) {
            return servicioReservas.obtenerReservasPendientes().stream()
                    .filter(reserva -> reserva.getMatriculaEstudiante().equals(usuarioActual.getMatricula()))
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }
}
