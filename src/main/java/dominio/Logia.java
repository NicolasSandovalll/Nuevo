package dominio;

import org.json.JSONObject;

public class Logia {
    private String idLogia;               // Correspondiente a "idLogia" en el JSON
    private int capacidad;                // Correspondiente a "capacidad" en el JSON
    private String ubicacion;             // Correspondiente a "ubicacion" en el JSON
    private JSONObject disponibilidad;    // Correspondiente a "disponibilidad" en el JSON

    // Constructor
    public Logia(String idLogia, int capacidad, String ubicacion, JSONObject disponibilidad) {
        this.idLogia = idLogia;
        this.capacidad = capacidad;
        this.ubicacion = ubicacion;
        this.disponibilidad = disponibilidad;
    }

    // Getters
    public String getIdLogia() {
        return idLogia;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public JSONObject getDisponibilidad() {
        return disponibilidad;
    }

    // Setters (opcional si necesitas modificar los datos después de la creación)
    public void setIdLogia(String idLogia) {
        this.idLogia = idLogia;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public void setDisponibilidad(JSONObject disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

    // Métodos adicionales para manejar disponibilidad
    public void actualizarEstado(String tramoHorario, String estado) {
        if (!disponibilidad.has(tramoHorario)) {
            throw new IllegalArgumentException("El tramo horario no existe: " + tramoHorario);
        }
        disponibilidad.put(tramoHorario, estado);
    }

    public String obtenerEstado(String tramoHorario) {
        if (!disponibilidad.has(tramoHorario)) {
            throw new IllegalArgumentException("El tramo horario no existe: " + tramoHorario);
        }
        return disponibilidad.getString(tramoHorario);
    }

    @Override
    public String toString() {
        return "Logia [idLogia=" + idLogia + ", capacidad=" + capacidad + ", ubicacion=" + ubicacion + ", disponibilidad=" + disponibilidad + "]";
    }
}