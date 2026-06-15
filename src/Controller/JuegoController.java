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

    /**
     * Inicializa el controlador de InvestigacionUI
     */
    public  JuegoController() {
        this.casoActual = casoService.obtenerCasoActivo();
    }

    /**
     * Sirve para abrir la ventana pistas con las evidencias y pistas obtenidas en el caso
     * Cierra la ventana activa actual si existe para liberar recursos.
     * @param ventanaCaso ventana para obtener las pistas del caso que hay activo
     */
    public void AbrirPistas(VentanaCaso ventanaCaso){
        if (ventanaActual != null) {
            ventanaActual.dispose();
        }

        ventanaActual = new VentanaPistas(ventanaCaso);
        ventanaActual.setLocationRelativeTo(null);
        ventanaActual.setVisible(true);
    }

    /**
     * Funciona para abrir la ventana de interrogatorio
     * Cierra la ventana activa actual si existe para liberar recursos.
     * @param ventanaCaso ventana para obtener las preguntas y sospechosos del caso que hay activo
     */
    public void AbrirInterrogatorio(VentanaCaso ventanaCaso){
        if (ventanaActual != null) {
            ventanaActual.dispose();
        }

        ventanaActual = new VentanaInterrogar(casoActual,ventanaCaso,this);
        ventanaActual.setLocationRelativeTo(null);
        ventanaActual.setVisible(true);
    }

    /**
     * Funciona para abrir la ventana notas
     * Cierra la ventana activa actual si existe para liberar recursos.
     */
    public void AbrirNotas(){
        if (ventanaActual != null) {
            ventanaActual.dispose();
        }

        ventanaActual = new VentanaNotas(casoActual);
        ventanaActual.setLocationRelativeTo(null);
        ventanaActual.setVisible(true);
    }

    /**
     * Es el controlador del boton guardar en la ventana VerNotas
     * Sirve para guardar las anotaciones que ha escrito el usuario en cada caso
     * @param texto el texto que hay dentro de la nota
     * @param casoActual el caso donde se guardan las notas
     */
    public void guardarNota(String texto,Caso casoActual){
        juegoService.tomarNota(texto,casoActual.getId_caso());
    }

    /**
     * Funcion donde le das una pregunta a un sospechoso y obtienes una respuesta
     * Este gestiona las ventanas emergentes que salen cuando no obtienes una evidencia o pista
     * También te limita cuando ya no tienes más preguntas disponibles o el caso eta cerrado dejandote sin poder preguntar.
     * @param pregunta el texto de la pregunta que le has hecho al sospechoso
     * @param id_sospechoso el sospechoso al cual estás interrogando
     * @param ventanaInterrogar sirve para mostrar ventanas emergentes y para poder actualizar el diálogo de la interfaz.
     */
    public void interrogar(String pregunta,int id_sospechoso,VentanaInterrogar ventanaInterrogar){
        Caso caso = casoService.obtenerCasoActivo();
        if (caso.getEstado() != Estado.RESUELTO){
            if (caso.getContador_preguntas() >= 0){
                //Logica
                ResultadoPregunta rs = juegoService.preguntarSospechoso(pregunta,id_sospechoso,caso.getId_caso());

                //Actualizar texto
                ventanaInterrogar.actualizarTextoDialogo(rs.getNombreSospechoso() + ": " + rs.getRespuesta());

                //Lanzar ventana de que has obtenido una pista nueva
                if(rs.isPistaObtenida()){
                    ventanaInterrogar.mostrarDialogo("Has obtenido una pista");
                }
                //Lanzar una ventana de que ha obtenido una evidencia
                if (rs.isEvidenciaObtenida()){
                    ventanaInterrogar.mostrarDialogo("Has obtenido una evidencia");
                }

                //Actualizar el contador
                System.out.println("Actualizando contador preguntas actuales : " + caso.getContador_preguntas());
                juegoService.actualizarContadorPreguntas(caso.getId_caso(),caso.getContador_preguntas() - 1);
                caso.setContador_preguntas(caso.getContador_preguntas()-1);
                System.out.println("Contador Actual: " + caso.getContador_preguntas());
                ventanaInterrogar.actualizarContador(caso.getContador_preguntas());
            }else{
                ventanaInterrogar.mostrarDialogo("No tienes mas preguntas");
            }
        }else{
            ventanaInterrogar.mostrarDialogo("No puedes hacer eso el caso ya esta cerrado");
        }
    }

    /**
     *  Funcion que sirve para acusar a un sospechos y te muestra si es culpable o no
     * @param id_sospechoso sopechoso acusado
     * @param ventanaInterrogar sirve para mostrar a la interfaz si era el impostor o no
     */
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
