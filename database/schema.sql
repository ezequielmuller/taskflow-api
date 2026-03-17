DROP TABLE IF EXISTS usuario_projeto CASCADE;
DROP TABLE IF EXISTS tarefas CASCADE;
DROP TABLE IF EXISTS grupo CASCADE;
DROP TABLE IF EXISTS projeto CASCADE;
DROP TABLE IF EXISTS usuario CASCADE;

CREATE TABLE usuario
(
    usu_id          SERIAL PRIMARY KEY,
    usu_nome        VARCHAR(100) NOT NULL,
    usu_email       VARCHAR(100) NOT NULL UNIQUE,
    usu_senha       VARCHAR(100) NOT NULL,
    usu_gerenciador BOOLEAN DEFAULT FALSE
);

CREATE TABLE projeto
(
    pro_id           SERIAL PRIMARY KEY,
    pro_nome         VARCHAR(100) NOT NULL,
    pro_descricao    VARCHAR(255),
    pro_data_criacao TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE grupo
(
    gp_id      SERIAL PRIMARY KEY,
    gp_titulo  VARCHAR(100) NOT NULL,
    fk_projeto INTEGER      NOT NULL,

    CONSTRAINT fk_grupo_projeto
        FOREIGN KEY (fk_projeto)
            REFERENCES projeto (pro_id)
            ON DELETE CASCADE
);

CREATE TABLE tarefas
(
    ta_id               SERIAL PRIMARY KEY,
    ta_titulo           VARCHAR(100) NOT NULL,
    ta_descricao        VARCHAR(255),
    ta_data_criacao     TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    ta_data_finalizacao TIMESTAMP WITH TIME ZONE,

    fk_usuario          INTEGER,
    fk_grupo            INTEGER,

    CONSTRAINT fk_tarefa_usuario
        FOREIGN KEY (fk_usuario)
            REFERENCES usuario (usu_id)
            ON DELETE SET NULL,

    CONSTRAINT fk_tarefa_grupo
        FOREIGN KEY (fk_grupo)
            REFERENCES grupo (gp_id)
            ON DELETE CASCADE
);

CREATE TABLE usuario_projeto
(
    fk_usuario INTEGER,
    fk_projeto INTEGER,

    PRIMARY KEY (fk_usuario, fk_projeto),

    CONSTRAINT fk_usuario_projeto_usuario
        FOREIGN KEY (fk_usuario)
            REFERENCES usuario (usu_id)
            ON DELETE CASCADE,

    CONSTRAINT fk_usuario_projeto_projeto
        FOREIGN KEY (fk_projeto)
            REFERENCES projeto (pro_id)
            ON DELETE CASCADE
);