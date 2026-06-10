package Main;

import Model.DAO.DBConnection;
import View.MenuPrincipal;

public class IniciarJuego {


    static void main() {
        DBConnection db = new DBConnection();
        db.iniciarBaseDeDatos();
        java.awt.EventQueue.invokeLater(() -> {
            new MenuPrincipal();
        });
    }
}
