package Controller;

import Model.Entities.Caso;
import Model.Service.CasoService;
import Model.Service.JuegoService;
import View.InvestigacionUI.VentanaCaso;
import View.ListarCasos;
import View.MenuPrincipal;
import View.VerCasos;

import javax.swing.*;

public class MenuController {
    private JFrame ventanaActual;
    private final CasoService casoService = new CasoService();

    public void accederAlCaso(MenuPrincipal menuPrincipal) {
        System.out.println("Acceder al Caso");
        Caso casoActivo = casoService.obtenerCasoActivo();
        if (casoActivo != null) {
            if (ventanaActual != null) {
                ventanaActual.dispose();
            }

            if (casoService.obtenerCasoActivo() == null){
                System.out.println("No hay ningun caso activo");
            }else{
                ventanaActual = new VentanaCaso(casoService.obtenerCasoActivo());
                ventanaActual.setLocationRelativeTo(null);
                ventanaActual.setVisible(true);
            }
        }else{
            menuPrincipal.mostrarDialogo("Tienes que seleccionar un caso");
        }
    }

    public void accederAVentanaCasos(){
        if (ventanaActual != null) {
            ventanaActual.dispose();
        }

        ventanaActual = new VerCasos();
        ventanaActual.setLocationRelativeTo(null);
        ventanaActual.setVisible(true);
    }

    public void accederAListarCasos(){
        if (ventanaActual != null) {
            ventanaActual.dispose();
        }

        ventanaActual = new ListarCasos();
        ventanaActual.setLocationRelativeTo(null);
        ventanaActual.setVisible(true);
    }
}
