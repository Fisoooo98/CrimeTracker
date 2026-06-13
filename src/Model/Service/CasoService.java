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

    /**
     * Desactiva todos los casos que hay y activa el caso que le pasas por parametro
     * @param id_caso caso que quieres poner en activo
     */
    public void seleccionarCasoActual(int id_caso) {
        //Ponemos todos los casos en false.
         casoDAO.desactivarTodosLosCasos();

        //Ponemos el idcaso en true
        casoDAO.actualizarCasoActual(id_caso,true);
    }


    /**
     * Calcula a traves de los datos de la BD las estadisticas del usuario.
     * @return devuelve todos los datos que se necesita mostrar en la interfaz sobre el usuario
     */
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

    /**
     * Recupera el caso que se encuentra actualmente activo en el sistema
     * @return caso activo del sistema
     */
    public Caso obtenerCasoActivo(){
        return casoDAO.obtenerCasoActivo();
    }


    /**
     * Actualiza el nivel de dificultad de un caso específico en la base de datos.
     * @param id_caso El identificador único del caso.
     * @param dificultad La nueva dificultad que se le va a asignar al caso.
     */
    public void actualizarDificultad(int id_caso, Dificultad dificultad) {
        casoDAO.actualizarDificultad(id_caso, dificultad);
    }

    /**
     * Obtiene una lista de casos filtrados según si fueron resueltos correctamente o no.
     * @param correcto True si se quieren obtener los casos acertados, false para los fallados.
     * @return Una lista con los casos que coinciden con el resultado de la resolución.
     */
    public List<Caso> obtenerCasosporCorrecto(boolean correcto) {
        return casoDAO.obtenerCasosporCorrecto(correcto);
    }

    /**
     * Obtiene una lista de todos los casos que se encuentran en un estado específico.
     * @param estado El estado del caso a buscar (Resuelto, Pendiente o No Resuelto).
     * @return Una lista con los casos que se encuentran en dicho estado.
     */
    public List<Caso> obtenerCasosporEstado(Estado estado) {
        return casoDAO.obtenerCasosporEstado(estado);
    }

    /**
     * Recupera los datos completos de un caso específico utilizando su identificador único.
     * @param id_caso El identificador único del caso que se quiere buscar.
     * @return El caso correspondiente al identificador proporcionado.
     */
    public Caso obtenerCasoPorId(int id_caso) {
        return casoDAO.obtenerCasoPorId(id_caso);
    }
}
