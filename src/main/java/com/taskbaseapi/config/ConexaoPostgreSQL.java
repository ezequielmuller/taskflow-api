package com.taskbaseapi.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoPostgreSQL {

  private static final String URL = "jdbc:postgresql://localhost:5432/taskbaseapi";
  private static final String USUARIO = "postgres";
  private static final String SENHA = "123";

  public Connection conectar() throws SQLException {
    Connection con = DriverManager.getConnection(URL, USUARIO, SENHA);
   // System.out.println("> Banco de dados postgres conectado");
    return con;
  }
}