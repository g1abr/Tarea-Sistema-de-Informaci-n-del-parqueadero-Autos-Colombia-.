package com.parqueo.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.parqueo.model.Pago;
import com.parqueo.model.Usuario;
import com.parqueo.model.Vehiculo;
import com.parqueo.util.ConexionBD;

public class PagoRepository {

    // -----------------------------
    // REGISTRAR PAGO
    // -----------------------------
    public boolean registrarPago(Pago pago) {

        String sql = "INSERT INTO pagos (id_usuario, id_vehiculo, fecha_pago, fecha_vencimiento, valor, estado) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionBD.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, pago.getUsuario().getId());
            ps.setInt(2, pago.getVehiculo().getId());
            ps.setObject(3, pago.getFechaPago());
            ps.setObject(4, pago.getFechaVencimiento());
            ps.setDouble(5, pago.getValor());
            ps.setString(6, pago.getEstado());

            ps.executeUpdate();

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //
    private Pago construirPago(ResultSet rs) throws Exception {

        Pago pago = new Pago();

        pago.setId(rs.getInt("id_pago"));
        pago.setValor(rs.getDouble("valor"));
        pago.setFechaPago(rs.getTimestamp("fecha_pago").toLocalDateTime());
        pago.setFechaVencimiento(rs.getTimestamp("fecha_vencimiento").toLocalDateTime());
        pago.setEstado(rs.getString("estado"));

        Usuario usuario = new Usuario();
        usuario.setNombreCompleto(rs.getString("nombre_completo"));

        pago.setUsuario(usuario);

        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setPlaca(rs.getString("placa"));

        pago.setVehiculo(vehiculo);

        return pago;
    }

    // -----------------------------
    // VALIDAR MENSUALIDAD ACTIVA
    // -----------------------------
    public boolean tieneMensualidadActiva(int idVehiculo) {

        String sql = "SELECT * FROM pagos "
                + "WHERE id_vehiculo = ? "
                + "AND fecha_vencimiento >= NOW() "
                + "ORDER BY fecha_vencimiento DESC LIMIT 1";

        try (Connection conn = ConexionBD.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idVehiculo);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // -----------------------------
    // LISTAR PAGOS POR VEHICULO
    // -----------------------------
    public List<Pago> listarPagosVehiculo(int idVehiculo) {

        List<Pago> lista = new ArrayList<>();

        String sql = " SELECT p.*,\n" +
                "u.nombre_completo,\n" +
                " v.placa\n" +
                "FROM pagos p\n" +
                "JOIN usuarios u ON p.id_usuario = u.id_usuario\n" +
                "JOIN vehiculos v ON p.id_vehiculo = v.id_vehiculo\n" +
                " WHERE p.id_vehiculo = ?";

        try (Connection conn = ConexionBD.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idVehiculo);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Pago pago = construirPago(rs);

                lista.add(pago);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    // -----------------------------
    // CONSULTAR PAGOS POR VEHICULO
    // -----------------------------
    public List<Pago> consultarPagos(int vehiculoId) {

        List<Pago> lista = new ArrayList<>();

        String sql = """
                SELECT p.*,
                u.nombre_completo,
                v.placa
                FROM pagos p
                JOIN usuarios u ON p.id_usuario = u.id_usuario
                JOIN vehiculos v ON p.id_vehiculo = v.id_vehiculo
                WHERE p.id_vehiculo = ?
                """;

        try (Connection conn = ConexionBD.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, vehiculoId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Pago pago = construirPago(rs);
                lista.add(pago);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    // -----------------------------
    // LISTAR TODOS LOS PAGOS
    // -----------------------------
    public List<Pago> listarTodos() {

        List<Pago> lista = new ArrayList<>();

        String sql = """
                  SELECT p.*,
                u.nombre_completo,
                v.placa
                FROM pagos p
                JOIN usuarios u ON p.id_usuario = u.id_usuario
                JOIN vehiculos v ON p.id_vehiculo = v.id_vehiculo
                """;

        try (Connection conn = ConexionBD.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Pago pago = construirPago(rs);
                lista.add(pago);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    // -----------------------------
    // LISTAR ACTIVOS
    // -----------------------------
    public List<Pago> listarActivos() {

        List<Pago> lista = new ArrayList<>();

        String sql = """
                                SELECT p.*,
                u.nombre_completo,
                v.placa
                FROM pagos p
                JOIN usuarios u ON p.id_usuario = u.id_usuario
                JOIN vehiculos v ON p.id_vehiculo = v.id_vehiculo
                WHERE p.id_pago IN (

                SELECT id_pago
                FROM pagos
                WHERE (id_vehiculo, fecha_vencimiento) IN (

                SELECT id_vehiculo, MAX(fecha_vencimiento)
                FROM pagos
                GROUP BY id_vehiculo

                )

                )
                AND p.fecha_vencimiento >= NOW();
                                """;

        try (Connection conn = ConexionBD.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Pago pago = construirPago(rs);
                lista.add(pago);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    // -----------------------------
    // LISTAR VENCIDOS
    // -----------------------------
    public List<Pago> listarVencidos() {

        List<Pago> lista = new ArrayList<>();

        String sql = """
                                SELECT p.*,
                u.nombre_completo,
                v.placa
                FROM pagos p
                JOIN usuarios u ON p.id_usuario = u.id_usuario
                JOIN vehiculos v ON p.id_vehiculo = v.id_vehiculo
                WHERE p.id_pago IN (

                SELECT id_pago
                FROM pagos
                WHERE (id_vehiculo, fecha_vencimiento) IN (

                SELECT id_vehiculo, MAX(fecha_vencimiento)
                FROM pagos
                GROUP BY id_vehiculo

                )

                )
                AND p.fecha_vencimiento < NOW();
                                """;

        try (Connection conn = ConexionBD.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Pago pago = construirPago(rs);
                lista.add(pago);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

}