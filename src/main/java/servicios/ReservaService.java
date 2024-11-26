package servicios;

import datos.ReservaHandler;
import dominio.Reserva;
import dominio.TramoHora;
import dominio.Usuario;

import java.util.UUID;

public class ReservaService {
    private final ReservaHandler reservaHandler;

    public ReservaService(ReservaHandler reservaHandler) {
        this.reservaHandler = reservaHandler;
    }

    public String crearReserva(String idLogia, TramoHora tramoHora, int piso) {
        if (idLogia == null || idLogia.isEmpty()) {
            throw new IllegalArgumentException("El ID de la logia no puede ser nulo o vacío.");
        }
        if (tramoHora == null) {
            throw new IllegalArgumentException("El tramo horario no puede ser nulo.");
        }
        if (piso <= 0) {
            throw new IllegalArgumentException("El piso debe ser mayor a cero.");
        }

        Usuario usuario = Session.getUsuario();

        if (usuario == null) {
            throw new IllegalStateException("No hay un usuario autenticado en la sesión.");
        }

        String matriculaUsuario = usuario.getMatricula();

        if (tieneReservaActiva(matriculaUsuario)) {
            throw new IllegalStateException("Ya tienes una reserva activa. No puedes reservar más logias.");
        }

        String codigoReserva = UUID.randomUUID().toString().substring(0, 6);
        Reserva nuevaReserva = new Reserva(codigoReserva, matriculaUsuario, piso, idLogia, tramoHora);

        reservaHandler.addReserva(nuevaReserva);

        return codigoReserva;
    }

    public boolean tieneReservaActiva(String matricula) {
        if (matricula == null || matricula.isEmpty()) {
            throw new IllegalArgumentException("La matrícula no puede ser nula o vacía.");
        }

        return reservaHandler.getReservas().stream()
                .anyMatch(reserva -> reserva.getMatriculaEstudiante().equals(matricula));
    }

    /**
     * Método para eliminar reservas relacionadas con una logia específica.
     * Esto se usa cuando el estado de la logia cambia a "DISPONIBLE" u "OCUPADA".
     *
     * @param piso       El piso donde se encuentra la logia.
     * @param idLogia    El ID de la logia.
     * @param tramoHora  El tramo horario asociado.
     */
    public void eliminarReservaPorLogia(int piso, String idLogia, TramoHora tramoHora) {
        if (idLogia == null || idLogia.isEmpty()) {
            throw new IllegalArgumentException("El ID de la logia no puede ser nulo o vacío.");
        }
        if (tramoHora == null) {
            throw new IllegalArgumentException("El tramo horario no puede ser nulo.");
        }
        if (piso <= 0) {
            throw new IllegalArgumentException("El piso debe ser mayor a cero.");
        }

        try {
            reservaHandler.eliminarReservaPorLogia(piso, idLogia, tramoHora);
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar la reserva relacionada con la logia: " + e.getMessage(), e);
        }
    }
}
