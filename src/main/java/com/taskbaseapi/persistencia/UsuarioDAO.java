package com.taskbaseapi.persistencia;

import com.taskbaseapi.config.ConexaoPostgreSQL;
import com.taskbaseapi.modelo.Usuario;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO extends ConexaoPostgreSQL {

  private static UsuarioDAO instance;

  public static UsuarioDAO getInstance() {
    if (instance == null) {
      instance = new UsuarioDAO();
    }
    return instance;
  }

  private static final String LOGAR_USUARIO = "select * from usuario where usu_email = ?";

  public Usuario logarUsuario(String email) throws SQLException {
    try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(LOGAR_USUARIO)) {
      ps.setString(1, email);

      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return new Usuario(
                  rs.getInt("usu_id"),
                  rs.getString("usu_nome"),
                  rs.getString("usu_email"),
                  rs.getString("usu_senha")
          );
        } else {
          return null;
        }
      }
    }
  }

  private static final String LISTAR_USUARIO = "select * from usuario";

  public List<Usuario> listarUsuarios() throws SQLException {
    List<Usuario> responseUsuario = new ArrayList<>();

    try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(LISTAR_USUARIO)) {
      try (ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
          Usuario rsUsuario = new Usuario(
                  rs.getInt("usu_id"),
                  rs.getString("usu_nome"),
                  rs.getString("usu_email"),
                  null //senha
          );

          responseUsuario.add(rsUsuario);
        }
      }
    }

    return responseUsuario;
  }

  private static final String LISTAR_USUARIO_POR_ID = """
          SELECT * FROM usuario WHERE usu_id = ?
          """;

  public Usuario listarUsuarioPorId(Integer usuarioId) throws SQLException {
    try (Connection con = conectar()) {
      Usuario usuarioEncontrado = new Usuario();

      try (PreparedStatement ps = con.prepareStatement(LISTAR_USUARIO_POR_ID)) {
        ps.setInt(1, usuarioId);

        try (ResultSet rs = ps.executeQuery()) {
          if (rs.next()) {
            usuarioEncontrado.setUsu_id(rs.getInt("usu_id"));
            usuarioEncontrado.setUsu_nome(rs.getString("usu_nome"));
            usuarioEncontrado.setUsu_email(rs.getString("usu_email"));
          }
        }
      }

      return usuarioEncontrado;
    }
  }

  private static final String GRAVAR_USUARIO = """
          INSERT INTO usuario(usu_nome, usu_email, usu_senha) VALUES (?, ?, ?) RETURNING usu_id
          """;

  public Usuario gravarUsuario(Usuario novoUsuario) throws SQLException {
    try (Connection con = conectar()) {
      con.setAutoCommit(false);

      try (PreparedStatement ps = con.prepareStatement(GRAVAR_USUARIO)) {
        ps.setString(1, novoUsuario.getUsu_nome());
        ps.setString(2, novoUsuario.getUsu_email());

        String senhaHash = BCrypt.hashpw(novoUsuario.getUsu_senha(), BCrypt.gensalt());
        ps.setString(3, senhaHash);

        try (ResultSet rs = ps.executeQuery()) {
          if (rs.next()) {
            int usuarioId = rs.getInt("usu_id");
            novoUsuario.setUsu_id(usuarioId);
          }
        }

        con.commit();
        return novoUsuario;
      } catch (SQLException ex) {
        con.rollback();
        throw ex;
      }
    }
  }

  private static final String EDITAR_USUARIO = """
          UPDATE usuario SET usu_nome = ?, usu_email = ? WHERE usu_id = ?
          """;

  public Usuario editarUsuario(Usuario usuarioEditado) throws SQLException {
    try (Connection con = conectar()) {
      con.setAutoCommit(false);

      try (PreparedStatement ps = con.prepareStatement(EDITAR_USUARIO)) {
        ps.setString(1, usuarioEditado.getUsu_nome());
        ps.setString(2, usuarioEditado.getUsu_email());
        ps.setInt(3, usuarioEditado.getUsu_id());
        ps.executeUpdate();

        con.commit();
        return usuarioEditado;
      } catch (SQLException ex) {
        con.rollback();
        throw ex;
      }
    }
  }

  private static final String DELETAR_USUARIO = """
          DELETE FROM usuario WHERE usu_id = ?
          """;

  public boolean deletarUsuario(Integer usuarioId) throws SQLException {
    try (Connection con = conectar()) {
      con.setAutoCommit(false);

      try (PreparedStatement ps = con.prepareStatement(DELETAR_USUARIO)) {
        ps.setInt(1, usuarioId);
        ps.executeUpdate();

        con.commit();
        return true;
      } catch (SQLException ex) {
        con.rollback();
        throw ex;
      }
    }
  }
}