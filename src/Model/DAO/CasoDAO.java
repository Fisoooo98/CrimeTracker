package Model.DAO;

import Model.Entities.Caso;
import Model.Entities.Estado;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CasoDAO {
    private static final String url = "jdbc:sqlite:test.db";
    private SospechosoDAO sospechosoDAO = new SospechosoDAO();
    private InvestigacionDAO investigacionDAO = new  InvestigacionDAO();
    private InventarioDAO inventarioDAO = new  InventarioDAO();
    //Obtiene los casos a traves de su id
    public Caso obtenerCasoPorId(int id) {
        String sql = "SELECT * FROM Casos WHERE id_caso = ?";
        Caso caso = null;
        try(
                Connection connection = DriverManager.getConnection(url);
                PreparedStatement ps = connection.prepareStatement(sql);
                )
        {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                caso = new Caso(
                        rs.getInt("id_caso"),
                        rs.getString("titulo"),
                        rs.getString("descripcion"),
                        rs.getString("texto_notas")
                );
                //Atributos que están fuera del contructor
                caso.setCorrecto(rs.getBoolean("correcto"));
                caso.setEstado(Estado.valueOf(rs.getString("estado")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        //Cargar Sospechosos
        caso.setSospechosos(sospechosoDAO.obtenerSospechososPorCaso(caso.getId_caso()));
        caso.setEvidencias(investigacionDAO.obtenerEvidenciasPorCaso(caso.getId_caso()));
        caso.setPistas(inventarioDAO.cargarPistas(caso.getId_caso()));
        return caso;
    }

    public Caso obtenerCasoActivo() {
        String sql = "SELECT * FROM Casos WHERE jugando_ahora = true";
        Caso caso = null;
        try(
                Connection connection = DriverManager.getConnection(url);
                PreparedStatement ps = connection.prepareStatement(sql);
        )
        {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                caso = new Caso(
                        rs.getInt("id_caso"),
                        rs.getString("titulo"),
                        rs.getString("descripcion"),
                        rs.getString("texto_notas")
                );
                //Atributos que están fuera del contructor
                caso.setCorrecto(rs.getBoolean("correcto"));
                caso.setEstado(Estado.valueOf(rs.getString("estado")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        //Cargar Sospechosos
        if (caso != null) {
            caso.setSospechosos(sospechosoDAO.obtenerSospechososPorCaso(caso.getId_caso()));
            caso.setEvidencias(investigacionDAO.obtenerEvidenciasPorCaso(caso.getId_caso()));
            caso.setPistas(inventarioDAO.cargarPistas(caso.getId_caso()));
        }
        return caso;
    }


    //Te muestra los casos según el estado
    public List<Caso> obtenerCasosporEstado(Estado estado) {
        List<Caso> casos = new ArrayList<>();
        Caso caso = null;
        String sql = "SELECT * FROM Casos WHERE estado = ?";
        try(
                Connection connection = DriverManager.getConnection(url);
                PreparedStatement ps = connection.prepareStatement(sql);
                ) {
            ps.setString(1, estado.name());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                caso = new Caso(
                        rs.getInt("id_caso"),
                        rs.getString("titulo"),
                        rs.getString("descripcion"),
                        rs.getString("texto_notas")
                );
                caso.setCorrecto(rs.getBoolean("correcto"));
                caso.setEstado(Estado.valueOf(rs.getString("estado")));
                casos.add(caso);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        //Cargar los sospechosos
        for (Caso c : casos) {
            c.setSospechosos(sospechosoDAO.obtenerSospechososPorCaso(c.getId_caso()));
        }
        return casos;
    }

    //Te muestra los casos segun si el usuario los ha acertado o no.
    public List<Caso> obtenerCasosporCorrecto(boolean correcto) {
        List<Caso> casos = new ArrayList<>();
        Caso caso = null;
        String sql = "SELECT * FROM Casos WHERE correcto = ?";
        try(
                Connection connection = DriverManager.getConnection(url);
                PreparedStatement ps = connection.prepareStatement(sql);
        ) {
            ps.setBoolean(1, correcto);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                caso = new Caso(
                        rs.getInt("id_caso"),
                        rs.getString("titulo"),
                        rs.getString("descripcion"),
                        rs.getString("texto_notas")
                );
                caso.setCorrecto(rs.getBoolean("correcto"));
                caso.setEstado(Estado.valueOf(rs.getString("estado")));
                casos.add(caso);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        //Cargar los sospechosos
        for (Caso c : casos) {
            c.setSospechosos(sospechosoDAO.obtenerSospechososPorCaso(c.getId_caso()));
        }
        return casos;
    }

    //Actualiza el estado del caso
    public int actualizarEstadoCaso(int id_caso,Estado estado) {
        String sql = "UPDATE Casos SET estado = ? WHERE id_caso = ?";
        try(
                Connection connection = DriverManager.getConnection(url);
                PreparedStatement ps = connection.prepareStatement(sql);
                ) {
            ps.setString(1, estado.name());
            ps.setInt(2, id_caso);
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //Actualiza si has acertado el caso
    public int marcarAciertoCaso(int id_caso, boolean correcto) {
        String sql = "UPDATE Casos SET correcto = ? WHERE id_caso = ?";
        try(
                Connection connection = DriverManager.getConnection(url);
                PreparedStatement ps = connection.prepareStatement(sql);
        ) {
            ps.setBoolean(1,correcto);
            ps.setInt(2, id_caso);
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //Desactivar todos los casos
    public int desactivarTodosLosCasos(){
        String sql = "UPDATE Casos SET jugando_ahora = 0";
        try(
                Connection connection = DriverManager.getConnection(url);
                PreparedStatement ps = connection.prepareStatement(sql);
        ) {
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //Actualiza el caso actual
    public int actualizarCasoActual(int id_caso,boolean jugando) {
        String sql = "UPDATE Casos SET jugando_ahora = ? WHERE id_caso = ?";
        try(
                Connection connection = DriverManager.getConnection(url);
                PreparedStatement ps = connection.prepareStatement(sql);
        ) {
            ps.setBoolean(1,jugando);
            ps.setInt(2, id_caso);
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
