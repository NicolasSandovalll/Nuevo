package datos;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ManejadorDetallesLogias {
    private final String rutaArchivo;

    public ManejadorDetallesLogias(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }

    public List<JSONObject> cargarDetallesLogias() {
        List<JSONObject> detallesLogias = new ArrayList<>();
        try (FileReader reader = new FileReader(rutaArchivo)) {
            StringBuilder contenido = new StringBuilder();
            int i;
            while ((i = reader.read()) != -1) {
                contenido.append((char) i);
            }

            // Convertir el contenido a JSONArray
            JSONArray jsonArray = new JSONArray(contenido.toString());

            for (int j = 0; j < jsonArray.length(); j++) {
                detallesLogias.add(jsonArray.getJSONObject(j));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return detallesLogias;
    }
}