package Core;

import GUI.MainWindow;

public class Loader {
    private static MainWindow mainWindow;

    public Loader(){
        mainWindow = new MainWindow();
        mainWindow.setVisible(true);
    }

    public static MainWindow getMainWindow() {
        return mainWindow;
    }
}
