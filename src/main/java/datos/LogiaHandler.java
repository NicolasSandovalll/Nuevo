package datos;

import dominio.Logia;
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

public class LogiaHandler {
    private static final Logger logger = LoggerFactory.getLogger(LogiaHandler.class);
    private final String filePath;
    private final List<Logia> logias;

    public LogiaHandler(String filePath) {
        this.filePath = filePath;
        this.logias = new ArrayList<>();
        loadLogias();
    }

    public void loadLogias() {
        try (FileReader reader = new FileReader(filePath)) {
            StringBuilder stringBuilder = new StringBuilder();
            int i;
            while ((i = reader.read()) != -1) {
                stringBuilder.append((char) i);
            }

            JSONObject jsonObject = new JSONObject(stringBuilder.toString());
            JSONArray jsonLogias = jsonObject.getJSONArray("logias");

            for (int j = 0; j < jsonLogias.length(); j++) {
                JSONObject jsonLogia = jsonLogias.getJSONObject(j);
                String idLogia = jsonLogia.getString("idLogia");
                int capacidad = jsonLogia.getInt("capacidad");
                String ubicacion = jsonLogia.getString("ubicacion");
                JSONObject disponibilidad = jsonLogia.getJSONObject("disponibilidad");

                Logia logia = new Logia(idLogia, capacidad, ubicacion, disponibilidad);
                logias.add(logia);
            }
        } catch (IOException e) {
            logger.error("Error al cargar las logias desde el archivo JSON: {}", e.getMessage(), e);
        }
    }

    public void actualizarDisponibilidad(int piso, String idLogia, TramoHora tramoHora, String nuevoEstado) {
        if (!nuevoEstado.equalsIgnoreCase("DISPONIBLE")
                && !nuevoEstado.equalsIgnoreCase("RESERVADA")
                && !nuevoEstado.equalsIgnoreCase("OCUPADA")) {
            throw new IllegalArgumentException("Estado no válido: " + nuevoEstado);
        }

        String pisoStr = "Piso " + piso;
        for (Logia logia : logias) {
            if (logia.getUbicacion().equalsIgnoreCase(pisoStr) && logia.getIdLogia().equals(idLogia)) {
                logia.getDisponibilidad().put(tramoHora.name(), nuevoEstado.toUpperCase());
                saveLogias();
                return;
            }
        }
        throw new IllegalArgumentException("Logia no encontrada en Piso " + piso + " con ID: " + idLogia);
    }

    public void saveLogias() {
        try (FileWriter writer = new FileWriter(filePath)) {
            JSONArray jsonArray = new JSONArray();
            for (Logia logia : logias) {
                JSONObject jsonLogia = new JSONObject();
                jsonLogia.put("idLogia", logia.getIdLogia());
                jsonLogia.put("capacidad", logia.getCapacidad());
                jsonLogia.put("ubicacion", logia.getUbicacion());
                jsonLogia.put("disponibilidad", logia.getDisponibilidad());
                jsonArray.put(jsonLogia);
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("logias", jsonArray);

            writer.write(jsonObject.toString(4));
        } catch (IOException e) {
            logger.error("Error al guardar las logias en el archivo JSON: {}", e.getMessage(), e);
        }
    }

    public List<Logia> obtenerLogiasDisponibles(int piso, int capacidadMinima, TramoHora tramoHora) {
        List<Logia> logiasDisponibles = new ArrayList<>();
        String pisoStr = "Piso " + piso;

        for (Logia logia : logias) {
            String estado = logia.getDisponibilidad().getString(tramoHora.name());
            if (logia.getUbicacion().equalsIgnoreCase(pisoStr)
                    && logia.getCapacidad() >= capacidadMinima
                    && estado.equalsIgnoreCase("DISPONIBLE")) {
                logiasDisponibles.add(logia);
            }
        }
        return logiasDisponibles;
    }

    public List<Logia> obtenerLogiasPorEstado(String estado) {
        List<Logia> logiasFiltradas = new ArrayList<>();
        for (Logia logia : logias) {
            for (String tramo : logia.getDisponibilidad().keySet()) {
                if (logia.getDisponibilidad().getString(tramo).equalsIgnoreCase(estado)) {
                    logiasFiltradas.add(logia);
                    break; // Evita agregar la misma logia varias veces.
                }
            }
        }
        return logiasFiltradas;
    }

    public List<Logia> obtenerLogiasPorEstadoYPiso(String estado, int piso) {
        List<Logia> logiasFiltradas = new ArrayList<>();
        String pisoStr = "Piso " + piso;

        for (Logia logia : logias) {
            if (!logia.getUbicacion().equalsIgnoreCase(pisoStr)) continue;

            for (String tramo : logia.getDisponibilidad().keySet()) {
                if (logia.getDisponibilidad().getString(tramo).equalsIgnoreCase(estado)) {
                    logiasFiltradas.add(logia);
                    break; // Evita agregar la misma logia varias veces.
                }
            }
        }
        return logiasFiltradas;
    }
}
