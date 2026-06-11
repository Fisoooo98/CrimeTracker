package Test;

import Model.DAO.CasoDAO;
import Model.DAO.InvestigacionDAO;
import Model.DAO.DBConnection;
import Model.DAO.SospechosoDAO;
import Model.Entities.Caso;
import Model.Entities.Dificultad;
import Model.Entities.Estado;
import Model.Entities.Sospechoso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DAOTest {
    static void main() {
        DBConnection db = new DBConnection();
        db.iniciarBaseDeDatos();
        CasoDAO casoDAO = new CasoDAO();
        SospechosoDAO sospechosoDAO = new SospechosoDAO();
        InvestigacionDAO  investigacionDAO = new InvestigacionDAO();

        System.out.println("-----Cargado de Datos de casos-------");
        Caso caso1 = casoDAO.obtenerCasoPorId(1);
        caso1.setSospechosos(sospechosoDAO.obtenerSospechososPorCaso(caso1.getId_caso()));
        System.out.println(caso1);
        System.out.println(sospechosoDAO.obtenerCulpablePorCaso(1));

        System.out.println("------Preguntas y respuestas-------");

        Sospechoso sospechoso1 = sospechosoDAO.obtenerSospechosoPorId(1);
        HashMap<String, String> PYRSospechoso1;
        List<Integer> id_pistas = new ArrayList<>();
        PYRSospechoso1 = sospechosoDAO.obtenerPreguntasYrespuestas(sospechoso1.getId_sospechoso(),id_pistas);
        System.out.println("Sherlock: ¿Dónde se encontraba usted en el momento del crimen?");
        System.out.println(sospechoso1.getNombre() + ": " + PYRSospechoso1.get("¿Dónde se encontraba usted en el momento del crimen?"));
        System.out.println("Sospechoso caso2");
        System.out.println(sospechosoDAO.obtenerSospechososPorCaso(2));

        System.out.println("Prueba de preguntas restantes");
        casoDAO.actualizarPreguntasRestantes(1,5);
        System.out.println(casoDAO.obtenerCasoPorId(1).getContador_preguntas());

        System.out.println("Prueba de obtnener evidenicas");
        investigacionDAO.actualizarEvidencia(1,1,true);
        System.out.println(investigacionDAO.obtenerEvidenciasPorCaso(1).size());
        System.out.println(casoDAO.obtenerCasoPorId(1).getProbEvidencia());
        System.out.println("Prueva dificultad");
        System.out.println(casoDAO.actualizarDificultad(1, Dificultad.NORMAL));
        System.out.println(casoDAO.obtenerCasoPorId(1).getProbEvidencia());




    }
}
