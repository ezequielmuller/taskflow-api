package com.taskbaseapi.servlet;

import com.taskbaseapi.modelo.Projeto;
import com.taskbaseapi.modelo.Usuario;
import com.taskbaseapi.persistencia.ProjetoDAO;
import com.taskbaseapi.persistencia.UsuarioDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/projetos/*")
public class ProjetoServlet extends BaseServlet {

  protected void doGet(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {

    try {
      validarToken(request);
    } catch (Exception ex) {
      retornarErro(response, HttpServletResponse.SC_UNAUTHORIZED, "Token inválido!");
      return;
    }

    JSONObject json = lerBody(request);

    try {
      List<Projeto> projetoList = ProjetoDAO.getInstance().listarProjetos(json.getInt("fk_usuario"));

      retorno(response, HttpServletResponse.SC_OK, new JSONArray(projetoList));
    } catch (Exception ex) {
      retornarErro(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
    }
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {
    JSONObject json = lerBody(request);

    try {
      if (!json.has("pro_nome") || json.isNull("pro_nome")) {
        retornarErro(response, HttpServletResponse.SC_BAD_REQUEST, "Nome obrigatórios!");
        return;
      }

      if (!json.has("pro_descricao") || json.isNull("pro_descricao")) {
        retornarErro(response, HttpServletResponse.SC_BAD_REQUEST, "Descrição obrigatório!");
        return;
      }

      Projeto novoProjeto = new Projeto(
              0,
              json.getString("pro_nome"),
              json.getString("pro_descricao"),
              null
      );

      novoProjeto = ProjetoDAO.getInstance().gravarProjeto(novoProjeto);

      retorno(response, HttpServletResponse.SC_CREATED, novoProjeto);
    } catch (Exception ex) {
      retornarErro(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
    }
  }

  protected void doPut(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {

    try {
      validarToken(request);
    } catch (Exception ex) {
      retornarErro(response, HttpServletResponse.SC_UNAUTHORIZED, "Token inválido!");
      return;
    }

    try {
      JSONObject json = lerBody(request);

      if (request.getPathInfo() == null || !request.getPathInfo().matches("/\\d+")) {
        retornarErro(response, HttpServletResponse.SC_BAD_REQUEST, "ID inválido");
        return;
      }

      if (!json.has("pro_nome") || json.isNull("pro_nome")) {
        retornarErro(response, HttpServletResponse.SC_BAD_REQUEST, "Nome obrigatórios!");
        return;
      }

      if (!json.has("pro_descricao") || json.isNull("pro_descricao")) {
        retornarErro(response, HttpServletResponse.SC_BAD_REQUEST, "Descrição obrigatório!");
        return;
      }

      Integer projetoId = Integer.parseInt(request.getPathInfo().substring(1));

      Projeto projetoEditado = new Projeto(
              projetoId,
              json.getString("pro_nome"),
              json.getString("pro_descricao"),
              null
      );

      projetoEditado = ProjetoDAO.getInstance().editarProjeto(projetoEditado);

      retorno(response, HttpServletResponse.SC_OK, new JSONObject(projetoEditado));
    } catch (Exception ex) {
      retornarErro(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
    }
  }

  protected void doDelete(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {

    try {
      validarToken(request);
    } catch (Exception ex) {
      retornarErro(response, HttpServletResponse.SC_UNAUTHORIZED, "Token inválido!");
      return;
    }

    try {
      if (request.getPathInfo() == null || !request.getPathInfo().matches("/\\d+")) {
        retornarErro(response, HttpServletResponse.SC_BAD_REQUEST, "ID inválido");
        return;
      }

      Integer projetoId = Integer.parseInt(request.getPathInfo().substring(1));

      boolean resposta = ProjetoDAO.getInstance().excluirProjeto(projetoId);
      if (resposta) {
        retorno(response, HttpServletResponse.SC_NO_CONTENT, null);
      } else {
        retornarErro(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Não foi possivel deletar o projeto!");
      }
    } catch (Exception ex) {
      retornarErro(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
    }
  }
}