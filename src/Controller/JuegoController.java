package Controller;

import Model.DAO.InventarioDAO;
import Model.DAO.SospechosoDAO;
import Model.Entities.Caso;
import Model.Entities.Estado;
import Model.Entities.ResultadoPregunta;
import Model.Service.CasoService;
import Model.Service.JuegoService;
import View.InvestigacionUI.VentanaCaso;
import View.InvestigacionUI.VentanaInterrogar;
import View.InvestigacionUI.VentanaNotas;
import View.InvestigacionUI.VentanaPistas;

import javax.swing.*;
import java.util.List;

public class JuegoController {
    private JFrame ventanaActual;
    private Caso casoActual;
    private final CasoService casoService = new CasoService();
    private final JuegoService juegoService = new JuegoService();
    private final SospechosoDAO sospechosoDAO = new SospechosoDAO();
    private final InventarioDAO inventarioDAO = new InventarioDAO();
    public  JuegoController() {
        this.casoActual = casoService.obtenerCasoActivo();
    }
    public void AbrirPistas(VentanaCaso ventanaCaso){
        if (ventanaActual != null) {
            ventanaActual.dispose();
        }

        ventanaActual = new VentanaPistas(ventanaCaso);
        ventanaActual.setLocationRelativeTo(null);
        ventanaActual.setVisible(true);
    }

    public void AbrirInterrogatorio(VentanaCaso ventanaCaso){
        if (ventanaActual != null) {
            ventanaActual.dispose();
        }

        ventanaActual = new VentanaInterrogar(casoActual,ventanaCaso,this);
        ventanaActual.setLocationRelativeTo(null);
        ventanaActual.setVisible(true);
    }

    public void AbrirNotas(){
        if (ventanaActual != null) {
            ventanaActual.dispose();
        }

        ventanaActual = new VentanaNotas(casoActual);
        ventanaActual.setLocationRelativeTo(null);
        ventanaActual.setVisible(true);
    }

    public void guardarNota(String texto,Caso casoActual){
        juegoService.tomarNota(texto,casoActual.getId_caso());
    }

    public void interrogar(String pregunta,int id_sospechoso,VentanaInterrogar ventanaInterrogar){
        Caso caso = casoService.obtenerCasoActivo();
        if (caso.getEstado() != Estado.RESUELTO){
            //Logica
            ResultadoPregunta rs = juegoService.preguntarSospechoso(pregunta,id_sospechoso,caso.getId_caso());
            //Actualizar texto
            ventanaInterrogar.actualizarTextoDialogo(rs.getNombreSospechoso() + ": " + rs.getRespuesta());


            //Lanzar ventana de que has obtenido una pista nueva
            if(rs.isPistaObtenida()){
                ventanaInterrogar.mostrarDialogo("Has obtenido una pista");
            }
        }else{
            ventanaInterrogar.mostrarDialogo("No puedes hacer eso el caso ya esta cerrado");
        }
    }

    public void acusar(int id_sospechoso,VentanaInterrogar ventanaInterrogar){
        Caso caso = casoService.obtenerCasoActivo();
        if (caso.getEstado() != Estado.RESUELTO){
            boolean esculpable = juegoService.acusarSospechoso(id_sospechoso,caso.getId_caso());
            String nombre = sospechosoDAO.obtenerSospechosoPorId(id_sospechoso).getNombre();
            casoService.actualizarEstadoCaso(Estado.RESUELTO,caso.getId_caso());
            if (esculpable) {
                ventanaInterrogar.actualizarTextoDialogo("Ha acusado a " +  nombre + ".El si era el culpable !Has acertado el caso.");
                casoService.valorarCaso(caso.getId_caso(),true);
            }else{
                ventanaInterrogar.actualizarTextoDialogo("Ha acusado a " +  nombre + ".El no era el culpable, caso fallido.");
                casoService.valorarCaso(caso.getId_caso(),false);
            }
        }else{
            ventanaInterrogar.mostrarDialogo("No puedes hacer eso el caso ya esta cerrado");
        }
    }
}
