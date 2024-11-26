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

public class ReservaHandler {
    private static final Logger logger = LoggerFactory.getLogger(ReservaHandler.class);
    private final String filePath;
    private final List<Reserva> reservas;
    private final LogiaHandler logiaHandler;

    public ReservaHandler(String filePath, LogiaHandler logiaHandler) {
        this.filePath = filePath;
        this.reservas = new ArrayList<>();
        this.logiaHandler = logiaHandler;
        loadReservas();
    }

    public void addReserva(Reserva reserva) {
        boolean reservaExistente = reservas.stream()
                .anyMatch(r -> r.getIdLogia().equals(reserva.getIdLogia())
                        && r.getHoraReserva().equals(reserva.getHoraReserva())
                        && r.getPiso() == reserva.getPiso());

        if (reservaExistente) {
            throw new IllegalStateException("La logia ya está reservada para este tramo horario en este piso.");
        }

        reservas.add(reserva);

        logiaHandler.actualizarDisponibilidad(reserva.getPiso(), reserva.getIdLogia(), reserva.getHoraReserva(), "RESERVADA");

        saveReservas();
    }

    public void cancelarReserva(String idReserva) {
        Reserva reservaAEliminar = reservas.stream()
                .filter(reserva -> reserva.getIdReserva().equals(idReserva))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Reserva no encontrada con ID: " + idReserva));

        logiaHandler.actualizarDisponibilidad(reservaAEliminar.getPiso(), reservaAEliminar.getIdLogia(), reservaAEliminar.getHoraReserva(), "DISPONIBLE");

        reservas.remove(reservaAEliminar);

        saveReservas();
    }

    public void eliminarReservaPorLogia(int piso, String idLogia, TramoHora tramoHora) {
        Reserva reservaAEliminar = reservas.stream()
                .filter(reserva -> reserva.getPiso() == piso &&
                        reserva.getIdLogia().equals(idLogia) &&
                        reserva.getHoraReserva().equals(tramoHora))
                .findFirst()
                .orElse(null);

        if (reservaAEliminar != null) {
            reservas.remove(reservaAEliminar);
            logger.info("Reserva eliminada: {}", reservaAEliminar.getIdReserva());
            saveReservas();
        }
    }

    public void saveReservas() {
        try (FileWriter writer = new FileWriter(filePath)) {
            JSONArray jsonArray = new JSONArray();
            for (Reserva reserva : reservas) {
                JSONObject jsonReserva = new JSONObject();
                jsonReserva.put("idReserva", reserva.getIdReserva());
                jsonReserva.put("matriculaEstudiante", reserva.getMatriculaEstudiante());
                jsonReserva.put("piso", reserva.getPiso());
                jsonReserva.put("idLogia", reserva.getIdLogia());
                jsonReserva.put("horaReserva", reserva.getHoraReserva().name());
                jsonArray.put(jsonReserva);
            }
            writer.write(jsonArray.toString(4));
        } catch (IOException e) {
            logger.error("Error al guardar las reservas en el archivo JSON: {}", e.getMessage(), e);
        }
    }

    public void loadReservas() {
        try (FileReader reader = new FileReader(filePath)) {
            StringBuilder stringBuilder = new StringBuilder();
            int i;
            while ((i = reader.read()) != -1) {
                stringBuilder.append((char) i);
            }

            JSONArray jsonArray = new JSONArray(stringBuilder.toString());
            for (int j = 0; j < jsonArray.length(); j++) {
                JSONObject jsonReserva = jsonArray.getJSONObject(j);
                TramoHora tramoHora = TramoHora.valueOf(jsonReserva.getString("horaReserva"));

                Reserva reserva = new Reserva(
                        jsonReserva.getString("idReserva"),
                        jsonReserva.getString("matriculaEstudiante"),
                        jsonReserva.getInt("piso"),
                        jsonReserva.getString("idLogia"),
                        tramoHora
                );
                reservas.add(reserva);
            }
        } catch (IOException e) {
            logger.error("Error al cargar las reservas desde el archivo: {}", e.getMessage(), e);
        }
    }

    public List<Reserva> getReservas() {
        return reservas;
    }
}
