package com.taskbaseapi.servlet;

import com.taskbaseapi.security.JwtUtil;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.stream.Collectors;

public abstract class BaseServlet extends HttpServlet {

  protected JSONObject lerBody(HttpServletRequest request) throws IOException {
    String body = request.getReader().lines().collect(Collectors.joining());
    return new JSONObject(body);
  }

  protected void retorno(HttpServletResponse response, int status, Object data)
          throws IOException {
    JSONObject resposta = new JSONObject();
    resposta.put("data", data != null ? data : JSONObject.NULL);

    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    response.setStatus(status);

    PrintWriter out = response.getWriter();
    out.print(resposta.toString());
    out.flush();
  }

  protected void retornarErro(HttpServletResponse response, int status, String mensagem)
          throws IOException {
    JSONObject erro = new JSONObject();
    erro.put("status", status);
    erro.put("mensagem", mensagem);

    JSONObject resposta = new JSONObject();
    resposta.put("data", JSONObject.NULL);
    resposta.put("error", erro);

    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    response.setStatus(status);

    PrintWriter out = response.getWriter();
    out.print(resposta.toString());
    out.flush();
  }

  protected String validarToken(HttpServletRequest request) throws Exception {
    String authHeader = request.getHeader("Authorization");

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      throw new Exception("Token não fornecido");
    }

    String token = authHeader.substring(7); // remove "Bearer "

    return JwtUtil.validarToken(token).getSubject();
  }
}