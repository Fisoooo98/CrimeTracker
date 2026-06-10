package Model.Service;

import Model.DAO.CasoDAO;
import Model.DAO.InventarioDAO;
import Model.DAO.InvestigacionDAO;
import Model.DAO.SospechosoDAO;
import Model.Entities.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JuegoService {
    private final InvestigacionDAO investigacionDAO = new InvestigacionDAO();
    private final InventarioDAO inventarioDAO = new InventarioDAO();
    private final SospechosoDAO sospechosoDAO =  new SospechosoDAO();
    private final CasoDAO casoDAO = new CasoDAO();

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
        //Retornamos el resultado adquirido
        return new ResultadoPregunta(respuestaSospechoso, nuevaPista,textoPista,nombre_sospechoso,idPista);
    }

    //Obtienes las pistas que sabes sobre el caso
    public List<String> obtenerPistas(int id_caso){
        List<Integer> pistas = inventarioDAO.leerPistas(id_caso);
        List<String> textoPistas = new ArrayList<>();
        for (Integer pista : pistas) {
            String texto_pista = inventarioDAO.obtenerPistaPorId(pista).getTexto();
            textoPistas.add(texto_pista);
        }
        return textoPistas;
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

    //Miras las evidencias que hay en el caso
    public List<Evidencia> verEvidencias(int id_caso) {
         return investigacionDAO.obtenerEvidenciasPorCaso(id_caso);
    }

    //Obtenemos los sospechosos por caso
    public List<Sospechoso> obtenerSospechososDelCaso(int id_caso) {
        return sospechosoDAO.obtenerSospechososPorCaso(id_caso);
    }

    //Obtenemos la descripcion y el nombre del caso
    public Caso obtenerDetallesCaso(int id_caso) {
        return casoDAO.obtenerCasoPorId(id_caso);
    }
}
