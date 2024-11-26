package datos;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class JSONHandler {
    private static final Logger logger = LoggerFactory.getLogger(JSONHandler.class);
    private String filePath;
    private JSONArray jsonData;

    public JSONHandler(String filePath) {
        this.filePath = filePath;
        this.jsonData = new JSONArray();
        this.loadJSON();
    }

    private void loadJSON() {
        try (FileReader reader = new FileReader(filePath)) {
            StringBuilder stringBuilder = new StringBuilder();
            int i;
            while ((i = reader.read()) != -1) {
                stringBuilder.append((char) i);
            }
            this.jsonData = new JSONArray(stringBuilder.toString());
        } catch (IOException e) {
            logger.error("Error al cargar el archivo JSON: {}", e.getMessage(), e);
        }
    }

    // Comparar credenciales (matrícula y contraseña) sin espacios y con sensibilidad a mayúsculas/minúsculas
    public boolean verificarCredenciales(String matricula, String contrasena) {
        for (int i = 0; i < jsonData.length(); i++) {
            JSONObject user = jsonData.getJSONObject(i);
            String storedMatricula = user.getString("matricula").trim(); // Eliminar espacios innecesarios
            String storedContrasena = user.getString("contrasena").trim(); // Eliminar espacios innecesarios
            if (storedMatricula.equals(matricula) && storedContrasena.equals(contrasena)) {
                return true;
            }
        }
        return false;
    }

    // Obtener el tipo de usuario por matrícula
    public String obtenerTipoUsuario(String matricula) {
        for (int i = 0; i < jsonData.length(); i++) {
            JSONObject user = jsonData.getJSONObject(i);
            if (user.getString("matricula").equals(matricula)) {
                return user.getString("tipo");
            }
        }
        return null;
    }

    // Agregar un nuevo usuario al archivo JSON
    public void agregarUsuario(JSONObject nuevoUsuario) {
        jsonData.put(nuevoUsuario);
        this.saveJSON();
    }

    // Guardar el archivo JSON después de cualquier modificación
    private void saveJSON() {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(jsonData.toString(4));  // Formato bonito con 4 espacios de indentación
        } catch (IOException e) {
            logger.error("Error al guardar el archivo JSON: {}", e.getMessage(), e);
        }
    }
}
