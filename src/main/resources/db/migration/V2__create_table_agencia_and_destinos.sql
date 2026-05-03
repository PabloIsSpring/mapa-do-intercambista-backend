CREATE TABLE agencias (
    id CHAR(36) PRIMARY KEY,
    id_user CHAR(36) NOT NULL UNIQUE,
    razao_social VARCHAR(255) UNIQUE,
    nome_fantasia VARCHAR (255),
    username VARCHAR(255) UNIQUE,
    cnpj VARCHAR(18) UNIQUE,
    deleted_at DATE,
    FOREIGN KEY (id_user) REFERENCES users(id)
);

CREATE TABLE paises (
    id CHAR(2) PRIMARY KEY,
    nome VARCHAR(100),
    idioma_principal VARCHAR(30) NOT NULL,
    moeda VARCHAR(30),
    deleted_at DATE
);

CREATE TABLE destinos (
    id CHAR(36) PRIMARY KEY,
    id_agencia CHAR(36) NOT NULL,
    id_pais CHAR(2) NOT NULL,
    cidade VARCHAR(60) NOT NULL,
    universidade VARCHAR(60) NOT NULL,
    descricao TEXT,
    curtidas INT,
    deleted_at DATE,
    FOREIGN KEY (id_agencia) REFERENCES agencias(id),
    FOREIGN KEY (id_pais) REFERENCES paises(id)
);