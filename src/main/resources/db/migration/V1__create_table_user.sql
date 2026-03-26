CREATE TABLE users (
    id CHAR(36) primary key,
    nome VARCHAR(128) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR (50) NOT NULL,
    deletedAt DATE
);