package Test;

import Model.DAO.CasoDAO;
import Model.DAO.InvestigacionDAO;
import Model.DAO.DBConnection;
import Model.DAO.SospechosoDAO;
import Model.Entities.Caso;
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



    }
}
