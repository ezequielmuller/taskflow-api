package com.taskbaseapi.modelo;

public class UsuarioXProjeto {
  private Usuario fk_usuario;
  private Projeto fk_projeto;
  private Boolean up_gerenciador;

  public UsuarioXProjeto(Usuario fk_usuario, Projeto fk_projeto, Boolean up_gerenciador) {
    this.fk_usuario = fk_usuario;
    this.fk_projeto = fk_projeto;
    this.up_gerenciador = up_gerenciador;
  }

  public Usuario getFk_usuario() {
    return fk_usuario;
  }

  public void setFk_usuario(Usuario fk_usuario) {
    this.fk_usuario = fk_usuario;
  }

  public Projeto getFk_projeto() {
    return fk_projeto;
  }

  public void setFk_projeto(Projeto fk_projeto) {
    this.fk_projeto = fk_projeto;
  }

  public Boolean getUp_gerenciador() {
    return up_gerenciador;
  }

  public void setUp_gerenciador(Boolean up_gerenciador) {
    this.up_gerenciador = up_gerenciador;
  }
}
