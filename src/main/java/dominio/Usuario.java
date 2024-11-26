package dominio;

public abstract class Usuario {
    private String matricula;
    private String contrasena;

    public Usuario(String matricula, String contrasena) {
        this.matricula = matricula;
        this.contrasena = contrasena;
    }

    public String getMatricula() {
        return matricula;
    }

    public String getContrasena() {
        return contrasena;
    }

    public abstract String getTipo();
}
