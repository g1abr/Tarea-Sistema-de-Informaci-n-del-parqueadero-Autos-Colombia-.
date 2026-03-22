package com.parqueo.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.List;

import com.parqueo.model.Usuario;
import com.parqueo.util.ConexionBD;

public class UsuarioRepository {

    public void guardar(Usuario usuario) throws SQLException {

        String sql = """
                INSERT INTO usuarios
                (identificacion, nombre_completo, telefono, email, direccion, fecha_registro)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = ConexionBD.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, usuario.getIdentificacion());
            stmt.setString(2, usuario.getNombreCompleto());
            stmt.setString(3, usuario.getTelefono());
            stmt.setString(4, usuario.getEmail());
            stmt.setString(5, usuario.getDireccion());

            if (usuario.getFechaRegistro() == null) {
                stmt.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
            } else {
                stmt.setTimestamp(6, Timestamp.valueOf(usuario.getFechaRegistro()));
            }

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();

            if (rs.next()) {
                usuario.setId(rs.getInt(1));
            }
        }
    }

    public boolean actualizar(Usuario usuario) throws SQLException {

        String sql = """
                UPDATE usuarios
                SET nombre_completo = ?, telefono = ?, email = ?, direccion = ?
                WHERE id_usuario = ?
                """;

        try (Connection conn = ConexionBD.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario.getNombreCompleto());
            stmt.setString(2, usuario.getTelefono());
            stmt.setString(3, usuario.getEmail());
            stmt.setString(4, usuario.getDireccion());
            stmt.setInt(5, usuario.getId());

            int filas = stmt.executeUpdate();

            return filas > 0;

        }
    }

    public Usuario buscarPorId(int id) throws SQLException {

        String sql = "SELECT * FROM usuarios WHERE id_usuario = ?";

        try (Connection conn = ConexionBD.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapearUsuario(rs);
            }
        }

        return null;
    }

    public Usuario buscarPorIdentificacion(String identificacion) throws SQLException {

        String sql = "SELECT * FROM usuarios WHERE identificacion = ?";

        try (Connection conn = ConexionBD.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, identificacion);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapearUsuario(rs);
            }
        }

        return null;
    }

    public List<Usuario> listarTodos() throws SQLException {

        List<Usuario> lista = new ArrayList<>();

        String sql = "SELECT * FROM usuarios";

        try (Connection conn = ConexionBD.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(mapearUsuario(rs));
            }
        }

        return lista;
    }

    public boolean eliminar(int id) throws SQLException {

        String sql = "DELETE FROM usuarios WHERE id_usuario = ?";

        try (Connection conn = ConexionBD.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            int filas = stmt.executeUpdate();

            return filas > 0;
        }
    }

    private Usuario mapearUsuario(ResultSet rs) throws SQLException {

        Usuario u = new Usuario();

        u.setId(rs.getInt("id_usuario"));
        u.setIdentificacion(rs.getString("identificacion"));
        u.setNombreCompleto(rs.getString("nombre_completo"));
        u.setTelefono(rs.getString("telefono"));
        u.setEmail(rs.getString("email"));
        u.setDireccion(rs.getString("direccion"));

        Timestamp fechaSQL = rs.getTimestamp("fecha_registro");

        if (fechaSQL != null) {
            u.setFechaRegistro(fechaSQL.toLocalDateTime());
        }

        return u;
    }
}