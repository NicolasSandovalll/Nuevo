package servicios;

import dominio.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Session {
    private static final Logger logger = LoggerFactory.getLogger(Session.class);
    private static Usuario usuario;

    public static synchronized Usuario getUsuario() {
        return usuario;
    }

    public static synchronized void setUsuario(Usuario usuario) {
        if (usuario == null) {
            logger.warn("Intento de establecer una sesión con un usuario nulo.");
            throw new IllegalArgumentException("El usuario no puede ser nulo.");
        }
        Session.usuario = usuario;
        logger.info("Sesión iniciada para el usuario con matrícula: {}", usuario.getMatricula());
    }

    public static synchronized void clearSession() {
        if (usuario != null) {
            logger.info("Sesión cerrada para el usuario con matrícula: {}", usuario.getMatricula());
        } else {
            logger.warn("Intento de cerrar sesión sin un usuario autenticado.");
        }
        usuario = null;
    }
}
