package servicios;

import datos.JSONHandler;
import dominio.Administrador;
import dominio.Estudiante;
import dominio.Usuario;
import org.json.JSONObject;

public class AuthService {
    private JSONHandler jsonHandler;

    public AuthService(String filePath) {
        this.jsonHandler = new JSONHandler(filePath);
    }

    public Usuario autenticarUsuario(String matricula, String contrasena) {
        if (jsonHandler.verificarCredenciales(matricula, contrasena)) {
            String tipo = jsonHandler.obtenerTipoUsuario(matricula);
            if ("administrador".equalsIgnoreCase(tipo)) { // Ajuste para coincidir con el archivo JSON
                return new Administrador(matricula, contrasena);
            } else if ("estudiante".equalsIgnoreCase(tipo)) { // Aseguramos coincidencia con "estudiante"
                return new Estudiante(matricula, contrasena);
            }
        }
        return null; // Si no hay coincidencia, retornar null
    }

    public void registrarUsuario(String matricula, String contrasena, String tipo) {
        jsonHandler.agregarUsuario(new JSONObject()
                .put("matricula", matricula)
                .put("contrasena", contrasena)
                .put("tipo", tipo));
    }
}
