package servicios;

import datos.ManejadorJson;
import dominio.Administrador;
import dominio.Estudiante;
import dominio.Usuario;
import org.json.JSONObject;

public class ServicioAutenticacion {
    private ManejadorJson jsonHandler;

    public ServicioAutenticacion(String filePath) {
        this.jsonHandler = new ManejadorJson(filePath);
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
