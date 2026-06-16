package Controller;

import Model.Entities.Dificultad;
import Model.Entities.Estado;
import Model.Service.CasoService;
import View.VerCasos;

public class CasoController {

    CasoService casoService;

    /**
     * Inicializa el controlador de VerCasos
     */
    public CasoController() {
        this.casoService = new CasoService();
    }

    /**
     * Funcion para seleccionar un caso, ponerlo en activo y gestion de dificultad
     * Si no hay dificultad seleccionada, despliega el JDialog correspondiente
     * @param id_caso caso seleccionado por el usuario
     * @param verCasos ventana de VerCasos para utilizar dialogos emergentes
     */
    public void seleccionarCaso(int id_caso, VerCasos  verCasos) {
       if (casoService.obtenerCasoPorId(id_caso).getDificultad() == Dificultad.NOSELECCIONADO){
           verCasos.panelDificultad(id_caso);
       }

        casoService.seleccionarCasoActual(id_caso);
        casoService.actualizarEstadoCaso(Estado.PENDIENTE, id_caso);
    }
}
