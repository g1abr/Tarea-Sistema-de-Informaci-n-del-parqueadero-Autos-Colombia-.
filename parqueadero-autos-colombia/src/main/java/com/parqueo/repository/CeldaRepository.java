package com.parqueo.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.parqueo.model.Celda;
import com.parqueo.model.Enum.EstadoCelda;
import com.parqueo.model.Enum.TipoCelda;
import com.parqueo.util.ConexionBD;

public class CeldaRepository {
public void ocupar(Celda celda) throws SQLException {

        String sql = """
        UPDATE celdas
        SET estado = 'OCUPADA'
        WHERE id_celda = ?
        """;

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, celda.getId());

            stmt.executeUpdate();
        }
    }

    public void liberar(Celda celda) throws SQLException {

        String sql = """
        UPDATE celdas
        SET estado = 'DISPONIBLE'
        WHERE id_celda = ?
        """;

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
        WHERE tipo_celda = ? AND estado = 'DISPONIBLE'
        """;

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, tipo.name());

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                Celda c = new Celda();

                c.setId(rs.getInt("id_celda"));
                c.setNumeroCelda(rs.getString("numero_celda"));

                c.setTipoCelda(
                        TipoCelda.valueOf(rs.getString("tipo_celda"))
                );

                c.setEstado(
                        EstadoCelda.valueOf(rs.getString("estado"))
                );

                lista.add(c);
            }
        }

        return lista;
    }
}
