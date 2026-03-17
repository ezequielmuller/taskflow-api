package com.taskflow.servlet;

import com.taskflow.modelo.Usuario;
import com.taskflow.persistencia.UsuarioDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/usuario")
public class UsuarioServlet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {

    response.setContentType("application/json");
    UsuarioDAO dao = new UsuarioDAO();

    try{
      List<Usuario> usuariosList = dao.listarUsuarios();
      System.out.println("USUARIOS" + usuariosList);
    }catch(Exception ex){
      System.out.println("Erro servlet: " + ex);
    }

  }


}