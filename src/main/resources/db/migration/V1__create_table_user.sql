CREATE TABLE users (
    id CHAR(36) primary key,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR (50) NOT NULL,
    deletedAt DATE
);

CREATE TABLE intercambistas (
    id CHAR (36) primary key,
    id_user CHAR() NOT NULL,
    nome VARCHAR(60),
    sobrenome VARCHAR(60),
    username VARCHAR(50),
    idade int,
    FOREIGN KEY (id_user) REFERENCES users(id)
);