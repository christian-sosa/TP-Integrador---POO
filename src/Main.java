import ui.GestorEventosGUI;

public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            GestorEventosGUI aplicacion = new GestorEventosGUI();
            aplicacion.iniciar();
        });
    }
}