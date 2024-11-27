package servicios;

import datos.ManejadorReservas;
import dominio.Reserva;
import dominio.TramoHora;
import dominio.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;

public class ServicioReservas {
    private static final Logger logger = LoggerFactory.getLogger(ServicioReservas.class);
    private final ManejadorReservas manejadorReservas;

    public ServicioReservas(ManejadorReservas manejadorReservas) {
        this.manejadorReservas = manejadorReservas;
    }

    public String crearReserva(String idLogia, TramoHora tramoHorario, int piso, int capacidad) {
        if (idLogia == null || idLogia.isEmpty()) {
            throw new IllegalArgumentException("El ID de la logia no puede ser nulo o vacío.");
        }
        if (tramoHorario == null) {
            throw new IllegalArgumentException("El tramo horario no puede ser nulo.");
        }
        if (piso <= 0) {
            throw new IllegalArgumentException("El piso debe ser mayor a cero.");
        }
        if (capacidad <= 0) {
            throw new IllegalArgumentException("La capacidad debe ser mayor a cero.");
        }

        Usuario usuario = Sesion.getUsuario();

        if (usuario == null) {
            throw new IllegalStateException("No hay un usuario autenticado en la sesión.");
        }

        String matriculaUsuario = usuario.getMatricula();

        if (tieneReservaActiva(matriculaUsuario)) {
            throw new IllegalStateException("Ya tienes una reserva activa. No puedes reservar más logias.");
        }

        // Generar un código único para la reserva
        String codigoReserva = UUID.randomUUID().toString().substring(0, 6);

        // Crear la nueva reserva
        Reserva nuevaReserva = new Reserva(codigoReserva, matriculaUsuario, piso, idLogia, tramoHorario, capacidad);

        // Agregar la reserva al manejador
        manejadorReservas.agregarReserva(nuevaReserva);
        logger.info("Reserva creada exitosamente: {} para la logia: {} con capacidad: {}", codigoReserva, idLogia, capacidad);

        return codigoReserva;
    }

    public boolean tieneReservaActiva(String matricula) {
        if (matricula == null || matricula.isEmpty()) {
            throw new IllegalArgumentException("La matrícula no puede ser nula o vacía.");
        }

        // Verificar si existe una reserva activa para la matrícula proporcionada
        boolean reservaActiva = manejadorReservas.obtenerReservas().stream()
                .anyMatch(reserva -> reserva.getMatriculaEstudiante().equals(matricula));
        if (reservaActiva) {
            logger.warn("El usuario con matrícula {} ya tiene una reserva activa.", matricula);
        }
        return reservaActiva;
    }

    public void eliminarReservaPorLogia(int piso, String idLogia, TramoHora tramoHorario) {
        if (idLogia == null || idLogia.isEmpty()) {
            throw new IllegalArgumentException("El ID de la logia no puede ser nulo o vacío.");
        }
        if (tramoHorario == null) {
            throw new IllegalArgumentException("El tramo horario no puede ser nulo.");
        }
        if (piso <= 0) {
            throw new IllegalArgumentException("El piso debe ser mayor a cero.");
        }

        manejadorReservas.eliminarReservaPorLogia(piso, idLogia, tramoHorario);
        logger.info("Reserva eliminada para la logia: {} en el tramo horario: {}", idLogia, tramoHorario);
    }

    public List<Reserva> obtenerReservasPendientes() {
        List<Reserva> reservasPendientes = manejadorReservas.obtenerReservas();
        logger.info("Se obtuvieron {} reservas pendientes.", reservasPendientes.size());
        return reservasPendientes;
    }

    public void confirmarReserva(String idReserva) {
        if (idReserva == null || idReserva.isEmpty()) {
            throw new IllegalArgumentException("El ID de la reserva no puede ser nulo o vacío.");
        }

        // Buscar la reserva correspondiente
        Reserva reserva = manejadorReservas.obtenerReservas().stream()
                .filter(r -> r.getIdReserva().equals(idReserva))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No se encontró una reserva con el ID proporcionado: " + idReserva));

        // Actualizar el estado de la logia a "OCUPADA"
        manejadorReservas.getManejadorLogias().actualizarDisponibilidad(
                reserva.getPiso(),
                reserva.getIdLogia(),
                reserva.getTramoHora(),
                "OCUPADA"
        );
        logger.info("Estado de la logia actualizado a OCUPADA: ID Logia = {}, Piso = {}, Tramo Horario = {}",
                reserva.getIdLogia(), reserva.getPiso(), reserva.getTramoHora());

        // Eliminar la reserva sin cambiar el estado de la logia
        manejadorReservas.eliminarReservaSinCambiarEstado(idReserva);

        logger.info("Reserva confirmada y eliminada: {}", idReserva);
    }

    public void cancelarReserva(String idReserva) {
        manejadorReservas.cancelarReservaPorId(idReserva);
        logger.info("Reserva cancelada a través del servicio: {}", idReserva);
    }



}
