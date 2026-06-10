package Model.DAO;


import Model.Entities.Evidencia;
import Model.Entities.Pista;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InvestigacionDAO {
    private static final String url = "jdbc:sqlite:test.db";
    private InventarioDAO inventarioDAO = new InventarioDAO();
    public Pista obtenerPistaPorPregunta(int idcaso,int idPregunta,int idSospechoso) {
        String sql = "SELECT * FROM PISTAS WHERE id_pregunta_conectora = ? and id_sospechoso_conector = ? and id_caso = ?";
        try(
                Connection connection = DriverManager.getConnection(url);
                PreparedStatement ps = connection.prepareStatement(sql);
        ){
            ps.setInt(1, idPregunta);
            ps.setInt(2, idSospechoso);
            ps.setInt(3, idcaso);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                return new Pista(
                        rs.getInt("id_caso"),
                        rs.getInt("id_pista"),
                        rs.getString("texto_pista"),
                        rs.getInt("id_sospechoso_conector"),
                        rs.getInt("id_pregunta_conectora")
                );
            }
        }catch(SQLException ex){
            throw new RuntimeException(ex);
        }
        return null;
    }

    public int obtenerIdPregunta(String textoPregunta){
        String sql = "SELECT * FROM Preguntas WHERE texto_pregunta = ?";
        try(
                Connection connection = DriverManager.getConnection(url);
                PreparedStatement ps = connection.prepareStatement(sql);
        ){
            ps.setString(1, textoPregunta);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                return rs.getInt("id_pregunta");
            }
        }catch(SQLException ex){
            throw new RuntimeException(ex);
        }
        return 0;
    }

    public List<Evidencia> obtenerEvidenciasPorCaso(int idcaso) {
        String sql = "SELECT * FROM Evidencias WHERE id_caso = ?";
        List<Evidencia> evidencias = new ArrayList<>();
        try(
                Connection connection = DriverManager.getConnection(url);
                PreparedStatement ps = connection.prepareStatement(sql)
        ){
            ps.setInt(1, idcaso);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                evidencias.add(new Evidencia(
                        rs.getInt("id_caso"),
                        rs.getInt("id_evidencia"),
                        rs.getString("texto_evidencia")
                ));
            }
        }catch(SQLException ex){
            throw new RuntimeException(ex);
        }
        return evidencias;
    }
    public Pista obtenerPistasPorId(int id_pista) {
        String sql = "SELECT * FROM Pistas WHERE id_pista = ?";
        try(
                Connection connection = DriverManager.getConnection(url);
                PreparedStatement ps = connection.prepareStatement(sql);
        ){
            ps.setInt(1, id_pista);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                return new Pista(
                        rs.getInt("id_pista"),
                        rs.getString("texto_pista")
                );
            }
        }catch(SQLException ex){
            throw new RuntimeException(ex);
        }
        return null;
    }
    public int actualizarNota(String texto,int id_caso){
        int res;
        String sql = "Update Casos set texto_notas = ? where id_caso = ?";
        try(
                Connection connection = DriverManager.getConnection(url);
                PreparedStatement ps = connection.prepareStatement(sql);
        ){
            ps.setString(1,texto);
            ps.setInt(2, id_caso);
            res = ps.executeUpdate();
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
        return res;
    }

    public String leerNota(int id_caso){
        String sql = "select texto_notas from Casos where id_caso = ?";
        try(
                Connection connection = DriverManager.getConnection(url);
                PreparedStatement ps = connection.prepareStatement(sql)
        ){
            ps.setInt(1, id_caso);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                return rs.getString("texto_notas");
            }
        }catch(SQLException ex){
            throw new RuntimeException(ex);
        }
        return null;
    }
}
