package datos;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ManejadorJson {
    private static final Logger logger = LoggerFactory.getLogger(ManejadorJson.class);
    private final String rutaArchivo;
    private JSONArray datosJson;

    public ManejadorJson(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
        this.datosJson = new JSONArray();
        this.cargarJson();
    }

    private void cargarJson() {
        try (FileReader reader = new FileReader(rutaArchivo)) {
            StringBuilder contenido = new StringBuilder();
            int i;
            while ((i = reader.read()) != -1) {
                contenido.append((char) i);
            }
            this.datosJson = new JSONArray(contenido.toString());
        } catch (IOException e) {
            logger.error("Error al cargar el archivo JSON: {}", e.getMessage(), e);
        }
    }

    // Verificar credenciales (matrícula y contraseña) distinguiendo mayúsculas y eliminando espacios
    public boolean verificarCredenciales(String matricula, String contrasena) {
        for (int i = 0; i < datosJson.length(); i++) {
            JSONObject usuario = datosJson.getJSONObject(i);
            String matriculaGuardada = usuario.getString("matricula").trim();
            String contrasenaGuardada = usuario.getString("contrasena").trim();
            if (matriculaGuardada.equals(matricula) && contrasenaGuardada.equals(contrasena)) {
                return true;
            }
        }
        return false;
    }

    // Obtener el tipo de usuario por matrícula
    public String obtenerTipoUsuario(String matricula) {
        for (int i = 0; i < datosJson.length(); i++) {
            JSONObject usuario = datosJson.getJSONObject(i);
            if (usuario.getString("matricula").equals(matricula)) {
                return usuario.getString("tipo");
            }
        }
        return null;
    }

    // Agregar un nuevo usuario al archivo JSON
    public void agregarUsuario(JSONObject nuevoUsuario) {
        datosJson.put(nuevoUsuario);
        this.guardarJson();
    }

    // Guardar el archivo JSON después de cualquier modificación
    private void guardarJson() {
        try (FileWriter writer = new FileWriter(rutaArchivo)) {
            writer.write(datosJson.toString(4)); // Imprimir con formato de 4 espacios de indentación
        } catch (IOException e) {
            logger.error("Error al guardar el archivo JSON: {}", e.getMessage(), e);
        }
    }
}
