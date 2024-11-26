package servicios;

import datos.LogiaHandler;
import datos.ReservaHandler;
import guis.*;

import javax.swing.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WindowManager {
    private static final Logger logger = LoggerFactory.getLogger(WindowManager.class);
    private JFrame currentWindow;

    // Instancias compartidas
    private final LogiaHandler logiaHandler = new LogiaHandler("src/main/resources/logias.json");
    private final ReservaHandler reservaHandler = new ReservaHandler("src/main/resources/reservas.json", logiaHandler);
    private final LogiaService logiaService = new LogiaService(logiaHandler, reservaHandler);

    private final ReservaService reservaService = new ReservaService(reservaHandler);

    // Método genérico para mostrar ventanas
    private void showWindow(JFrame newWindow) {
        if (currentWindow != null) {
            logger.info("Cerrando ventana actual: {}", currentWindow.getTitle());
            currentWindow.dispose();
        }
        currentWindow = newWindow;
        logger.info("Abriendo nueva ventana: {}", newWindow.getTitle());
        currentWindow.setVisible(true);
    }

    public void showLoginScreen() {
        showWindow(new LoginScreen(this));
    }

    public void showStudentMenu() {
        showWindow(new StudentMenuScreen(this));
    }

    public void showRegisterScreen() {
        showWindow(new RegisterScreen(this));
    }

    public void showStudentReserveScreen() {
        showWindow(new StudentReserveScreen(logiaService, reservaService, this));
    }

    public void showStudentReservationsScreen() {
        showWindow(new StudentReservationsScreen(this));
    }

    public void showAdminMenu() {
        showWindow(new AdminLogiaScreen(logiaService, this)); // Redirige correctamente al menú del administrador
    }
}
