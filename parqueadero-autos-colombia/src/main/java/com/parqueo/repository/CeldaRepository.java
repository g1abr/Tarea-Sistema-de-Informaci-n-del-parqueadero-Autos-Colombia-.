package com.parqueo.repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.parqueo.model.Celda;
import com.parqueo.model.Enum.EstadoCelda;
import com.parqueo.model.Enum.TipoCelda;
import com.parqueo.util.ConexionBD;

public class CeldaRepository {

    public void guardar(Celda celda) throws SQLException {

        String sql = """
                INSERT INTO celdas (numero_celda, tipo_celda, estado)
                VALUES (?, ?, ?)
                """;

        try (Connection conn = ConexionBD.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, celda.getNumeroCelda());
            stmt.setString(2, celda.getTipoCelda().name());
            stmt.setString(3, celda.getEstado().name());

            stmt.executeUpdate();
        }
    }

    public boolean actualizar(Celda celda) throws SQLException {

        String sql = """
                UPDATE celdas
                SET numero_celda = ?, tipo_celda = ?, estado = ?
                WHERE id_celda = ?
                """;

        try (Connection conn = ConexionBD.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, celda.getNumeroCelda());
            stmt.setString(2, celda.getTipoCelda().name());
            stmt.setString(3, celda.getEstado().name());
            stmt.setInt(4, celda.getId());

            return stmt.executeUpdate() > 0;
        }
    }

    public boolean eliminar(int idCelda) throws SQLException {

        String sql = "DELETE FROxM celdas WHERE id_celda = ?";

        try (Connection conn = ConexionBD.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idCelda);
            return stmt.executeUpdate() > 0;
        }
    }

    public Celda buscarPorId(int id) throws SQLException {

        String sql = "SELECT * FROM celdas WHERE id_celda = ?";

        try (Connection conn = ConexionBD.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Celda c = new Celda();

                c.setId(rs.getInt("id_celda"));
                c.setNumeroCelda(rs.getString("numero_celda"));
                c.setTipoCelda(TipoCelda.valueOf(rs.getString("tipo_celda").trim()));
                c.setEstado(EstadoCelda.valueOf(rs.getString("estado").trim()));

                return c;
            }
        }
        return null;
    }

    public List<Celda> listarTodas() throws SQLException {

        List<Celda> lista = new ArrayList<>();
        String sql = "SELECT * FROM celdas";

        try (Connection conn = ConexionBD.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Celda c = new Celda();

                c.setId(rs.getInt("id_celda"));
                c.setNumeroCelda(rs.getString("numero_celda"));
                c.setTipoCelda(TipoCelda.valueOf(rs.getString("tipo_celda").trim()));
                c.setEstado(EstadoCelda.valueOf(rs.getString("estado").trim()));

                lista.add(c);
            }
        }
        return lista;
    }

    public Celda buscarPorNumero(String numero) throws SQLException {

        String sql = "SELECT * FROM celdas WHERE numero_celda = ?";

        try (Connection conn = ConexionBD.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, numero.trim());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Celda c = new Celda();

                c.setId(rs.getInt("id_celda"));
                c.setNumeroCelda(rs.getString("numero_celda"));
                c.setTipoCelda(TipoCelda.valueOf(rs.getString("tipo_celda").trim()));
                c.setEstado(EstadoCelda.valueOf(rs.getString("estado").trim()));

                return c;
            }
        }
        return null;
    }

    public void ocupar(Celda celda) throws SQLException {
        String sql = "UPDATE celdas SET estado = 'Ocupada' WHERE id_celda = ?";

        try (Connection conn = ConexionBD.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, celda.getId());
            stmt.executeUpdate();
        }
    }

    public void liberar(Celda celda) throws SQLException {
        String sql = "UPDATE celdas SET estado = 'Disponible' WHERE id_celda = ?";

        try (Connection conn = ConexionBD.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, celda.getId());
            stmt.executeUpdate();
        }
    }

    public List<Celda> buscarDisponibles(TipoCelda tipo) throws SQLException {

        List<Celda> lista = new ArrayList<>();

        String sql = """
                SELECT * FROM celdas
                WHERE tipo_celda = ? AND estado = 'Disponible'
                """;

        try (Connection conn = ConexionBD.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, tipo.name());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Celda c = new Celda();

                c.setId(rs.getInt("id_celda"));
                c.setNumeroCelda(rs.getString("numero_celda"));
                c.setTipoCelda(TipoCelda.valueOf(rs.getString("tipo_celda").trim()));
                c.setEstado(EstadoCelda.valueOf(rs.getString("estado").trim()));

                lista.add(c);
            }
        }
        return lista;
    }
}