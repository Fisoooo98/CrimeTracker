package Model.Service;

import Model.DAO.CasoDAO;
import Model.DAO.InventarioDAO;
import Model.DAO.InvestigacionDAO;
import Model.DAO.SospechosoDAO;
import Model.Entities.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class JuegoService {
    private final InvestigacionDAO investigacionDAO = new InvestigacionDAO();
    private final InventarioDAO inventarioDAO = new InventarioDAO();
    private final SospechosoDAO sospechosoDAO =  new SospechosoDAO();
    private final CasoDAO casoDAO = new CasoDAO();
    Random random = new Random();

    /**
     * Funcion para obtener las preguntas del sospechoso que le puedes hacer
     * @param id_sospechoso sospechoso que quieres obtener las preguntas
     * @param id_caso el caso actual donde está el sospechoso
     * @return una lista de preguntas que devuelve el sospechoso
     */
    public List<String> InterrogarSospechoso(int id_sospechoso,int id_caso) {
        //Definimos las estructuras que vamos a necesitar
        List<String> preguntas = new ArrayList<>();
        List<Integer> pistas = inventarioDAO.leerPistas(id_caso);
        HashMap<String, String> preguntasYrespuestas = sospechosoDAO.obtenerPreguntasYrespuestas(id_sospechoso,pistas);
        //Mostramos las preguntas
        for  (String pregunta : preguntasYrespuestas.keySet()) {
            preguntas.add(pregunta);
        }
        return preguntas;
    }


    /**
     * Funcion para obtener la respuesta del sospechoso y si esta tiene una pista o obtienes una evidencia
     * @param textoPregunta pregunta que le haces al sospechoso
     * @param id_sospechoso id del sospechoso interrogado
     * @param id_caso el caso donde está el sospechoso
     * @return una clase que contiene varios datos sobre la respuesta(Si devuelve pista, el texto de la respuesta, si devuelve evidencia)
     */
    public ResultadoPregunta preguntarSospechoso(String textoPregunta, int id_sospechoso, int id_caso) {
        //Definimos variables
        boolean nuevaPista = false;
        String textoPista = null;
        List<Integer> pistasObtenidas = inventarioDAO.leerPistas(id_caso);
        //Vemos si nos ha dado una pista nueva
        int idpregunta = investigacionDAO.obtenerIdPregunta(textoPregunta);
        Pista pista = investigacionDAO.obtenerPistaPorPregunta(id_caso,idpregunta,id_sospechoso);
        //Si desbloqueamos una nueva pista la obtenemos
        if (pista != null) {
            //Si ya contenemos la pista no la guardamos
            if (pistasObtenidas.contains(pista.getId_pista())) {
                textoPista = "";
            }else{
                inventarioDAO.obtenerPista(pista.getId_pista(),id_caso);
                nuevaPista = true;
                textoPista = pista.getTexto();
            }

        }
        //Leemos las pistas
        List<Integer> pistas = inventarioDAO.leerPistas(id_caso);
        //Obtenemos la respuesta según las pistas dadas
        HashMap<String, String> preguntasYrespuestas = sospechosoDAO.obtenerPreguntasYrespuestas(id_sospechoso,pistas);
        String respuestaSospechoso = preguntasYrespuestas.get(textoPregunta);
        String nombre_sospechoso = sospechosoDAO.obtenerSospechosoPorId(id_sospechoso).getNombre();
        boolean evidenciaObtenida = desbloquearEvidencia(id_caso);
        //Retornamos el resultado adquirido
        return new ResultadoPregunta(respuestaSospechoso, nuevaPista,textoPista,nombre_sospechoso,evidenciaObtenida);
    }

    /**
     * Metodo utilizado en preguntarSospechoso que lanza un prob para obtener una evidencia y la guarda en el caso
     * @param idcaso caso donde se guarda la evidencia
     * @return False si no ha lanzado una evidencia,True si la ha lanzado
     */
    public boolean desbloquearEvidencia(int idcaso){
        //Meter probabilidad
        double probabilidad = (double) casoDAO.obtenerCasoPorId(idcaso).getProbEvidencia() / 100;
        //Meter todas las preguntas no desbloqueadas en una lista
        List<Evidencia> evidenciasNoDesbloqueadas = investigacionDAO.obtenerEvidenciasPorCasoNoDesbloqueadas(idcaso);
        if (random.nextDouble() < probabilidad && !evidenciasNoDesbloqueadas.isEmpty()) {
            //Desbloquear una evidencia random que este en la lista
            int indexEvidencia = random.nextInt(0, evidenciasNoDesbloqueadas.size());
            int id_evidencia = evidenciasNoDesbloqueadas.get(indexEvidencia).getId_evidencia();
            //Metemos esta evidencia como true
            investigacionDAO.actualizarEvidencia(id_evidencia,idcaso,true);

            return true;
        }else{
            return false;
        }
    }

    /**
     * Devuelve True si el sospechoso del caso era culpable,False si no lo era
     * @param id_sospechoso sospechoso acusado
     * @param id_caso id del caso actual para ver cual es el culpable
     * @return
     */
    public boolean acusarSospechoso(int id_sospechoso, int id_caso) {
        Sospechoso culpable = sospechosoDAO.obtenerCulpablePorCaso(id_caso);
        Sospechoso sospechosoeleguido = sospechosoDAO.obtenerSospechosoPorId(id_sospechoso);
        return sospechosoeleguido.getNombre().equals(culpable.getNombre());
    }

    /**
     * Guarda la nota apuntada por el usuario
     * @param nota el texto actual de la nota apuntada por el usuario
     * @param id_caso el caso donde se situa la nota
     */
    public void tomarNota(String nota,int id_caso) {
        investigacionDAO.actualizarNota(nota,id_caso);
    }

    /**
     * Devuelve el contenido de la nota que hay en el caso
     * @param id_caso caso donde se situa la nota
     * @return el contenido de la nota
     */
    public String leerNota(int id_caso) {
        return investigacionDAO.leerNota(id_caso);
    }

    /**
     * Actualiza el contador de preguntas del caso
     * @param id_caso el caso donde está en contador de preguntas
     * @param contpreguntas el contador de preguntas que tiene el caso
     */
    public void actualizarContadorPreguntas(int id_caso,int contpreguntas) {
        casoDAO.actualizarPreguntasRestantes(id_caso,contpreguntas);
    }
}
