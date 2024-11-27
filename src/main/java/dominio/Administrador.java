package dominio;

public class Administrador extends Usuario {

    public Administrador(String matricula, String contrasena) { // Ajuste de nombres
        super(matricula, contrasena);
    }

    @Override
    public String getTipo() { // Cambiado de getRole a getTipo
        return "administrador"; // Coincide con el valor en el JSON
    }
}
