package Model.DAO;

import Model.Entities.Pista;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InventarioDAO {
    private static final String url = "jdbc:sqlite:test.db";
    public int obtenerPista(int id_pista,int id_caso){
        int res;
        String sql = "Insert OR IGNORE INTO Inventario_Pistas (id_pista,id_caso) values (?,?)";
        try(
                Connection connection = DriverManager.getConnection(url);
                PreparedStatement ps = connection.prepareStatement(sql);
        ){
            ps.setInt(1, id_pista);
            ps.setInt(2, id_caso);
            res = ps.executeUpdate();
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
        return res;
    }

    public List<Integer> leerPistas(int id_caso){
        List<Integer> pistas = new ArrayList<>();
        String sql = "select id_pista from Inventario_Pistas where id_caso = ?";
        try(
                Connection connection = DriverManager.getConnection(url);
                PreparedStatement ps = connection.prepareStatement(sql);
        ){
            ps.setInt(1, id_caso);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                pistas.add(rs.getInt("id_pista"));
            }
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
        return pistas;
    }
    public List<Pista> cargarPistas(int id_caso){
        List<Pista> pistas = new ArrayList<>();
        String sql = "select id_pista from Inventario_Pistas where id_caso = ?";
        try(
                Connection connection = DriverManager.getConnection(url);
                PreparedStatement ps = connection.prepareStatement(sql);
        ){
            ps.setInt(1, id_caso);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Pista p = obtenerPistaPorId(rs.getInt("id_pista"));
                pistas.add(p);
            }
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
        return pistas;
    }


    public Pista obtenerPistaPorId(int id_pista){
        String sql = "select * from Pistas where id_pista = ?";
        try (
                Connection connection = DriverManager.getConnection(url);
                PreparedStatement ps = connection.prepareStatement(sql);
        ){
            ps.setInt(1,id_pista);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return new Pista(rs.getInt("id_pista"),rs.getString("texto_pista"));
            }
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
        return null;
    }
}
