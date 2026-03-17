package com.taskflow.persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {

  private static final String URL = "jdbc:postgresql://localhost:5432/taskflow-database";
  private static final String USUARIO = "postgres";
  private static final String SENHA = "123";

  public static Connection conectar() {
    Connection con = null;

    try {
      con = DriverManager.getConnection(URL, USUARIO, SENHA);
      System.out.println("Banco de dados conectado com sucesso");
    } catch (SQLException e) {
      System.out.println("Erro ao conectar no banco de dados: " + e.getMessage());
    }

    return con;
  }
}