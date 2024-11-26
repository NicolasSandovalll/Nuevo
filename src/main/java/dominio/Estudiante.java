package dominio;

public class Estudiante extends Usuario {
    public Estudiante(String matricula, String contrasena) {
        super(matricula, contrasena);
    }

    @Override
    public String getTipo() {
        return "Estudiante";
    }
}
