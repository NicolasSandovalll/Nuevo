package launcher;

import servicios.WindowManager;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            WindowManager windowManager = new WindowManager();
            windowManager.showLoginScreen();
        });
    }
}
