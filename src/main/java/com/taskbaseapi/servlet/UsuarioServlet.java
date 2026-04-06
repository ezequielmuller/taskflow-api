package com.taskbaseapi.servlet;

import com.taskbaseapi.modelo.Usuario;
import com.taskbaseapi.persistencia.UsuarioDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/usuarios/*")
public class UsuarioServlet extends BaseServlet {

  protected void doGet(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {

    String pathInfo = request.getPathInfo();

    try {
      // se for /usuario/1 busca pelo id
      if (pathInfo != null && pathInfo.matches("/\\d+")) {

        try {
          validarToken(request);
        } catch (Exception ex) {
          retornarErro(response, HttpServletResponse.SC_UNAUTHORIZED, "Token inválido!");
          return;
        }

        Integer usuarioId = Integer.parseInt(pathInfo.substring(1));
        Usuario usuarioEncontrado = UsuarioDAO.getInstance().listarUsuarioPorId(usuarioId);

        if (usuarioEncontrado == null) {
          retornarErro(response, HttpServletResponse.SC_NOT_FOUND, "Usuário não encontrado");
          return;
        }

        retorno(response, HttpServletResponse.SC_OK, new JSONObject(usuarioEncontrado));
        return;
      } else {
        try {
          List<Usuario> usuariosList = UsuarioDAO.getInstance().listarUsuarios();

          retorno(response, HttpServletResponse.SC_OK, new JSONArray(usuariosList));
        } catch (Exception ex) {
          retornarErro(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
        }
      }
//      switch (pathInfo) {
//        case "/": {
//
//          break;
//        }
//
//        default: {
//          retornarErro(response, HttpServletResponse.SC_BAD_REQUEST, "Rota inválida!");
//          break;
//        }
//      }
    } catch (Exception ex) {
      retornarErro(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
    }
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {
    JSONObject json = lerBody(request);

    try {
      if (!json.has("nome") || json.isNull("nome")) {
        retornarErro(response, HttpServletResponse.SC_BAD_REQUEST, "Nome obrigatórios!");
        return;
      }

      if (!json.has("email") || json.isNull("email")) {
        retornarErro(response, HttpServletResponse.SC_BAD_REQUEST, "E-mail obrigatório!");
        return;
      }

      if (!json.has("senha") || json.isNull("senha")) {
        retornarErro(response, HttpServletResponse.SC_BAD_REQUEST, "Senha obrigatório!");
        return;
      }

      Usuario novoUsuario = new Usuario(0, json.getString("nome"), json.getString("email"), json.getString("senha"));
      novoUsuario = UsuarioDAO.getInstance().gravarUsuario(novoUsuario);

      JSONObject obj = new JSONObject();
      obj.put("id", novoUsuario.getUsu_id());
      obj.put("nome", novoUsuario.getUsu_nome());
      obj.put("email", novoUsuario.getUsu_email());

      retorno(response, HttpServletResponse.SC_CREATED, obj);
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

      if (!json.has("nome") || json.isNull("nome")) {
        retornarErro(response, HttpServletResponse.SC_BAD_REQUEST, "Nome obrigatório!");
        return;
      }

      if (!json.has("email") || json.isNull("email")) {
        retornarErro(response, HttpServletResponse.SC_BAD_REQUEST, "E-mail obrigatório!");
        return;
      }

      if (!json.has("senha") || json.isNull("senha")) {
        retornarErro(response, HttpServletResponse.SC_BAD_REQUEST, "Senha obrigatória!");
        return;
      }

      Integer usuarioId = Integer.parseInt(request.getPathInfo().substring(1));

      Usuario usuarioEditado = new Usuario(
              usuarioId,
              json.getString("nome"),
              json.getString("email"),
              json.getString("senha")
      );

      usuarioEditado = UsuarioDAO.getInstance().editarUsuario(usuarioEditado);

      retorno(response, HttpServletResponse.SC_OK, new JSONObject(usuarioEditado));

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

      Integer usuarioId = Integer.parseInt(request.getPathInfo().substring(1));

      boolean resposta = UsuarioDAO.getInstance().deletarUsuario(usuarioId);
      if (resposta) {
        retorno(response, HttpServletResponse.SC_NO_CONTENT, null);
      } else {
        retornarErro(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Não foi possivel deletar usuario!");
      }
    } catch (Exception ex) {
      retornarErro(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
    }
  }
}