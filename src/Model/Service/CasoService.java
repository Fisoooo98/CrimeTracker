package Model.Service;

import Model.DAO.CasoDAO;
import Model.Entities.Caso;
import Model.Entities.Dificultad;
import Model.Entities.Estado;

import java.util.List;

public class CasoService {

    private final CasoDAO casoDAO = new CasoDAO();

    public void actualizarEstadoCaso(Estado estado,int id_caso) {
        casoDAO.actualizarEstadoCaso(id_caso,estado);
    }

    public void valorarCaso(int id_caso,boolean valor) {
        casoDAO.marcarAciertoCaso(id_caso,valor);
    }

    public void seleccionarCasoActual(int id_caso) {
        //Ponemos todos los casos en false.
         casoDAO.desactivarTodosLosCasos();

        //Ponemos el idcaso en true
        casoDAO.actualizarCasoActual(id_caso,true);
    }

    public Caso obtenerCasoActivo(){
        return casoDAO.obtenerCasoActivo();
    }


    public void actualizarDificultad(int id_caso, Dificultad dificultad) {
        casoDAO.actualizarDificultad(id_caso,dificultad);
    }



    public List<Caso> obtenerCasosporCorrecto(boolean correcto) {
        return casoDAO.obtenerCasosporCorrecto(correcto);
    }

    public List<Caso> obtenerCasosporEstado(Estado estado) {
        return casoDAO.obtenerCasosporEstado(estado);
    }


    public Caso obtenerCasoPorId(int id_caso) {
        return casoDAO.obtenerCasoPorId(id_caso);
    }
}
