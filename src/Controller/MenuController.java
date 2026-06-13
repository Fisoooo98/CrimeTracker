package Controller;

import Model.Entities.Caso;
import Model.Entities.ResultadoPerfil;
import Model.Service.CasoService;
import Model.Service.JuegoService;
import View.InvestigacionUI.VentanaCaso;
import View.ListarCasos;
import View.MenuPrincipal;
import View.VerCasos;
import View.VistaPerfil;

import javax.swing.*;

public class MenuController {
    private JFrame ventanaActual;
    private final CasoService casoService = new CasoService();

    /**
     * Funciona para acceder a la ventana de interrogatorio y gestiona si puedes entrar o no
     * Cierra la ventana activa actual si existe para liberar recursos.
     * @param menuPrincipal sirve para mostrar una ventana emergene cuando no hay ningun caso seleccionado
     */
    public void accederAlCaso(MenuPrincipal menuPrincipal) {
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

    /**
     * Funciona para acceder a la ventana casos
     * Cierra la ventana activa actual si existe para liberar recursos.
     */
    public void accederAVentanaCasos(){
        if (ventanaActual != null) {
            ventanaActual.dispose();
        }

        ventanaActual = new VerCasos();
        ventanaActual.setLocationRelativeTo(null);
        ventanaActual.setVisible(true);
    }

    /**
     * Funciona para cargar y acceder al Perfil
     * Cierra la ventana activa actual si existe para liberar recursos.
     */
    public void accederAlPerfil(){
        if (ventanaActual != null) {
            ventanaActual.dispose();
        }
        ResultadoPerfil resultadoPerfil = casoService.obtenerPerfil();
        ventanaActual = new VistaPerfil(resultadoPerfil);
        ventanaActual.setLocationRelativeTo(null);
        ventanaActual.setVisible(true);
    }
    /**
     * Funciona para acceder a la ventana Listar Casos
     * Cierra la ventana activa actual si existe para liberar recursos.
     */
    public void accederAListarCasos(){
        if (ventanaActual != null) {
            ventanaActual.dispose();
        }

        ventanaActual = new ListarCasos();
        ventanaActual.setLocationRelativeTo(null);
        ventanaActual.setVisible(true);
    }
}
