package com.parqueo.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;

import com.parqueo.model.Enum.TipoVehiculo;
import com.parqueo.model.Usuario;
import com.parqueo.model.Vehiculo;
import com.parqueo.model.RegistroParqueo;
import com.parqueo.util.ConexionBD;

public class VehiculoRepository {

    public void guardar(Vehiculo vehiculo) throws SQLException {

        String sql = """
                INSERT INTO vehiculos
                (placa, marca, modelo, color, tipo, id_usuario)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = ConexionBD.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, vehiculo.getPlaca());
            stmt.setString(2, vehiculo.getMarca());
            stmt.setString(3, vehiculo.getModelo());
            stmt.setString(4, vehiculo.getColor());

            stmt.setString(5, vehiculo.getTipo().name());
            stmt.setInt(6, vehiculo.getPropietario().getId());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();

            if (rs.next()) {
                vehiculo.setId(rs.getInt(1));
            }
        }
    }

    public boolean actualizar(Vehiculo vehiculo) throws SQLException {

        String sql = """
                UPDATE vehiculos
                SET marca = ?, modelo = ?, color = ?, tipo = ?, id_usuario = ?
                WHERE placa = ?
                """;

        try (Connection conn = ConexionBD.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, vehiculo.getMarca());
            stmt.setString(2, vehiculo.getModelo());
            stmt.setString(3, vehiculo.getColor());
            stmt.setString(4, vehiculo.getTipo().name());
            stmt.setInt(5, vehiculo.getPropietario().getId());
            stmt.setString(6, vehiculo.getPlaca());

            stmt.executeUpdate();

            return true;
        }
    }

    public Vehiculo buscarPorPlaca(String placa) throws SQLException {

        String sql = """
                SELECT v.*, u.id_usuario, u.nombre_completo
                FROM vehiculos v
                JOIN usuarios u ON v.id_usuario = u.id_usuario
                WHERE v.placa = ?
                """;

        try (Connection conn = ConexionBD.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, placa);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {

                Vehiculo v = new Vehiculo();

                v.setId(rs.getInt("id_vehiculo"));
                v.setPlaca(rs.getString("placa"));
                v.setMarca(rs.getString("marca"));
                v.setModelo(rs.getString("modelo"));
                v.setColor(rs.getString("color"));

                v.setTipo(TipoVehiculo.valueOf(rs.getString("tipo").toUpperCase()));

                Usuario u = new Usuario();
                u.setId(rs.getInt("id_usuario"));
                u.setNombreCompleto(rs.getString("nombre_completo"));

                v.setPropietario(u);

                return v;
            }
        }

        return null;
    }

    public List<Vehiculo> listarTodos() throws SQLException {

        List<Vehiculo> lista = new ArrayList<>();

        String sql = """
                SELECT v.*, u.id_usuario, u.nombre_completo
                FROM vehiculos v
                JOIN usuarios u ON v.id_usuario = u.id_usuario
                """;

        try (Connection conn = ConexionBD.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                Vehiculo v = new Vehiculo();

                v.setId(rs.getInt("id_vehiculo"));
                v.setPlaca(rs.getString("placa"));
                v.setMarca(rs.getString("marca"));
                v.setModelo(rs.getString("modelo"));
                v.setColor(rs.getString("color"));

                v.setTipo(TipoVehiculo.valueOf(rs.getString("tipo").toUpperCase()));

                Usuario u = new Usuario();
                u.setId(rs.getInt("id_usuario"));
                u.setNombreCompleto(rs.getString("nombre_completo"));

                v.setPropietario(u);

                lista.add(v);
            }
        }

        return lista;
    }

    public List<RegistroParqueo> obtenerHistorial(int idVehiculo) throws SQLException {

        List<RegistroParqueo> lista = new ArrayList<>();

        String sql = """
                SELECT * FROM registros_parqueo
                WHERE id_vehiculo = ?
                ORDER BY fecha_hora_entrada DESC
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

                lista.add(r);
            }
        }

        return lista;
    }

    public boolean eliminar(String placa) throws SQLException {

        String sql = "DELETE FROM vehiculos WHERE placa = ?";

        try (Connection conn = ConexionBD.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, placa);

            int filas = stmt.executeUpdate();

            return filas > 0;
        }
    }

}