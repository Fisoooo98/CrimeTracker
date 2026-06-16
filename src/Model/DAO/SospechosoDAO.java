package Model.DAO;

import Model.Entities.Sospechoso;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SospechosoDAO {
    private static final String url = "jdbc:sqlite:test.db";

    public Sospechoso obtenerSospechosoPorId(int id_sospechoso){
        String sql = "SELECT * FROM Sospechosos WHERE id_sospechoso=?";
        try(
                Connection connection = DriverManager.getConnection(url);
                PreparedStatement ps = connection.prepareStatement(sql);
        ){
            ps.setInt(1, id_sospechoso);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return new Sospechoso(
                        rs.getInt("id_sospechoso"),
                        rs.getString("nombre"),
                        rs.getBoolean("es_culpable")
                );
            }
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
        return null;
    }

    public List<Sospechoso> obtenerSospechososPorCaso(int id_caso){
        List<Sospechoso> sospechosos = new ArrayList<>();
        String sql = "SELECT * FROM Sospechosos WHERE id_caso= ?";
        try(
                Connection connection = DriverManager.getConnection(url);
                PreparedStatement ps = connection.prepareStatement(sql);
        ){
            ps.setInt(1, id_caso);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                sospechosos.add(
                        new Sospechoso(
                                rs.getInt("id_sospechoso"),
                                rs.getString("nombre"),
                                rs.getBoolean("es_culpable")
                        )
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return sospechosos;
    }

    public Sospechoso obtenerCulpablePorCaso(int id_caso){
        String sql = "SELECT * FROM Sospechosos WHERE id_caso = ? and es_culpable = 1";
        try(
                Connection connection = DriverManager.getConnection(url);
                PreparedStatement ps = connection.prepareStatement(sql);
        ){
            ps.setInt(1, id_caso);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                        int esCulpableInt = rs.getInt("es_culpable");
                        boolean esCulpable = esCulpableInt == 1;
                        return new Sospechoso(
                                rs.getInt("id_sospechoso"),
                                rs.getString("nombre"),
                                esCulpable
                        );
            }
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }


    public HashMap<String,String> obtenerPreguntasYrespuestas(int id_sospechoso,List<Integer> id_pistas){
        int posicion = 2;
        HashMap<String,String> preguntasYrespuestas = new HashMap<>();
        StringBuilder obtenerPlaceHolders = new StringBuilder();
        if (id_pistas.isEmpty()){
            obtenerPlaceHolders = new StringBuilder("IS NULL)");
        }else{

            for (int i=0;i<id_pistas.size();i++){
                obtenerPlaceHolders.append("?,");
            }
            obtenerPlaceHolders = new StringBuilder("IN ("+obtenerPlaceHolders.substring(0, obtenerPlaceHolders.length() - 1) + "))");
        }

        String sql = "Select p.texto_pregunta,r.texto_respuesta from Respuestas r,Preguntas p " +
        "where r.id_pregunta = p.id_pregunta and r.id_sospechoso = ?" +
                "and (p.id_pista_requisito IS NULL OR p.id_pista_requisito " + obtenerPlaceHolders;

        try(
                Connection connection = DriverManager.getConnection(url);
                PreparedStatement ps = connection.prepareStatement(sql);
        ){
            ps.setInt(1, id_sospechoso);
            for(int i=0;i<id_pistas.size();i++){
                ps.setInt(posicion, id_pistas.get(i));
                posicion++;
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                preguntasYrespuestas.put(rs.getString("texto_pregunta"), rs.getString("texto_respuesta"));
            }
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return preguntasYrespuestas;
    }
}
