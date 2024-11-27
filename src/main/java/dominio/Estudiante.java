package dominio;

public class Estudiante extends Usuario {
    public Estudiante(String matricula, String contrasena) { // Ajuste de nombres
        super(matricula, contrasena);
    }

    @Override
    public String getTipo() { // Cambiado de getRole a getTipo
        return "estudiante"; // Coincide con el valor en el JSON
    }
}
