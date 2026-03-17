package com.taskflow.persistencia;

import com.taskflow.modelo.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO extends Conexao {

  private static UsuarioDAO instance;

  public static UsuarioDAO getInstance() {
    if (instance == null) {
      instance = new UsuarioDAO();
    }
    return instance;
  }

  private static final String LISTAR_USUARIO = "select * from usuario";

  public List<Usuario> listarUsuarios() throws SQLException {
    List<Usuario> responseUsuario = new ArrayList<>();

    try (Connection con = conectar(); PreparedStatement ps = con.prepareCall(LISTAR_USUARIO)) {
      try (ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
          Usuario rsUsuario = new Usuario(
                  rs.getInt("usu_id"), rs.getString("usu_nome"),
                  rs.getString("usu_email"), rs.getString("usu_senha"),
                  rs.getBoolean("usu_gerenciador")
          );

          responseUsuario.add(rsUsuario);
        }
      }
    }
    return responseUsuario;
  }


}