package Core;

import GUI.MainWindow;

import static javax.swing.SwingUtilities.invokeLater;

public class Loader {
    private static MainWindow mainWindow;

    public Loader(){
        invokeLater(() -> mainWindow = new MainWindow());
    }

    public static MainWindow getMainWindow() {
        return mainWindow;
    }
}
