package com.taskbaseapi.persistencia;

import com.taskbaseapi.config.ConexaoPostgreSQL;
import com.taskbaseapi.modelo.Projeto;
import com.taskbaseapi.modelo.Usuario;
import org.json.JSONArray;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProjetoDAO extends ConexaoPostgreSQL {

  private static ProjetoDAO instance;

  public static ProjetoDAO getInstance() {
    if (instance == null) {
      instance = new ProjetoDAO();
    }
    return instance;
  }

  private static final String LISTAR_PROJETOS_USUARIO = """
          SELECT up.*, p.* 
          FROM usuario_projeto up
            LEFT JOIN projeto p on p.pro_id = up.fk_projeto
          WHERE up.fk_usuario = ?
          """;

  public List<Projeto> listarProjetos(Integer usuarioId) throws SQLException {
    List<Projeto> responseProjetos = new ArrayList<>();

    try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(LISTAR_PROJETOS_USUARIO)) {
      try (ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
          Projeto rsProjeto = new Projeto(
                  rs.getInt("pro_id"),
                  rs.getString("pro_nome"),
                  rs.getString("pro_descricao"),
                  rs.getTimestamp("pro_data_criacao")
          );

          responseProjetos.add(rsProjeto);
        }
      }
    }

    return responseProjetos;
  }

  private static final String GRAVAR_PROJETO = """
          
          """;

  public Projeto gravarProjeto(Projeto novoProjeto){

    return novoProjeto;
  }

  private static final String EDITAR_PROJETO = """
          
          """;

  public Projeto editarProjeto(Projeto projetoEditado){

    return projetoEditado;
  }

  private static final String EXCLUIR_PROJETO = """
          
          """;

  public boolean excluirProjeto(Integer projetoId){

    return true;
  }

}
