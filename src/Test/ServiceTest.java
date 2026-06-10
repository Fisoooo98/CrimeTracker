package Test;

import Model.DAO.DBConnection;
import Model.Entities.Estado;
import Model.Entities.Sospechoso;
import Model.Service.CasoService;
import Model.Service.JuegoService;

import java.util.List;

public class ServiceTest {
    public static void main(String[] args) {
        DBConnection db = new DBConnection();
        db.iniciarBaseDeDatos();
        JuegoService juegoService = new JuegoService();
        CasoService casoService = new CasoService();
        List<String> preguntas = juegoService.InterrogarSospechoso(1,1);
        System.out.println(preguntas);
        System.out.println(juegoService.preguntarSospechoso(preguntas.get(0),1,1));

        System.out.println(casoService.obtenerCasosporCorrecto(false));
        System.out.println(casoService.obtenerCasosporEstado(Estado.NORESUELTO));

        System.out.println(juegoService.InterrogarSospechoso(1,1));

        System.out.println(casoService.obtenerCasoActivo());
    }
}
