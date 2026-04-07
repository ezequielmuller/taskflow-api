package com.taskbaseapi.servlet;

import com.taskbaseapi.modelo.Usuario;
import com.taskbaseapi.persistencia.UsuarioDAO;
import com.taskbaseapi.security.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/login/*")
public class LoginServlet extends BaseServlet {

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {
    JSONObject json = lerBody(request);

    try {
      if (!json.has("usu_email") || json.isNull("usu_email")) {
        retornarErro(response, HttpServletResponse.SC_BAD_REQUEST, "E-mail obrigatórios!");
        return;
      }

      if (!json.has("usu_senha") || json.isNull("usu_senha")) {
        retornarErro(response, HttpServletResponse.SC_UNAUTHORIZED, "Senha obrigatório!");
        return;
      }

      Usuario usuario = UsuarioDAO.getInstance().logarUsuario(json.getString("usu_email"));

      if (usuario == null || usuario.getUsu_senha() == null || !BCrypt.checkpw(json.getString("usu_senha"), usuario.getUsu_senha())) {
        retornarErro(response, HttpServletResponse.SC_UNAUTHORIZED, "Credenciais inválidas");
        return;
      }

      JSONObject respostaLogin = new JSONObject();
      String token = JwtUtil.gerarToken(usuario.getUsu_email());
      respostaLogin.put("usu_id", usuario.getUsu_id());
      respostaLogin.put("usu_nome", usuario.getUsu_nome());
      respostaLogin.put("usu_email", usuario.getUsu_email());
      respostaLogin.put("token", token);

      retorno(response, HttpServletResponse.SC_OK, respostaLogin);
    } catch (Exception ex) {
      retornarErro(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
    }
  }


}