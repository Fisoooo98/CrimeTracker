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

    //Obtienes todas las preguntas de un sospechoso
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

    //Obtienes la respuesta del sospechsoso
    public ResultadoPregunta preguntarSospechoso(String textoPregunta, int id_sospechoso, int id_caso) {
        //Definimos variables
        boolean nuevaPista = false;
        String textoPista = null;
        List<Integer> pistasObtenidas = inventarioDAO.leerPistas(id_caso);
        //Vemos si nos ha dado una pista nueva
        int idpregunta = investigacionDAO.obtenerIdPregunta(textoPregunta);
        Pista pista = investigacionDAO.obtenerPistaPorPregunta(id_caso,idpregunta,id_sospechoso);
        //Si desbloqueamos una nueva pista la obtenemos
        int idPista = -1;
        if (pista != null) {
            //Si ya contenemos la pista no la guardamos
            if (pistasObtenidas.contains(pista.getId_pista())) {
                textoPista = "";
            }else{
                inventarioDAO.obtenerPista(pista.getId_pista(),id_caso);
                nuevaPista = true;
                textoPista = pista.getTexto();
                idPista = pista.getId_pista();
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
        return new ResultadoPregunta(respuestaSospechoso, nuevaPista,textoPista,nombre_sospechoso,idPista,evidenciaObtenida);
    }

    //Lanzar prob de evidencias
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

    //Acusas al sospechoso si devuelve false has perdido si devuelve true has ganado
    public boolean acusarSospechoso(int id_sospechoso, int id_caso) {
        Sospechoso culpable = sospechosoDAO.obtenerCulpablePorCaso(id_caso);
        Sospechoso sospechosoeleguido = sospechosoDAO.obtenerSospechosoPorId(id_sospechoso);
        return sospechosoeleguido.getNombre().equals(culpable.getNombre());
    }

    //Tomas nota sobre el caso
    public String tomarNota(String nota,int id_caso) {
        investigacionDAO.actualizarNota(nota,id_caso);
        return nota;
    }

    public String leerNota(int id_caso) {
        return investigacionDAO.leerNota(id_caso);
    }

    public void actualizarContadorPreguntas(int id_caso,int preguntas) {
        casoDAO.actualizarPreguntasRestantes(id_caso,preguntas);
    }
}
