package dominio;

import org.json.JSONObject;

public class Logia {
    private String idLogia;
    private int capacidad;
    private String ubicacion;
    private JSONObject disponibilidad; // Disponibilidad por tramo horario

    // Constructor
    public Logia(String idLogia, int capacidad, String ubicacion, JSONObject disponibilidad) {
        this.idLogia = idLogia;
        this.capacidad = capacidad;
        this.ubicacion = ubicacion;
        this.disponibilidad = disponibilidad;
    }

    // Getters y setters
    public String getIdLogia() {
        return idLogia;
    }

    public void setIdLogia(String idLogia) {
        this.idLogia = idLogia;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public JSONObject getDisponibilidad() {
        return disponibilidad;
    }

    public void setDisponibilidad(JSONObject disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

    /**
     * Actualiza el estado de la logia para un tramo horario específico.
     *
     * @param tramoHora El tramo horario.
     * @param estado    El nuevo estado ("DISPONIBLE", "RESERVADA", "OCUPADA").
     */
    public void actualizarEstado(String tramoHora, String estado) {
        if (!disponibilidad.has(tramoHora)) {
            throw new IllegalArgumentException("El tramo horario no existe: " + tramoHora);
        }
        disponibilidad.put(tramoHora, estado);
    }

    /**
     * Obtiene el estado actual de la logia para un tramo horario específico.
     *
     * @param tramoHora El tramo horario.
     * @return El estado actual ("DISPONIBLE", "RESERVADA", "OCUPADA").
     */
    public String obtenerEstado(String tramoHora) {
        if (!disponibilidad.has(tramoHora)) {
            throw new IllegalArgumentException("El tramo horario no existe: " + tramoHora);
        }
        return disponibilidad.getString(tramoHora);
    }

    @Override
    public String toString() {
        return "Logia [idLogia=" + idLogia + ", capacidad=" + capacidad + ", ubicacion=" + ubicacion + ", disponibilidad=" + disponibilidad + "]";
    }
}
