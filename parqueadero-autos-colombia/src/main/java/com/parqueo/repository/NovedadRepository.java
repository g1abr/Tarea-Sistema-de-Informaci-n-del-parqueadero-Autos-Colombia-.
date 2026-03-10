package com.parqueo.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.parqueo.model.Novedad;
import com.parqueo.model.RegistroParqueo;
import com.parqueo.util.ConexionBD;

public class NovedadRepository {
public void guardar(Novedad novedad) throws SQLException {

        String sql = """
        INSERT INTO novedades
        (id_registro, fecha_hora, descripcion)
        VALUES (?, ?, ?)
        """;

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, novedad.getRegistro().getId());

            stmt.setTimestamp(2,
                    Timestamp.valueOf(novedad.getFechaHora()));

            stmt.setString(3, novedad.getDescripcion());

          
            stmt.executeUpdate();
        }
    }

    public List<Novedad> listarPorRegistro(int idRegistro) throws SQLException {

        List<Novedad> lista = new ArrayList<>();

        String sql = """
        SELECT * FROM novedades
        WHERE id_registro = ?
        ORDER BY fecha_hora DESC
        """;

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idRegistro);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                Novedad n = new Novedad();

                n.setId(rs.getInt("id_novedad"));

                n.setFechaHora(
                        rs.getTimestamp("fecha_hora").toLocalDateTime()
                );

                n.setDescripcion(
                        rs.getString("descripcion")
                );

                RegistroParqueo r = new RegistroParqueo();
                r.setId(rs.getInt("id_registro"));

                n.setRegistro(r);

                lista.add(n);
            }
        }

        return lista;
    }
}
