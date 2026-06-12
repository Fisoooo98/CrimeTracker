package Model.Service;

import Model.DAO.CasoDAO;
import Model.Entities.*;
import org.w3c.dom.ls.LSInput;

import java.util.ArrayList;
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

    //Calcular estadisticas del perfil
    public ResultadoPerfil obtenerPerfil() {
        //Variables
        List<Caso> casosFaciles = casoDAO.obtenerCasosPorDificultad(Dificultad.FACIL);
        List<Caso> casosNormales = casoDAO.obtenerCasosPorDificultad(Dificultad.NORMAL);
        List<Caso> casosDificiles = casoDAO.obtenerCasosPorDificultad(Dificultad.DIFICIL);
        int contadorTotalFaciles = casosFaciles.size();
        int contadorTotalNormales = casosNormales.size();
        int contadorTotalDificiles = casosDificiles.size();
        int contadorResueltosFaciles = 0;
        int contadorResueltosNormales = 0;
        int contadorResueltosDificiles = 0;
        int puntuacion = 0;
        Tier tierperfil = Tier.SINTIER;
        for  (Caso c : casosFaciles) {
            if (c.isCorrecto()){
                contadorResueltosFaciles++;
                puntuacion+= Config.puntuacionFacil;
            }
        }
        for  (Caso c : casosNormales) {
            if (c.isCorrecto()){
                contadorResueltosNormales++;
                puntuacion+=Config.puntuacionNormal;
            }
        }
        for  (Caso c : casosDificiles) {
            if (c.isCorrecto()){
                contadorResueltosDificiles++;
                puntuacion+=Config.puntuacionDificil;
            }
        }
        //Tier Novato - 100 - 600
        //Tier Profesional 600 - 1000
        //Tier Experto 1000+
       if (puntuacion>Config.puntuacionTierNovato && puntuacion<Config.puntuacionTierPro){
           tierperfil = Tier.NOVATO;
       } else if (puntuacion>=Config.puntuacionTierPro && puntuacion<Config.puntuacionTierExperto) {
           tierperfil = Tier.PROFESIONAL;
       } else if (puntuacion>=Config.puntuacionTierExperto) {
           tierperfil = Tier.EXPERTO;
       }else {
           tierperfil = Tier.SINTIER;
       }

       return new ResultadoPerfil(contadorResueltosDificiles,contadorResueltosFaciles,contadorResueltosNormales,puntuacion,
               tierperfil,contadorTotalDificiles,contadorTotalNormales,contadorTotalFaciles);
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
