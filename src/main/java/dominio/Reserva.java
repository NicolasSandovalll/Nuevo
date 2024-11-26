package dominio;

public class Reserva {
    private final String idReserva;
    private final String matriculaEstudiante;
    private final int piso;
    private final String idLogia;
    private final TramoHora horaReserva;

    public Reserva(String idReserva, String matriculaEstudiante, int piso, String idLogia, TramoHora horaReserva) {
        if (idReserva == null || idReserva.isEmpty()) {
            throw new IllegalArgumentException("El ID de la reserva no puede ser nulo o vacío.");
        }
        if (matriculaEstudiante == null || matriculaEstudiante.isEmpty()) {
            throw new IllegalArgumentException("La matrícula del estudiante no puede ser nula o vacía.");
        }
        if (piso <= 0) {
            throw new IllegalArgumentException("El piso debe ser mayor a 0.");
        }
        if (idLogia == null || idLogia.isEmpty()) {
            throw new IllegalArgumentException("El ID de la logia no puede ser nulo o vacío.");
        }
        if (horaReserva == null) {
            throw new IllegalArgumentException("El tramo horario no puede ser nulo.");
        }

        this.idReserva = idReserva;
        this.matriculaEstudiante = matriculaEstudiante;
        this.piso = piso;
        this.idLogia = idLogia;
        this.horaReserva = horaReserva;
    }

    public String getIdReserva() {
        return idReserva;
    }

    public String getMatriculaEstudiante() {
        return matriculaEstudiante;
    }

    public int getPiso() {
        return piso;
    }

    public String getIdLogia() {
        return idLogia;
    }

    public TramoHora getHoraReserva() {
        return horaReserva;
    }
}
