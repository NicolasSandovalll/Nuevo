package dominio;

public abstract class Usuario {
    private String matricula; // Cambiado de registrationNumber
    private String contrasena; // Cambiado de password

    public Usuario(String matricula, String contrasena) {
        if (matricula == null || matricula.isEmpty()) {
            throw new IllegalArgumentException("La matrícula no puede ser nula o vacía.");
        }
        if (contrasena == null || contrasena.isEmpty()) {
            throw new IllegalArgumentException("La contraseña no puede ser nula o vacía.");
        }
        this.matricula = matricula;
        this.contrasena = contrasena;
    }

    public String getMatricula() {
        return matricula;
    }

    public String getContrasena() {
        return contrasena;
    }

    public abstract String getTipo(); // Cambiado de getRole
}