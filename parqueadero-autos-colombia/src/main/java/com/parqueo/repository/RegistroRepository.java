package com.parqueo.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.sql.Statement;
import com.parqueo.model.RegistroParqueo;
import com.parqueo.util.ConexionBD;

public class RegistroRepository {
    public void registrarEntrada(RegistroParqueo registro) throws SQLException {

        String sql = """
                INSERT INTO registros_parqueo
                (id_vehiculo, id_celda, fecha_hora_entrada, es_mensualidad)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection conn = ConexionBD.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, registro.getVehiculo().getId());
            stmt.setInt(2, registro.getCelda().getId());

            stmt.setTimestamp(3, Timestamp.valueOf(registro.getFechaHoraEntrada()));

            stmt.setBoolean(4, registro.isEsMensualidad());


            stmt.executeUpdate();
        }
    }

    public boolean registrarSalida(RegistroParqueo registro) throws SQLException {

        String sql = """
                UPDATE registros_parqueo
                SET fecha_hora_salida = ?
                WHERE id_registro = ?
                """;

        try (Connection conn = ConexionBD.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setTimestamp(1, Timestamp.valueOf(registro.getFechaHoraSalida()));


            stmt.setInt(3, registro.getId());

            stmt.executeUpdate();

            return true;
        }
    }
     public List<RegistroParqueo> listarActivos() {

        List<RegistroParqueo> lista = new ArrayList<>();

        String sql = "SELECT * FROM registros_parqueo WHERE fecha_hora_salida IS NULL";

        try (Connection conn = ConexionBD.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {

                RegistroParqueo r = new RegistroParqueo();

                r.setId(rs.getInt("id_registro"));

                r.setFechaHoraEntrada(
                        rs.getTimestamp("fecha_hora_entrada").toLocalDateTime());

                lista.add(r);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }









}
