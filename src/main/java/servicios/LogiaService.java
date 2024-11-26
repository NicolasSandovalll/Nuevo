package servicios;

import datos.LogiaHandler;
import datos.ReservaHandler;
import dominio.Logia;
import dominio.TramoHora;

import java.util.List;

public class LogiaService {
    private final LogiaHandler logiaHandler;
    private final ReservaHandler reservaHandler;

    public LogiaService(LogiaHandler logiaHandler, ReservaHandler reservaHandler) {
        this.logiaHandler = logiaHandler;
        this.reservaHandler = reservaHandler;
    }


    public void marcarLogiaComoOcupada(int piso, String idLogia, TramoHora tramoHora) {
        actualizarEstadoLogia(piso, idLogia, tramoHora, "OCUPADA");
    }

    public List<Logia> buscarDisponibles(int piso, int capacidadMinima, TramoHora tramoHora) {
        if (piso <= 0) {
            throw new IllegalArgumentException("El piso debe ser un número positivo.");
        }
        if (capacidadMinima <= 0) {
            throw new IllegalArgumentException("La capacidad mínima debe ser mayor a 0.");
        }
        if (tramoHora == null) {
            throw new IllegalArgumentException("El tramo horario no puede ser nulo.");
        }

        return logiaHandler.obtenerLogiasDisponibles(piso, capacidadMinima, tramoHora);
    }

    public List<Logia> buscarPorEstado(String estado) {
        if (estado == null || estado.isEmpty()) {
            throw new IllegalArgumentException("El estado no puede ser nulo o vacío.");
        }

        return logiaHandler.obtenerLogiasPorEstado(estado);
    }

    public List<Logia> buscarPorEstadoYPiso(String estado, int piso) {
        if (estado == null || estado.isEmpty()) {
            throw new IllegalArgumentException("El estado no puede ser nulo o vacío.");
        }

        if (piso <= 0) {
            throw new IllegalArgumentException("El piso debe ser un número positivo.");
        }

        return logiaHandler.obtenerLogiasPorEstadoYPiso(estado, piso);
    }

    public void actualizarEstadoLogia(int piso, String idLogia, TramoHora tramoHora, String nuevoEstado) {
        if (idLogia == null || idLogia.isEmpty())
            throw new IllegalArgumentException("El ID de la logia no puede ser nulo o vacío.");

        if (tramoHora == null)
            throw new IllegalArgumentException("El tramo horario no puede ser nulo.");

        if (piso <= 0)
            throw new IllegalArgumentException("El piso debe ser un número positivo.");

        if (!nuevoEstado.equalsIgnoreCase("DISPONIBLE") &&
                !nuevoEstado.equalsIgnoreCase("RESERVADA") &&
                !nuevoEstado.equalsIgnoreCase("OCUPADA")) {
            throw new IllegalArgumentException("Estado no válido: " + nuevoEstado);
        }

        try {
            logiaHandler.actualizarDisponibilidad(piso, idLogia, tramoHora, nuevoEstado.toUpperCase());

            // Si la logia pasa a "DISPONIBLE" u "OCUPADA", eliminarla del archivo de reservas
            if (nuevoEstado.equalsIgnoreCase("DISPONIBLE") || nuevoEstado.equalsIgnoreCase("OCUPADA")) {
                reservaHandler.eliminarReservaPorLogia(piso, idLogia, tramoHora);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar el estado de la logia: " + e.getMessage(), e);
        }
    }
}
