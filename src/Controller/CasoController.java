package Controller;

import Model.Entities.Caso;
import Model.Entities.Dificultad;
import Model.Entities.Estado;
import Model.Service.CasoService;
import View.VerCasos;

public class CasoController {

    CasoService casoService;
    private Caso casoActual;
    public CasoController() {
        this.casoService = new CasoService();
    }

    //Hace que puedas selecionar el caso Actual
    public void seleccionarCaso(int id_caso, VerCasos  verCasos) {
       if (casoService.obtenerCasoPorId(id_caso).getDificultad() == Dificultad.NOSELECCIONADO){
           verCasos.panelDificultad(id_caso);
       }

        casoService.seleccionarCasoActual(id_caso);
        casoService.actualizarEstadoCaso(Estado.PENDIENTE, id_caso);
        this.casoActual = casoService.obtenerCasoPorId(id_caso);



    }
    //Carga el caso activo
    public Caso cargarCasoActual(){
        return casoService.obtenerCasoActivo();
    }

    public Caso getCasoActual() {
        return casoActual;
    }
}
