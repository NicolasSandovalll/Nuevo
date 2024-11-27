package datos;

import dominio.Reserva;
import dominio.TramoHora;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ManejadorReservas {
    private static final Logger logger = LoggerFactory.getLogger(ManejadorReservas.class);
    private final String rutaArchivo;
    private final List<Reserva> reservas;
    private final ManejadorLogias manejadorLogias;

    public ManejadorReservas(String rutaArchivo, ManejadorLogias manejadorLogias) {
        this.rutaArchivo = rutaArchivo;
        this.reservas = new ArrayList<>();
        this.manejadorLogias = manejadorLogias;
        cargarReservas();
    }

    public void agregarReserva(Reserva reserva) {
        boolean reservaExistente = reservas.stream()
                .anyMatch(r -> r.getIdLogia().equals(reserva.getIdLogia())
                        && r.getTramoHora().equals(reserva.getTramoHora())
                        && r.getPiso() == reserva.getPiso());

        if (reservaExistente) {
            throw new IllegalStateException("La logia ya está reservada para este tramo horario en este piso.");
        }

        reservas.add(reserva);
        manejadorLogias.actualizarDisponibilidad(reserva.getPiso(), reserva.getIdLogia(), reserva.getTramoHora(), "RESERVADA");
        guardarReservas();
        logger.info("Reserva agregada exitosamente: {}", reserva.getIdReserva());
    }


    public void eliminarReservaPorLogia(int piso, String idLogia, TramoHora tramoHorario) {
        Reserva reservaAEliminar = reservas.stream()
                .filter(reserva -> reserva.getPiso() == piso &&
                        reserva.getIdLogia().equals(idLogia) &&
                        reserva.getTramoHora().equals(tramoHorario))
                .findFirst()
                .orElse(null);

        if (reservaAEliminar != null) {
            reservas.remove(reservaAEliminar);
            manejadorLogias.actualizarDisponibilidad(piso, idLogia, tramoHorario, "DISPONIBLE");
            guardarReservas();
            logger.info("Reserva eliminada: {}", reservaAEliminar.getIdReserva());
        } else {
            logger.warn("No se encontró reserva para eliminar en logia: {}, tramo: {}, piso: {}", idLogia, tramoHorario, piso);
        }
    }

    public void guardarReservas() {
        try (FileWriter writer = new FileWriter(rutaArchivo)) {
            JSONArray jsonArray = new JSONArray();
            for (Reserva reserva : reservas) {
                JSONObject jsonReserva = new JSONObject();
                jsonReserva.put("idReserva", reserva.getIdReserva());
                jsonReserva.put("idEstudiante", reserva.getMatriculaEstudiante());
                jsonReserva.put("piso", reserva.getPiso());
                jsonReserva.put("idLogia", reserva.getIdLogia());
                jsonReserva.put("tramoHorario", reserva.getTramoHora().name());
                jsonReserva.put("capacidad", reserva.getCapacidad()); // Nuevo atributo
                jsonArray.put(jsonReserva);
            }
            writer.write(jsonArray.toString(4));
            logger.info("Reservas guardadas correctamente en el archivo JSON.");
        } catch (IOException e) {
            logger.error("Error al guardar las reservas en el archivo JSON: {}", e.getMessage(), e);
        }
    }

    public void cargarReservas() {
        try (FileReader reader = new FileReader(rutaArchivo)) {
            StringBuilder contenido = new StringBuilder();
            int i;
            while ((i = reader.read()) != -1) {
                contenido.append((char) i);
            }

            JSONArray jsonArray = new JSONArray(contenido.toString());
            for (int j = 0; j < jsonArray.length(); j++) {
                JSONObject jsonReserva = jsonArray.getJSONObject(j);
                TramoHora tramoHorario = TramoHora.valueOf(jsonReserva.getString("tramoHorario"));

                Reserva reserva = new Reserva(
                        jsonReserva.getString("idReserva"),
                        jsonReserva.getString("idEstudiante"),
                        jsonReserva.getInt("piso"),
                        jsonReserva.getString("idLogia"),
                        tramoHorario,
                        jsonReserva.getInt("capacidad") // Nuevo atributo
                );
                reservas.add(reserva);
            }
            logger.info("Reservas cargadas correctamente desde el archivo JSON.");
        } catch (IOException e) {
            logger.error("Error al cargar las reservas desde el archivo JSON: {}", e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Error inesperado al procesar el archivo JSON: {}", e.getMessage(), e);
        }
    }

    public List<Reserva> obtenerReservas() {
        return new ArrayList<>(reservas);
    }

    public ManejadorLogias getManejadorLogias() {
        return manejadorLogias;
    }

    public void eliminarReservaSinCambiarEstado(String idReserva) {
        Reserva reservaAEliminar = reservas.stream()
                .filter(reserva -> reserva.getIdReserva().equals(idReserva))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No se encontró una reserva con el ID: " + idReserva));

        reservas.remove(reservaAEliminar);
        guardarReservas();
        logger.info("Reserva eliminada sin cambiar el estado de la logia: {}", idReserva);
    }

    public void cancelarReservaPorId(String idReserva) {
        Reserva reservaAEliminar = reservas.stream()
                .filter(reserva -> reserva.getIdReserva().equals(idReserva))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No se encontró una reserva con el ID: " + idReserva));

        // Cambiar el estado de la logia a DISPONIBLE
        manejadorLogias.actualizarDisponibilidad(
                reservaAEliminar.getPiso(),
                reservaAEliminar.getIdLogia(),
                reservaAEliminar.getTramoHora(),
                "DISPONIBLE"
        );

        // Eliminar la reserva de la lista
        reservas.remove(reservaAEliminar);

        // Guardar cambios en el archivo
        guardarReservas();

        logger.info("Reserva cancelada correctamente: {}", idReserva);
    }

}