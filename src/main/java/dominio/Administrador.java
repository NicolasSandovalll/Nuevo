package dominio;

public class Administrador extends Usuario {
    public Administrador(String matricula, String contrasena) {
        super(matricula, contrasena);
    }

    @Override
    public String getTipo() {
        return "Administrador";
    }
}
