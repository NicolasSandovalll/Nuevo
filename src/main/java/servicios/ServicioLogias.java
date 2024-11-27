package servicios;

import datos.ManejadorLogias;
import datos.ManejadorReservas;
import dominio.Logia;
import dominio.TramoHora;
import dominio.EstadoLogia;

import java.util.List;

public class ServicioLogias {
    private final ManejadorLogias manejadorLogias;
    private final ManejadorReservas manejadorReservas;

    // Constructor
    public ServicioLogias(ManejadorLogias manejadorLogias, ManejadorReservas manejadorReservas) {
        this.manejadorLogias = manejadorLogias;
        this.manejadorReservas = manejadorReservas;
    }

    // Marcar logia como ocupada
    public void marcarLogiaComoOcupada(int piso, String idLogia, TramoHora tramoHorario) {
        actualizarEstadoLogia(piso, idLogia, tramoHorario, EstadoLogia.OCUPADA);
    }

    // Buscar logias disponibles
    public List<Logia> buscarDisponibles(int piso, int capacidadMinima, TramoHora tramoHorario) {
        if (piso <= 0) {
            throw new IllegalArgumentException("El piso debe ser un número positivo. Valor recibido: " + piso);
        }
        if (capacidadMinima <= 0) {
            throw new IllegalArgumentException("La capacidad mínima debe ser mayor a 0. Valor recibido: " + capacidadMinima);
        }
        if (tramoHorario == null) {
            throw new IllegalArgumentException("El tramo horario no puede ser nulo.");
        }

        return manejadorLogias.obtenerLogiasDisponibles(piso, capacidadMinima, tramoHorario);
    }

    // Buscar logias por estado
    public List<Logia> buscarPorEstado(EstadoLogia estado) {
        if (estado == null) {
            throw new IllegalArgumentException("El estado no puede ser nulo.");
        }

        return manejadorLogias.obtenerLogiasPorEstado(estado.name());
    }

    // Buscar logias por estado y piso
    public List<Logia> buscarPorEstadoYPiso(EstadoLogia estado, int piso) {
        if (estado == null) {
            throw new IllegalArgumentException("El estado no puede ser nulo.");
        }

        if (piso <= 0) {
            throw new IllegalArgumentException("El piso debe ser un número positivo. Valor recibido: " + piso);
        }

        return manejadorLogias.obtenerLogiasPorEstadoYPiso(estado.name(), piso);
    }

    // Actualizar el estado de una logia
    public void actualizarEstadoLogia(int piso, String idLogia, TramoHora tramoHorario, EstadoLogia nuevoEstado) {
        if (idLogia == null || idLogia.isEmpty()) {
            throw new IllegalArgumentException("El ID de la logia no puede ser nulo o vacío.");
        }

        if (tramoHorario == null) {
            throw new IllegalArgumentException("El tramo horario no puede ser nulo.");
        }

        if (piso <= 0) {
            throw new IllegalArgumentException("El piso debe ser un número positivo. Valor recibido: " + piso);
        }

        manejadorLogias.actualizarDisponibilidad(piso, idLogia, tramoHorario, nuevoEstado.name());

        // Eliminar reservas si la logia se actualiza a DISPONIBLE u OCUPADA
        if (nuevoEstado == EstadoLogia.DISPONIBLE || nuevoEstado == EstadoLogia.OCUPADA) {
            manejadorReservas.eliminarReservaPorLogia(piso, idLogia, tramoHorario);
        }
    }
}
