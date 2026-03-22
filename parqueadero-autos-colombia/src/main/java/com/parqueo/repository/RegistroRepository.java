package com.parqueo.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.parqueo.model.Celda;
import com.parqueo.model.RegistroParqueo;
import com.parqueo.model.Vehiculo;
import com.parqueo.util.ConexionBD;

public class RegistroRepository {

    // ----------------------------
    // REGISTRAR ENTRADA
    // ----------------------------

    public void registrarEntrada(RegistroParqueo registro) {

        String sql = """
                    INSERT INTO registros_parqueo
                    (id_vehiculo, id_celda, es_mensualidad)
                    VALUES (?, ?, ?)
                """;

        try (Connection conn = ConexionBD.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, registro.getVehiculo().getId());
            stmt.setInt(2, registro.getCelda().getId());
            stmt.setBoolean(3, registro.isEsMensualidad());

            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ----------------------------
    // REGISTRAR SALIDA
    // ----------------------------

    public boolean registrarSalida(RegistroParqueo registro) {

        String sql = """
                UPDATE registros_parqueo
                SET fecha_hora_salida = ?
                WHERE id_registro = ?
                """;

        try (Connection conn = ConexionBD.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setTimestamp(1, Timestamp.valueOf(registro.getFechaHoraSalida()));
            stmt.setInt(2, registro.getId());

            stmt.executeUpdate();

            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // ----------------------------
    // VEHICULOS ACTUALMENTE EN PARQUEADERO
    // ----------------------------

    public List<RegistroParqueo> listarActivos() {

        List<RegistroParqueo> lista = new ArrayList<>();

        String sql = """
                    SELECT r.*, v.placa, c.numero_celda
                    FROM registros_parqueo r
                    JOIN vehiculos v ON r.id_vehiculo = v.id_vehiculo
                    JOIN celdas c ON r.id_celda = c.id_celda
                    WHERE r.fecha_hora_salida IS NULL
                """;

        try (Connection conn = ConexionBD.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                RegistroParqueo r = new RegistroParqueo();

                r.setId(rs.getInt("id_registro"));
                r.setFechaHoraEntrada(
                        rs.getTimestamp("fecha_hora_entrada").toLocalDateTime());

                // 🔥 VEHICULO
                Vehiculo v = new Vehiculo();
                v.setPlaca(rs.getString("placa"));
                r.setVehiculo(v);

                // 🔥 CELDA
                Celda c = new Celda();
                c.setNumeroCelda(rs.getString("numero_celda"));
                r.setCelda(c);

                lista.add(r);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }
    // ----------------------------
    // BUSCAR REGISTRO ACTIVO
    // ----------------------------

    public RegistroParqueo buscarRegistroActivo(int idVehiculo) {

        String sql = """
                    SELECT r.*, v.placa, c.numero_celda
                    FROM registros_parqueo r
                    JOIN vehiculos v ON r.id_vehiculo = v.id_vehiculo
                    JOIN celdas c ON r.id_celda = c.id_celda
                    WHERE r.id_vehiculo = ? AND r.fecha_hora_salida IS NULL
                """;

        try (Connection conn = ConexionBD.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idVehiculo);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {

                RegistroParqueo r = new RegistroParqueo();

                r.setId(rs.getInt("id_registro"));
                r.setFechaHoraEntrada(
                        rs.getTimestamp("fecha_hora_entrada").toLocalDateTime());

                Vehiculo v = new Vehiculo();
                v.setPlaca(rs.getString("placa"));
                r.setVehiculo(v);

                Celda c = new Celda();
                c.setNumeroCelda(rs.getString("numero_celda"));
                r.setCelda(c);

                return r;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    // ----------------------------
    // HISTORIAL DE VEHICULO
    // ----------------------------

    public List<RegistroParqueo> historialVehiculo(int idVehiculo) {

        List<RegistroParqueo> lista = new ArrayList<>();

        String sql = """
                    SELECT r.*, c.numero_celda
                    FROM registros_parqueo r
                    JOIN celdas c ON r.id_celda = c.id_celda
                    WHERE r.id_vehiculo = ?
                    ORDER BY r.fecha_hora_entrada DESC
                """;

        try (Connection conn = ConexionBD.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idVehiculo);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                RegistroParqueo r = new RegistroParqueo();

                r.setId(rs.getInt("id_registro"));
                r.setFechaHoraEntrada(
                        rs.getTimestamp("fecha_hora_entrada").toLocalDateTime());

                if (rs.getTimestamp("fecha_hora_salida") != null) {
                    r.setFechaHoraSalida(
                            rs.getTimestamp("fecha_hora_salida").toLocalDateTime());
                }

                Celda c = new Celda();
                c.setNumeroCelda(rs.getString("numero_celda"));
                r.setCelda(c);

                lista.add(r);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

}