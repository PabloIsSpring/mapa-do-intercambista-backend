CREATE TABLE users (
    id CHAR(36) primary key,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR (50) NOT NULL,
    deleted_at DATE
);

CREATE TABLE intercambistas (
    id CHAR (36) primary key,
    id_user CHAR(36) NOT NULL UNIQUE,
    nome VARCHAR(255),
    sobrenome VARCHAR(255),
    username VARCHAR(255) UNIQUE,
    idade int,
    FOREIGN KEY (id_user) REFERENCES users(id)
);